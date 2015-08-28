import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import net.jmatrix.eproperties.EProperties;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.autosys.smser.ResponseReaderJob;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.utils.IRUtils;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.quartz.JobExecutionException;


public class EPICertificateTest {
	public static void main(String[] args) throws IOException, InstanceAlreadyExistsException, JobExecutionException {
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
		
		TarseelContext.instantiate(prop, "smstarseel.cfg.xml");

		new ResponseReaderJob().readResponses("EPICertificateTest");
	}
}
