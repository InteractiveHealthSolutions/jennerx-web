package org.ird.unfepi.rest.resources;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.MetadataServiceHelper;
import org.ird.unfepi.rest.helper.ResponseBuilder;

@Path("/metadata")
public class MetadataService
{	
	public String getMetadata(String data_request[])
	{
		String data=null;
		return data;		
	}
	
//	@GET
//	@Produces("application/zip")
//	@Consumes(MediaType.TEXT_PLAIN)
//	public Response dowloadAll(@QueryParam(RequestElements.METADATA_TYPE) String dataRequest)
//	{	
//		MetadataServiceHelper helper = new MetadataServiceHelper();
//		String metadata = helper.getMetadata(dataRequest);
//		
//		if(metadata == null)
//			return Response.status(Response.Status.NO_CONTENT).build();
//		
//		File tempFile = helper.createTempFile(metadata);
//		
//		if(tempFile== null)
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//		
//		File zipFile = helper.zipData(tempFile);
//		
//		if(zipFile!=null && zipFile.exists())
//		{
//			return Response.ok(zipFile, "application/zip").build();
//		}
//		else
//		{
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//		}
//	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	public String downloadAll()
	{	
		MetadataServiceHelper helper = new MetadataServiceHelper();
		
		try
		{
			String metadata = helper.getMetadata();
			return metadata;
		}
		catch (Exception e)
		{
			HashMap<String, Object>resp = new HashMap<String, Object>();
			resp.put("error", "Error occured in fetching data");
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE,resp);
		}
		
	}
	

}
