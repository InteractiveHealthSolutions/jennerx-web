package org.ird.unfepi.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({ "/s_home_incentive", 
				  "/s_export_unconsumed_incentive",
				  "/s_upload_processed_child_incentive",
				  "/s_home_reminder",
				  "/s_gallery", 
				  "/s_location_tree" })

public class ViewController {
	
	@RequestMapping
	public String test(HttpServletRequest request){
		return request.getServletPath().replace(".htm", "");
	}

}
