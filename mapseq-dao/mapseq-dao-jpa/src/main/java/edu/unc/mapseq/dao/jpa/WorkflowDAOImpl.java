package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.WorkflowDAO;
import edu.unc.mapseq.dao.model.Workflow;
import edu.unc.mapseq.dao.model.Workflow_;

@OsgiServiceProvider(classes = { WorkflowDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class WorkflowDAOImpl extends DictionaryEntityDAOImpl<Workflow, Long> implements WorkflowDAO {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowDAOImpl.class);

    public WorkflowDAOImpl() {
        super();
    }

    @Override
    public Class<Workflow> getPersistentClass() {
        return Workflow.class;
    }

    @Override
    public List<Workflow> findAll() throws MaPSeqDAOException {
        logger.debug("ENTERING findAll()");
        List<Workflow> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Workflow> crit = critBuilder.createQuery(Workflow.class);
            Root<Workflow> root = crit.from(Workflow.class);
            crit.where(critBuilder.equal(root.get(Workflow_.active), Boolean.TRUE));
            crit.orderBy(critBuilder.asc(root.get(Workflow_.created)));
            crit.distinct(true);
            TypedQuery<Workflow> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
