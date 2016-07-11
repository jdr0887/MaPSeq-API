package edu.unc.mapseq.dao.model;

public enum WorkflowSystemType {

    PRODUCTION("prod"),

    DEVELOPMENT("dev"),

    EXPERIMENTAL("exp");

    private String value;

    private WorkflowSystemType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
