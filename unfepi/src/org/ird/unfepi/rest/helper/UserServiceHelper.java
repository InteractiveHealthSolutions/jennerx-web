package org.ird.unfepi.rest.helper;
import java.util.HashMap;
import java.util.Map;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.service.VaccinationService;
import org.ird.unfepi.service.exception.UserServiceException;
import org.ird.unfepi.utils.SecurityUtils;
import org.json.simple.JSONObject;

import com.github.zafarkhaja.semver.Version;

public class UserServiceHelper
{
	public static String parseUser(JSONObject jsonObject)
	{
		String userName = null;
		String password = null;
		String userType = null;
		IdMapper idMapperLoginUser = null;
		ServiceContext cxt = Context.getServices();

		try
		{
			
			// GET PARAMETERS FROM MOBILE
			userName = (String) jsonObject.get(RequestElements.LG_USERNAME);
			password = (String) jsonObject.get(RequestElements.LG_PASSWORD);
			String reqVer = (String) jsonObject.get(RequestElements.APP_VER);
			try
			{
				String currentVersion = Context.getSetting("mobile.app.version", "0.1.0");
				String allowPreRelease = Context.getSetting("mobile.pre-release.allow", "false");
				Boolean boolAllowPreRelse = Boolean.valueOf(allowPreRelease);

				Boolean _isValid = isAppVersionValid(reqVer, currentVersion, boolAllowPreRelse);

				if (_isValid == false)
				{
					return ResponseBuilder.buildResponse(ResponseStatus.STATUS_OLD_VERSION, null);
				}
			}
			catch (Exception ex)
			{
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_UNRECOGNISED_OR_INVALID_VERSION,
						null);
			}
			userType= (String)jsonObject.get(RequestElements.USER_TYPE);		
			
			//MATCH ROLE TYPE
			String role="";
			if(userType.equalsIgnoreCase(RequestElements.USER_TYPE_VACCINATOR))
			{
				role = GlobalParams.VACCINATOR_ROLE_NAME;
			}
			else if(userType.equalsIgnoreCase(RequestElements.USER_TYPE_STOREKEEPER))
			{
				role = GlobalParams.STOREKEEPER_ROLE_NAME;
			}
			password = SecurityUtils.decrypt(password, userName);
			
			//AUTHENTICATION
			User user = authenticateUser(password, userName, role);
			
			if(user!=null)
			{
				//RETURN SUCCESS ONLY IF THERE IS A VALID IDMAPPER ALONG WITH CORRECT USERNAME AND PASSWORD
				Map<String, Object>userdata = new HashMap<String, Object>();
				
				//add programid of the remote user
				userdata.put(ResponseElements.LG_USERID, Integer.valueOf(user.getMappedId()));
				//vacination center
				if(role.equalsIgnoreCase(GlobalParams.VACCINATOR_ROLE_NAME))
				{
					VaccinationService vs = cxt.getVaccinationService();
					
					Vaccinator vaccinator = cxt.getVaccinationService().findVaccinatorById(user.getMappedId());
					if(vaccinator!=null)
						userdata.put(ResponseElements.CENTRE_ID, Integer.valueOf(vaccinator.getVaccinationCenterId()));
				}
				//add email for testing //TODO remove this block
				userdata.put("email", user.getEmail());
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, userdata);
			}
			else
			{
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INCORRECT_CREDENTIALS, null);
			}
		}
		catch(Exception ex)
		{
			GlobalParams.MOBILELOGGER.error("Error while parsing JSON for LoginService");
			GlobalParams.MOBILELOGGER.error(ex.getMessage());
			return ResponseBuilder.buildResponse( ResponseStatus.STATUS_INTERNAL_ERROR, null);
		}finally{cxt.closeSession();}
	}
	
	public static User authenticateUser(String password, String username, String role)
	{
		ServiceContext cxt = Context.getServices();
		User user;
		try
		{
			//password is encypted, it will get decrypted by the user service
			LoggedInUser mobileUser = Context.getAuthenticatedUser(username, password);			
			if(! mobileUser.hasRole(role))
			{
				return null;
			}
			user=mobileUser.getUser();
			return user;
		}
		catch (UserServiceException e)
		{
			return handleException(e);
		}
		catch (Exception e)
		{
			return handleException(e);
		}finally{
			cxt.closeSession();
		}
	}

	/**
	 * It compares version number. If allowPreRelease is false then both version should not
	 * have Pre-Release version and if allowPreRelease is true then both version can have or not.
	 * 
	 * @param requestVersion
	 * @param savedVersion
	 * @return If Major and and minor version are same as currentVersion then it will return
	 *         true otherwise return false
	 * @throws Exception
	 *             If Pre-Release is found while it is not allowed.
	 *             If Request version (major and minor version) is bigger then saved version
	 */
	private static Boolean isAppVersionValid(String requestVersion, String savedVersion, Boolean allowPreRelease) throws Exception
	{
		Boolean versionValid = false;
		Version _reqVersion = Version.valueOf(requestVersion);
		Version _savedVersion = Version.valueOf(savedVersion);

		String bigVersionError = "Request version (major and minor version) can't be bigger then saved version";

		String emptyString = "";

		if (allowPreRelease == false)
		{
			// Ensuring any version should not have pre-release version

			if (emptyString.equals(_reqVersion.getPreReleaseVersion()) == false || emptyString.equals(_savedVersion.getPreReleaseVersion()) == false)
			{
				throw new Exception("Pre-Release versions are not allowed");
			}
		}

		/*
		 * Major version should be same
		 */

		if (_reqVersion.getMajorVersion() == _savedVersion.getMajorVersion())
		{
			/*
			 * Minor version should be same
			 */

			if (_reqVersion.getMinorVersion() == _savedVersion.getMinorVersion())
			{
				versionValid = true;
			}

			else if (_reqVersion.getMinorVersion() > _savedVersion.getMinorVersion())
			{
				throw new Exception(bigVersionError);
			}
		}

		else if (_reqVersion.getMajorVersion() > _savedVersion.getMajorVersion())
		{
			throw new Exception(bigVersionError);
		}

		return versionValid;
	}
	
	private static User handleException(Exception ex)
	{
		GlobalParams.MOBILELOGGER.equals("Login Failed: due to the following exception");
		GlobalParams.MOBILELOGGER.error(ex.getMessage());		
		ex.printStackTrace();
		return null;
	}

}
