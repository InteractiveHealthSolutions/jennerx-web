package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.VaccinationCalendar;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;;

@Controller
public class ViewImmunizationRecordsController extends DataDisplayController {

	ViewImmunizationRecordsController(){
		super("dataForm", new  DataSearchForm("immunization_record", "Immunization Records", SystemPermissions.VIEW_CHILDREN_DATA, false));
	}

	@RequestMapping(value="/viewImmunizationRecords", method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp){
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();

		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		int startRecord = 0;
		
		try{

			
			List list =new ArrayList();
			LoggedInUser user = UserSessionUtils.getActiveUser(req);

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			String searchFilter = UnfepiUtils.getStringFilter(SearchFilter.SEARCH_FILTER, req);
			String columns = UnfepiUtils.getStringFilter(SearchFilter.COLUMNS, req);
			String calenderId = UnfepiUtils.getStringFilter(SearchFilter.CALENDER, req);

			if(calenderId != null && !calenderId.equals("")){

				String dmpTable = "jennerx_data_dump_"+calenderId;			

				searchFilter = StringUtils.isEmptyOrWhitespaceOnly(searchFilter)?"":searchFilter.trim();
				Integer userLocation = user.getUser().getIdMapper().getIdentifiers().get(0).getLocationId();

				String userLocationFilter = " EXISTS (SELECT vaccinationRecordNum FROM vaccination v WHERE childId="+dmpTable+".MappedId AND "
						+ "	EXISTS (SELECT locationId FROM identifier WHERE mappedId = v.vaccinationCenterId  "
						+ "	AND (locationId = "+userLocation+" OR GetAncestry(locationId) REGEXP CONCAT('[[:<:]]',"+userLocation+",'[[:>:]]')) ) )";
				// If user has no location mapped show all data, but if user has been restricted to any location
				// add location filter in results
				String sf = " WHERE 1=1 "
						+	(userLocation==null?"":" AND "+userLocationFilter)
						+	(StringUtils.isEmptyOrWhitespaceOnly(searchFilter)?"":" AND "+searchFilter);

				String[] cn = (columns==null?"":columns).split(",");

				
				if(action == null && pagerOffset == null){//page is accessed first time
					//enteredDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
					//enteredDateto = new Date();
				} else if (!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)) {//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				}


				List<String> colNames = sc.getCustomQueryService().getDataBySQL("SELECT column_name FROM information_schema.columns WHERE table_name='"+dmpTable+"' ORDER BY ORDINAL_POSITION");
				String qry = "SELECT * FROM "+dmpTable+" "+sf+" LIMIT "+startRecord+", "+WebGlobals.PAGER_PAGE_SIZE;
				list = sc.getCustomQueryService().getDataBySQLMapResult(qry );

				totalRows=Integer.parseInt(sc.getCustomQueryService().getDataBySQL("SELECT COUNT(*) FROM "+dmpTable+" "+sf).get(0).toString());		

				String searchFilterMessage = searchFilter.replace("'", "").replace("%", "").replace("AND", "<br>").replace("=", " is ").replace("!=", " is not ").replace("like", " contains ");
				List<Map<String,Object>> dmp = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM dmp_ WHERE dmpTableName='"+dmpTable+"'");

				addModelAttribute(model, "vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
				//			addModelAttribute(model, "dateDmpUpdated", dmp.get(0).get("lastDumpDate"));
				addModelAttribute(model, "dataLocationMessage", userLocation==null?"":"Showing data of <b>"+user.getUser().getIdMapper().getIdentifiers().get(0).getLocation().getName()+"</b> immunizations only");
				//			addModelAttribute(model, "dmpFileNameInit", dmp.get(0).get("dmpFileNameInit"));
				addModelAttribute(model, "searchFilterMessage", searchFilterMessage);
				addModelAttribute(model, "columnNames", colNames);
				
				List<HashMap> vaccineNames = sc.getCustomQueryService().getDataBySQLMapResult(
						"SELECT vaccineId, name FROM vaccine vac WHERE vaccineId IN (SELECT vaccineId FROM vaccinegap "
								+ "WHERE vaccinegaptypeid = 1 AND vaccineId = vac.vaccineId AND vaccinationcalendarId = "+calenderId +") ORDER BY vac.vaccineId;");

				addModelAttribute(model, "vaccineNames", vaccineNames);
			}
			
			addModelAttribute(model, "datalist", list);
			addModelAttribute(model, "totalRows", totalRows);
			addModelAttribute(model, SearchFilter.COLUMNS.FILTER_NAME(), columns);
			addModelAttribute(model, SearchFilter.SEARCH_FILTER.FILTER_NAME(), searchFilter);
			addModelAttribute(model, SearchFilter.CALENDER.FILTER_NAME(), calenderId);
			List<VaccinationCalendar> vc = sc.getCustomQueryService().getDataByHQL("from VaccinationCalendar order by shortName");
			addModelAttribute(model, "vaccinationcalendar", vc);
			

			return showForm(model);	
		}catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			sc.closeSession();//incase of error close session
			return new ModelAndView("exception");
		}finally{
			sc.closeSession();
		}
	}

}
