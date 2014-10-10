package edu.unc.mapseq.dao.jpa;

import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.StudyDAO;
import edu.unc.mapseq.dao.model.Study;

public class StudyDAOImpl extends DictionaryEntityDAOImpl<Study, Long> implements StudyDAO {

    private final Logger logger = LoggerFactory.getLogger(StudyDAOImpl.class);

    public StudyDAOImpl() {
        super();
    }

    @Override
    public Class<Study> getPersistentClass() {
        return Study.class;
    }

    @Override
    public List<Study> findAll() {
        logger.debug("ENTERING findAll()");
        TypedQuery<Study> query = getEntityManager().createNamedQuery("Study.findAll", Study.class);
        List<Study> ret = query.getResultList();
        return ret;
    }

    @Override
    public Study findBySampleId(Long id) throws MaPSeqDAOException {
        logger.debug("ENTERING findBySampleId(Long)");
        TypedQuery<Study> query = getEntityManager().createNamedQuery("Study.findBySampleId", Study.class);
        query.setParameter("id", id);
        Study ret = query.getSingleResult();
        return ret;
    }

}
