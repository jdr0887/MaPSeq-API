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

import edu.unc.mapseq.dao.model.Flowcell;

@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "FlowcellService", portName = "FlowcellPort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/FlowcellService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FlowcellService {

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public Flowcell findById(@PathParam("id") @WebParam(name = "id") Long id);

    @POST
    @Path("/")
    @WebMethod
    public Long save(@WebParam(name = "flowcell") Flowcell flowcell);

    @POST
    @Path("/addFileData/{fileDataId}/{flowcellId}")
    @WebMethod
    public void addFileData(@PathParam("fileDataId") @WebParam(name = "fileDataId") Long fileDataId,
            @PathParam("flowcellId") @WebParam(name = "flowcellId") Long jobId);

    @GET
    @Path("/findByCreatedDateRange/{started}/{finished}")
    @WebMethod
    public List<Flowcell> findByCreatedDateRange(@PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished);

    @GET
    @Path("/findAll")
    @WebMethod
    public List<Flowcell> findAll();

    @GET
    @Path("/findByStudyName/{name}")
    @WebMethod
    public List<Flowcell> findByStudyName(@PathParam("name") @WebParam(name = "name") String name);

    @GET
    @Path("/findByStudyId/{studyId}")
    @WebMethod
    public List<Flowcell> findByStudyId(@PathParam("studyId") @WebParam(name = "studyId") Long studyId);

    @GET
    @Path("/findByName/{flowcellName}")
    @WebMethod
    public List<Flowcell> findByName(@PathParam("flowcellName") @WebParam(name = "flowcellName") String flowcellName);

    @GET
    @Path("/findByWorkflowRunId/{workflowRunId}")
    @WebMethod
    public List<Flowcell> findByWorkflowRunId(
            @PathParam("workflowRunId") @WebParam(name = "workflowRunlId") Long workflowRunId);

}
