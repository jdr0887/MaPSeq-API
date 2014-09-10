package edu.unc.mapseq.dao.model;

public enum WorkflowRunAttemptStatusType {

    HELD("held"),

    DONE("done"),

    PENDING("pending"),

    FAILED("failed"),

    RUNNING("running"),

    RESET("reset");

    private String state;

    private WorkflowRunAttemptStatusType(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
