package edu.unc.mapseq.dao;

import java.util.List;

import edu.unc.mapseq.dao.model.Study;

public interface StudyDAO extends DictionaryEntityDAO<Study, Long> {

    public List<Study> findAll() throws MaPSeqDAOException;

    public Study findBySampleId(Long id) throws MaPSeqDAOException;

}
