package edu.unc.mapseq.dao.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.DataCache;
import org.apache.openjpa.persistence.jdbc.ContainerTable;
import org.apache.openjpa.persistence.jdbc.Index;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NamedEntity", propOrder = {})
@XmlRootElement(name = "namedEntity")
@Entity
@Table(name = "named_entity")
@DataCache(enabled = false)
@Cacheable(value = false)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class NamedEntity implements Persistable {

    private static final long serialVersionUID = 2499258196824667496L;

    @XmlAttribute(name = "id")
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "named_entity_id_seq")
    @SequenceGenerator(name = "named_entity_id_seq", sequenceName = "named_entity_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id", nullable = false)
    protected Long id;

    @XmlAttribute(name = "name")
    @Index
    @Column(name = "name")
    protected String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "America/New_York")
    @XmlAttribute(name = "created")
    @Column(name = "created")
    protected Date created;

    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    @ManyToMany(targetEntity = Attribute.class, cascade = { CascadeType.REMOVE, CascadeType.PERSIST,
            CascadeType.MERGE }, fetch = FetchType.EAGER)
    @ContainerTable(name = "named_entity_attribute", joinIndex = @Index(columnNames = { "named_entity_fid" }) )
    @JoinTable(name = "named_entity_attribute", joinColumns = @JoinColumn(name = "named_entity_fid") , inverseJoinColumns = @JoinColumn(name = "attribute_fid") )
    protected Set<Attribute> attributes;

    @XmlElementWrapper(name = "files")
    @XmlElement(name = "file")
    @ManyToMany(targetEntity = FileData.class, cascade = { CascadeType.REMOVE, CascadeType.PERSIST,
            CascadeType.MERGE }, fetch = FetchType.EAGER)
    @ContainerTable(name = "named_entity_file_data", joinIndex = @Index(columnNames = { "named_entity_fid" }) )
    @JoinTable(name = "named_entity_file_data", joinColumns = @JoinColumn(name = "named_entity_fid") , inverseJoinColumns = @JoinColumn(name = "file_data_fid") )
    protected Set<FileData> fileDatas;

    public NamedEntity() {
        super();
        this.created = new Date();
        this.attributes = new HashSet<Attribute>();
        this.fileDatas = new HashSet<FileData>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Attribute> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Set<FileData> getFileDatas() {
        return this.fileDatas;
    }

    public void setFileDatas(Set<FileData> fileDatas) {
        this.fileDatas = fileDatas;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        NamedEntity other = (NamedEntity) obj;
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
