package org.ird.unfepi;

import org.ird.unfepi.GlobalParams.ServiceType;
import org.ird.unfepi.constants.SystemPermissions;

public class DataViewForm extends DataForm{
	private static final ServiceType serviceType = ServiceType.DATA_VIEW;

	public DataViewForm () {
		super(serviceType);
	}
	DataViewForm(String formName, String formTitle, SystemPermissions permission, boolean isInsertable){
		super(formName, formTitle, permission, serviceType, isInsertable);
	}
}
