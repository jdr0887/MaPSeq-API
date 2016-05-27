package edu.unc.mapseq.workflow;

import edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType;

public class WorkflowRunStatusInfo {

    private WorkflowRunAttemptStatusType status;

    private String message;

    public WorkflowRunStatusInfo() {
        super();
    }

    public WorkflowRunStatusInfo(WorkflowRunAttemptStatusType status) {
        super();
        this.status = status;
    }

    public WorkflowRunStatusInfo(WorkflowRunAttemptStatusType status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public WorkflowRunAttemptStatusType getStatus() {
        return status;
    }

    public void setStatus(WorkflowRunAttemptStatusType status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("WorkflowRunStatusInfo [status=%s, message=%s]", status, message);
    }

}
