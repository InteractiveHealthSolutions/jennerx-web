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
import org.ird.unfepi.model.CommunicationNote;
import org.ird.unfepi.model.CommunicationNote.CommunicationCategory;
import org.ird.unfepi.model.CommunicationNote.CommunicationEventType;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Response.ResponseType;
import org.ird.unfepi.model.UserSms;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewEpiCertificatesController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			List<Map<String, Object>> list =new ArrayList<Map<String, Object>>();
			
			String id = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			String originator = UnfepiUtils.getStringFilter(SearchFilter.ORIGINATOR, req);
			String recipient = UnfepiUtils.getStringFilter(SearchFilter.RECIPIENT, req);
			Date datefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req);
			Date dateto = UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req);

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			
			if(action == null && pagerOffset == null){//page is accessed first time
				datefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				dateto = new Date();
			}
			else if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
			}
			
			Integer mappedId = null;
			if(id != null){
				IdMapper idm = sc.getIdMapperService().findIdMapper(id);
				if(idm != null){
					mappedId = idm.getMappedId();
				}
			}
			
			String[] oarr = StringUtils.isEmptyOrWhitespaceOnly(originator)?null:new String[]{originator};
			String[] rarr = StringUtils.isEmptyOrWhitespaceOnly(recipient)?null:new String[]{recipient};
			//Inbound that triggered the inquiry-identification-response cycle
			List<Response> inquiryl = sc.getCommunicationService().getResponseByCriteria(mappedId, 
					null, false, datefrom, dateto, oarr, rarr, 
					new ResponseType[]{ResponseType.VACCINE_SCHEDULE_INQUIRY}, null, null, null, null, 
					null, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, null, null);

			for (Response r : inquiryl) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("inquirysms", r);
				List<CommunicationNote> inquirynote = sc.getCommunicationService().findCommunicationNoteByCriteria(null, null, null, null, 
							new CommunicationEventType[]{CommunicationEventType.INBOUND}, new CommunicationCategory[]{CommunicationCategory.VACCINE_SCHEDULE_INQUIRY}, 
							Response.class, r.getResponseId(), 0, 10, true, null, null);
				if(inquirynote.size() > 1){
					throw new IllegalStateException("Multiple inbound note ids associated with same inquiry id "+r.getResponseId());
				}

				if(inquirynote.size() == 1){
					m.put("inquirynote", inquirynote.get(0));
					
					//Outbound sent as reply to the inquiry
					List<CommunicationNote> replyl = sc.getCommunicationService().getCommunicationNoteByGroupId(inquirynote.get(0).getGroupId(), new CommunicationEventType[]{CommunicationEventType.OUTBOUND}, null, true, null);
					if(replyl.size() > 1){
						throw new IllegalStateException("Multiple outbound ids associated with same inquiry id "+r.getResponseId());
					}
					if(replyl.size()>0){
						CommunicationNote reply = replyl.get(0);
						m.put("replynote", reply);
						if(!StringUtils.isEmptyOrWhitespaceOnly(reply.getProbeId()) 
								&& !StringUtils.isEmptyOrWhitespaceOnly(reply.getProbeClass()) 
								&& reply.getProbeClass().equalsIgnoreCase(UserSms.class.getName())){
							m.put("replysms", sc.getUserSmsService().findUserSmsById(Integer.parseInt(reply.getProbeId())));
						}
					}
				}
				
				list.add(m);
			}
			
			if(sc.getReportService().LAST_QUERY_TOTAL_ROW_COUNT(CommunicationNote.class)!=null){
				totalRows = sc.getReportService().LAST_QUERY_TOTAL_ROW_COUNT(CommunicationNote.class).intValue();
			}
			else{
				totalRows=list.size();
			}

			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), id);
			addModelAttribute(model, SearchFilter.ORIGINATOR.FILTER_NAME(), originator);
			addModelAttribute(model, SearchFilter.RECIPIENT.FILTER_NAME(), recipient);
			addModelAttribute(model, SearchFilter.DATE1_FROM.FILTER_NAME(), UnfepiUtils.setDateFilter(datefrom));
			addModelAttribute(model, SearchFilter.DATE1_TO.FILTER_NAME(), UnfepiUtils.setDateFilter(dateto));

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
			sc.closeSession();
		}
	}

}
