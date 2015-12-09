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
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.IncentiveStatus;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.EncounterUtil.ElementVaccination;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewChildIncentivesController extends DataDisplayController{

	public enum TabIncentive{
		WON("won","won"),
		LOST("lost","lost"),
		ACTIVE("active","active"),
		INACTIVE("inactive","inactive"),
		ALL("all","all"),;
		
		private String type;
		private String title;
		
		public String TYPE(){return type;}
		public String TITLE(){return title;}
		private TabIncentive(String outcomeType, String tabTitle) {
			type = outcomeType;
			title = tabTitle;
		}
		
		public static String findTypeFromTitle(String title){
			for (TabIncentive tr : TabIncentive.values()) {
				if(tr.TITLE().equalsIgnoreCase(title)){
					return tr.TYPE();
				}
			}
			return null;
		}
		
	}
	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List list =new ArrayList();
			
			String programId = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			Date incentiveDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req);
			Date incentiveDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req);
			Integer amounttransferredrangeL = UnfepiUtils.getIntegerFilter(SearchFilter.AMOUNT1_LOWER, req);
			Integer amounttransferredrangeU = UnfepiUtils.getIntegerFilter(SearchFilter.AMOUNT1_UPPER, req);

			LoggedInUser user = UserSessionUtils.getActiveUser(req);
			Integer userLocation = user.getUser().getIdMapper().getIdentifiers().get(0).getLocationId();

			String type = req.getParameter("type");
			if(StringUtils.isEmptyOrWhitespaceOnly(type)){
				type = UnfepiUtils.getStringFilter(SearchFilter.TYPE, req);
			}
			if(StringUtils.isEmptyOrWhitespaceOnly(type)){
				type = TabIncentive.ACTIVE.TITLE();
			}
			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			if(action == null && pagerOffset == null){//page is accessed first time
				incentiveDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				incentiveDateto = new Date();
			}
			else {
				if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				}
			}
			IdMapper idm = null;
			if(!StringUtils.isEmptyOrWhitespaceOnly(programId)){
				idm = sc.getIdMapperService().findIdMapper(programId);
			}
			
			IncentiveStatus status = null;
		//	String code = null;
			if(type.equalsIgnoreCase(TabIncentive.ACTIVE.name())){
				status = IncentiveStatus.AVAILABLE;
			}
			else if(type.equalsIgnoreCase(TabIncentive.INACTIVE.name())){
				status = IncentiveStatus.WAITING;
			}
					
			List<ChildIncentive> listChildIncentive = sc.getIncentiveService().findChildIncentiveByCriteria(null, (idm == null?null:idm.getMappedId()), null, null ,status, incentiveDatefrom, incentiveDateto, null, null, amounttransferredrangeL, amounttransferredrangeU, /*null, */userLocation, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, new String[]{"vaccination"});
			
			for (ChildIncentive childIncentive : listChildIncentive) {
				Map<String, Object> incentivemap = new HashMap<String, Object>();
				incentivemap.put("incentive", childIncentive);
				incentivemap.put("reminder", sc.getReminderService().findReminderSmsRecordByCriteria(childIncentive.getVaccination().getChildId(), null, new Short[]{childIncentive.getVaccination().getVaccineId()}, new ReminderType[]{ReminderType.LOTTERY_WON_REMINDER}, null, null, null, null, null, null, false, 0, 10, true, null));
				String vaccrElemName = EncounterUtil.getEncounterElementName(EncounterUtil.getIncentiveEncounterPrefix(childIncentive.getVaccination().getVaccine().getName(), childIncentive.getVaccination().getVaccineId()), ElementVaccination.VACCINATION_RECORD_NUM);
				incentivemap.put("encounter", sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM encounterresults er JOIN encounter e USING(encounterId,p1id,p2id) WHERE er.p1id="+childIncentive.getVaccination().getChildId()+" AND element='"+vaccrElemName+"' AND value="+childIncentive.getVaccinationRecordNum()));
			
				list.add(incentivemap);
			}
			
			Number r = sc.getIncentiveService().LAST_QUERY_TOTAL_ROW_COUNT(ChildIncentive.class);
			totalRows = r==null?list.size():r.intValue();
			
			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), programId);
			addModelAttribute(model, SearchFilter.DATE1_FROM.FILTER_NAME(), UnfepiUtils.setDateFilter(incentiveDatefrom));
			addModelAttribute(model, SearchFilter.DATE1_TO.FILTER_NAME(), UnfepiUtils.setDateFilter(incentiveDateto));
			addModelAttribute(model, SearchFilter.AMOUNT1_LOWER.FILTER_NAME(), amounttransferredrangeL);
			addModelAttribute(model, SearchFilter.AMOUNT1_UPPER.FILTER_NAME(), amounttransferredrangeU);
			
			addModelAttribute(model, SearchFilter.TYPE.FILTER_NAME(), type);

			addModelAttribute(model, "datalist", list);
			addModelAttribute(model, "totalRows", totalRows);
			addModelAttribute(model, "dataLocationMessage", userLocation==null?"":"Showing data of <b>"+user.getUser().getIdMapper().getIdentifiers().get(0).getLocation().getName()+"</b> immunizations only");

			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			sc.closeSession();//incase of error close session
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			req.setAttribute("sc"	, sc);
			/*sc.closeSession();*///will close after page have been loaded
		}
	}
}
