import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import net.jmatrix.eproperties.EProperties;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.autosys.utils.ReminderUtils;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.utils.IRUtils;


public class testReminderUtil {

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
		
		ServiceContext sc = Context.getServices();
		ReminderSms reminderSms = sc.getReminderService().getReminderSmsRecord(40702, true, new String[]{"vaccination", "reminder"});
		ReminderUtils.pickupRandomReminderText(reminderSms, sc);
	}
}
