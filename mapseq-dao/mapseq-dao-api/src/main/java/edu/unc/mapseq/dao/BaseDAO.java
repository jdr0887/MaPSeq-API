package edu.unc.mapseq.dao;

import java.io.Serializable;
import java.util.List;

import edu.unc.mapseq.dao.model.Persistable;

public interface BaseDAO<T extends Persistable, ID extends Serializable> {

    public abstract Long save(T entity) throws MaPSeqDAOException;

    public abstract void delete(T entity) throws MaPSeqDAOException;

    public abstract void delete(List<T> idList) throws MaPSeqDAOException;

    public abstract T findById(ID id) throws MaPSeqDAOException;

}
