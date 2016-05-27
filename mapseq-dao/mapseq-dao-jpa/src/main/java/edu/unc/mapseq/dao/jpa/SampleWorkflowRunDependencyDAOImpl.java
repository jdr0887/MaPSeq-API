package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.SampleWorkflowRunDependencyDAO;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.SampleWorkflowRunDependency;
import edu.unc.mapseq.dao.model.SampleWorkflowRunDependency_;
import edu.unc.mapseq.dao.model.Sample_;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRun_;

@OsgiServiceProvider(classes = { SampleWorkflowRunDependencyDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class SampleWorkflowRunDependencyDAOImpl extends BaseDAOImpl<SampleWorkflowRunDependency, Long>
        implements SampleWorkflowRunDependencyDAO {

    private static final Logger logger = LoggerFactory.getLogger(SampleWorkflowRunDependencyDAOImpl.class);

    public SampleWorkflowRunDependencyDAOImpl() {
        super();
    }

    @Override
    public Class<SampleWorkflowRunDependency> getPersistentClass() {
        return SampleWorkflowRunDependency.class;
    }

    @Override
    public List<SampleWorkflowRunDependency> findBySampleIdAndChildWorkflowRunId(Long sampleId, Long workflowRunId)
            throws MaPSeqDAOException {
        logger.debug("ENTERING findBySampleIdAndChildWorkflowRunId(Long, Long)");
        List<SampleWorkflowRunDependency> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<SampleWorkflowRunDependency> crit = critBuilder
                    .createQuery(SampleWorkflowRunDependency.class);
            Root<SampleWorkflowRunDependency> root = crit.from(SampleWorkflowRunDependency.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<SampleWorkflowRunDependency, Sample> sampleWorkflowRunDependencySampleJoin = root
                    .join(SampleWorkflowRunDependency_.sample);
            predicates.add(critBuilder.equal(sampleWorkflowRunDependencySampleJoin.get(Sample_.id), sampleId));
            Join<SampleWorkflowRunDependency, WorkflowRun> sampleWorkflowRunDependencyWorkflowRunJoin = root
                    .join(SampleWorkflowRunDependency_.child);
            predicates.add(
                    critBuilder.equal(sampleWorkflowRunDependencyWorkflowRunJoin.get(WorkflowRun_.id), workflowRunId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<SampleWorkflowRunDependency> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
