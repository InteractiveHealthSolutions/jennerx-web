import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import net.jmatrix.eproperties.EProperties;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.RoundVaccine;
import org.ird.unfepi.model.VialCount;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.SecurityUtils;


public class ProgramTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println(">>>>LOADING SYSTEM PROPERTIES...");
		InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("unfepi.properties");
		// Java Properties donot seem to support substitutions hence EProperties are used to accomplish the task
		EProperties root = new EProperties();
		root.load(f);

		// Java Properties to send to context and other APIs for configuration
		Properties prop = new Properties();
		prop.putAll(IRUtils.convertEntrySetToMap(root.entrySet()));

		Context.instantiate(prop);
		GlobalParams.UNFEPI_PROPERTIES = prop;
		
		ServiceContext sc = Context.getServices();
		
		RoundVaccine rv = new RoundVaccine();
		
//		VialCount vc =  new VialCount();
//		vc.setDate(new Date());
//		vc.setCount(12);
//		vc.setBeginning(true);
//		vc.setRoundId(1);
//		vc.setVaccineId((short)1);
//		vc.setWasteCount(2);
		
		
		sc.getCustomQueryService().save(rv);
		sc.commitTransaction();
		
//		String str =SecurityUtils.decrypt("fz/xDJTMlLb1QyPv+USU1zZZ4XbY7M9KS9eYdcGC2XNXubieem42Vy3i+cu7oLP3RXLPXJcAi96IM6Y5UXhN5g==", "administrator");
//		System.out.println("----------" + str);
//		HealthProgram hp = new HealthProgram();
//		hp.setName("program2");
//		hp.setDescription("test program");
//		hp.setEnrollmentLimit(10);
//		
//		Integer hpId = (Integer) sc.getHealthProgramService().saveHealthProgram(hp);
//		
//		System.out.println("\n\n" + hpId + "\n\n");
//		System.out.println(hp.getProgramId()+ "\n\n");
//		
////		48022
//		VaccinationCenter vc = (VaccinationCenter) sc.getCustomQueryService().getDataByHQL("from VaccinationCenter where mappedId = 48022").get(0);
//		
//		
//		CenterProgram cp = new CenterProgram();
//		cp.setVaccinationCenterId(vc.getMappedId());
//		cp.setHealthProgramId(hpId);
//		cp.setStartDate(new Date());
//		cp.setIsActive(true);
//		
//		Integer cpId = (Integer) sc.getHealthProgramService().saveCenterProgram(cp);
//		
//		Round rd = new Round();
//		rd.setName("round21");
//		rd.setStartDate(new Date());
//		rd.setCenterProgramId(cpId);
//		
//		Integer rdId = (Integer) sc.getHealthProgramService().saveRound(rd);

//		VaccinationCalendar vac = new VaccinationCalendar();
//		vac.setShortName("WHO");
//		
//		Integer id = (Integer) sc.getCustomQueryService().save(vac);
//		
//		sc.commitTransaction();
		
//		String[] columns = new String[] {
//				RequestElements.METADATA_FIELD_VACCINE_ID,
//				RequestElements.METADATA_FIELD_VACCINE_NAME,
//				RequestElements.METADATA_FIELD_VACCINE_ISSUPPLEMENTARY,
//				RequestElements.METADATA_FIELD_VACCINE_ENTITY,
//				RequestElements.METADATA_FIELD_VACCINE_FULL_NAME };
//		
//		System.out.println(Arrays.toString(columns));
//		System.out.println(Arrays.toString(columns).replaceAll("\\[|\\]", ""));
//		
//		System.out.println("_*_*_*_*_*");
		
//		String str = /*"    apple mango banana orange"*/ "a  b ";
//		System.out.println(str.indexOf(' '));
//		System.out.println(Arrays.toString(str.split("\\s")) + " length- " + str.split("\\s").length);

	}
}
