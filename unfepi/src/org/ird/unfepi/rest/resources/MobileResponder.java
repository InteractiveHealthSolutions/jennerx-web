package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

@Path("/ResponseService") 
public class MobileResponder
{
	@Produces (MediaType.APPLICATION_JSON)
	public static String sendResponse(int resoponseCode)
	{
		return "";
	}
}
