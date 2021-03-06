package edu.unc.mapseq.dao.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.DictionaryEntityDAO;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Persistable;

@Transactional(Transactional.TxType.SUPPORTS)
public abstract class DictionaryEntityDAOImpl<T extends Persistable, ID extends Serializable> extends BaseDAOImpl<T, ID>
        implements DictionaryEntityDAO<T, ID> {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryEntityDAOImpl.class);

    public DictionaryEntityDAOImpl() {
        super();
    }

    @Override
    public List<T> findByName(String name) throws MaPSeqDAOException {
        logger.debug("ENTERING findByName(String)");
        List<T> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> crit = critBuilder.createQuery(getPersistentClass());
            Root<T> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.like(root.<String> get("name"), name));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.orderBy(critBuilder.asc(root.<String> get("created")));
            TypedQuery<T> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<T> findByCreatedDateRange(Date startDate, Date endDate) throws MaPSeqDAOException {
        logger.debug("ENTERING findByCreatedDateRange");
        List<T> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> crit = critBuilder.createQuery(getPersistentClass());
            Root<T> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.between(root.<Date> get("created"), startDate, endDate));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.orderBy(critBuilder.asc(root.<Date> get("created")));
            TypedQuery<T> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
