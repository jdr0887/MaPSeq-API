package edu.unc.mapseq.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@XmlType(name = "Workflow", propOrder = {})
@XmlRootElement(name = "workflow")
@Entity
@Table(schema = "mapseq", name = "workflow")
@NamedQueries({
        @NamedQuery(name = "Workflow.findAll", query = "SELECT a FROM Workflow a where a.active = TRUE order by a.name") })
@Cacheable
public class Workflow extends DictionaryEntity {

    private static final long serialVersionUID = -6745909962563675950L;

    @OneToMany(mappedBy = "workflow", fetch = FetchType.LAZY)
    private Set<WorkflowRun> workflowRuns;

    @Enumerated(EnumType.STRING)
    @Column(name = "system")
    private WorkflowSystemType system;

    public Workflow() {
        super();
        this.workflowRuns = new HashSet<>();
    }

    public Workflow(String name, WorkflowSystemType system) {
        super(name);
        this.workflowRuns = new HashSet<>();
        this.system = system;
    }

    public Set<WorkflowRun> getWorkflowRuns() {
        return workflowRuns;
    }

    public void setWorkflowRuns(Set<WorkflowRun> workflowRuns) {
        this.workflowRuns = workflowRuns;
    }

    public WorkflowSystemType getSystem() {
        return system;
    }

    public void setSystem(WorkflowSystemType system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return String.format("Workflow [id=%s, name=%s, created=%s, system=%s]", id, name, created, system);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((system == null) ? 0 : system.hashCode());
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
        Workflow other = (Workflow) obj;
        if (system != other.system)
            return false;
        return true;
    }

}
