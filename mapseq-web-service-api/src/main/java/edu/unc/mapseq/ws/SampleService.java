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
import edu.unc.mapseq.dao.model.Sample;

@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "SampleService", portName = "SamplePort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/SampleService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SampleService {

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public Sample findById(@PathParam("id") @WebParam(name = "id") Long id);

    @POST
    @Path("/")
    @WebMethod
    public Long save(@WebParam(name = "sample") Sample sample);

    @GET
    @Path("/findByFlowcellId/{flowcellId}")
    @WebMethod
    public List<Sample> findByFlowcellId(@PathParam("flowcellId") @WebParam(name = "flowcellId") Long flowcellId);

    @GET
    @Path("/findByWorkflowRunId/{workflowRunId}")
    @WebMethod
    public List<Sample> findByWorkflowRunId(
            @PathParam("workflowRunId") @WebParam(name = "workflowRunlId") Long workflowRunId);

    @GET
    @Path("/findByNameAndFlowcellId/{name}/{flowcellId}")
    @WebMethod
    public List<Sample> findByNameAndFlowcellId(@PathParam("name") @WebParam(name = "name") String name,
            @PathParam("flowcellId") @WebParam(name = "flowcellId") Long flowcellId);

    @GET
    @Path("/findByFlowcellNameAndSampleNameAndLaneIndex/{flowcellName}/{sampleName}/{laneIndex}")
    @WebMethod
    public List<Sample> findByFlowcellNameAndSampleNameAndLaneIndex(
            @PathParam("flowcellName") @WebParam(name = "flowcellName") String flowcellName,
            @PathParam("sampleName") @WebParam(name = "sampleName") String sampleName,
            @PathParam("laneIndex") @WebParam(name = "laneIndex") Integer laneIndex);

    @GET
    @Path("/findByCreatedDateRange/{started}/{finished}")
    @WebMethod
    public List<Sample> findByCreatedDateRange(@PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished);

    @POST
    @Path("/addFileData/{fileDataId}/{sampleId}")
    @WebMethod
    public void addFileData(@PathParam("fileDataId") @WebParam(name = "fileDataId") Long fileDataId,
            @PathParam("sampleId") @WebParam(name = "sampleId") Long sampleId);

    @GET
    @Path("/findSampleIdListByFlowcellId/{flowcellId}")
    @WebMethod
    public List<Long> findSampleIdListByFlowcellId(
            @PathParam("flowcellId") @WebParam(name = "flowcellId") Long flowcellId);

    @GET
    @Path("/findByName/{name}")
    @WebMethod
    public List<Sample> findByName(@PathParam("name") @WebParam(name = "name") String name);

}
