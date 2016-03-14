package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.FlowcellDAO;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Flowcell;
import edu.unc.mapseq.dao.model.Flowcell_;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.Sample_;
import edu.unc.mapseq.dao.model.Study;
import edu.unc.mapseq.dao.model.Study_;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRun_;

@OsgiServiceProvider(classes = { FlowcellDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class FlowcellDAOImpl extends NamedEntityDAOImpl<Flowcell, Long> implements FlowcellDAO {

    private final Logger logger = LoggerFactory.getLogger(FlowcellDAOImpl.class);

    public FlowcellDAOImpl() {
        super();
    }

    @Override
    public Class<Flowcell> getPersistentClass() {
        return Flowcell.class;
    }

    @Override
    public List<Flowcell> findByStudyName(String name) throws MaPSeqDAOException {
        logger.debug("ENTERING findByStudyName(String)");
        List<Flowcell> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Flowcell> crit = critBuilder.createQuery(Flowcell.class);
            Root<Flowcell> root = crit.from(Flowcell.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            SetJoin<Flowcell, Sample> flowcellSampleJoin = root.join(Flowcell_.samples);
            Join<Sample, Study> sampleStudyJoin = flowcellSampleJoin.join(Sample_.study);
            predicates.add(critBuilder.equal(sampleStudyJoin.get(Study_.name), name));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            crit.orderBy(critBuilder.desc(root.get(Flowcell_.created)));
            TypedQuery<Flowcell> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Flowcell> findByExample(Flowcell flowcell) throws MaPSeqDAOException {
        logger.debug("ENTERING findByExample(Flowcell)");
        List<Flowcell> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Flowcell> crit = critBuilder.createQuery(Flowcell.class);
            Root<Flowcell> root = crit.from(Flowcell.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.like(root.get(Flowcell_.name), flowcell.getName()));
            predicates.add(critBuilder.equal(root.get(Flowcell_.baseDirectory), flowcell.getBaseDirectory()));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.orderBy(critBuilder.desc(root.get(Flowcell_.created)));
            TypedQuery<Flowcell> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Flowcell> findByWorkflowRunId(Long workflowRunId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowRunId(String)");
        List<Flowcell> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Flowcell> crit = critBuilder.createQuery(Flowcell.class);
            Root<Flowcell> root = crit.from(Flowcell.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<Flowcell, WorkflowRun> flowcellWorkflowRunJoin = root.join(Flowcell_.workflowRuns, JoinType.LEFT);
            predicates.add(critBuilder.equal(flowcellWorkflowRunJoin.get(WorkflowRun_.id), workflowRunId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            crit.orderBy(critBuilder.desc(root.get(Flowcell_.created)));
            TypedQuery<Flowcell> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Flowcell> findAll() throws MaPSeqDAOException {
        logger.debug("ENTERING findAll()");
        List<Flowcell> ret = new ArrayList<>();
        TypedQuery<Flowcell> query = getEntityManager().createNamedQuery("Flowcell.findAll", Flowcell.class);
        ret.addAll(query.getResultList());
        return ret;
    }

}
