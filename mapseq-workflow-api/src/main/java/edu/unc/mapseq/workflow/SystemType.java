package edu.unc.mapseq.workflow;

public enum SystemType {

    PRODUCTION("prod"),

    DEVELOPMENT("dev"),

    EXPERIMENTAL("exp");

    private String value;

    private SystemType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
