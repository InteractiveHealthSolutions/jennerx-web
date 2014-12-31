package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.utils.IRUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ViewChildDetailsController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
		
		ServiceContext sc = Context.getServices();

		String programId = request.getParameter("programId");
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			Child child = sc.getChildService().findChildById(programId, true, new String[]{"idMapper", "guardianRelation", "nicOwnerRelation", "religion", "language", "createdByUserId", "lastEditedByUserId"});
			addModelAttribute(model, "programId", programId);
			addModelAttribute(model, "child", child);
			String epiNumber = request.getParameter("epiNumber");
			if(epiNumber == null){
				List epiNumberl = sc.getCustomQueryService().getDataBySQL("select epiNumber from vaccination where childId="+child.getMappedId()+" and vaccinationStatus IN ('VACCINATED','LATE_VACCINATED') order by vaccinationDate DESC");
				epiNumber = (String) (epiNumberl.size()==0?"":epiNumberl.get(0));
			}
			addModelAttribute(model, "epiNumber", epiNumber);
			addModelAttribute(model, "vaccinations", IRUtils.addRecord(child));
			addModelAttribute(model, "contacts", sc.getDemographicDetailsService().getContactNumber(child.getMappedId(), true, new String[]{"ownerRelation"}));
			addModelAttribute(model, "addresses", sc.getDemographicDetailsService().getAddress(child.getMappedId(), true, new String[]{"city"}));
			addModelAttribute(model, "preferences", sc.getChildService().findLotterySmsByChild(child.getMappedId(), true, 0, 50, null));

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

			addModelAttribute(model, "vacc", enrVaccination);
			VaccinationCenter vc = null;
			if(enrVaccination != null && enrVaccination.getVaccinationCenterId() != null){
				vc = sc.getVaccinationService().findVaccinationCenterById(enrVaccination.getVaccinationCenterId(), true, new String[]{"idMapper"});
			}
			addModelAttribute(model, "vcenter", vc);
			Vaccinator vaccinator = null;
			if(enrVaccination != null && enrVaccination.getVaccinatorId() != null){
				vaccinator = sc.getVaccinationService().findVaccinatorById(enrVaccination.getVaccinatorId(), true, new String[]{"idMapper"});
			}
			addModelAttribute(model, "vaccinator", vaccinator);
			
			String hqlchildlottery = "FROM ChildLottery cl " +
					" LEFT JOIN FETCH cl.vaccination v " +
					" LEFT JOIN FETCH v.vaccine vc " +
					" LEFT JOIN FETCH v.vaccinationCenter center " +
					" LEFT JOIN FETCH center.idMapper centerId " +
					" LEFT JOIN FETCH v.vaccinator vctor " +
					" LEFT JOIN FETCH vctor.idMapper vctorId " +
					" LEFT JOIN FETCH cl.storekeeper stork " + 
					" LEFT JOIN FETCH cl.storekeeper.idMapper storkId " +
					" LEFT JOIN FETCH cl.createdByUserId creator" +
					" LEFT JOIN FETCH cl.lastEditedByUserId editor" + 
					" WHERE v.childId = " + child.getMappedId() +
					" ORDER BY v.vaccinationDate ASC";
			List childlotteries = sc.getCustomQueryService().getDataByHQL(hqlchildlottery);
			
			addModelAttribute(model, "lottery", childlotteries);

			return showForm(model);
		}catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));		
		}
		finally{
			request.setAttribute("sc"	, sc);
			/*sc.closeSession();*///will close after page have been loaded
		}
	}
}