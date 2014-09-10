package edu.unc.mapseq.workflow.model;

public class WorkflowAttribute {

    private String name;

    private String value;

    public WorkflowAttribute() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("WorkflowAttribute [name=%s, value=%s]", name, value);
    }

}
