package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.DailySummaryWrapper;
import org.ird.unfepi.constants.EncounterType;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.DailySummaryVaccineGiven;
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.EncounterId;
import org.ird.unfepi.model.EncounterResults;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AddDailySummaryController extends DataEntryFormController
{
	private static final FormType formType = FormType.DAILY_SUMMARY_ADD;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		DailySummaryWrapper dsw = (DailySummaryWrapper) command;
		
		DailySummary ds = dsw.getDailySummary();

		ServiceContext sc = Context.getServices();

		try{
			ds.setCreator(user.getUser());
			int dsid = Integer.parseInt(sc.getReportService().saveDailySummary(ds).toString());
			
			for (DailySummaryVaccineGiven dsvg : dsw.getDsvgList()) 
			{
				dsvg.setDailySummaryId(dsid);
				
				sc.getReportService().saveDailySummaryVaccineGiven(dsvg);
			}
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(ds), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));

			List<EncounterResults> encr = new ArrayList<EncounterResults>();
			
			List<Encounter> listenc = sc.getEncounterService().findEncounter(ds.getVaccinatorId(), ds.getVaccinatorId(), null);
			
			EncounterId id = new EncounterId(listenc.size()+1, ds.getVaccinatorId(), ds.getVaccinatorId());
			Encounter e = new Encounter(id , EncounterType.DAILY_SUMMARY.name(), DataEntrySource.WEB, ds.getVaccinationCenterId()==null?null:ds.getVaccinationCenterId().toString(), ds.getSummaryDate(), dateFormStart, new Date(), null);
			e.setCreatedByUserId(user.getUser().getMappedId());
			sc.getEncounterService().saveEncounter(e);

	/*TODO		encr.add(EncounterUtil.createEncounterResult(e, "VACCINATION_CENTER", ds.getVaccinationCenterId()==null?null:sc.getVaccinationService().findVaccinationCenterById(ds.getVaccinationCenterId(), true, new String[]{"idMapper"}).getIdMapper().getIdentifiers().get(0).getIdentifier()));
			
			for (DailySummaryVaccineGiven dsvg :  dsw.getDsvgList()) {
				encr.add(EncounterUtil.createEncounterResult(e, dsvg.getVaccineName(), dsvg.getQuantityGiven()));
			}
*/
			for (EncounterResults encounterres : encr) {
				sc.getEncounterService().saveEncounterResult(encounterres);
			}
			
			sc.commitTransaction();
			
			String editmessage = "updated successfully";
			return new ModelAndView(new RedirectView("viewDailySummaries.htm?action=search&editOrUpdateMessage="+editmessage));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		dateFormStart = new Date();
		
		ArrayList<DailySummaryVaccineGiven> dsvacgvnl = new ArrayList<DailySummaryVaccineGiven>();
		for (int i = 0; i < 16; i++) {
			dsvacgvnl.add(new DailySummaryVaccineGiven());
		}
		return new DailySummaryWrapper(new DailySummary(), dsvacgvnl);
	}

	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception 
	{
		Map<String, Object> model=new HashMap<String, Object>();
		
		ServiceContext sc = Context.getServices();
		try{
			model.put("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
			model.put("vaccinators", sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));
		}catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while reference data list. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		return model;
	}
}
