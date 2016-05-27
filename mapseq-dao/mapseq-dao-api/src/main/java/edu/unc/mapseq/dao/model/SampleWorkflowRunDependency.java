package edu.unc.mapseq.dao.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.DataCache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SampleWorkflowRunDependency", propOrder = {})
@XmlRootElement(name = "sampleWorkflowRunDependency")
@Entity
@Table(name = "sample_workflow_run_dependency")
@DataCache(enabled = false)
@Cacheable(value = false)
public class SampleWorkflowRunDependency implements Persistable {

    private static final long serialVersionUID = 8716839110762664102L;

    @XmlAttribute(name = "id")
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sample_workflow_run_dependency_id_seq")
    @SequenceGenerator(name = "sample_workflow_run_dependency_id_seq", schema = "mapseq", sequenceName = "sample_workflow_run_dependency_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sample_fid")
    private Sample sample;

    @ManyToOne
    @JoinColumn(name = "parent_workflow_run_fid")
    private WorkflowRun parent;

    @ManyToOne
    @JoinColumn(name = "child_workflow_run_fid")
    private WorkflowRun child;

    public SampleWorkflowRunDependency() {
        super();
    }

    public SampleWorkflowRunDependency(Sample sample, WorkflowRun parent, WorkflowRun child) {
        super();
        this.sample = sample;
        this.parent = parent;
        this.child = child;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public WorkflowRun getParent() {
        return parent;
    }

    public void setParent(WorkflowRun parent) {
        this.parent = parent;
    }

    public WorkflowRun getChild() {
        return child;
    }

    public void setChild(WorkflowRun child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return String.format("SampleWorkflowRunDependency [id=%s]", id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        SampleWorkflowRunDependency other = (SampleWorkflowRunDependency) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
