package edu.unc.mapseq.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.DataCache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sample", propOrder = {})
@XmlRootElement(name = "sample")
@Entity
@DataCache(enabled = false)
@Cacheable(value = false)
@Table(name = "sample")
@NamedQueries({
        @NamedQuery(name = "Sample.findByFlowcellId", query = "SELECT a FROM Sample a where a.flowcell.id = :id order by a.created"),
        @NamedQuery(name = "Sample.findByNameAndFlowcellId", query = "SELECT a FROM Sample a where a.name = :name and a.flowcell.id = :id order by a.created") })
public class Sample extends NamedEntity {

    private static final long serialVersionUID = 8576719023767091419L;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "lane_index")
    private Integer laneIndex;

    @ManyToOne
    @JoinColumn(name = "study_fid")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "flowcell_fid")
    private Flowcell flowcell;

    @ManyToMany(mappedBy = "samples")
    private Set<WorkflowRun> workflowRuns;

    @OneToMany(mappedBy = "sample", fetch = FetchType.LAZY)
    private Set<SampleWorkflowRunDependency> workflowRunDependencies;

    public Sample() {
        super();
        this.workflowRuns = new HashSet<>();
    }

    public Set<SampleWorkflowRunDependency> getWorkflowRunDependencies() {
        return workflowRunDependencies;
    }

    public void setWorkflowRunDependencies(Set<SampleWorkflowRunDependency> workflowRunDependencies) {
        this.workflowRunDependencies = workflowRunDependencies;
    }

    public Sample(String name) {
        super(name);
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Flowcell getFlowcell() {
        return flowcell;
    }

    public void setFlowcell(Flowcell flowcell) {
        this.flowcell = flowcell;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getLaneIndex() {
        return laneIndex;
    }

    public void setLaneIndex(Integer laneIndex) {
        this.laneIndex = laneIndex;
    }

    public Set<WorkflowRun> getWorkflowRuns() {
        return workflowRuns;
    }

    public void setWorkflowRuns(Set<WorkflowRun> workflowRuns) {
        this.workflowRuns = workflowRuns;
    }

    @Override
    public String toString() {
        return String.format("Sample [id=%s, name=%s, created=%s, barcode=%s, laneIndex=%s]", id, name, created,
                barcode, laneIndex);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((laneIndex == null) ? 0 : laneIndex.hashCode());
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
        Sample other = (Sample) obj;
        if (barcode == null) {
            if (other.barcode != null)
                return false;
        } else if (!barcode.equals(other.barcode))
            return false;
        if (laneIndex == null) {
            if (other.laneIndex != null)
                return false;
        } else if (!laneIndex.equals(other.laneIndex))
            return false;
        return true;
    }

}
