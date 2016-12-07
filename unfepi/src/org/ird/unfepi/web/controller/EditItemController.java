package org.ird.unfepi.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditForm;
import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ItemStock;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/edititem")
public class EditItemController extends DataEditFormController{
	
	private static final FormType formType = FormType.ITEM_CORRECT;
	
	EditItemController(){
		super(new  DataEditForm("item", "Item (Edit)", SystemPermissions.EDIT_ITEM));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addItemView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")ItemStock itemStock, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView) throws Exception {
		
		//validator
		if(results.hasErrors()){	
			System.out.println(results.toString());
			return showForm(modelAndView, "dataForm");	
		}
		
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		try{
//			System.out.println(itemStock.getName() + "  " + itemStock.getQuantity() + "  " + itemStock.getUnit_per_pack());
			
			sc.getCustomQueryService().update(itemStock);
			sc.commitTransaction();
			
			return new ModelAndView(new RedirectView("viewitem.htm"));
			
		} catch (Exception e) {
			sc.rollbackTransaction();
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			sc.closeSession();
		}
	}
	
	protected ItemStock formBackingObject(HttpServletRequest request) {
		ItemStock itemStock = null;
		
		String item_id = request.getParameter("iid");
		ServiceContext sc = Context.getServices();
		try {
			itemStock = (ItemStock) sc.getCustomQueryService().getDataByHQL("from ItemStock where itemRecordNum = "+ item_id).get(0);
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Location. Error message is:"+e.getMessage());
		
		} finally {
			sc.closeSession();
		}
		return itemStock;
	}
}
