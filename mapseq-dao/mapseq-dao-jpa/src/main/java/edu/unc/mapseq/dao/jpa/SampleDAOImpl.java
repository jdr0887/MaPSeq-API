package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.SampleDAO;
import edu.unc.mapseq.dao.model.Attribute;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.Sample_;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRun_;

public class SampleDAOImpl extends NamedEntityDAOImpl<Sample, Long> implements SampleDAO {

    private final Logger logger = LoggerFactory.getLogger(SampleDAOImpl.class);

    public SampleDAOImpl() {
        super();
    }

    @Override
    public Class<Sample> getPersistentClass() {
        return Sample.class;
    }

    @Override
    public List<Sample> findByFlowcellId(Long flowcellId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByFlowcellId(Long)");
        TypedQuery<Sample> query = getEntityManager().createNamedQuery("Sample.findByFlowcellId", Sample.class);
        query.setParameter("id", flowcellId);
        List<Sample> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<Sample> findByNameAndFlowcellId(String name, Long flowcellId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByNameAndFlowcellId(String, Long)");
        TypedQuery<Sample> query = getEntityManager().createNamedQuery("Sample.findByNameAndFlowcellId", Sample.class);
        query.setParameter("name", name);
        query.setParameter("id", flowcellId);
        List<Sample> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<Sample> findByWorkflowRunId(Long workflowRunId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowRunId(String)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sample> crit = critBuilder.createQuery(Sample.class);
        Root<Sample> root = crit.from(Sample.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        Join<Sample, WorkflowRun> sampleWorkflowRunJoin = root.join(Sample_.workflowRuns, JoinType.LEFT);
        predicates.add(critBuilder.equal(sampleWorkflowRunJoin.get(WorkflowRun_.id), workflowRunId));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        crit.distinct(true);
        crit.orderBy(critBuilder.desc(root.get(Sample_.created)));
        TypedQuery<Sample> query = getEntityManager().createQuery(crit);
        List<Sample> ret = query.getResultList();
        return ret;
    }

    @Override
    public void addFileData(Long fileDataId, Long sampleId) throws MaPSeqDAOException {
        logger.debug("ENTERING addFileData(Long, Sample)");
        if (sampleId == null) {
            logger.warn("sampleId arg was null");
            return;
        }
        Sample entity = findById(sampleId);
        if (entity == null) {
            logger.warn("no Sample found");
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
    public void addAttribute(Long attributeId, Long sampleId) throws MaPSeqDAOException {
        logger.debug("ENTERING addAttribute(Long, Long)");
        if (sampleId == null) {
            logger.warn("Job arg was null");
            return;
        }
        Sample entity = findById(sampleId);
        if (entity == null) {
            logger.warn("no Sample found");
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
