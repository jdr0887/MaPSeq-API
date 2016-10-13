package edu.unc.mapseq.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.jdbc.ContainerTable;
import org.apache.openjpa.persistence.jdbc.Index;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkflowRun", propOrder = {})
@XmlRootElement(name = "workflowRun")
@Entity
@Table(schema = "mapseq", name = "workflow_run")
@NamedQueries({
        @NamedQuery(name = "WorkflowRun.findByWorkflowId", query = "SELECT a FROM WorkflowRun a where a.workflow.id = :id order by a.created") })
public class WorkflowRun extends NamedEntity {

    private static final long serialVersionUID = 8700198002784754453L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workflow_fid")
    private Workflow workflow;

    @XmlElementWrapper(name = "attempts")
    @XmlElement(name = "attempt")
    @OneToMany(mappedBy = "workflowRun", fetch = FetchType.LAZY)
    private Set<WorkflowRunAttempt> attempts;

    @XmlElementWrapper(name = "samples")
    @XmlElement(name = "sample")
    @ManyToMany(targetEntity = Sample.class, cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @ContainerTable(name = "workflow_run_sample", joinIndex = @Index(columnNames = { "workflow_run_fid" }))
    @JoinTable(name = "workflow_run_sample", joinColumns = @JoinColumn(name = "workflow_run_fid"), inverseJoinColumns = @JoinColumn(name = "sample_fid"))
    private Set<Sample> samples;

    @XmlElementWrapper(name = "flowcells")
    @XmlElement(name = "flowcell")
    @ManyToMany(targetEntity = Flowcell.class, cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @ContainerTable(name = "workflow_run_flowcell", joinIndex = @Index(columnNames = { "workflow_run_fid" }))
    @JoinTable(name = "workflow_run_flowcell", joinColumns = @JoinColumn(name = "workflow_run_fid"), inverseJoinColumns = @JoinColumn(name = "flowcell_fid"))
    private Set<Flowcell> flowcells;

    public WorkflowRun() {
        super();
        this.attempts = new HashSet<>();
        this.samples = new HashSet<>();
        this.flowcells = new HashSet<>();
    }

    public WorkflowRun(String name) {
        super(name);
        this.attempts = new HashSet<>();
        this.samples = new HashSet<>();
        this.flowcells = new HashSet<>();
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public Set<WorkflowRunAttempt> getAttempts() {
        return attempts;
    }

    public void setAttempts(Set<WorkflowRunAttempt> attempts) {
        this.attempts = attempts;
    }

    public Set<Flowcell> getFlowcells() {
        return flowcells;
    }

    public void setFlowcells(Set<Flowcell> flowcells) {
        this.flowcells = flowcells;
    }

    public Set<Sample> getSamples() {
        return samples;
    }

    public void setSamples(Set<Sample> samples) {
        this.samples = samples;
    }

    @Override
    public String toString() {
        return String.format("WorkflowRun [id=%s, name=%s, created=%s]", id, name, created);
    }

}
