package edu.unc.mapseq.module;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.JobStatusType;

public class DryRunJobObserver implements Observer {

    private final Logger logger = LoggerFactory.getLogger(DryRunJobObserver.class);

    public DryRunJobObserver() {
        super();
    }

    @Override
    public void update(Observable observable, Object o) {
        logger.debug("ENTERING update(Observable, Object)");
        if (observable instanceof ModuleExecutor && o instanceof JobStatusType) {
            ModuleExecutor executor = (ModuleExecutor) observable;
            JobStatusType status = (JobStatusType) o;
            logger.info("status: {}", status.getState());
            Job job = executor.getJob();
            job.setStatus(status);

            switch (status) {
                case DONE:
                case FAILED:
                    job.setFinished(new Date());
                    job.setFileDatas(executor.getModule().getFileDatas());
                    break;
                case RUNNING:
                    job.setStarted(new Date());
                    break;
            }

        }
    }

}
