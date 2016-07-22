package org.ird.unfepi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ird.unfepi.constants.WebGlobals;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataEditFormController {

	private DataEditForm dataEditForm;

	DataEditFormController() {
		this(new DataEditForm("", "", null));
	}

	public DataEditFormController(DataEditForm dataEditForm) {
		this.dataEditForm = dataEditForm;
	}

	public void setDataEditForm(DataEditForm dataEditForm) {
		this.dataEditForm = dataEditForm;
	}

	public DataEditForm getDataEditForm() {
		return dataEditForm;
	}

	public void addModelAttribute(Map<String, Object> model, String name,
			Object attribute) {
		model.put(name, attribute);
	}

	protected final ModelAndView showForm(ModelAndView modelAndView,
			String viewName) {

		modelAndView.addObject("dataFormObject", getDataEditForm());
		modelAndView.setViewName(viewName);
		return modelAndView;
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING, WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING, true));
	}
}
