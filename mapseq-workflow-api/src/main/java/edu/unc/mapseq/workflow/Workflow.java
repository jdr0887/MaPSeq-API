package edu.unc.mapseq.workflow;

import java.util.concurrent.Callable;

import org.jgrapht.Graph;
import org.renci.jlrm.condor.CondorJob;
import org.renci.jlrm.condor.CondorJobEdge;

import edu.unc.mapseq.dao.model.WorkflowRunAttempt;

/**
 * 
 * @author jdr0887
 */
public interface Workflow extends Callable<CondorJob> {

    public abstract Graph<CondorJob, CondorJobEdge> createGraph() throws WorkflowException;

    public abstract Integer getBackOffMultiplier();

    public abstract void setBackOffMultiplier(Integer backOffMultiplier);

    public abstract WorkflowBeanService getWorkflowBeanService();

    public abstract void setWorkflowBeanService(WorkflowBeanService pipelineBeanService);

    public abstract void init() throws WorkflowException;

    public abstract void validate() throws WorkflowException;

    public abstract void preRun() throws WorkflowException;

    public abstract void postRun() throws WorkflowException;

    public abstract void cleanUp() throws WorkflowException;

    public abstract WorkflowRunAttempt getWorkflowRunAttempt();

    public abstract String getVersion();

    public abstract String getName();

}
