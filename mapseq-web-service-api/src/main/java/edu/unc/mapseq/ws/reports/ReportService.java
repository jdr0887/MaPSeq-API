package edu.unc.mapseq.ws.reports;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@WebService(targetNamespace = "http://reports.ws.mapseq.unc.edu", serviceName = "ReportService", portName = "ReportPort")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Path("/ReportService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ReportService {

    @GET
    @Path("/findWorkflowRunCount/{started}/{finished}/{status}")
    @WebMethod
    public List<ReportDataItem> findWorkflowRunCount(@PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished,
            @PathParam("status") @WebParam(name = "status") String status);

    @GET
    @Path("/findWorkflowRunDuration/{started}/{finished}/{status}")
    @WebMethod
    public List<ReportDataItem> findWorkflowRunDuration(
            @PathParam("started") @WebParam(name = "started") String started,
            @PathParam("finished") @WebParam(name = "finished") String finished,
            @PathParam("status") @WebParam(name = "status") String status);

}
