package edu.unc.mapseq.dao;

import java.util.List;

import edu.unc.mapseq.dao.model.WorkflowRun;

public interface WorkflowRunDAO extends NamedEntityDAO<WorkflowRun, Long> {

    public abstract List<WorkflowRun> findByWorkflowId(Long id) throws MaPSeqDAOException;

    public abstract WorkflowRun findByWorkflowRunAttemptId(Long workflowRunAttemptId) throws MaPSeqDAOException;

    public abstract List<WorkflowRun> findByStudyNameAndSampleNameAndWorkflowName(String studyName, String sampleName,
            String workflowName) throws MaPSeqDAOException;

    public abstract List<WorkflowRun> findByFlowcellId(Long flowcellId) throws MaPSeqDAOException;

    public abstract List<WorkflowRun> findBySampleId(Long sampleId) throws MaPSeqDAOException;

    public abstract List<WorkflowRun> findByFlowcellIdAndWorkflowId(Long flowcellId, Long workflowId)
            throws MaPSeqDAOException;

    public abstract List<WorkflowRun> findByStudyId(Long studyId) throws MaPSeqDAOException;

}
