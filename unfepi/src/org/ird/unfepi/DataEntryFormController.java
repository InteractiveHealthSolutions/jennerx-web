package org.ird.unfepi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.ird.unfepi.constants.WebGlobals;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataEntryFormController {

	private DataEntryForm dataEntryForm;

	public DataEntryFormController() {
		this(new DataEntryForm("", "", null));
	}

	public DataEntryFormController(DataEntryForm dataEntryForm) {
		this.dataEntryForm = dataEntryForm;
	}

	public void setDataEntryForm(DataEntryForm dataEntryForm) {
		this.dataEntryForm = dataEntryForm;
	}

	public DataEntryForm getDataEntryForm() {
		return dataEntryForm;
	}

	public void addModelAttribute(Map<String, Object> model, String name,
			Object attribute) {
		model.put(name, attribute);
	}

	protected final ModelAndView showForm(ModelAndView modelAndView,
			String viewName) {

		modelAndView.addObject("dataFormObject", getDataEntryForm());
		modelAndView.setViewName(viewName);
		return modelAndView;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING, WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING, true));
	}
}
