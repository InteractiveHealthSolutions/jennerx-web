package org.ird.unfepi.web.validator;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CenterProgram;
import org.ird.unfepi.model.ItemsDistributed;
import org.ird.unfepi.model.MuacMeasurement;
import org.ird.unfepi.model.Round;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VaccinationValidator implements Validator {

    public boolean supports(Class clazz) {
        return VaccinationCenterVisit.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
    	
    }
    
    public void validateVaccinationForm(VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule, Errors errors, HttpServletRequest request){
    	ServiceContext sc = Context.getServices();
		try{
			ValidatorUtils.validateFollowupForm(DataEntrySource.WEB, vaccineSchedule, centerVisit, null, errors, sc);
			
			CenterProgram centerProgram = (CenterProgram) sc.getCustomQueryService().getDataByHQL("from CenterProgram where vaccinationCenterId ="+ centerVisit.getVaccinationCenterId() +" and healthProgramId =" + centerVisit.getHealthProgramId()).get(0);
			List<Round> roundL = sc.getCustomQueryService().getDataByHQL("from Round where healthProgramId =" + centerProgram.getHealthProgramId()+" and isActive = 1");
			if(roundL == null || roundL.size() == 0){
				errors.reject("", null, "round info. not found for the selected health program in the selected vaccination center");
			}
			if(roundL != null && roundL.size() > 1){
				errors.reject("", null, "more than one round is active for the selected health program in the selected vaccination center");
			}
			
			if(centerVisit.getItemsDistributedL() != null && centerVisit.getItemsDistributedL().size() >0){
				Date date = centerVisit.getItemsDistributedL().get(0).getItemDistributedId().getDistributedDate();
				int cid =  centerVisit.getItemsDistributedL().get(0).getItemDistributedId().getMappedId();
				
				List<ItemsDistributed> records = sc.getCustomQueryService().getDataByHQL("from ItemsDistributed where itemDistributedId.distributedDate = '" +WebGlobals.GLOBAL_SQL_DATE_FORMAT.format(date)+ "' and itemDistributedId.mappedId = "+cid);
				if(records != null && records.size() > 0){
					errors.reject("", null, "item(s) already given today !");
				}
			}
			
			if(centerVisit.getMuacMeasurement() != null && centerVisit.getMuacMeasurement().getColorrange() != null ){
				Date date = centerVisit.getVisitDate();
				int cid =  centerVisit.getChildId();
				
				List<MuacMeasurement> records = sc.getCustomQueryService().getDataByHQL("from MuacMeasurement where muacId.measureDate = '" +WebGlobals.GLOBAL_SQL_DATE_FORMAT.format(date)+ "' and muacId.mappedId = "+cid);
				if(records != null && records.size() > 0){
					errors.reject("", null, "MUAC already measured today !");
				}
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
    }
}
