package edu.unc.mapseq.dao.jpa;

import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.WorkflowDAO;
import edu.unc.mapseq.dao.model.Workflow;

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
        TypedQuery<Workflow> query = getEntityManager().createNamedQuery("Workflow.findAll", Workflow.class);
        List<Workflow> ret = query.getResultList();
        return ret;
    }

}
