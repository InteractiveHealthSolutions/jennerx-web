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
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.UserSms;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewUserSmsController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    	int totalRows=0;
    	Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			List<UserSms> list=new ArrayList<UserSms>();
			
			String recpid = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			String cellNumber = UnfepiUtils.getStringFilter(SearchFilter.CELL_NUMBER, req);
			Date dueDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req);
			Date dueDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req);
			Date sentDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE2_FROM, req);
			Date sentDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE2_TO, req);
			SmsStatus status = (SmsStatus) UnfepiUtils.getEnumFilter(SearchFilter.SMS_STATUS, SmsStatus.class, req);	

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;

			if(action == null && pagerOffset == null){//page is accessed first time
				dueDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*3L));
				dueDateto = new Date(System.currentTimeMillis()+(1000*60*60*24*2L));
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
			}
			
			IdMapper idm = null;
			if(!StringUtils.isEmptyOrWhitespaceOnly(recpid)){
				idm = sc.getIdMapperService().findIdMapper(recpid);
				if(idm == null){
					idm = new IdMapper();//to make sure that it would not skip id in searching incentive
				}
			}

			list = sc.getUserSmsService().findUserSmsByCriteria(dueDatefrom, dueDateto, sentDatefrom, sentDateto, status, false, null, cellNumber, (idm==null?null:idm.getMappedId()), null, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, new String[]{"idMapper","createdByUserId"});

			if(sc.getUserSmsService().LAST_QUERY_TOTAL_ROW_COUNT(UserSms.class)!=null){
			totalRows= sc.getUserSmsService().LAST_QUERY_TOTAL_ROW_COUNT(UserSms.class).intValue();
			}
			else{
				totalRows=list.size();
			}

			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), recpid);
			addModelAttribute(model, SearchFilter.CELL_NUMBER.FILTER_NAME(), cellNumber);
			addModelAttribute(model, SearchFilter.SMS_STATUS.FILTER_NAME(), status);
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