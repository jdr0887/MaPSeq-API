package edu.unc.mapseq.dao.model;

public enum WorkflowRunAttemptPhaseType {

    INIT("init"),

    VALIDATE("validate"),

    PRE_RUN("preRun"),

    RUN("run"),

    POST_RUN("postRun"),

    CLEAN("clean");

    private String phase;

    private WorkflowRunAttemptPhaseType(String phase) {
        this.phase = phase;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

}
