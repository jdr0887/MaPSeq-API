package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.JobDAO;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Attribute;
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

public class JobDAOImpl extends NamedEntityDAOImpl<Job, Long> implements JobDAO {

    private final Logger logger = LoggerFactory.getLogger(JobDAOImpl.class);

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
        TypedQuery<Job> query = getEntityManager().createNamedQuery("Job.findByWorkflowRunAttemptId", Job.class);
        query.setParameter("id", id);
        List<Job> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<Job> findByFileDataId(Long fileDataId, String clazzName) {
        logger.debug("ENTERING findByFileDataId(Long, String)");
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
        List<Job> ret = query.getResultList();
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Job> findByFileDataIdAndWorkflowId(Long fileDataId, String clazzName, Long workflowId) {
        logger.debug("ENTERING findByFileDataIdAndWorkflowId(Long, String, Long)");
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
        Query query = getEntityManager().createQuery(crit);
        List<Job> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<Job> findByWorkflowIdAndCreatedDateRange(Long workflowId, Date startDate, Date endDate)
            throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowIdAndCreatedDateRange(Long, Date, Date)");
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
        List<Job> ret = query.getResultList();
        return ret;
    }

    @Override
    public void addFileData(Long fileDataId, Long jobId) throws MaPSeqDAOException {
        logger.debug("ENTERING addFileData(Long, Job)");
        if (jobId == null) {
            logger.warn("jobId arg was null");
            return;
        }
        Job entity = findById(jobId);
        if (entity == null) {
            logger.warn("no Job found");
            return;
        }
        Set<FileData> fileDataSet = entity.getFileDatas();
        if (fileDataSet == null) {
            fileDataSet = new HashSet<FileData>();
        }
        FileData fileData = getFileDataDAO().findById(fileDataId);
        if (fileData == null) {
            logger.warn("no FileData found: {}", fileDataId);
            return;
        }
        fileDataSet.add(fileData);
        entity.setFileDatas(fileDataSet);
        save(entity);
    }

    @Override
    public void addAttribute(Long attributeId, Long jobId) throws MaPSeqDAOException {
        logger.debug("ENTERING addAttribute(Long, Long)");
        if (jobId == null) {
            logger.warn("jobId arg was null");
            return;
        }
        Job entity = findById(jobId);
        if (entity == null) {
            logger.warn("no Job found");
            return;
        }
        Set<Attribute> attributeSet = entity.getAttributes();
        if (attributeSet == null) {
            attributeSet = new HashSet<Attribute>();
        }
        Attribute attribute = getAttributeDAO().findById(attributeId);
        if (attribute == null) {
            logger.warn("no Attribute found: {}", attributeId);
            return;
        }
        attributeSet.add(attribute);
        entity.setAttributes(attributeSet);
        save(entity);
    }

}
