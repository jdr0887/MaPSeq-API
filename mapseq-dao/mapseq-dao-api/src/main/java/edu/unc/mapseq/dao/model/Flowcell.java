package edu.unc.mapseq.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.DataCache;
import org.apache.openjpa.persistence.ElementDependent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Flowcell", propOrder = {})
@XmlRootElement(name = "flowcell")
@Entity
@Table(name = "flowcell")
@DataCache(enabled = false)
@Cacheable(value = false)
@NamedQueries({ @NamedQuery(name = "Flowcell.findAll", query = "SELECT a FROM Flowcell a order by a.created") })
public class Flowcell extends NamedEntity {

    private static final long serialVersionUID = -1227669306463166971L;

    @Column(name = "base_directory", length = 32672)
    private String baseDirectory;

    @OneToMany(mappedBy = "flowcell", fetch = FetchType.LAZY, orphanRemoval = true)
    @ElementDependent
    private Set<Sample> samples;

    @ManyToMany(mappedBy = "flowcells")
    private Set<WorkflowRun> workflowRuns;

    public Flowcell() {
        super();
    }

    public Set<WorkflowRun> getWorkflowRuns() {
        return workflowRuns;
    }

    public void setWorkflowRuns(Set<WorkflowRun> workflowRuns) {
        this.workflowRuns = workflowRuns;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public Set<Sample> getSamples() {
        if (samples == null) {
            return new HashSet<Sample>();
        }
        return samples;
    }

    public void setSamples(Set<Sample> samples) {
        this.samples = samples;
    }

    @Override
    public String toString() {
        return String.format("Flowcell [id=%s, name=%s, created=%s, baseDirectory=%s]", id, name, created,
                baseDirectory);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((baseDirectory == null) ? 0 : baseDirectory.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Flowcell other = (Flowcell) obj;
        if (baseDirectory == null) {
            if (other.baseDirectory != null)
                return false;
        } else if (!baseDirectory.equals(other.baseDirectory))
            return false;
        return true;
    }

}
