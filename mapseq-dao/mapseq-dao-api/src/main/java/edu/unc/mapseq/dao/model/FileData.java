package edu.unc.mapseq.dao.model;

import java.io.File;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.DataCache;
import org.apache.openjpa.persistence.jdbc.Index;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileData", propOrder = {})
@XmlRootElement(name = "file")
@Entity
@DataCache(enabled = false)
@Cacheable(value = false)
@Table(name = "file_data")
public class FileData implements Persistable {

    private static final long serialVersionUID = -6855317876595139041L;

    @XmlAttribute(name = "id")
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_data_id_seq")
    @SequenceGenerator(name = "file_data_id_seq", sequenceName = "file_data_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id")
    private Long id;

    @XmlAttribute(name = "name")
    @Index
    @Column(name = "name")
    private String name;

    @XmlAttribute(name = "path")
    @Index
    @Column(name = "path", length = 2048)
    private String path;

    @XmlAttribute(name = "mimeType")
    @Column(name = "mime_type")
    @Enumerated(EnumType.STRING)
    private MimeType mimeType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "America/New_York")
    @XmlAttribute(name = "created")
    @Column(name = "created")
    private Date created;

    public FileData() {
        super();
        this.created = new Date();
    }

    public FileData(String name, String path, MimeType mimeType) {
        this();
        this.name = name;
        this.path = path;
        this.mimeType = mimeType;
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

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public File toFile() {
        return new File(this.path, this.name);
    }

    @Override
    public String toString() {
        return String.format("FileData [id=%s, name=%s, path=%s, mimeType=%s, created=%s]", id, name, path, mimeType,
                created);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
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
        FileData other = (FileData) obj;
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
        if (mimeType != other.mimeType)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }

}
