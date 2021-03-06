package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.transaction.Transactional;

import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.JobDAO;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.dao.model.FileData_;
import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.Job_;
import edu.unc.mapseq.dao.model.Workflow;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt_;
import edu.unc.mapseq.dao.model.WorkflowRun_;
import edu.unc.mapseq.dao.model.Workflow_;

@OsgiServiceProvider(classes = { JobDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class JobDAOImpl extends NamedEntityDAOImpl<Job, Long> implements JobDAO {

    private static final Logger logger = LoggerFactory.getLogger(JobDAOImpl.class);

    public JobDAOImpl() {
        super();
    }

    @Override
    public Class<Job> getPersistentClass() {
        return Job.class;
    }

    @Override
    public List<Job> findByWorkflowRunAttemptId(Long id) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowRunAttemptId(Long)");
        List<Job> ret = new ArrayList<>();
        try {
            List<Predicate> predicates = new ArrayList<Predicate>();
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Job> crit = critBuilder.createQuery(getPersistentClass());
            Root<Job> root = crit.from(getPersistentClass());
            Join<Job, WorkflowRunAttempt> jobWorkflowRunAttemptJoin = root.join(Job_.workflowRunAttempt);
            predicates.add(critBuilder.equal(jobWorkflowRunAttemptJoin.get(WorkflowRunAttempt_.id), id));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            crit.orderBy(critBuilder.desc(root.get(Job_.created)));
            TypedQuery<Job> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Job> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
            // ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Job> findByWorkflowRunAttemptIdAndName(Long id, String name) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowRunAttemptIdAndClassName(Long, String)");
        List<Job> ret = new ArrayList<>();
        try {
            List<Predicate> predicates = new ArrayList<Predicate>();
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Job> crit = critBuilder.createQuery(getPersistentClass());
            Root<Job> root = crit.from(getPersistentClass());
            Join<Job, WorkflowRunAttempt> jobWorkflowRunAttemptJoin = root.join(Job_.workflowRunAttempt);
            predicates.add(critBuilder.equal(jobWorkflowRunAttemptJoin.get(WorkflowRunAttempt_.id), id));
            predicates.add(critBuilder.equal(root.get(Job_.name), name));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            crit.orderBy(critBuilder.desc(root.get(Job_.created)));
            TypedQuery<Job> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Job> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Job> findByFileDataId(Long fileDataId, String clazzName) {
        logger.debug("ENTERING findByFileDataId(Long, String)");
        List<Job> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Job> crit = critBuilder.createQuery(getPersistentClass());
            Root<Job> root = crit.from(Job.class);
            Predicate condition1 = critBuilder.equal(root.get(Job_.name), clazzName);
            SetJoin<Job, FileData> fileDataJoin = root.join(Job_.fileDatas);
            Predicate condition2 = critBuilder.equal(fileDataJoin.get(FileData_.id), fileDataId);
            crit.where(condition1, condition2);
            crit.distinct(true);
            crit.orderBy(critBuilder.desc(root.get(Job_.created)));
            TypedQuery<Job> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Job> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
            // ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Job> findByFileDataIdAndWorkflowId(Long fileDataId, String clazzName, Long workflowId) {
        logger.debug("ENTERING findByFileDataIdAndWorkflowId(Long, String, Long)");
        List<Job> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Job> crit = critBuilder.createQuery(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Root<Job> root = crit.from(Job.class);
            predicates.add(critBuilder.equal(root.get(Job_.name), clazzName));
            Join<Job, WorkflowRunAttempt> jobWorkflowRunAttemptJoin = root.join(Job_.workflowRunAttempt);
            Join<WorkflowRunAttempt, WorkflowRun> workflowRunAttemptWorkflowRunJoin = jobWorkflowRunAttemptJoin
                    .join(WorkflowRunAttempt_.workflowRun);
            Join<WorkflowRun, Workflow> workflowRunWorkflowJoin = workflowRunAttemptWorkflowRunJoin
                    .join(WorkflowRun_.workflow);
            predicates.add(critBuilder.equal(workflowRunWorkflowJoin.get(Workflow_.id), workflowId));
            SetJoin<Job, FileData> fileDataJoin = root.join(Job_.fileDatas);
            predicates.add(critBuilder.equal(fileDataJoin.get(FileData_.id), fileDataId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            crit.orderBy(critBuilder.desc(root.get(Job_.created)));
            TypedQuery<Job> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Job> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
            // ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Job> findByWorkflowIdAndCreatedDateRange(Long workflowId, Date startDate, Date endDate)
            throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowIdAndCreatedDateRange(Long, Date, Date)");
        List<Job> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Job> crit = critBuilder.createQuery(getPersistentClass());
            Root<Job> root = crit.from(Job.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.between(root.<Date> get(Job_.created), startDate, endDate));
            Join<Job, WorkflowRunAttempt> jobWorkflowRunAttemptJoin = root.join(Job_.workflowRunAttempt);
            Join<WorkflowRunAttempt, WorkflowRun> workflowRunAttemptWorkflowRunJoin = jobWorkflowRunAttemptJoin
                    .join(WorkflowRunAttempt_.workflowRun);
            Join<WorkflowRun, Workflow> workflowRunWorkflowJoin = workflowRunAttemptWorkflowRunJoin
                    .join(WorkflowRun_.workflow);
            predicates.add(critBuilder.equal(workflowRunWorkflowJoin.get(Workflow_.id), workflowId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.orderBy(critBuilder.desc(root.get(Job_.created)));
            TypedQuery<Job> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Job> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
            // ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void addFileData(Long fileDataId, Long jobId) throws MaPSeqDAOException {
        logger.debug("ENTERING addFileData(Long, Long)");
        Job job = findById(jobId);
        FileData fileData = getEntityManager().find(FileData.class, fileDataId);
        if (!job.getFileDatas().contains(fileData)) {
            job.getFileDatas().add(fileData);
            save(job);
        }
    }

}
