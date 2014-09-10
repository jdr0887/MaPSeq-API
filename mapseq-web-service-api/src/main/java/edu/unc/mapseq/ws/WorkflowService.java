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

import edu.unc.mapseq.dao.model.Workflow;

@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "WorkflowService", portName = "WorkflowPort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/WorkflowService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface WorkflowService {

    @GET
    @Path("/findAll")
    @WebMethod
    public List<Workflow> findAll();

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public Workflow findById(@PathParam("id") @WebParam(name = "id") Long id);

    @GET
    @Path("/findByName/{name}")
    @WebMethod
    public List<Workflow> findByName(@PathParam("name") @WebParam(name = "name") String name);

    @POST
    @Path("/")
    @WebMethod
    public Long save(@WebParam(name = "entity") Workflow entity);

    @GET
    @Path("/findByCreatedDateRange/{started}/{finished}")
    @WebMethod
    public List<Workflow> findByCreatedDateRange(@PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished);

}
