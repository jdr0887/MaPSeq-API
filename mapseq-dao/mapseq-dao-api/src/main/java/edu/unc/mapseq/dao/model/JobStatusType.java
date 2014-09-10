package edu.unc.mapseq.dao.model;

public enum JobStatusType {

    DONE("done"),

    FAILED("failed"),

    RUNNING("running");

    private String state;

    private JobStatusType(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
