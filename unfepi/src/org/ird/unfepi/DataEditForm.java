package org.ird.unfepi;

import org.ird.unfepi.GlobalParams.ServiceType;
import org.ird.unfepi.constants.SystemPermissions;

public class DataEditForm extends DataForm{
	private static final ServiceType serviceType = ServiceType.DATA_EDIT;

	public DataEditForm () {
		super(serviceType);
	}
	DataEditForm(String formName, String formTitle, SystemPermissions permission){
		super(formName, formTitle, permission, serviceType, false);
	}
}
