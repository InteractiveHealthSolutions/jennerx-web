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
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewLotterySmsController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
    	int totalRows=0;
    	Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			List<ReminderSms> list=new ArrayList<ReminderSms>();
			
			String childId_Name = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			String cellNumber = UnfepiUtils.getStringFilter(SearchFilter.CELL_NUMBER, req);
			Date dueDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req);
			Date dueDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req);
			Date sentDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE2_FROM, req);
			Date sentDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE2_TO, req);
			REMINDER_STATUS remstatus = (REMINDER_STATUS) UnfepiUtils.getEnumFilter(SearchFilter.SMS_STATUS, REMINDER_STATUS.class, req);	
			
			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;

			Integer childId = null;
			if(action == null && pagerOffset == null){//page is accessed first time
				dueDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*3L));
				dueDateto = new Date(System.currentTimeMillis()+(1000*60*60*24*2L));
			}
			else if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				if(childId_Name != null){
					try{
						childId = sc.getIdMapperService().findIdMapper(childId_Name).getMappedId();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			list = sc.getReminderService().findReminderSmsRecordByCriteria(childId, null, null, new ReminderType[]{ReminderType.LOTTERY_WON_REMINDER, ReminderType.LOTTERY_CONSUMED_REMINDER}, cellNumber, dueDatefrom, dueDateto, sentDatefrom, sentDateto, remstatus, false, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, null);

			Number r = sc.getReminderService().LAST_QUERY_TOTAL_ROW_COUNT(ReminderSms.class);
			totalRows = r==null?list.size():r.intValue();

			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), childId_Name);
			addModelAttribute(model, SearchFilter.CELL_NUMBER.FILTER_NAME(), cellNumber);
			addModelAttribute(model, SearchFilter.SMS_STATUS.FILTER_NAME(), remstatus);
			addModelAttribute(model, SearchFilter.DATE1_FROM.FILTER_NAME(), UnfepiUtils.setDateFilter(dueDatefrom));
			addModelAttribute(model, SearchFilter.DATE1_TO.FILTER_NAME(), UnfepiUtils.setDateFilter(dueDateto));
			addModelAttribute(model, SearchFilter.DATE2_FROM.FILTER_NAME(), UnfepiUtils.setDateFilter(sentDatefrom));
			addModelAttribute(model, SearchFilter.DATE2_TO.FILTER_NAME(), UnfepiUtils.setDateFilter(sentDateto));


			addModelAttribute(model, "datalist", list);
			addModelAttribute(model, "totalRows", totalRows);
			
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