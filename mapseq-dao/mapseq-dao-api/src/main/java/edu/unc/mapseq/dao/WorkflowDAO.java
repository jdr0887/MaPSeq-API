package edu.unc.mapseq.dao;

import java.util.List;

import edu.unc.mapseq.dao.model.Workflow;

public interface WorkflowDAO extends DictionaryEntityDAO<Workflow, Long> {

    public abstract List<Workflow> findAll() throws MaPSeqDAOException;

}
