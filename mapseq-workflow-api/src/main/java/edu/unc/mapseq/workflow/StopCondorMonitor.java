package edu.unc.mapseq.workflow;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopCondorMonitor implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(StopCondorMonitor.class);

    private ScheduledExecutorService scheduler;

    private StartCondorMonitor startCondorMonitor;

    private ScheduledFuture<?> startCondorMonitorFuture;

    public StopCondorMonitor(ScheduledExecutorService scheduler, StartCondorMonitor startCondorMonitor,
            ScheduledFuture<?> startCondorMonitorFuture) {
        super();
        this.scheduler = scheduler;
        this.startCondorMonitor = startCondorMonitor;
        this.startCondorMonitorFuture = startCondorMonitorFuture;
    }

    @Override
    public void run() {
        logger.debug("ENTERING run()");
        if (startCondorMonitor.isJobFinished()) {
            startCondorMonitorFuture.cancel(true);
            logger.info("shutting down scheduler");
            scheduler.shutdownNow();
        }
    }

}
