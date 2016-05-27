package edu.unc.mapseq.dao.jpa;

import edu.unc.mapseq.dao.AttributeDAO;
import edu.unc.mapseq.dao.FileDataDAO;
import edu.unc.mapseq.dao.FlowcellDAO;
import edu.unc.mapseq.dao.JobDAO;
import edu.unc.mapseq.dao.MaPSeqDAOBeanService;
import edu.unc.mapseq.dao.SampleDAO;
import edu.unc.mapseq.dao.SampleWorkflowRunDependencyDAO;
import edu.unc.mapseq.dao.StudyDAO;
import edu.unc.mapseq.dao.WorkflowDAO;
import edu.unc.mapseq.dao.WorkflowRunAttemptDAO;
import edu.unc.mapseq.dao.WorkflowRunDAO;

public class MaPSeqDAOBeanServiceImpl implements MaPSeqDAOBeanService {

    private AttributeDAO attributeDAO;

    private FileDataDAO fileDataDAO;

    private SampleDAO sampleDAO;

    private SampleWorkflowRunDependencyDAO sampleWorkflowRunDependencyDAO;

    private JobDAO jobDAO;

    private FlowcellDAO flowcellDAO;

    private StudyDAO studyDAO;

    private WorkflowDAO workflowDAO;

    private WorkflowRunAttemptDAO workflowRunAttemptDAO;

    private WorkflowRunDAO workflowRunDAO;

    public MaPSeqDAOBeanServiceImpl() {
        super();
    }

    public SampleWorkflowRunDependencyDAO getSampleWorkflowRunDependencyDAO() {
        return sampleWorkflowRunDependencyDAO;
    }

    public void setSampleWorkflowRunDependencyDAO(SampleWorkflowRunDependencyDAO sampleWorkflowRunDependencyDAO) {
        this.sampleWorkflowRunDependencyDAO = sampleWorkflowRunDependencyDAO;
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
