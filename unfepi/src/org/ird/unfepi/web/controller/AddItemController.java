package org.ird.unfepi.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ItemStock;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.ItemValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/additem")
public class AddItemController extends DataEntryFormController{
	
	private static final FormType formType = FormType.ITEM_ADD;
	private Date dateFormStart = new Date();
	
	public AddItemController() {
		super(new DataEntryForm("item", "Item (New)", SystemPermissions.ADD_ITEM));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addItemView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", new ItemStock());
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")ItemStock itemStock, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView) throws Exception {
		
		new ItemValidator().validate(itemStock, results);
		if(results.hasErrors()){	
//			System.out.println(results.toString());
			return showForm(modelAndView, "dataForm");	
		}
		
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		try{
//			System.out.println(itemStock.getName() + "  " + itemStock.getQuantity() + "  " + itemStock.getUnit_per_pack());
			
			sc.getCustomQueryService().save(itemStock);
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
	
	
}
