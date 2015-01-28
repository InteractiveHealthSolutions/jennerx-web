package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.TimelinessStatus;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewVaccinationRecordController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,HttpServletResponse resp) throws Exception {
    	int totalRows=0;
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<Vaccination> list=new ArrayList<Vaccination>();
			
			String childIdName=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childIdName")))?null:req.getParameter("childIdName");
			String recordNum=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("recordNum")))?null:req.getParameter("recordNum");
			Date vaccinationDuedatefrom=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationDuedatefrom")))?null:new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("vaccinationDuedatefrom"));
			Date vaccinationDuedateto;
			if(vaccinationDuedatefrom==null){
				vaccinationDuedateto=null;
			}
			else{
				vaccinationDuedateto=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationDuedateto")))?new Date():new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("vaccinationDuedateto"));
			}
			
			Date vaccinationDatefrom=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationDatefrom")))?null:new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("vaccinationDatefrom"));
			Date vaccinationDateto;
			if(vaccinationDatefrom==null){
				vaccinationDateto=null;
			}else{
				vaccinationDateto=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationDateto")))?new Date():new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("vaccinationDateto"));
			}
			Integer vaccCenter = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationCenterddp"))) ? null : Integer.parseInt(req.getParameter("vaccinationCenterddp"));
			Integer vaccinator = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinatorddp"))) ? null : Integer.parseInt(req.getParameter("vaccinatorddp"));	

			String epiNumber=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("epiNumber"))?null:req.getParameter("epiNumber");
			String vaccName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccName"))?null:req.getParameter("vaccName");
			VACCINATION_STATUS vaccstatus=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccstatus"))?null:VACCINATION_STATUS.valueOf(req.getParameter("vaccstatus"));		
			TimelinessStatus timelinessstatus=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("timelinessstatus"))?null:TimelinessStatus.valueOf(req.getParameter("timelinessstatus"));		

			String exclusionCriteria = req.getParameter("exclusionCriteria")==null?"excludeEnrollments":req.getParameter("exclusionCriteria");
			
			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			Integer childId = null;

			if(action == null && pagerOffset == null){//page is accessed first time
				vaccinationDuedatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				vaccinationDuedateto = new Date();
			}
			else {
				if(action != null && action.trim().equalsIgnoreCase("search"))
				{//new search display from 0
										/*req.setAttribute("searchlog", "child ID:"+childIdName+" , Epi Num :"+epiNum+
					" , vacciz : "+lastSearchFollowupstatusNotchked+" "+lastSearchVaccinator+
					" , clinic :"+lastSearchScreeningDateto+" , current cell :"+lastSearchVaccCenter+" , Arm :"+lastSearchScreeningDatefrom+" , MR Num :"+lastSearchMrNum);
	*/			}
				else if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				}
			}

			if(childIdName != null){
				try{
					IdMapper chl = sc.getIdMapperService().findIdMapper(childIdName);
					childId = chl == null?-1:chl.getMappedId();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(recordNum != null){
				try{
					Vaccination obj = sc.getVaccinationService().getVaccinationRecord(Integer.parseInt(recordNum), true, new String[]{"child"}, null);
					if(obj != null) list.add(obj);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(exclusionCriteria.toLowerCase().contains("nothing")){
				list = sc.getVaccinationService().findVaccinationRecordByCriteria(childId, vaccName, vaccinator, vaccCenter, epiNumber, vaccinationDuedatefrom, vaccinationDuedateto, vaccinationDatefrom, vaccinationDateto, timelinessstatus, vaccstatus, false, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, new String[]{"child"}, null);
			}
			else {
				String qury = "from Vaccination v " +
						" left join fetch v.child c left join fetch c.idMapper " +
						" left join fetch v.vaccine vac " +
						" left join fetch v.vaccinationCenter vcent left join fetch vcent.idMapper " +
						" left join fetch v.vaccinator vtor left join fetch vtor.idMapper " +
						" where v.childId <> null ";
				
				String qcount = "select count(*) from Vaccination v " +
						/*" left join fetch v.child c left join fetch c.idMapper " +
						" left join fetch v.vaccine vac " +
						" left join fetch v.vaccinationCenter vcent left join fetch vcent.idMapper " +
						" left join fetch v.vaccinator vtor left join fetch vtor.idMapper " +*/
						" where v.childId <> null ";
				
				if(exclusionCriteria.toLowerCase().contains("enrollment"))
				{
					qury += " and " + GlobalParams.HQL_FOLLOWUP_FILTER_v;
					
					qcount += " and " + GlobalParams.HQL_FOLLOWUP_FILTER_v;
				}
				else if(exclusionCriteria.toLowerCase().contains("followup"))
				{
					qury += " and " + GlobalParams.HQL_ENROLLMENT_FILTER_v;
	
					qcount += " and " + GlobalParams.HQL_ENROLLMENT_FILTER_v;
				}
				
				String filter = "";
				if(vaccinationDuedatefrom != null && vaccinationDuedateto != null){
					filter += " and vaccinationDueDate between '"+GlobalParams.SQL_DATE_FORMAT.format(vaccinationDuedatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(vaccinationDuedateto)+"' ";
				}
				if(vaccinationDatefrom != null && vaccinationDateto != null){
					filter += " and vaccinationDate between '"+GlobalParams.SQL_DATE_FORMAT.format(vaccinationDatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(vaccinationDateto)+"' ";
				}
				if(childId != null){
					filter += " and childId ="+childId+" ";
				}
				if(epiNumber != null){
					filter += " and epiNumber ='"+epiNumber+"' ";
				}
				if(vaccName != null){
					Vaccine vacc = sc.getVaccinationService().getByName(vaccName);
					filter += " and v.vaccineId ="+(vacc==null?-1:vacc.getVaccineId())+" ";
				}
				if(vaccstatus != null){
					filter += " and vaccinationStatus ='"+vaccstatus+"' ";
				}
				if (timelinessstatus != null) {
					filter += " and timelinessStatus ='"+timelinessstatus+"' ";
				}
				
				//////////////////FOR searching VACCINATION CENTERS
				if(vaccstatus != null && vaccstatus.equals(VACCINATION_STATUS.PENDING) && vaccCenter != null){
					filter += " and v.child.idMapper.identifiers[0].identifier like '"+sc.getIdMapperService().findIdMapper(vaccCenter).getProgramId()+"%' ";
				}
				else if(vaccCenter != null){
					filter += " and v.vaccinationCenterId ="+vaccCenter+" ";
				}
				
				if(vaccinator != null){
					filter += " and v.vaccinatorId ="+vaccinator+" ";
				}
				
				qury += (filter + " order by vaccinationDuedate desc, vaccinationDate desc");
				
				qcount += filter;
				
				Session ss = Context.getNewSession();
				try{
					totalRows=Integer.parseInt(ss.createQuery(qcount).list().get(0).toString());
					
					Query q = ss.createQuery(qury);
					list = q.setReadOnly(true)
							.setFirstResult(startRecord)
							.setMaxResults(WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS)
							.list();
				}
				finally{
					ss.close();
				}
			}
			if(sc.getVaccinationService().LAST_QUERY_TOTAL_ROW_COUNT(Vaccination.class)!=null){
				totalRows=sc.getVaccinationService().LAST_QUERY_TOTAL_ROW_COUNT(Vaccination.class).intValue();
			}
			else if(totalRows==0){
				totalRows=list.size();
			}
	//so that our count int is not lost or overridden
			req.setAttribute("childIdName", childIdName);
			req.setAttribute("recordNum", recordNum);
			req.setAttribute("vaccinationDuedatefrom", vaccinationDuedatefrom != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(vaccinationDuedatefrom):vaccinationDuedatefrom);
			req.setAttribute("vaccinationDuedateto", vaccinationDuedateto != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(vaccinationDuedateto):vaccinationDuedateto);
			req.setAttribute("vaccinationDatefrom", vaccinationDatefrom != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(vaccinationDatefrom):vaccinationDatefrom);
			req.setAttribute("vaccinationDateto", vaccinationDateto != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(vaccinationDateto):vaccinationDateto);
			req.setAttribute("lastSearchVaccCenter", /*vaccCenter==null?"":*/vaccCenter);
			req.setAttribute("lastSearchVaccinator", /*vaccinator==null?"":*/vaccinator);
			req.setAttribute("epiNumber", epiNumber);
			req.setAttribute("vaccName", vaccName);
			req.setAttribute("vaccstatus", vaccstatus);
			req.setAttribute("timelinessstatus", timelinessstatus);
			req.setAttribute("exclusionCriteria", exclusionCriteria);

	//		req.setAttribute("lastSearchVaccstatusNotchked", lastSearchVaccstatusNotchked);
	
			model.put("vaccine", sc.getVaccinationService().getAll(true, "vaccineId"));		
			model.put("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
			model.put("vaccinators", sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));
			model.put("pvacc", list);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewVaccinationRecord","model",model);
		}catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			sc.closeSession();//incase of error close session
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			req.setAttribute("sc"	, sc);
			/*sc.closeSession();*///will close after page have been loaded
		}
	}
}