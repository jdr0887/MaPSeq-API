package edu.unc.mapseq.dao.model;

import java.util.HashSet;
import java.util.Set;

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
@XmlType(name = "Workflow", propOrder = {})
@XmlRootElement(name = "workflow")
@Entity
@Table(name = "workflow")
@NamedQueries({
        @NamedQuery(name = "Workflow.findAll", query = "SELECT a FROM Workflow a where a.active = TRUE order by a.name") })
public class Workflow extends DictionaryEntity {

    private static final long serialVersionUID = -6745909962563675950L;

    @OneToMany(mappedBy = "workflow", fetch = FetchType.LAZY)
    private Set<WorkflowRun> workflowRuns;

    public Workflow() {
        super();
        this.workflowRuns = new HashSet<>();
    }

    public Workflow(String name) {
        super(name);
        this.workflowRuns = new HashSet<>();
    }

    public Set<WorkflowRun> getWorkflowRuns() {
        return workflowRuns;
    }

    public void setWorkflowRuns(Set<WorkflowRun> workflowRuns) {
        this.workflowRuns = workflowRuns;
    }

    @Override
    public String toString() {
        return String.format("Workflow [id=%s, name=%s, created=%s]", id, name, created);
    }

}
