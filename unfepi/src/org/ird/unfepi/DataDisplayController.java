package org.ird.unfepi;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public abstract class DataDisplayController implements Controller{
	private String formView;
	private DataForm dataForm;

	public void setFormView (String formView) {
		this.formView = formView;
	}
	
	public void setDataForm (DataForm dataForm) {
		this.dataForm = dataForm;
	}

	public String getFormView () {
		return formView;
	}

	public DataForm getDataForm () {
		return dataForm;
	}

	public void addModelAttribute(Map<String, Object> model, String name, Object attribute){
		model.put(name, attribute);
	}
	
	public ModelAndView showForm(Map<String, Object> model){
		model.put("dataFormObject", getDataForm());
		return new ModelAndView(getFormView(), "model", model);
	}
}
