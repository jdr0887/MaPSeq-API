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
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;

import edu.unc.mapseq.dao.model.Job;

@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "JobService", portName = "JobPort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_MTOM_BINDING)
@MTOM(enabled = true)
@Path("/JobService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface JobService {

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public Job findById(@PathParam("id") @WebParam(name = "id") Long id);

    @POST
    @Path("/")
    @WebMethod
    public Long save(@WebParam(name = "job") Job job);

    @GET
    @Path("/findByWorkflowRunAttemptId/{workflowRunAttemptId}")
    @WebMethod
    public List<Job> findByWorkflowRunAttemptId(
            @PathParam("workflowRunAttemptId") @WebParam(name = "workflowRunAttemptId") Long workflowRunAttemptId);

    @POST
    @Path("/addFileData/{fileDataId}/{jobId}")
    @WebMethod
    public void addFileData(@PathParam("fileDataId") @WebParam(name = "fileDataId") Long fileDataId,
            @PathParam("jobId") @WebParam(name = "jobId") Long jobId);

    @GET
    @Path("/findByWorkflowIdAndCreatedDateRange/{workflowId}/{started}/{finished}")
    @WebMethod
    public List<Job> findByWorkflowIdAndCreatedDateRange(
            @PathParam("workflowId") @WebParam(name = "workflowId") Long workflowId,
            @PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished);

    @GET
    @Path("/findByCreatedDateRange/{started}/{finished}")
    @WebMethod
    public List<Job> findByCreatedDateRange(@PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished);

}
