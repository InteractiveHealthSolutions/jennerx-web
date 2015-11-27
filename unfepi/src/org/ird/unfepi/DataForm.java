package org.ird.unfepi;

import org.ird.unfepi.GlobalParams.ServiceType;
import org.ird.unfepi.constants.SystemPermissions;

public abstract class DataForm {
	public DataForm (ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	private String formTitle;
	private String formName;
	private SystemPermissions permission;
	private ServiceType serviceType;
	private boolean isInsertable;
	
	DataForm(String formName, String formTitle, SystemPermissions permission, ServiceType serviceType, boolean isInsertable){
		this.formName = formName;
		this.formTitle = formTitle;
		this.permission = permission;
		this.serviceType = serviceType;
		this.isInsertable = isInsertable;
	}

	public String getFormTitle () {
		return formTitle;
	}

	public void setFormTitle (String formTitle) {
		this.formTitle = formTitle;
	}

	public String getFormName () {
		return formName;
	}

	public void setFormName (String formName) {
		this.formName = formName;
	}

	public SystemPermissions getPermission () {
		return permission;
	}

	public void setPermission (SystemPermissions permission) {
		this.permission = permission;
	}

	public ServiceType getServiceType () {
		return serviceType;
	}

	public void setServiceType (ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public boolean getIsInsertable () {
		return isInsertable;
	}

	public void setIsInsertable (boolean isInsertable) {
		this.isInsertable = isInsertable;
	}
	
}
