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

import edu.unc.mapseq.dao.model.Study;

@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "StudyService", portName = "StudyPort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/StudyService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StudyService {

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public Study findById(@PathParam("id") @WebParam(name = "id") Long id);

    @GET
    @Path("/findByName/{name}")
    @WebMethod
    public List<Study> findByName(@PathParam("name") @WebParam(name = "name") String name);

    @GET
    @Path("/findAll")
    @WebMethod
    public List<Study> findAll();

    @GET
    @Path("/findBySampleId/{sampleId}")
    @WebMethod
    public Study findBySampleId(@PathParam("sampleId") @WebParam(name = "sampleId") Long sampleId);

    @GET
    @Path("/findByCreatedDateRange/{started}/{finished}")
    @WebMethod
    public List<Study> findByCreatedDateRange(@PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished);

    @POST
    @Path("/")
    @WebMethod
    public Long save(@WebParam(name = "study") Study study);

}
