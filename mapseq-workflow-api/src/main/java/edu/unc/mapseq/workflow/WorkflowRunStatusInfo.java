package edu.unc.mapseq.workflow;

import edu.unc.mapseq.dao.model.WorkflowRunAttemptPhaseType;
import edu.unc.mapseq.dao.model.WorkflowRunAttemptStatusType;

public class WorkflowRunStatusInfo {

    private WorkflowRunAttemptStatusType status;

    private WorkflowRunAttemptPhaseType phase;

    private String message;

    public WorkflowRunStatusInfo() {
        super();
    }

    public WorkflowRunStatusInfo(WorkflowRunAttemptStatusType status, WorkflowRunAttemptPhaseType phase) {
        super();
        this.status = status;
        this.phase = phase;
    }

    public WorkflowRunStatusInfo(WorkflowRunAttemptStatusType status, WorkflowRunAttemptPhaseType phase,
            String message) {
        super();
        this.status = status;
        this.phase = phase;
        this.message = message;
    }

    public WorkflowRunAttemptStatusType getStatus() {
        return status;
    }

    public void setStatus(WorkflowRunAttemptStatusType status) {
        this.status = status;
    }

    public WorkflowRunAttemptPhaseType getPhase() {
        return phase;
    }

    public void setPhase(WorkflowRunAttemptPhaseType phase) {
        this.phase = phase;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("WorkflowRunStatusInfo [status=%s, phase=%s, message=%s]", status, phase, message);
    }

}
