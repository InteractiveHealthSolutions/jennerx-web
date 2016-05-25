import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Identifier;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccine;


public class locationtest {

	public static void main(String[] args) throws IOException, InstanceAlreadyExistsException {
		System.out.println(">>>>LOADING SYSTEM PROPERTIES...");

		Context.instantiate(null);
		//GlobalParams.UNFEPI_PROPERTIES = prop;
		
		ServiceContext sc = Context.getServices();	
		/*new String[]{"idMapper"}*/
		//Child child = new Child();
		//child=sc.getChildService().findChildById("10000004", false,new String[]{"1"} );
		//a valid child should be provided
							
		
		List<Vaccine> list=sc.getVaccinationService().getAll(true,new String[]{},null);
		System.out.println(list.size());
		for(Vaccine v:list){
			System.out.print(v.getName());
			System.out.print("   "+v.getVaccineId()+"\n");
			
		}
		sc.closeSession();
		/*IdMapper childIdMapper = child.getIdMapper();
		List<Identifier> identifiers = childIdMapper.getIdentifiers();
		Iterator< Identifier> idIterator = identifiers.iterator();//org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.VACCINATED
		//sc.getVaccinationService().findVaccinationRecordByCriteria(childId, vaccineName, vaccinatorId, vaccinationCenterId, epiNumber, dueDatesmaller, dueDategreater, vaccinationDatesmaller, vaccinationDategreater, timelinessStatus, vaccinationStatus, putNotWithVaccinationStatus, firstResult, fetchsize, isreadonly, mappingsToJoin, sqlFilter)
		List<Vaccination> vaccinatedList = sc.getVaccinationService().findByCriteria(childIdMapper.getMappedId(), (short)2,null, 0, 15, true,  new String[] {"idMapper"});
		System.out.println(vaccinatedList.size());
		for (Iterator iterator = vaccinatedList.iterator(); iterator.hasNext();) {
			Vaccination vaccination = (Vaccination) iterator.next();
			System.out.println(vaccination.getVaccineId()+"--"+vaccination.getVaccine().getFullName()+"--"+vaccination.getChildId()+" --"+vaccination.getVaccinationStatus()+"--"+vaccination.getRole());
			
		}
		if(vaccinatedList!=null) {
			if(vaccinatedList.size()>0){
			//#TODO: do role based criteria 
				short it =  vaccinatedList.get(0).getVaccine().getVaccineId();//getPrerequisites().iterator();
				
				List list = sc.getCustomQueryService().getDataBySQLMapResult("select * from vaccineprerequisite  where vaccinePrerequisiteId="+it);
				HashMap map = (HashMap) list.get(0);
				map.get("vaccineId");
				System.out.println(map.get("vaccineId")+"----");
				Vaccine vaccine = sc.getVaccinationService().findVaccineById((Short)map.get("vaccineId"));
				
				System.out.println(vaccine.getFullName());
				//sc.getVaccinationService().
			//	IMRUtils.
				//		sc.getVaccinationService().findVaccineById(list.)
				
			//	return null;
			}
		}
		*/
	//	List lv = sc.getCustomQueryService().getDataBySQL("select * from vaccine");
	/*	String query="SELECT vc.mappedId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "+
				"v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus, i.identifier identifier,v.childId, "+
				"v.vaccinatorId ,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor "+
				"FROM unfepi.vaccination  v inner join child c on c.mappedId=v.childId "+ 
				"inner join identifier i on v.childid=i.mappedid inner join vaccine on v.vaccineId=vaccine.vaccineId "+ 
				"inner join vaccinationcenter vc on vc.mappedid=v.vaccinationcenterid;";
		*/
		String query="SELECT vc.mappedId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "+
				"v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus, i.identifier identifier,v.childId, "+
				"v.vaccinatorId ,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor "+
				"FROM unfepi.vaccination  v inner join child c on c.mappedId=v.childId "+ 
				"inner join identifier i on v.childid=i.mappedid inner join vaccine on v.vaccineId=vaccine.vaccineId "+ 
				"inner join vaccinationcenter vc on vc.mappedid=v.vaccinationcenterid;";

		
	//	List lv2 = sc.getCustomQueryService().getDataBySQLMapResult(query);
	//s	List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
	//	System.out.println(map.size());
		//for(map.)
		//lv2.size();

		
		
	}
	
	
}
