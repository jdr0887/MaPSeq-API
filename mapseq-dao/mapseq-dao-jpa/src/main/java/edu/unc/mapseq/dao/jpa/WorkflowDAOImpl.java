package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.WorkflowDAO;
import edu.unc.mapseq.dao.model.Workflow;

@OsgiServiceProvider(classes = { WorkflowDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class WorkflowDAOImpl extends DictionaryEntityDAOImpl<Workflow, Long> implements WorkflowDAO {

    private final Logger logger = LoggerFactory.getLogger(WorkflowDAOImpl.class);

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
            TypedQuery<Workflow> query = getEntityManager().createNamedQuery("Workflow.findAll", Workflow.class);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
