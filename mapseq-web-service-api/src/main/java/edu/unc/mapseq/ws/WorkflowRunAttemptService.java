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
import edu.unc.mapseq.dao.model.WorkflowRunAttempt;

@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "WorkflowRunAttemptService", portName = "WorkflowRunAttemptPort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/WorkflowRunAttemptService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface WorkflowRunAttemptService {

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public WorkflowRunAttempt findById(@PathParam("id") @WebParam(name = "id") Long id);

    @GET
    @Path("/findByWorkflowRunId/{workflowRunId}")
    @WebMethod
    public List<WorkflowRunAttempt> findByWorkflowRunId(
            @PathParam("workflowRunId") @WebParam(name = "workflowRunId") Long workflowRunId) throws MaPSeqDAOException;

    @GET
    @Path("/findByCreatedDateRangeAndWorkflowId/{started}/{finished}/{workflowId}")
    @WebMethod
    public List<WorkflowRunAttempt> findByCreatedDateRangeAndWorkflowId(
            @PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished,
            @PathParam("workflowId") @WebParam(name = "workflowId") Long workflowId);

    @GET
    @Path("/findByCreatedDateRangeAndWorkflowIdAndStatus/{started}/{finished}/{workflowId}/{status}")
    @WebMethod
    public List<WorkflowRunAttempt> findByCreatedDateRangeAndWorkflowIdAndStatus(
            @PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished,
            @PathParam("workflowId") @WebParam(name = "workflowId") Long workflowId,
            @PathParam("status") @WebParam(name = "status") String status);

    @GET
    @Path("/findByWorkflowId/{workflowId}")
    @WebMethod
    public List<WorkflowRunAttempt> findByWorkflowId(
            @PathParam("workflowId") @WebParam(name = "workflowId") Long workflowId) throws MaPSeqDAOException;

    @POST
    @Path("/")
    @WebMethod
    public Long save(@WebParam(name = "workflowRunAttempt") WorkflowRunAttempt workflowRunAttempt);

    @GET
    @Path("/findByWorkflowNameAndStatus/{workflowName}/{status}")
    @WebMethod
    public List<WorkflowRunAttempt> findByWorkflowNameAndStatus(
            @PathParam("workflowName") @WebParam(name = "workflowName") String workflowName,
            @PathParam("status") @WebParam(name = "status") String status) throws MaPSeqDAOException;

}
