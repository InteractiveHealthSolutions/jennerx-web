package org.ird.unfepi.web.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.DataViewForm;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ItemStock;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ViewItemController  extends DataDisplayController{
	
	ViewItemController(){
		super("dataForm", new  DataSearchForm("item", "Item", SystemPermissions.VIEW_ITEM, true));
		
	}
	
	@RequestMapping(value="/viewitem.htm", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		int totalRows=0;
		ServiceContext sc = Context.getServices();
		Map<String, Object> model = new LinkedHashMap<String, Object>();
		
		try{
			List<ItemStock> itemStocksL = sc.getCustomQueryService().getDataByHQL("from ItemStock");
			totalRows = itemStocksL.size();
			addModelAttribute(model,"itemStocks", itemStocksL);
			addModelAttribute(model, "totalRows", totalRows);
			
			return showForm(model);

		}catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");		
		}
		finally{
			sc.closeSession();
		}
		
	}
}
