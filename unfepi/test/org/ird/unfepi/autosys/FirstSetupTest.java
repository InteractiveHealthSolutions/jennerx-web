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
import org.ird.unfepi.utils.IRUtils;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({ UserServiceHelper.class })
@PowerMockIgnore("javax.management.*")
public class FirstSetupTest {
	private FirstSetupService fst = new FirstSetupService();
	
	private void initDb() throws InstanceAlreadyExistsException, IOException{
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
	public void shouldReturnErrorWhenEmptyRequest() throws JSONException, IOException, InstanceAlreadyExistsException {
		String res = fst.authenticate("{}");
		assertTrue(res.equalsIgnoreCase(String.valueOf(ResponseStatus.STATUS_INCORRECT_CREDENTIALS)));
	}
	
	@Test
	public void shouldReturnAuthenticationErrorWhenCredentialsDonotMatch() throws JSONException, InstanceAlreadyExistsException, IOException {
		PowerMockito.mockStatic(UserServiceHelper.class);
		when(UserServiceHelper.authenticateUser("pw1", "u1", "ADMIN")).thenReturn(null);
		JSONObject req = new JSONObject();
		req.put(RequestElements.LG_USERNAME, "u1");
		req.put(RequestElements.LG_PASSWORD, "pw");
		String res = fst.authenticate(req.toString());
		assertTrue(res.equalsIgnoreCase(String.valueOf(ResponseStatus.STATUS_INCORRECT_CREDENTIALS)));
	}
	
	@Test
	public void shouldReturnAuthenticationSuccessWhenCredentialsMatch() throws JSONException, InstanceAlreadyExistsException, IOException {
		PowerMockito.mockStatic(UserServiceHelper.class);
		when(UserServiceHelper.authenticateUser("pw1", "u1", "ADMIN")).thenReturn(new User(1, "u1", "pw1"));
		JSONObject req = new JSONObject();
		req.put(RequestElements.LG_USERNAME, "u1");
		req.put(RequestElements.LG_PASSWORD, "pw");
		String res = fst.authenticate(req.toString());
		JSONObject jo = new JSONObject(res);
		assertTrue(jo.has("status"));
		assertTrue(jo.getString("status").equalsIgnoreCase(String.valueOf(ResponseStatus.STATUS_SUCCESS)));
	}
}
