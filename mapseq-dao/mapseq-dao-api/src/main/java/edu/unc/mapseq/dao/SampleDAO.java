package edu.unc.mapseq.dao;

import java.util.List;

import edu.unc.mapseq.dao.model.Sample;

public interface SampleDAO extends NamedEntityDAO<Sample, Long> {

    public abstract List<Sample> findByFlowcellId(Long flowcellId) throws MaPSeqDAOException;

    public abstract List<Sample> findByNameAndFlowcellId(String name, Long flowcellId) throws MaPSeqDAOException;

    public abstract List<Sample> findByWorkflowRunId(Long workflowRunId) throws MaPSeqDAOException;

}
