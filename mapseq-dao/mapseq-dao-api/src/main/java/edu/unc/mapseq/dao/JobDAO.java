package edu.unc.mapseq.dao;

import java.util.Date;
import java.util.List;

import edu.unc.mapseq.dao.model.Job;

public interface JobDAO extends NamedEntityDAO<Job, Long> {

    public abstract List<Job> findByWorkflowRunAttemptId(Long id) throws MaPSeqDAOException;

    public abstract List<Job> findByFileDataId(Long fileDataId, String clazzName) throws MaPSeqDAOException;

    public abstract List<Job> findByFileDataIdAndWorkflowId(Long fileDataId, String clazzName, Long workflowId)
            throws MaPSeqDAOException;

    public List<Job> findByWorkflowIdAndCreatedDateRange(Long workflowId, Date startDate, Date endDate)
            throws MaPSeqDAOException;

    public abstract void addAttribute(Long attributeId, Long jobId) throws MaPSeqDAOException;

    public abstract void addFileData(Long fileDataId, Long jobId) throws MaPSeqDAOException;

}
