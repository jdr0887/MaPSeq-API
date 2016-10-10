package edu.unc.mapseq.workflow;

import static edu.unc.mapseq.dao.model.WorkflowRunAttemptPhaseType.CLEAN;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptPhaseType.INIT;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptPhaseType.POST_RUN;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptPhaseType.PRE_RUN;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptPhaseType.RUN;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptPhaseType.VALIDATE;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType.DONE;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType.FAILED;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType.RUNNING;

import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.renci.jlrm.condor.CondorJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowExecutor extends Observable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowExecutor.class);

    private Workflow workflow;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public WorkflowExecutor(Workflow workflow, boolean dryRun) {
        super();
        this.workflow = workflow;
        if (!dryRun) {
            addObserver(new PersistantObserver());
        }
    }

    public WorkflowExecutor(Workflow workflow) {
        this(workflow, false);
    }

    @Override
    public void run() {

        try {
            setWorkflowStatus(new WorkflowRunStatusInfo(RUNNING, INIT));
            workflow.init();
            setWorkflowStatus(new WorkflowRunStatusInfo(DONE, INIT));
        } catch (WorkflowException e) {
            logger.error("Problem with init: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, INIT, e.getMessage()));
        } catch (Exception e) {
            logger.warn("Problem with init: ", e);
        }

        try {
            setWorkflowStatus(new WorkflowRunStatusInfo(RUNNING, VALIDATE));
            workflow.validate();
            setWorkflowStatus(new WorkflowRunStatusInfo(DONE, VALIDATE));
        } catch (WorkflowException e) {
            logger.error("Problem with validate: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, VALIDATE, e.getMessage()));
        } catch (Exception e) {
            logger.warn("Problem with validate: ", e);
        }

        try {
            setWorkflowStatus(new WorkflowRunStatusInfo(RUNNING, PRE_RUN));
            workflow.preRun();
            setWorkflowStatus(new WorkflowRunStatusInfo(DONE, PRE_RUN));
        } catch (WorkflowException e) {
            logger.error("Problem with preRun: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, PRE_RUN, e.getMessage()));
        } catch (Exception e) {
            logger.warn("Problem with preRun: ", e);
        }

        try {
            setWorkflowStatus(new WorkflowRunStatusInfo(RUNNING, RUN));
            CondorJob condorJob = workflow.call();
            StartCondorMonitor startCondorMonitor = new StartCondorMonitor(condorJob);
            ScheduledFuture<?> startCondorMonitorFuture = scheduler.scheduleWithFixedDelay(startCondorMonitor, 1, 3,
                    TimeUnit.MINUTES);
            Runnable stopCondorMonitor = new StopCondorMonitor(scheduler, startCondorMonitor, startCondorMonitorFuture);
            scheduler.scheduleAtFixedRate(stopCondorMonitor, 5, 1, TimeUnit.MINUTES);
            scheduler.awaitTermination(5, TimeUnit.DAYS);
            setWorkflowStatus(new WorkflowRunStatusInfo(DONE, RUN));
        } catch (WorkflowException | InterruptedException e) {
            logger.error("Problem with run: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, RUN, e.getMessage()));
        } catch (Exception e) {
            logger.warn("Problem with run: ", e);
        }

        try {
            setWorkflowStatus(new WorkflowRunStatusInfo(RUNNING, POST_RUN));
            workflow.postRun();
            setWorkflowStatus(new WorkflowRunStatusInfo(DONE, POST_RUN));
        } catch (WorkflowException e) {
            logger.error("Problem with postRun: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, POST_RUN, e.getMessage()));
        } catch (Exception e) {
            logger.warn("Problem with postRun: ", e);
        }

        try {
            setWorkflowStatus(new WorkflowRunStatusInfo(RUNNING, CLEAN));
            workflow.cleanUp();
            setWorkflowStatus(new WorkflowRunStatusInfo(DONE, CLEAN));
        } catch (WorkflowException e) {
            logger.error("Problem with cleanUp: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, CLEAN, e.getMessage()));
        } catch (Exception e) {
            logger.warn("Problem with cleanUp: ", e);
        }

    }

    public void setWorkflowStatus(WorkflowRunStatusInfo workflowStatusInfo) {
        setChanged();
        notifyObservers(workflowStatusInfo);
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

}
