import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CenterProgram;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.Round;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCalendar;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.web.utils.IMRUtils;

import net.jmatrix.eproperties.EProperties;


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

	}
}
