package edu.unc.mapseq.dao;

import java.util.List;

import edu.unc.mapseq.dao.model.SampleWorkflowRunDependency;

public interface SampleWorkflowRunDependencyDAO extends BaseDAO<SampleWorkflowRunDependency, Long> {

    public List<SampleWorkflowRunDependency> findBySampleIdAndChildWorkflowRunId(Long sampleId, Long workflowRunId)
            throws MaPSeqDAOException;

    public List<SampleWorkflowRunDependency> findBySampleId(Long sampleId) throws MaPSeqDAOException;

}
