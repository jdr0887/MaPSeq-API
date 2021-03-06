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
import edu.unc.mapseq.dao.SampleDAO;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.dao.model.Flowcell;
import edu.unc.mapseq.dao.model.Flowcell_;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.Sample_;
import edu.unc.mapseq.dao.model.Study;
import edu.unc.mapseq.dao.model.Study_;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRun_;

@OsgiServiceProvider(classes = { SampleDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class SampleDAOImpl extends NamedEntityDAOImpl<Sample, Long> implements SampleDAO {

    private static final Logger logger = LoggerFactory.getLogger(SampleDAOImpl.class);

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
        List<Sample> ret = new ArrayList<>();
        try {
            TypedQuery<Sample> query = getEntityManager().createNamedQuery("Sample.findByFlowcellId", Sample.class);
            query.setParameter("id", flowcellId);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Sample> findByNameAndFlowcellId(String name, Long flowcellId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByNameAndFlowcellId(String, Long)");
        List<Sample> ret = new ArrayList<>();
        try {
            TypedQuery<Sample> query = getEntityManager().createNamedQuery("Sample.findByNameAndFlowcellId",
                    Sample.class);
            query.setParameter("name", name);
            query.setParameter("id", flowcellId);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Sample> findByFlowcellNameAndSampleNameAndLaneIndex(String flowcellName, String sampleName,
            Integer laneIndex) throws MaPSeqDAOException {
        logger.debug("ENTERING findByFlowcellNameAndSampleNameAndLaneIndex(String, String, Integer)");
        List<Sample> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Sample> crit = critBuilder.createQuery(Sample.class);
            Root<Sample> root = crit.from(Sample.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (!sampleName.endsWith("%")) {
                sampleName += "%";
            }
            predicates.add(critBuilder.like(root.get(Sample_.name), sampleName));
            predicates.add(critBuilder.equal(root.get(Sample_.laneIndex), laneIndex));
            Join<Sample, Flowcell> sampleFlowcellJoin = root.join(Sample_.flowcell);
            predicates.add(critBuilder.equal(sampleFlowcellJoin.get(Flowcell_.name), flowcellName));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            crit.orderBy(critBuilder.desc(root.get(Sample_.created)));
            TypedQuery<Sample> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Sample> findByLaneIndexAndBarcode(Integer laneIndex, String barcode) throws MaPSeqDAOException {
        logger.debug("ENTERING findByLaneIndexAndBarcode(Integer, String)");
        List<Sample> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Sample> crit = critBuilder.createQuery(Sample.class);
            Root<Sample> root = crit.from(Sample.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.like(root.get(Sample_.barcode), barcode));
            predicates.add(critBuilder.equal(root.get(Sample_.laneIndex), laneIndex));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            crit.orderBy(critBuilder.desc(root.get(Sample_.created)));
            TypedQuery<Sample> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void addFileData(Long fileDataId, Long sampleId) throws MaPSeqDAOException {
        logger.debug("ENTERING addFileData(Long, Long)");
        Sample sample = findById(sampleId);
        FileData fileData = getEntityManager().find(FileData.class, fileDataId);
        if (!sample.getFileDatas().contains(fileData)) {
            sample.getFileDatas().add(fileData);
            save(sample);
        }
    }

    @Override
    public List<Sample> findByWorkflowRunId(Long workflowRunId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowRunId(Long)");
        List<Sample> ret = new ArrayList<>();
        try {
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
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Sample> findByStudyId(Long studyId) throws MaPSeqDAOException {
        logger.debug("ENTERING findByStudyId(Long)");
        List<Sample> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Sample> crit = critBuilder.createQuery(Sample.class);
            Root<Sample> root = crit.from(Sample.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<Sample, Study> sampleStudyJoin = root.join(Sample_.study);
            predicates.add(critBuilder.equal(sampleStudyJoin.get(Study_.id), studyId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            crit.orderBy(critBuilder.desc(root.get(Sample_.created)));
            TypedQuery<Sample> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
