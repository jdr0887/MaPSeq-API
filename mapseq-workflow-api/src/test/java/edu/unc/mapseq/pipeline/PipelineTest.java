package edu.unc.mapseq.pipeline;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;

public class PipelineTest {

    @Test
    public void testMain() {
        StartTask startTask = new StartTask();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        ScheduledFuture<?> taskFuture = scheduler.scheduleWithFixedDelay(startTask, 2, 5, SECONDS);
        scheduler.scheduleAtFixedRate(new StopTask(scheduler, startTask, taskFuture), 10, 1, SECONDS);
        while (!scheduler.isShutdown()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class StartTask implements Runnable {
        private int count;

        public void run() {
            System.out.println(DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date()));
            count++;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

    }

    class StopTask implements Runnable {
        private ScheduledExecutorService scheduler;

        private StartTask startTask;

        private ScheduledFuture<?> taskFuture;

        public StopTask(ScheduledExecutorService scheduler, StartTask startTask, ScheduledFuture<?> taskFuture) {
            super();
            this.scheduler = scheduler;
            this.startTask = startTask;
            this.taskFuture = taskFuture;
        }

        public void run() {
            System.out.println(startTask.getCount());
            if (startTask.getCount() >= 10) {
                taskFuture.cancel(true);
                scheduler.shutdown();
            }
        }

    }

}
