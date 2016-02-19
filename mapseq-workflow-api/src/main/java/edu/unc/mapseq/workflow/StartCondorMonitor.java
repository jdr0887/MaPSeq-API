package edu.unc.mapseq.workflow;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.renci.jlrm.condor.CondorJob;
import org.renci.jlrm.condor.CondorJobStatusType;
import org.renci.jlrm.condor.cli.CondorLookupDAGStatusCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType;

public class StartCondorMonitor implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(StartCondorMonitor.class);

    private WorkflowExecutor pipelineExecutor;

    private CondorJob jobNode;

    private boolean jobFinished = false;

    public StartCondorMonitor(WorkflowExecutor pipelineExecutor, CondorJob jobNode) {
        super();
        this.pipelineExecutor = pipelineExecutor;
        this.jobNode = jobNode;
    }

    @Override
    public void run() {
        logger.debug("ENTERING run()");
        CondorJobStatusType statusType = null;

        try {
            // this parses the dagman.out file...ie, more error prone & subject to log format changes per condor version
            File dagmanOutFile = new File(jobNode.getSubmitFile().getParentFile(),
                    jobNode.getSubmitFile().getName().replace(".dag", ".dag.dagman.out"));
            CondorLookupDAGStatusCallable lookupStatus = new CondorLookupDAGStatusCallable(dagmanOutFile);

            // this shells out to run condor_q
            // CondorLookupStatusCallable lookupStatus = new CondorLookupStatusCallable(jobNode);

            statusType = lookupStatus.call();
        } catch (Exception e) {
            logger.warn("Error", e);
        }

        if (statusType == null) {
            return;
        }

        logger.debug("Job status: {}", StringUtils.capitalize(statusType.toString()));

        // go here for codes http://www.cs.wisc.edu/~adesmet/status.html
        try {
            switch (statusType.getCode()) {
                case 1:
                    pipelineExecutor.setWorkflowStatus(WorkflowRunAttemptStatusType.PENDING);
                    jobFinished = false;
                    break;
                case 2:
                    pipelineExecutor.setWorkflowStatus(WorkflowRunAttemptStatusType.RUNNING);
                    jobFinished = false;
                    break;
                case 3:
                    pipelineExecutor.setWorkflowStatus(WorkflowRunAttemptStatusType.FAILED);
                    jobFinished = true;
                    break;
                case 4:
                    pipelineExecutor.setWorkflowStatus(WorkflowRunAttemptStatusType.DONE);
                    jobFinished = true;
                    break;
                case 5:
                    pipelineExecutor.setWorkflowStatus(WorkflowRunAttemptStatusType.HELD);
                    jobFinished = true;
                    break;
                default:
                    jobFinished = false;
                    break;
            }
        } catch (Exception e) {
            logger.error("Error", e);
            jobFinished = true;
        }

        // if (!jobFinished) {
        // MaPSeqDAOBean daoBean = pipelineExecutor.getWorkflow().getWorkflowBeanService().getMaPSeqDAOBean();
        // try {
        // WorkflowRunAttempt attempt = daoBean.getWorkflowRunAttemptDAO().findById(
        // pipelineExecutor.getWorkflow().getWorkflowRunAttempt().getId());
        // if (attempt.getStatus().equals(WorkflowRunAttemptStatusType.RESET)) {
        // pipelineExecutor.setWorkflowStatus(WorkflowRunAttemptStatusType.DONE);
        // jobFinished = true;
        // }
        // } catch (MaPSeqDAOException e) {
        // e.printStackTrace();
        // }
        // }

    }

    public boolean isJobFinished() {
        return jobFinished;
    }

    public void setJobFinished(boolean jobFinished) {
        this.jobFinished = jobFinished;
    }

}
