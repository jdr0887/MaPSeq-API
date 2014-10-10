package edu.unc.mapseq.dao.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.AttributeDAO;
import edu.unc.mapseq.dao.model.Attribute;

public class AttributeDAOImpl extends BaseDAOImpl<Attribute, Long> implements AttributeDAO {

    private final Logger logger = LoggerFactory.getLogger(AttributeDAOImpl.class);

    public AttributeDAOImpl() {
        super();
    }

    @Override
    public Class<Attribute> getPersistentClass() {
        return Attribute.class;
    }

}
