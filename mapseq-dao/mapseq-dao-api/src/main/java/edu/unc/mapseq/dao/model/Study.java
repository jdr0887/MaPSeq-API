package edu.unc.mapseq.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Study", propOrder = {})
@XmlRootElement(name = "study")
@Entity
@Table(schema = "mapseq", name = "study")
@NamedQueries({
        @NamedQuery(name = "Study.findAll", query = "SELECT a FROM Study a where a.active = TRUE order by a.name"),
        @NamedQuery(name = "Study.findSampleId", query = "SELECT a FROM Study a where a.active = TRUE and a.samples.id = :id order by a.name") })
@Cacheable
public class Study extends DictionaryEntity {

    private static final long serialVersionUID = -8860506395856309697L;

    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    private Set<Sample> samples;

    public Study() {
        super();
        this.samples = new HashSet<>();
    }

    public Study(String name) {
        super(name);
        this.samples = new HashSet<>();
    }

    public Set<Sample> getSamples() {
        return samples;
    }

    public void setSamples(Set<Sample> samples) {
        this.samples = samples;
    }

    @Override
    public String toString() {
        return String.format("Study [id=%s, name=%s, created=%s]", id, name, created);
    }

}
