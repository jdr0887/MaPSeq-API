package edu.unc.mapseq.dao;

import java.util.List;

import edu.unc.mapseq.dao.model.Attribute;

public interface AttributeDAO extends BaseDAO<Attribute, Long> {

    public List<Attribute> findAll() throws MaPSeqDAOException;

}
