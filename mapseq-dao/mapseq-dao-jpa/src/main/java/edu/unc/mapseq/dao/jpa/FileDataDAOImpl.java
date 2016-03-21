package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.FileDataDAO;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.dao.model.FileData_;

@OsgiServiceProvider(classes = { FileDataDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class FileDataDAOImpl extends BaseDAOImpl<FileData, Long> implements FileDataDAO {

    private static final Logger logger = LoggerFactory.getLogger(FileDataDAOImpl.class);

    public FileDataDAOImpl() {
        super();
    }

    @Override
    public Class<FileData> getPersistentClass() {
        return FileData.class;
    }

    @Override
    public List<FileData> findByExample(FileData fileData) {
        logger.debug("ENTERING findByExample");
        List<FileData> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<FileData> crit = critBuilder.createQuery(getPersistentClass());
            Root<FileData> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (fileData.getMimeType() != null) {
                predicates.add(critBuilder.equal(root.get(FileData_.mimeType), fileData.getMimeType()));
            }
            if (StringUtils.isNotEmpty(fileData.getName())) {
                predicates.add(critBuilder.equal(root.get(FileData_.name), fileData.getName()));
            }
            if (StringUtils.isNotEmpty(fileData.getPath())) {
                predicates.add(critBuilder.equal(root.get(FileData_.path), fileData.getPath()));
            }
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<FileData> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
