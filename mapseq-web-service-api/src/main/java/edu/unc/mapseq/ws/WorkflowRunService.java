package edu.unc.mapseq.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.WorkflowRun;

@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "WorkflowRunService", portName = "WorkflowRunPort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/WorkflowRunService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface WorkflowRunService {

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public WorkflowRun findById(@PathParam("id") @WebParam(name = "id") Long id);

    @GET
    @Path("/findByWorkflowRunAttemptId/{workflowRunAttemptId}")
    @WebMethod
    public WorkflowRun findByWorkflowRunAttemptId(
            @PathParam("workflowRunAttemptId") @WebParam(name = "workflowRunAttemptId") Long workflowRunAttemptId);

    @GET
    @Path("/findByName/{name}")
    @WebMethod
    public List<WorkflowRun> findByName(@PathParam("name") @WebParam(name = "name") String name);

    @POST
    @Path("/")
    @WebMethod
    public Long save(@WebParam(name = "workflowRun") WorkflowRun workflowRun);

    @GET
    @Path("/findByCreatedDateRange/{started}/{finished}")
    @WebMethod
    public List<WorkflowRun> findByCreatedDateRange(@PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished);

    @GET
    @Path("/findByStudyNameAndSampleNameAndWorkflowName/{studyName}/{sampleName}/{workflowName}")
    @WebMethod
    public List<WorkflowRun> findByStudyNameAndSampleNameAndWorkflowName(
            @PathParam("studyName") @WebParam(name = "studyName") String studyName,
            @PathParam("sampleName") @WebParam(name = "sampleName") String sampleName,
            @PathParam("workflowName") @WebParam(name = "workflowName") String workflowName);

    @GET
    @Path("/findByFlowcellIdAndWorkflowId/{flowcellId}/{workflowId}")
    @WebMethod
    public List<WorkflowRun> findByFlowcellIdAndWorkflowId(
            @PathParam("flowcellId") @WebParam(name = "flowcellId") Long flowcellId,
            @PathParam("workflowId") @WebParam(name = "workflowId") Long workflowId);

    @GET
    @Path("/findByWorkflowId/{workflowId}")
    @WebMethod
    public List<WorkflowRun> findByWorkflowId(@PathParam("workflowId") @WebParam(name = "workflowId") Long workflowId)
            throws MaPSeqDAOException;

    @GET
    @Path("/findByFlowcellId/{flowcellId}")
    @WebMethod
    public List<WorkflowRun> findByFlowcellId(@PathParam("flowcellId") @WebParam(name = "flowcellId") Long flowcellId)
            throws MaPSeqDAOException;

    @GET
    @Path("/findBySampleId/{sampleId}")
    @WebMethod
    public List<WorkflowRun> findBySampleId(@PathParam("sampleId") @WebParam(name = "sampleId") Long sampleId)
            throws MaPSeqDAOException;

    @GET
    @Path("/findByStudyId/{studyId}")
    @WebMethod
    public List<WorkflowRun> findByStudyId(@PathParam("studyId") @WebParam(name = "studyId") Long studyId)
            throws MaPSeqDAOException;

}
