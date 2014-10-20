package edu.unc.mapseq.dao;

import java.util.Date;
import java.util.List;

import edu.unc.mapseq.dao.model.WorkflowRunAttempt;
import edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType;

public interface WorkflowRunAttemptDAO extends BaseDAO<WorkflowRunAttempt, Long> {

    public abstract List<WorkflowRunAttempt> findByWorkflowId(Long id) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findByCreatedDateRangeAndWorkflowId(Date started, Date finished, Long id)
            throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findByWorkflowRunId(Long workflowRunId) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findEnqueued(Long workflowId, int maxResults) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findEnqueued(Long workflowId) throws MaPSeqDAOException;

    public abstract List<WorkflowRunAttempt> findByWorkflowNameAndStatus(String workflowName,
            WorkflowRunAttemptStatusType status) throws MaPSeqDAOException;

}
