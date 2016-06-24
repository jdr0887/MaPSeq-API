package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.AttributeDAO;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Attribute;

@OsgiServiceProvider(classes = { AttributeDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class AttributeDAOImpl extends BaseDAOImpl<Attribute, Long> implements AttributeDAO {

    private static final Logger logger = LoggerFactory.getLogger(AttributeDAOImpl.class);

    public AttributeDAOImpl() {
        super();
    }

    @Override
    public Class<Attribute> getPersistentClass() {
        return Attribute.class;
    }

    @Override
    public List<Attribute> findAll() throws MaPSeqDAOException {
        logger.debug("ENTERING findAll");
        List<Attribute> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Attribute> crit = critBuilder.createQuery(getPersistentClass());
            TypedQuery<Attribute> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
