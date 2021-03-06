package edu.unc.mapseq.workflow;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOBeanService;
import edu.unc.mapseq.dao.WorkflowRunAttemptDAO;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt;
import edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType;

public class PersistantObserver implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(PersistantObserver.class);

    public PersistantObserver() {
        super();
    }

    @Override
    public void update(Observable o, Object arg) {
        logger.debug("ENTERING update(Observable, Object)");
        if (o instanceof WorkflowExecutor && arg instanceof WorkflowRunStatusInfo) {
            WorkflowExecutor pipelineExecutor = (WorkflowExecutor) o;
            WorkflowRunStatusInfo statusInfo = (WorkflowRunStatusInfo) arg;
            WorkflowRunAttemptStatusType status = statusInfo.getStatus();
            MaPSeqDAOBeanService maPSeqDAOBean = pipelineExecutor.getWorkflow().getWorkflowBeanService()
                    .getMaPSeqDAOBeanService();
            WorkflowRunAttemptDAO workflowRunAttemptDAO = maPSeqDAOBean.getWorkflowRunAttemptDAO();
            try {
                WorkflowRunAttempt workflowRunAttempt = workflowRunAttemptDAO
                        .findById(pipelineExecutor.getWorkflow().getWorkflowRunAttempt().getId());

                // could have been deleted
                if (workflowRunAttempt == null) {
                    logger.warn("unable to find WorkflowRunAttempt");
                    return;
                }

                if (workflowRunAttempt.getStatus().equals(WorkflowRunAttemptStatusType.RESET)) {
                    // kill this thread
                    pipelineExecutor.getScheduler().shutdownNow();
                    return;
                }

                logger.debug(workflowRunAttempt.toString());

                boolean hasStateChanged = !status.getState().equals(workflowRunAttempt.getStatus().getState());

                if (hasStateChanged) {
                    logger.info("changing status from : {} to {}", workflowRunAttempt.getStatus().getState(),
                            status.getState());

                    workflowRunAttempt.setStatus(status);
                    Date date = new Date();

                    switch (status) {
                        case DONE:
                        case FAILED:
                            if (workflowRunAttempt.getStarted() == null) {
                                workflowRunAttempt.setStarted(date);
                            }
                            workflowRunAttempt.setFinished(date);
                            break;
                        case RUNNING:
                            if (workflowRunAttempt.getStarted() == null) {
                                workflowRunAttempt.setStarted(date);
                            }
                            break;
                        case PENDING:
                        case HELD:
                        default:
                            break;
                    }

                    if (StringUtils.isNotEmpty(statusInfo.getMessage())) {
                        workflowRunAttempt.setMessage(statusInfo.getMessage());
                    }

                    workflowRunAttemptDAO.save(workflowRunAttempt);
                    logger.debug(workflowRunAttempt.toString());
                }
            } catch (Exception e) {
                logger.error("Error", e);
            }
        }
    }
}
