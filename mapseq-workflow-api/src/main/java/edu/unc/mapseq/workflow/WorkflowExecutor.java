package edu.unc.mapseq.workflow;

import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.renci.jlrm.condor.CondorJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType;

public class WorkflowExecutor extends Observable implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(WorkflowExecutor.class);

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

        setWorkflowStatus(WorkflowRunAttemptStatusType.RUNNING);

        try {
            workflow.init();
        } catch (WorkflowException e) {
            setWorkflowStatus(WorkflowRunAttemptStatusType.FAILED);
            logger.error("Problem with init: ", e);
        } catch (Exception e) {
            logger.warn("Problem with init: ", e);
        }

        try {
            workflow.validate();
        } catch (WorkflowException e) {
            setWorkflowStatus(WorkflowRunAttemptStatusType.FAILED);
            logger.error("Problem with validate: ", e);
        } catch (Exception e) {
            logger.warn("Problem with validate: ", e);
        }

        try {
            workflow.preRun();
        } catch (WorkflowException e) {
            setWorkflowStatus(WorkflowRunAttemptStatusType.FAILED);
            logger.error("Problem with preRun: ", e);
        } catch (Exception e) {
            logger.warn("Problem with preRun: ", e);
        }

        try {
            CondorJob condorJob = workflow.call();
            StartCondorMonitor startCondorMonitor = new StartCondorMonitor(this, condorJob);
            ScheduledFuture<?> startCondorMonitorFuture = scheduler.scheduleWithFixedDelay(startCondorMonitor, 1, 3,
                    TimeUnit.MINUTES);
            Runnable stopCondorMonitor = new StopCondorMonitor(scheduler, startCondorMonitor, startCondorMonitorFuture);
            scheduler.scheduleAtFixedRate(stopCondorMonitor, 5, 1, TimeUnit.MINUTES);
            scheduler.awaitTermination(5, TimeUnit.DAYS);
        } catch (WorkflowException | InterruptedException e) {
            setWorkflowStatus(WorkflowRunAttemptStatusType.FAILED);
            logger.error("Problem with run: ", e);
        } catch (Exception e) {
            logger.warn("Problem with run: ", e);
        }

        try {
            workflow.postRun();
        } catch (WorkflowException e) {
            setWorkflowStatus(WorkflowRunAttemptStatusType.FAILED);
            logger.error("Problem with postRun: ", e);
        } catch (Exception e) {
            logger.warn("Problem with postRun: ", e);
        }

        try {
            workflow.cleanUp();
        } catch (WorkflowException e) {
            setWorkflowStatus(WorkflowRunAttemptStatusType.FAILED);
            logger.error("Problem with cleanUp: ", e);
        } catch (Exception e) {
            logger.warn("Problem with cleanUp: ", e);
        }

    }

    public void setWorkflowStatus(WorkflowRunAttemptStatusType workflowStatus) {
        setChanged();
        notifyObservers(workflowStatus);
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
