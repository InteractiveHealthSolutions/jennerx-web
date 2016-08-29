package org.ird.unfepi.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.rest.helper.ProgramServiceHelper;
import org.json.simple.JSONObject;


@Path("/healthprogram")
public class ProgramService {
	
//	http://localhost:8080/unfepi/serv/healthprogram/list
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public List<String> getAllHealthProgram()
	{
		List<String> list = ProgramServiceHelper.getAllHealthPrograms();
		return list;
	}

}
