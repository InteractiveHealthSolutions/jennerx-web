package org.ird.unfepi;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataDisplayController {
	private String formView;
	private DataForm dataForm;

	DataDisplayController() {
		this("", new DataViewForm("", "", null, false));
	}

	public DataDisplayController(String formView, DataForm dataForm) {
		this.formView = formView;
		this.dataForm = dataForm;
	}

	public void setFormView(String formView) {
		this.formView = formView;
	}

	public void setDataForm(DataForm dataForm) {
		this.dataForm = dataForm;
	}

	public String getFormView() {
		return formView;
	}

	public DataForm getDataForm() {
		return dataForm;
	}

	public void addModelAttribute(Map<String, Object> model, String name,Object attribute) {
		model.put(name, attribute);
	}

	public ModelAndView showForm(Map<String, Object> model) {
		model.put("dataFormObject", getDataForm());
		return new ModelAndView(getFormView(), "model", model);
	}

}
