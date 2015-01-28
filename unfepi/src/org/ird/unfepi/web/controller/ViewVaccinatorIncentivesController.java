package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.VaccinatorIncentiveParticipant;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction;
import org.ird.unfepi.model.VaccinatorIncentiveWorkProgress;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewVaccinatorIncentivesController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		int totalRows=0;
		Map<String, Object> model=new HashMap<String, Object>();
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List<VaccinatorIncentiveParticipant> list =new ArrayList<VaccinatorIncentiveParticipant>();
			
			Integer vaccinator = UnfepiUtils.getIntegerFilter(SearchFilter.VACCINATOR, req);
			Date incentiveDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req);
			Date incentiveDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req);
			Integer amounttransferredrangeL = UnfepiUtils.getIntegerFilter(SearchFilter.AMOUNT1_LOWER, req);
			Integer amounttransferredrangeU = UnfepiUtils.getIntegerFilter(SearchFilter.AMOUNT1_UPPER, req);

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			if(action == null && pagerOffset == null){//page is accessed first time
				incentiveDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				incentiveDateto = new Date();
			}
			else if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
			}
			
			list = sc.getIncentiveService().findVaccinatorIncentiveParticipantByCriteria(null, vaccinator, null, null, amounttransferredrangeL, amounttransferredrangeU, incentiveDatefrom, incentiveDateto, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, new String[]{"vaccinatorIncentiveEvent","vaccinatorIncentiveParams"});

			if(sc.getIncentiveService().LAST_QUERY_TOTAL_ROW_COUNT(VaccinatorIncentiveParticipant.class)!=null){
			totalRows = sc.getIncentiveService().LAST_QUERY_TOTAL_ROW_COUNT(VaccinatorIncentiveParticipant.class).intValue();
			}
			else{
				totalRows=list.size();
			}
			
			List<Map<String, Object>> incentiveMap = new ArrayList<Map<String,Object>>();
			for (VaccinatorIncentiveParticipant parti : list) {
				Map<String, Object> hm = new HashMap<String, Object>();
				hm.put("parti", parti);
				if(parti.getIsIncentivised() != null && parti.getIsIncentivised()){
					List<VaccinatorIncentiveTransaction> tr = sc.getIncentiveService().findVaccinatorIncentiveTransactionByCriteria(parti.getVaccinatorIncentiveEventId(), parti.getVaccinatorId(), null, null, null, null, null, 0, 1, true, null);
					hm.put("transac", tr.size()>0?tr.get(0):null);
				}
				
				List<VaccinatorIncentiveWorkProgress> wrk = sc.getIncentiveService().findVaccinatorIncentiveWorkProgressByCriteria(parti.getSerialNumber(), null, null, 0, 10, true, null);
				for (VaccinatorIncentiveWorkProgress vaccIncWrkProg : wrk) {
					hm.put(vaccIncWrkProg.getWorkType(), vaccIncWrkProg.getWorkValue());
				}
				
				incentiveMap.add(hm);
			}
			
			addModelAttribute(model, "vaccinators", sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));

			addModelAttribute(model, SearchFilter.VACCINATOR.FILTER_NAME(), vaccinator);
			addModelAttribute(model, SearchFilter.DATE1_FROM.FILTER_NAME(), UnfepiUtils.setDateFilter(incentiveDatefrom));
			addModelAttribute(model, SearchFilter.DATE1_TO.FILTER_NAME(), UnfepiUtils.setDateFilter(incentiveDateto));
			addModelAttribute(model, SearchFilter.AMOUNT1_LOWER.FILTER_NAME(), amounttransferredrangeL);
			addModelAttribute(model, SearchFilter.AMOUNT1_UPPER.FILTER_NAME(), amounttransferredrangeU);

			addModelAttribute(model, "datalist", incentiveMap);
			addModelAttribute(model, "totalRows", totalRows);
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}
}
