package edu.unc.mapseq.dao;

import java.util.List;

import edu.unc.mapseq.dao.model.Flowcell;

public interface FlowcellDAO extends NamedEntityDAO<Flowcell, Long> {

    public abstract List<Flowcell> findByExample(Flowcell flowcell) throws MaPSeqDAOException;

    public abstract List<Flowcell> findByStudyName(String name) throws MaPSeqDAOException;

    public abstract List<Flowcell> findAll() throws MaPSeqDAOException;

    public abstract List<Flowcell> findByWorkflowRunId(Long workflowRunId) throws MaPSeqDAOException;

    public abstract void addFileData(Long fileDataId, Long flowcellId) throws MaPSeqDAOException;

}
