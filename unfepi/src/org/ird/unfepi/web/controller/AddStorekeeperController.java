package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Storekeeper;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.StringUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.validator.StorekeeperValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AddStorekeeperController extends DataEntryFormController{

	private static final FormType formType = FormType.STOREKEEPER_ADD;
	private Date dateFormStart = new Date();
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		Storekeeper stork = (Storekeeper) command;
		
		ServiceContext sc = Context.getServices();
		try{
			String cityId = sc.getVaccinationService().findVaccinationCenterById(stork.getClosestVaccinationCenterId(), true, new String[]{"idMapper"}).getIdMapper().getIdentifiers().get(0).getIdentifier().substring(0, 2);
			String sql = "SELECT IFNULL(MAX(SUBSTRING(i.identifier, "+(cityId.length()+1)+",LENGTH(i.identifier)-"+(cityId.length()-1)+")),0) FROM storekeeper s JOIN idmapper id ON s.mappedId=id.mappedId JOIN identifier i ON id.mappedId=i.mappedId WHERE closestVaccinationCenterId IS NOT NULL AND i.identifier LIKE '"+cityId+"%'";
			int maxidstk = Integer.parseInt(sc.getCustomQueryService().getDataBySQL(sql ).get(0).toString());
			String vcProgramId = cityId+StringUtils.leftPad(""+(maxidstk+1), 4, "0");//request.getParameter("autogenIdPart")+request.getParameter("storekeeperIdAssigned");
			String username = request.getParameter("usernamegiven");
			String pwd = request.getParameter("passwordgiven");
			
			ControllerUIHelper.doStorekeeperRegistration(vcProgramId, stork, username, pwd, false, user.getUser(), sc);
			
			EncounterUtil.createStorekeeperRegistrationEncounter(vcProgramId, stork, username, DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(stork), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));

			sc.commitTransaction();

			return new ModelAndView(new RedirectView("viewStorekeepers.htm?action=search&"+SearchFilter.PROGRAM_ID.FILTER_NAME()+"="+vcProgramId));
		}
		catch (Exception e) {
			sc.rollbackTransaction();
			
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {
		dateFormStart = new Date();
		
		return new Storekeeper();
	}
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		StorekeeperValidator valid = (StorekeeperValidator)getValidator();
		String programid = null;// (request.getParameter("autogenIdPart")+request.getParameter("storekeeperIdAssigned"))
		valid.validateRegistration(programid, request.getParameter("birthdateOrAge"), request.getParameter("storekeeperagey"), (Storekeeper) command, errors);
		valid.validateStorekeeperCredentials(request.getParameter("usernamegiven"), request.getParameter("passwordgiven"), request.getParameter("passwordconfirm"), errors);
		return super.processFormSubmission(request, response, command, errors);
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			model.put("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
			
/*//			String storekeeperIdAssigned = request.getParameter("storekeeperIdAssigned");
//			model.put("storekeeperIdAssigned", storekeeperIdAssigned);
//			
//			String autogenIdPart = request.getParameter("autogenIdPart");
//			model.put("autogenIdPart", autogenIdPart);
*/			model.put("usernamegiven", request.getParameter("usernamegiven"));
			model.put("passwordgiven", request.getParameter("passwordgiven"));
			model.put("birthdateOrAge", request.getParameter("birthdateOrAge"));
			model.put("storekeeperagey", request.getParameter("storekeeperagey"));
}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving storekeeper reference datas. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		
		return model;
	}
}
