package edu.unc.mapseq.workflow;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.renci.jlrm.condor.CondorJob;
import org.renci.jlrm.condor.CondorJobStatusType;
import org.renci.jlrm.condor.cli.CondorLookupDAGStatusCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartCondorMonitor implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(StartCondorMonitor.class);

    private CondorJob jobNode;

    private boolean jobFinished = false;

    private String message;

    public StartCondorMonitor(CondorJob jobNode) {
        super();
        this.jobNode = jobNode;
    }

    @Override
    public void run() {
        logger.debug("ENTERING run()");

        try {
            // this parses the dagman.out file...ie, more error prone & subject to log format changes per condor version
            File dagmanOutFile = new File(jobNode.getSubmitFile().getParentFile(),
                    jobNode.getSubmitFile().getName().replace(".dag", ".dag.dagman.out"));

            // this shells out to run condor_q
            CondorLookupDAGStatusCallable lookupStatus = new CondorLookupDAGStatusCallable(dagmanOutFile);
            // CondorLookupStatusCallable lookupStatus = new CondorLookupStatusCallable(jobNode);

            CondorJobStatusType statusType = lookupStatus.call();

            if (statusType == null) {
                return;
            }

            logger.debug("Job status: {}", StringUtils.capitalize(statusType.toString()));

            // go here for codes http://www.cs.wisc.edu/~adesmet/status.html
            switch (statusType.getCode()) {
                case 1:
                    jobFinished = false;
                    break;
                case 2:
                    jobFinished = false;
                    break;
                case 3:
                    message = "Removed from HTCondor";
                    jobFinished = true;
                    break;
                case 4:
                    message = "Completed in HTCondor";
                    jobFinished = true;
                    break;
                case 5:
                    message = "Held in HTCondor";
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

    }

    public boolean isJobFinished() {
        return jobFinished;
    }

    public void setJobFinished(boolean jobFinished) {
        this.jobFinished = jobFinished;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
