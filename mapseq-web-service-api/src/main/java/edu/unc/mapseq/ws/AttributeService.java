package edu.unc.mapseq.ws;

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

import edu.unc.mapseq.dao.model.Attribute;

@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_MTOM_BINDING)
@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "AttributeService", portName = "AttributePort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/AttributeService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AttributeService {

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public Attribute findById(@PathParam("id") @WebParam(name = "id") Long id);

    @POST
    @Path("/")
    @WebMethod
    public Long save(@WebParam(name = "entity") Attribute entity);

}
