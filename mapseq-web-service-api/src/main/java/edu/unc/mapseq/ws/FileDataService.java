package edu.unc.mapseq.ws;

import java.util.List;

import javax.activation.DataHandler;
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
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;

import edu.unc.mapseq.dao.model.FileData;

@MTOM(enabled = true, threshold = 0)
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_MTOM_BINDING)
@WebService(targetNamespace = "http://ws.mapseq.unc.edu", serviceName = "FileDataService", portName = "FileDataPort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/FileDataService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FileDataService {

    @GET
    @Path("/findById/{id}")
    @WebMethod
    public FileData findById(@PathParam("id") @WebParam(name = "id") Long id);

    @POST
    @Path("/")
    @WebMethod
    public Long save(@WebParam(name = "entity") FileData entity);

    @POST
    @Path("/findByExample/")
    @WebMethod
    public List<FileData> findByExample(@WebParam(name = "fileData") FileData fileData);

    @WebMethod
    public Long upload(@XmlMimeType("application/octet-stream") @WebParam(name = "data") DataHandler data,
            @WebParam(name = "flowcell") String flowcell, @WebParam(name = "workflow") String workflow,
            @WebParam(name = "name") String name, @WebParam(name = "mimeType") String mimeType);

    @WebMethod
    @XmlMimeType("application/octet-stream")
    public DataHandler download(@WebParam(name = "id") Long id);

}
