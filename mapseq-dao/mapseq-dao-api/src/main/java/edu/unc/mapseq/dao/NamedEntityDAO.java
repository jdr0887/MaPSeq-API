package edu.unc.mapseq.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import edu.unc.mapseq.dao.model.Persistable;

public interface NamedEntityDAO<T extends Persistable, ID extends Serializable> extends BaseDAO<T, ID> {

    public List<T> findByFileDataId(Long id) throws MaPSeqDAOException;

    public List<T> findByCreatedDateRange(Date startDate, Date endDate) throws MaPSeqDAOException;

    public abstract List<T> findByName(String name) throws MaPSeqDAOException;

}
