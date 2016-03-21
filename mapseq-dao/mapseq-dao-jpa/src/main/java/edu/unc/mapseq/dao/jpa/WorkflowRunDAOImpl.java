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
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.WorkflowRunDAO;
import edu.unc.mapseq.dao.model.Flowcell;
import edu.unc.mapseq.dao.model.Flowcell_;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.Sample_;
import edu.unc.mapseq.dao.model.Study;
import edu.unc.mapseq.dao.model.Study_;
import edu.unc.mapseq.dao.model.Workflow;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt_;
import edu.unc.mapseq.dao.model.WorkflowRun_;
import edu.unc.mapseq.dao.model.Workflow_;

@OsgiServiceProvider(classes = { WorkflowRunDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class WorkflowRunDAOImpl extends NamedEntityDAOImpl<WorkflowRun, Long> implements WorkflowRunDAO {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowRunDAOImpl.class);

    public WorkflowRunDAOImpl() {
        super();
    }

    @Override
    public Class<WorkflowRun> getPersistentClass() {
        return WorkflowRun.class;
    }

    @Override
    public List<WorkflowRun> findByWorkflowId(Long id) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowId(Long)");
        List<WorkflowRun> ret = new ArrayList<>();
        try {
            TypedQuery<WorkflowRun> query = getEntityManager().createNamedQuery("WorkflowRun.findByWorkflowId",
                    WorkflowRun.class);
            query.setParameter("id", id);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<WorkflowRun> findByStudyNameAndSampleNameAndWorkflowName(String studyName, String sampleName,
            String workflowName) throws MaPSeqDAOException {
        logger.debug("ENTERING findByStudyNameAndSampleNameAndWorkflowName(String, String, String)");

        List<WorkflowRun> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<WorkflowRun> crit = critBuilder.createQuery(WorkflowRun.class);
            Root<WorkflowRun> root = crit.from(WorkflowRun.class);

            Join<WorkflowRun, Sample> workflowRunSampleJoin = root.join(WorkflowRun_.samples, JoinType.LEFT);
            Predicate condition1 = critBuilder.like(workflowRunSampleJoin.get(Sample_.name), sampleName);

            Join<Sample, Study> workflowRunSampleStudyJoin = workflowRunSampleJoin.join(Sample_.study);
            Predicate condition2 = critBuilder.equal(workflowRunSampleStudyJoin.get(Study_.name), studyName);

            Predicate oneAndTwoPredicate = critBuilder.and(condition1, condition2);

            Join<WorkflowRun, Flowcell> workflowRunFlowcellJoin = root.join(WorkflowRun_.flowcells, JoinType.LEFT);
            Join<Flowcell, Sample> flowcellSampleJoin = workflowRunFlowcellJoin.join(Flowcell_.samples, JoinType.LEFT);
            Predicate condition3 = critBuilder.like(flowcellSampleJoin.get(Sample_.name), sampleName);

            Join<Sample, Study> flowcellSampleStudyJoin = flowcellSampleJoin.join(Sample_.study);
            Predicate condition4 = critBuilder.equal(flowcellSampleStudyJoin.get(Study_.name), studyName);

            Predicate threeAndFourPredicate = critBuilder.and(condition3, condition4);

            Predicate oneAndTwoOrThreeAndFourPredicate = critBuilder.or(oneAndTwoPredicate, threeAndFourPredicate);

            Join<WorkflowRun, Workflow> workflowRunWorkflowJoin = root.join(WorkflowRun_.workflow);
            Predicate condition5 = critBuilder.equal(workflowRunWorkflowJoin.get(Workflow_.name), workflowName);

            crit.where(oneAndTwoOrThreeAndFourPredicate, condition5);
            crit.distinct(true);
            crit.orderBy(critBuilder.asc(root.get(WorkflowRun_.created)));
            TypedQuery<WorkflowRun> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public List<WorkflowRun> findByFlowcellId(Long flowcellId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByFlowcellId(Long)");
        List<WorkflowRun> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<WorkflowRun> crit = critBuilder.createQuery(WorkflowRun.class);
            Root<WorkflowRun> root = crit.from(WorkflowRun.class);
            Join<WorkflowRun, Sample> workflowRunSampleJoin = root.join(WorkflowRun_.samples, JoinType.LEFT);
            Join<Sample, Flowcell> workflowRunSampleFlowcellJoin = workflowRunSampleJoin.join(Sample_.flowcell);
            Join<WorkflowRun, Flowcell> workflowRunFlowcellJoin = root.join(WorkflowRun_.flowcells);
            crit.where(critBuilder.or(critBuilder.equal(workflowRunSampleFlowcellJoin.get(Flowcell_.id), flowcellId),
                    critBuilder.equal(workflowRunFlowcellJoin.get(Flowcell_.id), flowcellId)));
            crit.orderBy(critBuilder.asc(root.get(WorkflowRun_.created)));
            TypedQuery<WorkflowRun> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public WorkflowRun findByWorkflowRunAttemptId(Long workflowRunAttemptId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowRunAttemptId(Long)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<WorkflowRun> crit = critBuilder.createQuery(WorkflowRun.class);
        Root<WorkflowRun> root = crit.from(WorkflowRun.class);
        Join<WorkflowRun, WorkflowRunAttempt> workflowRunWorkflowRunAttemptJoin = root.join(WorkflowRun_.attempts,
                JoinType.LEFT);
        crit.where(
                critBuilder.equal(workflowRunWorkflowRunAttemptJoin.get(WorkflowRunAttempt_.id), workflowRunAttemptId));
        crit.orderBy(critBuilder.asc(root.get(WorkflowRun_.created)));
        TypedQuery<WorkflowRun> query = getEntityManager().createQuery(crit);
        WorkflowRun ret = query.getSingleResult();
        return ret;
    }

    @Override
    public List<WorkflowRun> findBySampleId(Long sampleId) throws MaPSeqDAOException {
        logger.debug("ENTERING findBySampleId(Long)");
        List<WorkflowRun> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<WorkflowRun> crit = critBuilder.createQuery(WorkflowRun.class);
            Root<WorkflowRun> root = crit.from(WorkflowRun.class);
            Join<WorkflowRun, Sample> workflowRunSampleJoin = root.join(WorkflowRun_.samples, JoinType.LEFT);
            Join<WorkflowRun, Flowcell> workflowRunFlowcellJoin = root.join(WorkflowRun_.flowcells, JoinType.LEFT);
            Join<Flowcell, Sample> workflowRunFlowcellSampleJoin = workflowRunFlowcellJoin.join(Flowcell_.samples);
            crit.where(critBuilder.or(critBuilder.equal(workflowRunSampleJoin.get(Sample_.id), sampleId),
                    critBuilder.equal(workflowRunFlowcellSampleJoin.get(Sample_.id), sampleId)));
            TypedQuery<WorkflowRun> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<WorkflowRun> findByFlowcellIdAndWorkflowId(Long flowcellId, Long workflowId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByFlowcellIdAndWorkflowId(Long, Long)");
        List<WorkflowRun> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<WorkflowRun> crit = critBuilder.createQuery(WorkflowRun.class);
            Root<WorkflowRun> root = crit.from(WorkflowRun.class);
            List<Predicate> predicateList = new ArrayList<Predicate>();
            Join<WorkflowRun, Workflow> workflowRunWorkflowJoin = root.join(WorkflowRun_.workflow);
            predicateList.add(critBuilder.equal(workflowRunWorkflowJoin.get(Workflow_.id), workflowId));

            Join<WorkflowRun, Sample> workflowRunSampleJoin = root.join(WorkflowRun_.samples, JoinType.LEFT);
            Join<Sample, Flowcell> workflowRunSampleFlowcellJoin = workflowRunSampleJoin.join(Sample_.flowcell);
            Join<WorkflowRun, Flowcell> workflowRunFlowcellJoin = root.join(WorkflowRun_.flowcells);

            predicateList
                    .add(critBuilder.or(critBuilder.equal(workflowRunSampleFlowcellJoin.get(Flowcell_.id), flowcellId),
                            critBuilder.equal(workflowRunFlowcellJoin.get(Flowcell_.id), flowcellId)));
            crit.where(predicateList.toArray(new Predicate[predicateList.size()]));
            crit.orderBy(critBuilder.desc(root.get(WorkflowRun_.created)));
            TypedQuery<WorkflowRun> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<WorkflowRun> findByStudyId(Long studyId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByStudyId(String)");
        List<WorkflowRun> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<WorkflowRun> crit = critBuilder.createQuery(WorkflowRun.class);
            Root<WorkflowRun> root = crit.from(WorkflowRun.class);
            Join<WorkflowRun, Sample> workflowRunSampleJoin = root.join(WorkflowRun_.samples, JoinType.LEFT);
            Join<WorkflowRun, Flowcell> workflowRunFlowcellJoin = root.join(WorkflowRun_.flowcells, JoinType.LEFT);
            Join<Flowcell, Sample> flowcellSampleJoin = workflowRunFlowcellJoin.join(Flowcell_.samples, JoinType.LEFT);
            Join<Sample, Study> flowcellSampleStudyJoin = flowcellSampleJoin.join(Sample_.study);
            Join<Sample, Study> workflowRunSampleStudyJoin = workflowRunSampleJoin.join(Sample_.study);
            crit.where(critBuilder.or(critBuilder.equal(workflowRunSampleStudyJoin.get(Study_.id), studyId),
                    critBuilder.equal(flowcellSampleStudyJoin.get(Study_.id), studyId)));
            crit.orderBy(critBuilder.asc(root.get(WorkflowRun_.created)));
            TypedQuery<WorkflowRun> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
