package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewChildrenController implements Controller{

	@SuppressWarnings({"rawtypes", "unchecked"})
	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
    	int totalRows=0;
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<Child> list=new ArrayList<Child>();

			String childId=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childid")))?null:req.getParameter("childid").trim();
			String childNamepart=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childnamepart")))?null:req.getParameter("childnamepart").trim();		
			String cellNumber=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("cellNumber")))?null:req.getParameter("cellNumber").trim();
			STATUS status=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("followupstatus")))?null:STATUS.valueOf(req.getParameter("followupstatus"));	
			boolean isnotChecked=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("followupstatusNotchk")))?false:true;	
			Gender gender=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("gender")))?null:Gender.valueOf(req.getParameter("gender"));	
			String epiNumber=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("epiNumber")))?null:req.getParameter("epiNumber");	
			Date enrollmentDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("enrollmentDatefrom"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("enrollmentDatefrom"));	
			Date enrollmentDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("enrollmentDateto"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("enrollmentDateto"));	
			Date birthDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("birthDatefrom"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("birthDatefrom"));	
			Date birthDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("birthDateto"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("birthDateto"));	
			
			// our child may be mapped onto different centers but the center on which it was enrolled
			// will be only one and the very first and will be part of child's id hence for finding children 
			// of a center we will set child id digits as center id
			String vaccCenter = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationCenterddp"))) ? null : req.getParameter("vaccinationCenterddp");

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			if(action == null && pagerOffset == null){//page is accessed first time
				enrollmentDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				enrollmentDateto = new Date();
				/*req.setAttribute("searchlog", "All Enrollments");*/
			}
			else if(action != null && action.trim().equalsIgnoreCase("search"))
			{
				//filhaal do nothing
			}
			else if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				
			}
			if(cellNumber != null){
				List<ContactNumber> con = sc.getDemographicDetailsService().findContactNumber(cellNumber, true, null);
				for (ContactNumber contactNumber : con) {
					list.add(sc.getChildService().findChildById(contactNumber.getMappedId(), true, new String[]{"idMapper","enrollmentVaccine"}));
				}
				totalRows = list.size();
			}
			else if(epiNumber != null){
				list.addAll(sc.getCustomQueryService().getDataByHQL("from Child c left join fetch c.idMapper i left join fetch c.enrollmentVaccine where exists (select epiNumber from Vaccination where childId=c.mappedId and epiNumber ='"+epiNumber+"' and vaccinationStatus <> 'PENDING' "+(vaccCenter==null?"":" and vaccinationCenterId=(select mappedId from IdMapper where programId='"+vaccCenter+"')")+" )"));
				totalRows = list.size();
			}
			else{
				list = sc.getChildService().findChildByCriteria(childId==null?vaccCenter:childId, childNamepart, birthDatefrom, birthDateto, null, gender, null, null, null, status, isnotChecked, enrollmentDatefrom, enrollmentDateto, null, true, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, new String[]{"idMapper","enrollmentVaccine"});
				totalRows = sc.getChildService().LAST_QUERY_TOTAL_ROW_COUNT(Child.class).intValue();
			}

			req.setAttribute("lastSearchVaccCenter", /*vaccCenter==null?"":*/vaccCenter);
			
			req.setAttribute("lastSearchChildId", childId);
			req.setAttribute("lastSearchChildNamepart", childNamepart);
			req.setAttribute("lastSearchCellNumber", cellNumber);
			req.setAttribute("lastSearchGender", gender);
			req.setAttribute("lastSearchFollowupstatus", status);
			req.setAttribute("followupstatusNotchk", isnotChecked?isnotChecked:null);
			req.setAttribute("lastSearchEpiNumber", epiNumber);
			req.setAttribute("lastSearchEnrollmentDatefrom", enrollmentDatefrom != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(enrollmentDatefrom):enrollmentDatefrom);
			req.setAttribute("lastSearchEnrollmentDateto", enrollmentDateto != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(enrollmentDateto):enrollmentDateto);
			req.setAttribute("lastSearchBirthDatefrom", birthDatefrom != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(birthDatefrom):birthDatefrom);
			req.setAttribute("lastSearchBirthDateto", birthDateto != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(birthDateto):birthDateto);

			List<Map<String, Object>> childMap = new ArrayList<Map<String,Object>>();
			for (Child child : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("child", child);
				map.put("contacts", sc.getDemographicDetailsService().getContactNumber(child.getMappedId(), true, null));
				map.put("addresses", sc.getDemographicDetailsService().getAddress(child.getMappedId(), true, null));
				List<LotterySms> lsl = sc.getChildService().findLotterySmsByChild(child.getMappedId(), true, 0, 2, null);
				map.put("lastpreference", lsl.size()>0?lsl.get(0):null);
				List vaccl = sc.getCustomQueryService().getDataBySQL("select distinct iv.programId,v.epiNumber from vaccination v join vaccine vc on v.vaccineid=vc.vaccineid join idmapper iv on v.vaccinationCenterId=iv.mappedId where v.childid="+child.getMappedId()+" and vaccinationStatus <>'PENDING'");
				map.put("epiNumbers", vaccl);
				childMap.add(map);
			}
			
			model.put("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));

			model.put("childMap", childMap);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewChildren","model",model);
		}catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}