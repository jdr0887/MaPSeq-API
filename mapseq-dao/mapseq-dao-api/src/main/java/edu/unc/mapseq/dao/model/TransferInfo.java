package edu.unc.mapseq.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransferInfo", propOrder = {})
@XmlRootElement(name = "transferInfo")
@Entity
@Table(schema = "mapseq", name = "transfer_info")
public class TransferInfo {

    @XmlAttribute(name = "id")
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfer_info_id_seq")
    @SequenceGenerator(name = "transfer_info_id_seq", schema = "mapseq", sequenceName = "transfer_info_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "number_of_files")
    private Integer numberOfFiles;

    @Column(name = "bytes_transferred")
    private Long bytesSent;

    @Column(name = "transfer_rate")
    private Double transferRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_direction")
    private TransferDirectionType transferDirection;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "job_fid")
    private Job job;

    public TransferInfo() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberOfFiles() {
        return numberOfFiles;
    }

    public void setNumberOfFiles(Integer numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    public Long getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(Long bytesSent) {
        this.bytesSent = bytesSent;
    }

    public Double getTransferRate() {
        return transferRate;
    }

    public void setTransferRate(Double transferRate) {
        this.transferRate = transferRate;
    }

    public TransferDirectionType getTransferDirection() {
        return transferDirection;
    }

    public void setTransferDirection(TransferDirectionType transferDirection) {
        this.transferDirection = transferDirection;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return String.format(
                "TransferInfo [id=%s, numberOfFiles=%s, bytesSent=%s, transferRate=%s, transferDirection=%s]", id,
                numberOfFiles, bytesSent, transferRate, transferDirection);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bytesSent == null) ? 0 : bytesSent.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((job == null) ? 0 : job.hashCode());
        result = prime * result + ((numberOfFiles == null) ? 0 : numberOfFiles.hashCode());
        result = prime * result + ((transferDirection == null) ? 0 : transferDirection.hashCode());
        result = prime * result + ((transferRate == null) ? 0 : transferRate.hashCode());
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
        TransferInfo other = (TransferInfo) obj;
        if (bytesSent == null) {
            if (other.bytesSent != null)
                return false;
        } else if (!bytesSent.equals(other.bytesSent))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (job == null) {
            if (other.job != null)
                return false;
        } else if (!job.equals(other.job))
            return false;
        if (numberOfFiles == null) {
            if (other.numberOfFiles != null)
                return false;
        } else if (!numberOfFiles.equals(other.numberOfFiles))
            return false;
        if (transferDirection != other.transferDirection)
            return false;
        if (transferRate == null) {
            if (other.transferRate != null)
                return false;
        } else if (!transferRate.equals(other.transferRate))
            return false;
        return true;
    }

}
