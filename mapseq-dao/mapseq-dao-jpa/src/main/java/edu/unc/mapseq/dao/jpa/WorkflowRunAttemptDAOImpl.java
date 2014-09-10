package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.WorkflowRunAttemptDAO;
import edu.unc.mapseq.dao.model.Workflow;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt;
import edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt_;
import edu.unc.mapseq.dao.model.WorkflowRun_;
import edu.unc.mapseq.dao.model.Workflow_;

public class WorkflowRunAttemptDAOImpl extends BaseDAOImpl<WorkflowRunAttempt, Long> implements WorkflowRunAttemptDAO {

    private final Logger logger = LoggerFactory.getLogger(WorkflowRunAttemptDAOImpl.class);

    public WorkflowRunAttemptDAOImpl() {
        super();
    }

    @Override
    public Class<WorkflowRunAttempt> getPersistentClass() {
        return WorkflowRunAttempt.class;
    }

    @Override
    public List<WorkflowRunAttempt> findByWorkflowId(Long workflowId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowId(Long)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<WorkflowRunAttempt> crit = critBuilder.createQuery(WorkflowRunAttempt.class);
        Root<WorkflowRunAttempt> root = crit.from(WorkflowRunAttempt.class);
        Join<WorkflowRunAttempt, WorkflowRun> workflowRunAttemptWorkflowRunJoin = root
                .join(WorkflowRunAttempt_.workflowRun);
        Join<WorkflowRun, Workflow> workflowRunWorkflowJoin = workflowRunAttemptWorkflowRunJoin
                .join(WorkflowRun_.workflow);
        crit.where(critBuilder.equal(workflowRunWorkflowJoin.get(Workflow_.id), workflowId));
        crit.orderBy(critBuilder.asc(root.get(WorkflowRunAttempt_.created)));
        TypedQuery<WorkflowRunAttempt> query = getEntityManager().createQuery(crit);
        List<WorkflowRunAttempt> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<WorkflowRunAttempt> findByCreatedDateRangeAndWorkflowId(Date started, Date finished, Long workflowId)
            throws MaPSeqDAOException {
        logger.debug("ENTERING findByCreatedDateRangeAndWorkflowId(Long)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<WorkflowRunAttempt> crit = critBuilder.createQuery(WorkflowRunAttempt.class);
        Root<WorkflowRunAttempt> root = crit.from(WorkflowRunAttempt.class);
        Join<WorkflowRunAttempt, WorkflowRun> workflowRunAttemptWorkflowRunJoin = root
                .join(WorkflowRunAttempt_.workflowRun);
        Join<WorkflowRun, Workflow> workflowRunWorkflowJoin = workflowRunAttemptWorkflowRunJoin
                .join(WorkflowRun_.workflow);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        predicateList.add(critBuilder.between(root.<Date> get("created"), started, finished));
        predicateList.add(critBuilder.equal(workflowRunWorkflowJoin.get(Workflow_.id), workflowId));
        crit.where(predicateList.toArray(new Predicate[predicateList.size()]));
        crit.orderBy(critBuilder.asc(root.get(WorkflowRunAttempt_.created)));
        TypedQuery<WorkflowRunAttempt> query = getEntityManager().createQuery(crit);
        List<WorkflowRunAttempt> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<WorkflowRunAttempt> findByWorkflowRunId(Long workflowRunId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowRunId(Long)");
        TypedQuery<WorkflowRunAttempt> query = getEntityManager().createNamedQuery(
                "WorkflowRunAttempt.findByWorkflowRunId", WorkflowRunAttempt.class);
        query.setParameter("id", workflowRunId);
        List<WorkflowRunAttempt> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<WorkflowRunAttempt> findEnqueued(Long workflowId) throws MaPSeqDAOException {
        return findEnqueued(workflowId, 10);
    }

    @Override
    public List<WorkflowRunAttempt> findEnqueued(Long workflowId, int maxResults) throws MaPSeqDAOException {
        logger.debug("ENTERING findEnqueued(String)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<WorkflowRunAttempt> crit = critBuilder.createQuery(WorkflowRunAttempt.class);
        Root<WorkflowRunAttempt> root = crit.from(WorkflowRunAttempt.class);

        List<Predicate> predicateList = new ArrayList<Predicate>();

        predicateList
                .add(critBuilder.equal(root.get(WorkflowRunAttempt_.status), WorkflowRunAttemptStatusType.PENDING));
        predicateList.add(critBuilder.isNull(root.get(WorkflowRunAttempt_.started)));
        predicateList.add(critBuilder.isNull(root.get(WorkflowRunAttempt_.finished)));
        predicateList.add(critBuilder.isNull(root.get(WorkflowRunAttempt_.dequeued)));

        Join<WorkflowRunAttempt, WorkflowRun> workflowRunAttemptWorkflowRunJoin = root
                .join(WorkflowRunAttempt_.workflowRun);
        Join<WorkflowRun, Workflow> workflowRunWorkflowJoin = workflowRunAttemptWorkflowRunJoin
                .join(WorkflowRun_.workflow);
        predicateList.add(critBuilder.equal(workflowRunWorkflowJoin.get(Workflow_.id), workflowId));
        crit.where(predicateList.toArray(new Predicate[predicateList.size()]));
        crit.orderBy(critBuilder.asc(root.get(WorkflowRunAttempt_.created)));
        TypedQuery<WorkflowRunAttempt> query = getEntityManager().createQuery(crit);
        query.setMaxResults(maxResults);
        List<WorkflowRunAttempt> ret = query.getResultList();
        return ret;
    }

}
