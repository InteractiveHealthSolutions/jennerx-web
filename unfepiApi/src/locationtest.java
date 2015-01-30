import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ChildLottery;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.VaccinationCenter;


public class locationtest {

	public static void main(String[] args) throws IOException, InstanceAlreadyExistsException {
		System.out.println(">>>>LOADING SYSTEM PROPERTIES...");

		Context.instantiate(null);
		//GlobalParams.UNFEPI_PROPERTIES = prop;
		
		ServiceContext sc = Context.getServices();	
		List<ChildLottery> chll = sc.getIncentiveService().findChildLotteryByCriteria(null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1, 0, 10, true, new String[]{"vaccination"});
	//	VaccinationCenter vl = sc.getVaccinationService().findVaccinationCenterById("01001", true, null);
	//	List<Location> ll = sc.getCustomQueryService().getDataByHQL("FROM Location");
		//Set<Location> cl = ll.get(0).getChildLocations();
		//cl.getClass();
	}
}
