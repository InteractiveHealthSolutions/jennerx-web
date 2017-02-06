package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomePageController {
	
	@RequestMapping(value="/homepage", method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody String handleRequest() throws Exception {
		ServiceContext sc = Context.getServices();
		
		HashMap map = new HashMap();
		
		try {
			System.out.println("inside homepage ....");
			
			List records = sc.getCustomQueryService().getDataBySQLMapResult("select * from healthprogram");
			JSONArray data =  new JSONArray();
			for (Object object : records) {
				data.put(new JSONObject((HashMap)object));
				System.out.println(new JSONObject((HashMap)object).toString());
			}
			return data.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		} finally {
			sc.closeSession();
		}
		
	}
	
//	
//	@RequestMapping(value="/siteList/{programId}" , method=RequestMethod.GET)
//	public @ResponseBody String getSiteList(@PathVariable Integer programId){
//		ServiceContext sc = Context.getServices();
//		List<String> current_centers = sc.getCustomQueryService().getDataBySQL("select mappedId from vaccinationcenter where mappedId in (SELECT vaccinationCenterId FROM centerprogram WHERE healthProgramId = "+ programId + "  and isActive = true)");
//		sc.closeSession();
//		return current_centers.toString();
//	}

}
