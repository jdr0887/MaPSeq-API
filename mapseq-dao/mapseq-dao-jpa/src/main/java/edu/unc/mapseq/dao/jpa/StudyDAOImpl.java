package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.StudyDAO;
import edu.unc.mapseq.dao.model.Study;

@OsgiServiceProvider(classes = { StudyDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class StudyDAOImpl extends DictionaryEntityDAOImpl<Study, Long> implements StudyDAO {

    private static final Logger logger = LoggerFactory.getLogger(StudyDAOImpl.class);

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
        List<Study> ret = new ArrayList<>();
        try {
            TypedQuery<Study> query = getEntityManager().createNamedQuery("Study.findAll", Study.class);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
