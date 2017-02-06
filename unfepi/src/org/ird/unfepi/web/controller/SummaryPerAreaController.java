package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SummaryPerAreaController {
	
	@RequestMapping(value="/summaryPerArea", method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody String getsummaryPerArea(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ServiceContext sc = Context.getServices();
		
		int pageNumber =  req.getParameter("page") == null ?0 : Integer.parseInt(req.getParameter("page"))-1;
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");

		int pageSize = req.getParameter("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(req.getParameter("rows"));

		
		HashMap map = new HashMap();
		JSONObject response = new JSONObject();
		String query = "SELECT "
				+"TIMESTAMPDIFF(DAY,r.startDate,v.vaccinationDate) 'day', "
				+"c.name site,  "
//				+"r.roundId, "
//				+"r.healthProgramId, "
				+"COUNT(DISTINCT CASE WHEN vc.name LIKE 'OPV%' THEN v.vaccineId ELSE NULL END) opv,  "
				+"COUNT(DISTINCT CASE WHEN vc.name = 'BCG' THEN v.vaccineId ELSE NULL END) bcg,  "
				+"COUNT(DISTINCT CASE WHEN vc.name LIKE 'Penta%' THEN v.vaccineId ELSE NULL END) penta,  "
				+"COUNT(DISTINCT CASE WHEN vc.name LIKE 'PCV%' THEN v.vaccineId ELSE NULL END) pcv,  "
				+"COUNT(DISTINCT CASE WHEN vc.name LIKE 'Measles%' THEN v.vaccineId ELSE NULL END) measles,  "
				+"COUNT(DISTINCT CASE WHEN vc.name LIKE 'Yellow%' THEN v.vaccineId ELSE NULL END) yellowFever  "
				+"FROM round r  "
				+"LEFT JOIN vaccination v ON v.roundId=r.roundId AND v.vaccinationStatus = 'VACCINATED'  "
				+"LEFT JOIN vaccine vc ON v.vaccineId=vc.vaccineId  "
				+"LEFT JOIN vaccinationcenter c ON c.mappedId=v.vaccinationCenterId  "
				+"GROUP BY r.roundId, day, v.vaccinationCenterId";
		
		try {
			List records = sc.getCustomQueryService().getDataBySQLMapResult(query);
			JSONArray data =  new JSONArray();
			for (Object object : records) {
				data.put(new JSONObject((HashMap)object));
//					System.out.println(new JSONObject((HashMap)object).toString());
			}
			response.put("rows", data);
			response.put("total", records.size());
			System.out.println(response.toString());
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "error ";
		} finally {
			sc.closeSession();
		}
	}
	
}
