package edu.unc.mapseq.dao.jpa;

import edu.unc.mapseq.dao.AttributeDAO;
import edu.unc.mapseq.dao.model.Attribute;

public class AttributeDAOImpl extends BaseDAOImpl<Attribute, Long> implements AttributeDAO {

    public AttributeDAOImpl() {
        super();
    }

    @Override
    public Class<Attribute> getPersistentClass() {
        return Attribute.class;
    }

}
