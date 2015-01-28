import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import net.jmatrix.eproperties.EProperties;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.reporting.LoggerUtil;
import org.slf4j.LoggerFactory;

public class logT4st {
	
public static void main(String[] args)
{
	 GlobalParams.DBLOGGER.error("TEST LOGGER");
	 LoggerFactory.getLogger(logT4st.class).info("default logger");
	 
	 LoggerFactory.getLogger("dbAppender").info("db logger");

	 LoggerFactory.getLogger("fileAppender").info("file logger");
	
	System.out.println(">>>>LOADING SYSTEM PROPERTIES...");
	//InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("build.properties");
	FileInputStream fip = null;
	try {
			URL url = ClassLoader.getSystemResource("build.properties");
			fip = new FileInputStream(new File(url.getFile()));
			
			EProperties root = new EProperties();
			root.load(fip);

			Map map =IRUtils.convertEntrySetToMap(root.entrySet());
			EProperties nested = root.getProperties("nested");
			//System.out.println("the value of bar in nested is " + nested.getString("bar"));
			System.out.println("PROP::::" + root.getProperty("appserver.lib"));

			Properties prop = new Properties();
			prop.putAll(map);

			System.out.println(prop.toString());
			System.out.println("PROP::::" + prop.getProperty("appserver.lib"));
			Context.instantiate(prop);
	}
	catch (InstanceAlreadyExistsException e) {
		e.printStackTrace();
	}
	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("......PROPERTIES LOADED SUCCESSFULLY......");
	LoggerUtil.logIt("......PROPERTIES LOADED SUCCESSFULLY......");
	
	System.out.println(Integer.parseInt("20120000".substring(0, 4)));
	System.out.println(Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())));
	System.out.println(Integer.parseInt("20120000".substring(0, 4)) > Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())));

	System.out.println("StoREkeEper".matches("(?i:storekeeper|shopkeeper|salesman)"));
	
	///ServiceContext sc = Context.getServices();
	//List<VariableSetting> list = sc.getIRSettingService().findByCriteria(null,null, "DATA_QUERY", null, null, null, true, 0, 1);

	//System.out.println("01001130315001".substring(5).startsWith(new SimpleDateFormat("yyMMdd").format(new Date())));
	//System.out.println(UUID.randomUUID());
	 //LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	 //StatusPrinter.print(lc);
	 
}
}
