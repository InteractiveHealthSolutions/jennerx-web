package org.ird.unfepi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.constants.WebGlobals;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class DataEntryFormController extends SimpleFormController{
	private DataEntryForm dataEntryForm;

	public void setDataEntryForm (DataEntryForm dataEntryForm) {
		this.dataEntryForm = dataEntryForm;
	}

	public DataEntryForm getDataEntryForm () {
		return dataEntryForm;
	}
	
	public void addModelAttribute(Map<String, Object> model, String name, Object attribute){
		model.put(name, attribute);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected final ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel) throws Exception {
		if(controlModel == null){
			controlModel = new HashMap();
		}
		controlModel.put("dataFormObject", getDataEntryForm());
		return super.showForm(request, response, errors, controlModel);
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING, WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING, true));
	}
}
