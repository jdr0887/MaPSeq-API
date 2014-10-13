package edu.unc.mapseq.dao.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.DataCache;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Job", propOrder = {})
@XmlRootElement(name = "job")
@Entity
@Table(name = "job")
@DataCache(enabled = false)
@Cacheable(value = false)
@NamedQueries({ @NamedQuery(name = "Job.findByWorkflowRunAttemptId", query = "SELECT a FROM Job a where a.workflowRunAttempt.id = :id order by a.created") })
public class Job extends NamedEntity {

    private static final long serialVersionUID = -2076318051537463578L;

    @ManyToOne
    @JoinColumn(name = "workflow_run_attempt_fid")
    private WorkflowRunAttempt workflowRunAttempt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatusType status;

    @Transient
    @Lob
    @Column(name = "stdout")
    private String stdout;

    @Transient
    @Lob
    @Column(name = "stderr")
    private String stderr;

    @Column(name = "exit_code")
    private Integer exitCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "America/New_York")
    @Column(name = "started")
    private Date started;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "America/New_York")
    @Column(name = "finished")
    private Date finished;

    @OneToMany(mappedBy = "job", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<TransferInfo> transfers;

    public Job() {
        super();
    }

    public WorkflowRunAttempt getWorkflowRunAttempt() {
        return workflowRunAttempt;
    }

    public void setWorkflowRunAttempt(WorkflowRunAttempt workflowRunAttempt) {
        this.workflowRunAttempt = workflowRunAttempt;
    }

    public JobStatusType getStatus() {
        return status;
    }

    public void setStatus(JobStatusType status) {
        this.status = status;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public Set<TransferInfo> getTransfers() {
        if (transfers == null) {
            return new HashSet<TransferInfo>();
        }
        return transfers;
    }

    public void setTransfers(Set<TransferInfo> transfers) {
        this.transfers = transfers;
    }

    @Override
    public String toString() {
        return String.format("Job [status=%s, exitCode=%s, started=%s, finished=%s, id=%s, name=%s, created=%s]",
                status, exitCode, started, finished, id, name, created);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((exitCode == null) ? 0 : exitCode.hashCode());
        result = prime * result + ((finished == null) ? 0 : finished.hashCode());
        result = prime * result + ((started == null) ? 0 : started.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        Job other = (Job) obj;
        if (exitCode == null) {
            if (other.exitCode != null)
                return false;
        } else if (!exitCode.equals(other.exitCode))
            return false;
        if (finished == null) {
            if (other.finished != null)
                return false;
        } else if (!finished.equals(other.finished))
            return false;
        if (started == null) {
            if (other.started != null)
                return false;
        } else if (!started.equals(other.started))
            return false;
        if (status != other.status)
            return false;
        return true;
    }

}
