package org.ird.unfepi.autosys;

import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.model.User;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.UserServiceHelper;
import org.ird.unfepi.rest.resources.FirstSetupService;
import org.ird.unfepi.service.UserService;
import org.ird.unfepi.utils.GZipper;
import org.ird.unfepi.utils.IRUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import net.jmatrix.eproperties.EProperties;

public class FirstSetupTest2 {
	private FirstSetupService fst = new FirstSetupService();
	
	static {
		try {
			initDb();
		} catch (InstanceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void initDb() throws InstanceAlreadyExistsException, IOException{
		System.out.println(">>>>LOADING SYSTEM PROPERTIES...");
		InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("unfepi.properties");
		// Java Properties donot seem to support substitutions hence EProperties are used to accomplish the task
		EProperties root = new EProperties();
		root.load(f);

		// Java Properties to send to context and other APIs for configuration
		Properties prop = new Properties();
		prop.putAll(IRUtils.convertEntrySetToMap(root.entrySet()));

		Context.instantiate(prop);
	}
	
	@Test
	public void shouldReturnXChildren() throws InstanceAlreadyExistsException, IOException, JSONException {
		int childrenInDb = Integer.parseInt(Context.getServices().getCustomQueryService().getDataBySQL("select count(*) from child").get(0).toString());

		System.out.println(childrenInDb);
		
		String cl = fst.getAllChildren();
		JSONObject jl = new JSONObject(GZipper.decompress(cl));
		
		assertTrue(jl.has("allchildren"));
		JSONArray jcl = jl.getJSONArray("allchildren");
		assertTrue(jcl.length() == childrenInDb);
		
		for (int i = 0; i < jcl.length(); i++) {
			JSONObject it = jcl.getJSONObject(i);
			assertTrue(it.has(RequestElements.CHILD_IDENTIFIER));
			assertTrue(it.has(RequestElements.FIRST_NAME));
			assertTrue(it.has(RequestElements.GENDER));
//TODO			assertTrue(it.has(RequestElements.MOTHER_FIRST_NAME));
			assertTrue(it.has(RequestElements.BIRTH_DATE));
			assertTrue(it.has(RequestElements.ENROLLED_DATE));
			assertTrue(it.has(RequestElements.STATUS));
			assertTrue(it.has(RequestElements.ADDRESS1));
		//TODO	assertTrue(it.has(RequestElements.CHILD_ADDRESS2));
	//TODO		assertTrue(it.has(RequestElements.CHILD_NUMBER));

		//TODO	assertTrue(it.has(RequestElements.CHILD_CREATEDBYUSERID));
		//TODO	assertTrue(it.has(RequestElements.CHILD_CREATEDDATE));
		}
	}
	
	@Test
	public void shouldReturnXVaccination() throws InstanceAlreadyExistsException, IOException, JSONException {
		int vaccinationInDb = Integer.parseInt(Context.getServices().getCustomQueryService().getDataBySQL("select count(*) from vaccination").get(0).toString());

		System.out.println(vaccinationInDb);
		
		String vl = fst.getAllVaccinations();
		JSONObject jl = new JSONObject(GZipper.decompress(vl));
		
		assertTrue(jl.has("allvaccinations"));
		JSONArray jvl = jl.getJSONArray("allvaccinations");
		assertTrue(jvl.length() == vaccinationInDb);
		
		for (int i = 0; i < jvl.length(); i++) {
			JSONObject it = jvl.getJSONObject(i);
			assertTrue(it.has(RequestElements.VACCINATION_RECORD_NUM));
			assertTrue(it.has(RequestElements.CHILD_PROG_ID));
		/*	assertTrue(it.has(RequestElements.VACCINATION_CENTER));
			assertTrue(it.has(RequestElements.VACCINATION_DATE));
			assertTrue(it.has(RequestElements.VACCINATION_DUE_DATE));
			assertTrue(it.has(RequestElements.VACCINATION_STATUS));
			assertTrue(it.has(RequestElements.VACCINATOR));
			assertTrue(it.has(RequestElements.VACCINE));
			
			assertTrue(it.has(RequestElements.CREATED_BY));
			assertTrue(it.has(RequestElements.DATE_CREATED));*/

		}
	}
}
