package edu.unc.mapseq.workflow;

import static edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType.DONE;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType.FAILED;
import static edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType.RUNNING;

import java.util.Observable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.renci.jlrm.condor.CondorJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowExecutor extends Observable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowExecutor.class);

    private Workflow workflow;

    private final WorkflowScheduledTPE scheduler = new WorkflowScheduledTPE();

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

        setWorkflowStatus(new WorkflowRunStatusInfo(RUNNING));
        try {
            workflow.init();
        } catch (Exception e) {
            logger.error("Problem with init: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, e.getMessage()));
            return;
        }

        try {
            workflow.validate();
        } catch (Exception e) {
            logger.error("Problem with validate: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, e.getMessage()));
            return;
        }

        try {
            workflow.preRun();
        } catch (Exception e) {
            logger.error("Problem with preRun: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, e.getMessage()));
            return;
        }

        try {
            CondorJob condorJob = workflow.call();
            StartCondorMonitor startCondorMonitor = new StartCondorMonitor(condorJob);
            ScheduledFuture<?> startCondorMonitorFuture = scheduler.scheduleWithFixedDelay(startCondorMonitor, 1, 3,
                    TimeUnit.MINUTES);
            Runnable stopCondorMonitor = new StopCondorMonitor(scheduler, startCondorMonitor, startCondorMonitorFuture);
            scheduler.scheduleAtFixedRate(stopCondorMonitor, 5, 1, TimeUnit.MINUTES);
            scheduler.shutdown();
            scheduler.awaitTermination(3, TimeUnit.DAYS);
        } catch (Exception e) {
            logger.error("Problem with run: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, e.getMessage()));
            return;
        }

        try {
            workflow.postRun();
        } catch (Exception e) {
            logger.error("Problem with postRun: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, e.getMessage()));
            return;
        }

        try {
            workflow.cleanUp();
        } catch (Exception e) {
            logger.error("Problem with cleanUp: ", e);
            setWorkflowStatus(new WorkflowRunStatusInfo(FAILED, e.getMessage()));
            return;
        }
        setWorkflowStatus(new WorkflowRunStatusInfo(DONE));

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
