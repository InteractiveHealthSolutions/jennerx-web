package org.ird.unfepi.rest.resources;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ResponseService") 
public class MobileResponder
{
	@Produces (MediaType.APPLICATION_JSON)
	public static String sendResponse(int resoponseCode)
	{
		return "";
	}
}
