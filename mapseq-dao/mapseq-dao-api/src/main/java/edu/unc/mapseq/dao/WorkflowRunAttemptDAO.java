package edu.unc.mapseq.dao;

import java.util.Date;
import java.util.List;

import edu.unc.mapseq.dao.model.WorkflowRunAttempt;
import edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType;

public interface WorkflowRunAttemptDAO extends BaseDAO<WorkflowRunAttempt, Long> {

    public abstract List<WorkflowRunAttempt> findByWorkflowId(Long id) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findByCreatedDateRange(Date started, Date finished)
            throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findByCreatedDateRangeAndStatus(Date started, Date finished,
            WorkflowRunAttemptStatusType status) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findByCreatedDateRangeAndWorkflowId(Date started, Date finished, Long id)
            throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findByCreatedDateRangeAndWorkflowIdAndStatus(Date started, Date finished,
            Long workflowId, WorkflowRunAttemptStatusType status) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findByWorkflowRunId(Long workflowRunId) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findEnqueued(Long workflowId, int maxResults) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findEnqueued(Long workflowId) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findByWorkflowNameAndStatus(String workflowName,
            WorkflowRunAttemptStatusType status) throws MaPSeqDAOException;

}
