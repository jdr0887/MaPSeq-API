package edu.unc.mapseq.dao.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.BaseDAO;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Persistable;

@Transactional
public abstract class BaseDAOImpl<T extends Persistable, ID extends Serializable> implements BaseDAO<T, ID> {

    private final Logger logger = LoggerFactory.getLogger(BaseDAOImpl.class);

    @PersistenceContext(name = "mapseq", unitName = "mapseq")
    private EntityManager entityManager;

    public BaseDAOImpl() {
        super();
    }

    public abstract Class<T> getPersistentClass();

    @Override
    public synchronized Long save(T entity) throws MaPSeqDAOException {
        logger.debug("ENTERING save(T)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!entityManager.contains(entity) && entity.getId() != null) {
            entity = entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
        return entity.getId();
    }

    @Override
    public void delete(T entity) throws MaPSeqDAOException {
        logger.debug("ENTERING delete(T)");
        T foundEntity = entityManager.find(getPersistentClass(), entity.getId());
        entityManager.remove(foundEntity);
    }

    @Override
    public void delete(List<T> entityList) throws MaPSeqDAOException {
        logger.debug("ENTERING delete(List<T>)");
        List<Long> idList = new ArrayList<Long>();
        for (T t : entityList) {
            idList.add(t.getId());
        }
        Query qDelete = entityManager
                .createQuery("delete from " + getPersistentClass().getSimpleName() + " a where a.id in (?1)");
        qDelete.setParameter(1, idList);
        qDelete.executeUpdate();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public T findById(ID id) throws MaPSeqDAOException {
        logger.debug("ENTERING findById(T)");
        T ret = entityManager.find(getPersistentClass(), id);
        return ret;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
