package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class AddAddressController extends SimpleFormController
{
	private static final FormType formType = FormType.ADDRESS_ADD;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		Address add = (Address) command;
		ServiceContext sc = Context.getServices();
		
		try{
			if(true)
			throw new UnsupportedOperationException("ENCOUNTER RES thing not implemented");
			
			sc.getDemographicDetailsService().updateAddress(add);
			sc.commitTransaction();
			//TODO add encounter results
			//TODO add logging
			String editmessage = "updated successfully";
			return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage +"&childid="+add.getIdMapper().getIdentifiers().get(0).getIdentifier()));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception 
	{
		dateFormStart = new Date();
		
		String addressId=request.getParameter("ida");
		Address add = null;
		ServiceContext sc = Context.getServices();
		try{
			add = sc.getDemographicDetailsService().getAddressById(Integer.parseInt(addressId), false, new String[]{"idMapper"});
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while finding address. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		return add;
	}
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}else{
			ServiceContext sc = Context.getServices();
			try{
				List<Map<String, String>> cities = new ArrayList<Map<String,String>>();
				List cl = sc.getCustomQueryService().getDataByHQL("select cityId,cityName from City order by cityId");
				for (Object object : cl) {
					HashMap<String, String> city = new HashMap<String, String>();
					Object[] oar = (Object[]) object;
					city.put("cityId", oar[0].toString());
					city.put("cityName", oar[1].toString());
					cities.add(city);
				}			
				
				model.put("cities", cities);
			}catch (Exception e) {
				e.printStackTrace();
				GlobalParams.FILELOGGER.error(formType.name(), e);
				request.setAttribute("errorMessagev", "An error occurred while reference data list. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		
		
		return model;
	}
}
