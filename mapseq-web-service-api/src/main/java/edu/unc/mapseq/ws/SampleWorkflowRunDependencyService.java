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
import edu.unc.mapseq.dao.model.SampleWorkflowRunDependency;

@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "SampleWorkflowRunDependencyService", portName = "SampleWorkflowRunDependencyPort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/SampleWorkflowRunDependencyService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SampleWorkflowRunDependencyService {

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public SampleWorkflowRunDependency findById(@PathParam("id") @WebParam(name = "id") Long id);

    @POST
    @Path("/")
    @WebMethod
    public Long save(
            @WebParam(name = "sampleWorkflowRunDependency") SampleWorkflowRunDependency sampleWorkflowRunDependency);

    @GET
    @Path("/findBySampleIdAndChildWorkflowRunId/{sampleId}/{workflowRunId}")
    @WebMethod
    public List<SampleWorkflowRunDependency> findBySampleIdAndChildWorkflowRunId(
            @PathParam("sampleId") @WebParam(name = "sampleId") Long sampleId,
            @PathParam("workflowRunId") @WebParam(name = "workflowRunId") Long workflowRunId) throws MaPSeqDAOException;

}
