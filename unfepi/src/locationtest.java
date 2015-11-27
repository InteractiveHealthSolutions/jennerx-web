import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import net.jmatrix.eproperties.EProperties;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.autosys.utils.ReminderUtils;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.utils.IRUtils;


public class locationtest {

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
		List<Location> ll = sc.getCustomQueryService().getDataByHQL("FROM Location WHERE parentLocation IS NULL");
		for (Location object : ll) {
			Map<String, Object> c = getChild(object);
			c.get("null");
		}
	}
	
	private static Map<String, Object> getChild(Location location){
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("id", location.getLocationId());
		item.put("text", location.getName());
		if(location.getChildLocations().size() > 0){
			List children = new ArrayList();
			for (Location chl : location.getChildLocations()) {
				children.add(getChild(chl));
			}
			item.put("children", children);
		}
		
		return item;
	}
}
