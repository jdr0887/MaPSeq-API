package edu.unc.mapseq.dao.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.DataCache;
import org.apache.openjpa.persistence.jdbc.Index;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DictionaryEntity", propOrder = {})
@XmlRootElement(name = "dictionaryEntity")
@Entity
@Table(name = "dictionary_entity", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@DataCache(enabled = false)
@Cacheable(value = false)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class DictionaryEntity implements Persistable {

    private static final long serialVersionUID = 2499258196824667496L;

    @XmlAttribute(name = "id")
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dictionary_entity_id_seq")
    @SequenceGenerator(name = "dictionary_entity_id_seq", sequenceName = "dictionary_entity_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id", nullable = false)
    protected Long id;

    @XmlAttribute(name = "name")
    @Index
    @Column(name = "name")
    protected String name;

    @XmlTransient
    @Column(name = "active")
    private Boolean active;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "America/New_York")
    @XmlAttribute(name = "created")
    @Column(name = "created")
    protected Date created;

    public DictionaryEntity() {
        super();
        this.created = new Date();
        this.active = Boolean.TRUE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DictionaryEntity other = (DictionaryEntity) obj;
        if (active == null) {
            if (other.active != null)
                return false;
        } else if (!active.equals(other.active))
            return false;
        if (created == null) {
            if (other.created != null)
                return false;
        } else if (!created.equals(other.created))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
