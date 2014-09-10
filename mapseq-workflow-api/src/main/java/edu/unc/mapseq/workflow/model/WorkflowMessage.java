package edu.unc.mapseq.workflow.model;

import java.util.List;

public class WorkflowMessage {

    private List<WorkflowEntity> entities;

    public WorkflowMessage() {
        super();
    }

    public List<WorkflowEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<WorkflowEntity> entities) {
        this.entities = entities;
    }

}
