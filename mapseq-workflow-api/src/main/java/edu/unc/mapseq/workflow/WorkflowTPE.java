package edu.unc.mapseq.workflow;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkflowTPE extends ThreadPoolExecutor {

    public WorkflowTPE() {
        super(100, 100, 5L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }

}
