package edu.unc.mapseq.dao;

public interface MaPSeqDAOBeanService {

    public AttributeDAO getAttributeDAO();

    public void setAttributeDAO(AttributeDAO attributeDAO);

    public FileDataDAO getFileDataDAO();

    public void setFileDataDAO(FileDataDAO fileDataDAO);

    public SampleDAO getSampleDAO();

    public void setSampleDAO(SampleDAO sampleDAO);

    public JobDAO getJobDAO();

    public void setJobDAO(JobDAO jobDAO);

    public FlowcellDAO getFlowcellDAO();

    public void setFlowcellDAO(FlowcellDAO flowcellDAO);

    public StudyDAO getStudyDAO();

    public void setStudyDAO(StudyDAO studyDAO);

    public WorkflowDAO getWorkflowDAO();

    public void setWorkflowDAO(WorkflowDAO workflowDAO);

    public WorkflowRunAttemptDAO getWorkflowRunAttemptDAO();

    public void setWorkflowRunAttemptDAO(WorkflowRunAttemptDAO workflowRunAttemptDAO);

    public WorkflowRunDAO getWorkflowRunDAO();

    public void setWorkflowRunDAO(WorkflowRunDAO workflowRunDAO);

}