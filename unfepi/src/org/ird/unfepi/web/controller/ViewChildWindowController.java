package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataQuery;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewChildWindowController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			request.setAttribute("lmessage", "Your Session has expired . please login again.");
		}
		
		ServiceContext sc = Context.getServices();

		String programId = request.getParameter("programId");
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			Child child = sc.getChildService().findChildById(programId, true, new String[]{"idMapper", "guardianRelation", "nicOwnerRelation", "religion", "language", "createdByUserId", "lastEditedByUserId"});
			model.put("programId", programId);
			model.put("child", child);
			String epiNumber = request.getParameter("epiNumber");
			if(epiNumber == null){
				List epiNumberl = sc.getCustomQueryService().getDataBySQL("select epiNumber from vaccination where childId="+child.getMappedId()+" and vaccinationStatus IN ('VACCINATED','LATE_VACCINATED') order by vaccinationDate DESC");
				epiNumber = (String) (epiNumberl.size()==0?"":epiNumberl.get(0));
			}
			model.put("epiNumber", epiNumber);
			model.put("vaccinations", IRUtils.addRecord(child));
			model.put("contacts", sc.getDemographicDetailsService().getContactNumber(child.getMappedId(), true, new String[]{"ownerRelation"}));
			model.put("addresses", sc.getDemographicDetailsService().getAddress(child.getMappedId(), true, new String[]{"city"}));
			model.put("preferences", sc.getChildService().findLotterySmsByChild(child.getMappedId(), true, 0, 50, null));

			//int enrVacId = Integer.parseInt(sc.getCustomQueryService().getDataBySQL("SELECT vaccinationRecordNum FROM vaccination where vaccinationdate is not null and childId="+child.getMappedId()+" having min(vaccinationdate)").get(0).toString());

			String hql="from Vaccination v " +
					" left join fetch v.vaccine vacci " +
					" left join fetch v.broughtByRelationship relat " +
					" left join fetch v.createdByUserId creat " +
					" left join fetch v.lastEditedByUserId edito " +
					" where v.childId="+child.getMappedId()+" and "+GlobalParams.HQL_ENROLLMENT_FILTER_v;
			
			List vl = sc.getCustomQueryService().getDataByHQL(hql);
			Vaccination enrVaccination = vl.size() == 0 ? null : (Vaccination) vl.get(0);
			//Vaccination pv= sc.getVaccinationService().getVaccinationRecord(enrVacId, true, new String[]{"vaccine", "broughtByRelationship", "createdByUserId", "lastEditedByUserId"}, null);

			model.put("vacc", enrVaccination);
			VaccinationCenter vc = null;
			if(enrVaccination != null && enrVaccination.getVaccinationCenterId() != null){
				vc = sc.getVaccinationService().findVaccinationCenterById(enrVaccination.getVaccinationCenterId(), true, new String[]{"idMapper"});
			}
			model.put("vcenter", vc);
			Vaccinator vaccinator = null;
			if(enrVaccination != null && enrVaccination.getVaccinatorId() != null){
				vaccinator = sc.getVaccinationService().findVaccinatorById(enrVaccination.getVaccinatorId(), true, new String[]{"idMapper"});
			}
			model.put("vaccinator", vaccinator);
			
			List list = sc.getCustomQueryService().getDataBySQL(DataQuery.CHILD_INCENTIVE_QUERY + " WHERE v.childid="+child.getMappedId());
			
			ArrayList<Object[]> arrlivt = new ArrayList<Object[]>();
			
			Object[] heatvt = new Object[]{"Vaccine", "Lottery Won?", "Lottery Date", "Verification Code", "Status", "Amount","Storekeeper","Comsumption Date","Transction Date", "Vaccination Record Num"};
			for (Object object : list) {
				Object[] coldata = (Object[]) object;
				
				arrlivt.add(coldata);
			}
			model.put("lotteryhead", heatvt);
			model.put("lottery", arrlivt);

			
			return new ModelAndView("viewChildwindow","model",model);	
		}catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));		
		}
		finally{
			sc.closeSession();
		}
	}
}
