package edu.unc.mapseq.dao;

public class MaPSeqDAOBean {

    private AttributeDAO attributeDAO;

    private FileDataDAO fileDataDAO;

    private SampleDAO sampleDAO;

    private JobDAO jobDAO;

    private FlowcellDAO flowcellDAO;

    private StudyDAO studyDAO;

    private WorkflowDAO workflowDAO;

    private WorkflowRunAttemptDAO workflowRunAttemptDAO;

    private WorkflowRunDAO workflowRunDAO;

    public MaPSeqDAOBean() {
        super();
    }

    public AttributeDAO getAttributeDAO() {
        return attributeDAO;
    }

    public void setAttributeDAO(AttributeDAO attributeDAO) {
        this.attributeDAO = attributeDAO;
    }

    public FileDataDAO getFileDataDAO() {
        return fileDataDAO;
    }

    public void setFileDataDAO(FileDataDAO fileDataDAO) {
        this.fileDataDAO = fileDataDAO;
    }

    public SampleDAO getSampleDAO() {
        return sampleDAO;
    }

    public void setSampleDAO(SampleDAO sampleDAO) {
        this.sampleDAO = sampleDAO;
    }

    public JobDAO getJobDAO() {
        return jobDAO;
    }

    public void setJobDAO(JobDAO jobDAO) {
        this.jobDAO = jobDAO;
    }

    public FlowcellDAO getFlowcellDAO() {
        return flowcellDAO;
    }

    public void setFlowcellDAO(FlowcellDAO flowcellDAO) {
        this.flowcellDAO = flowcellDAO;
    }

    public StudyDAO getStudyDAO() {
        return studyDAO;
    }

    public void setStudyDAO(StudyDAO studyDAO) {
        this.studyDAO = studyDAO;
    }

    public WorkflowDAO getWorkflowDAO() {
        return workflowDAO;
    }

    public void setWorkflowDAO(WorkflowDAO workflowDAO) {
        this.workflowDAO = workflowDAO;
    }

    public WorkflowRunAttemptDAO getWorkflowRunAttemptDAO() {
        return workflowRunAttemptDAO;
    }

    public void setWorkflowRunAttemptDAO(WorkflowRunAttemptDAO workflowRunAttemptDAO) {
        this.workflowRunAttemptDAO = workflowRunAttemptDAO;
    }

    public WorkflowRunDAO getWorkflowRunDAO() {
        return workflowRunDAO;
    }

    public void setWorkflowRunDAO(WorkflowRunDAO workflowRunDAO) {
        this.workflowRunDAO = workflowRunDAO;
    }

}
