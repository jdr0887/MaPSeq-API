package edu.unc.mapseq.dao.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.DataCache;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkflowRunAttempt", propOrder = {})
@XmlRootElement(name = "workflowRunAttempt")
@Entity
@DataCache(enabled = false)
@Cacheable(value = false)
@Table(name = "workflow_run_attempt")
@NamedQueries({ @NamedQuery(name = "WorkflowRunAttempt.findByWorkflowRunId", query = "SELECT a FROM WorkflowRunAttempt a where a.workflowRun.id = :id order by a.created") })
public class WorkflowRunAttempt implements Persistable {

    private static final long serialVersionUID = 5301613685784983391L;

    @XmlAttribute(name = "id")
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workflow_run_attempt_id_seq")
    @SequenceGenerator(name = "workflow_run_attempt_id_seq", sequenceName = "workflow_run_attempt_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workflow_run_fid")
    private WorkflowRun workflowRun;

    @XmlAttribute(name = "status")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WorkflowRunAttemptStatusType status;

    @Column(name = "condor_dag_cluster_id")
    private Integer condorDAGClusterId;

    @Column(name = "submit_directory")
    private String submitDirectory;

    @XmlAttribute(name = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "America/New_York")
    @Column(name = "created")
    private Date created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "America/New_York")
    @Column(name = "started")
    private Date started;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "America/New_York")
    @Column(name = "dequeued")
    private Date dequeued;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "America/New_York")
    @Column(name = "finished")
    private Date finished;

    @XmlAttribute(name = "version")
    @Column(name = "version")
    private String version;

    @OneToMany(mappedBy = "workflowRunAttempt", fetch = FetchType.LAZY)
    private Set<Job> jobs;

    public WorkflowRunAttempt() {
        super();
        this.created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowRun getWorkflowRun() {
        return workflowRun;
    }

    public void setWorkflowRun(WorkflowRun workflowRun) {
        this.workflowRun = workflowRun;
    }

    public WorkflowRunAttemptStatusType getStatus() {
        return status;
    }

    public void setStatus(WorkflowRunAttemptStatusType status) {
        this.status = status;
    }

    public Integer getCondorDAGClusterId() {
        return condorDAGClusterId;
    }

    public void setCondorDAGClusterId(Integer condorDAGClusterId) {
        this.condorDAGClusterId = condorDAGClusterId;
    }

    public String getSubmitDirectory() {
        return submitDirectory;
    }

    public void setSubmitDirectory(String submitDirectory) {
        this.submitDirectory = submitDirectory;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getDequeued() {
        return dequeued;
    }

    public void setDequeued(Date dequeued) {
        this.dequeued = dequeued;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    @Override
    public String toString() {
        return String
                .format("WorkflowRunAttempt [id=%s, status=%s, condorDAGClusterId=%s, submitDirectory=%s, created=%s, started=%s, dequeued=%s, finished=%s, version=%s]",
                        id, status, condorDAGClusterId, submitDirectory, created, started, dequeued, finished, version);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((condorDAGClusterId == null) ? 0 : condorDAGClusterId.hashCode());
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        result = prime * result + ((dequeued == null) ? 0 : dequeued.hashCode());
        result = prime * result + ((finished == null) ? 0 : finished.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((started == null) ? 0 : started.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((submitDirectory == null) ? 0 : submitDirectory.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        WorkflowRunAttempt other = (WorkflowRunAttempt) obj;
        if (condorDAGClusterId == null) {
            if (other.condorDAGClusterId != null)
                return false;
        } else if (!condorDAGClusterId.equals(other.condorDAGClusterId))
            return false;
        if (created == null) {
            if (other.created != null)
                return false;
        } else if (!created.equals(other.created))
            return false;
        if (dequeued == null) {
            if (other.dequeued != null)
                return false;
        } else if (!dequeued.equals(other.dequeued))
            return false;
        if (finished == null) {
            if (other.finished != null)
                return false;
        } else if (!finished.equals(other.finished))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (started == null) {
            if (other.started != null)
                return false;
        } else if (!started.equals(other.started))
            return false;
        if (status != other.status)
            return false;
        if (submitDirectory == null) {
            if (other.submitDirectory != null)
                return false;
        } else if (!submitDirectory.equals(other.submitDirectory))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
