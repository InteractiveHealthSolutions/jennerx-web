package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.springframework.validation.Errors;

public class LotteryConsumerController extends DataEntryFormController{

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return new Object();
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception {
		Map<String, Object> model=new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			addModelAttribute(model, "storekeepers", sc.getStorekeeperService().getAllStorekeeper(true, new String[]{"idMapper"}));
		}
		catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace",e);
		}
		finally{
			sc.closeSession();
		}
		return model;
	}
}
