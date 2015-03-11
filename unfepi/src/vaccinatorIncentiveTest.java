import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import net.jmatrix.eproperties.EProperties;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.model.User;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.IncentiveUtils;


public class vaccinatorIncentiveTest {

	public static void main(String[] args) throws IOException, InstanceAlreadyExistsException {
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
		
		IncentiveUtilsForTEST.doVaccinatorIncentivization(null, new Date(), new User(43));
		
	}
}
