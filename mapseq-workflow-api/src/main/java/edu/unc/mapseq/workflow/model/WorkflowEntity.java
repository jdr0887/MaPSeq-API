package edu.unc.mapseq.workflow.model;

import java.util.List;

public class WorkflowEntity {

    private String entityType;

    private Long id;

    private String name;

    private Long upstreamWorkflowRunId;

    private List<WorkflowAttribute> attributes;

    public WorkflowEntity() {
        super();
    }

    public Long getUpstreamWorkflowRunId() {
        return upstreamWorkflowRunId;
    }

    public void setUpstreamWorkflowRunId(Long upstreamWorkflowRunId) {
        this.upstreamWorkflowRunId = upstreamWorkflowRunId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WorkflowAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<WorkflowAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return String.format("WorkflowEntity [entityType=%s, id=%s, name=%s]", entityType, id, name);
    }

}
