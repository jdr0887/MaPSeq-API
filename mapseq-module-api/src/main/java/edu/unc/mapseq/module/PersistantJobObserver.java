package edu.unc.mapseq.module;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.JobDAO;
import edu.unc.mapseq.dao.MaPSeqDAOBeanService;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.JobStatusType;

public class PersistantJobObserver implements Observer {

    private final Logger logger = LoggerFactory.getLogger(PersistantJobObserver.class);

    private MaPSeqDAOBeanService daoBean;

    public PersistantJobObserver(MaPSeqDAOBeanService daoBean) {
        super();
        this.daoBean = daoBean;
    }

    @Override
    public void update(Observable observable, Object o) {
        logger.debug("ENTERING update(Observable, Object)");
        if (observable instanceof ModuleExecutor && o instanceof JobStatusType) {
            ModuleExecutor executor = (ModuleExecutor) observable;
            JobStatusType status = (JobStatusType) o;
            logger.info("status: {}", status.getState());
            Date date = new Date();
            Job job = executor.getJob();
            logger.debug(job.toString());

            switch (status) {
                case DONE:
                case FAILED:
                    executor.getJob().setFinished(date);
                    break;
                case RUNNING:
                    break;
            }

            try {
                JobDAO jobDAO = daoBean.getJobDAO();
                job.setStatus(status);
                jobDAO.save(job);
            } catch (MaPSeqDAOException e) {
                e.printStackTrace();
            }

        }
    }
}
