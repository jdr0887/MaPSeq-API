package edu.unc.mapseq.dao.jpa;

import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import edu.unc.mapseq.dao.AttributeDAO;
import edu.unc.mapseq.dao.model.Attribute;

@OsgiServiceProvider(classes = { AttributeDAO.class })
@Transactional(Transactional.TxType.SUPPORTS)
@Singleton
public class AttributeDAOImpl extends BaseDAOImpl<Attribute, Long> implements AttributeDAO {

    public AttributeDAOImpl() {
        super();
    }

    @Override
    public Class<Attribute> getPersistentClass() {
        return Attribute.class;
    }

}
