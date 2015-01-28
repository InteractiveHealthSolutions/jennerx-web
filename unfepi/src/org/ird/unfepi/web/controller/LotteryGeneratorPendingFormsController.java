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
import org.ird.unfepi.constants.EncounterType;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;
public class LotteryGeneratorPendingFormsController implements Controller{

	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int totalRows=0;
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<Encounter> list =new ArrayList<Encounter>();
			
			String peronid = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("peronid"))) ? null : req.getParameter("peronid");
			Date enteredDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("enteredDatefrom"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("enteredDatefrom"));	
			Date enteredDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("enteredDateto"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("enteredDateto"));	

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			
			if(action == null && pagerOffset == null){//page is accessed first time
				enteredDatefrom = new Date(System.currentTimeMillis()-(1000*60*60*24*7L));
				enteredDateto = new Date();
				/*req.setAttribute("searchlog", "All Screenings");*/
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
					/*req.setAttribute("searchlog", req.getParameter("searchlog"));*/
				}
			}
			Session ss = Context.getNewSession();
			try{
				String qury = "from Encounter e" +
						" left join fetch e.p1 ep1 " +
						" left join fetch e.p2 ep2 " +
						" left join fetch e.createdByUser " +
						" where (encounterType='"+EncounterType.LOTTERY_GEN+"' OR EncounterType='"+EncounterType.LOTTERY_GEN+"') " +
						" and (detail like '%PENDING%' OR exists(select value from EncounterResults where element='FORM_STATUS' and value like '%pending%' and id.encounterId=e.id.encounterId and id.p1id =e.id.p1id and id.p2id =e.id.p2id) )";
				
				if(enteredDatefrom != null && enteredDateto != null){
					qury += " and dateEncounterEntered between '"+GlobalParams.SQL_DATE_FORMAT.format(enteredDatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(enteredDateto)+"' ";
				}
				if(peronid != null){
					IdMapper mapper = sc.getIdMapperService().findIdMapper(peronid);
					qury += " and id.p1Id ="+mapper.getMappedId()+" ";
				}
				qury += " order by dateEncounterEntered";
				
				
				String qcount = "select count(*) from Encounter e" +
						/*" left join fetch e.p1 ep1 " +
						" left join fetch e.p2 ep2 " +
						" left join fetch e.createdByUser " +*/
						" where (encounterType='"+EncounterType.LOTTERY_GEN+"' OR EncounterType='"+EncounterType.LOTTERY_GEN+"') "+
						" and (detail like '%PENDING%' OR exists(select value from EncounterResults where element='FORM_STATUS' and value like '%pending%' and id.encounterId=e.id.encounterId and id.p1id =e.id.p1id and id.p2id =e.id.p2id) )";
				
				if(enteredDatefrom != null && enteredDateto != null){
					qcount += " and dateEncounterEntered between '"+GlobalParams.SQL_DATE_FORMAT.format(enteredDatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(enteredDateto)+"' ";
				}
				if(peronid != null){
					IdMapper mapper = sc.getIdMapperService().findIdMapper(peronid);
					qcount += " and id.p1Id ="+mapper.getMappedId()+" ";
				}
				
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
			req.setAttribute("lastSearchPersonId", /*childIdName==null?"":*/peronid);
			//req.setAttribute("lastSearchEpiNum", /*epiNum==null?"":*/epiNum);
			req.setAttribute("lastSearchEnteredDatefrom", enteredDatefrom != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(enteredDatefrom):enteredDatefrom);
			req.setAttribute("lastSearchEnteredDateto", enteredDateto != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(enteredDateto):enteredDateto);

			model.put("encounters", list);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("lotteryGeneratorPendingForms","model",model);
		}catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			sc.closeSession();//incase of error close session
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}
