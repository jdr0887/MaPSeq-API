package edu.unc.mapseq.workflow;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class WorkflowScheduledTPE extends ScheduledThreadPoolExecutor {

    public WorkflowScheduledTPE() {
        super(2);
        setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
    }

}
