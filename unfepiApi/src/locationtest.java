import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.VaccinationCenter;


public class locationtest {

	public static void main(String[] args) throws IOException, InstanceAlreadyExistsException {
		System.out.println(">>>>LOADING SYSTEM PROPERTIES...");

		Context.instantiate(null);
		//GlobalParams.UNFEPI_PROPERTIES = prop;
		
		ServiceContext sc = Context.getServices();	
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
		List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
		System.out.println(map.size());
		//for(map.)
		//lv2.size();

		
		
	}
}
