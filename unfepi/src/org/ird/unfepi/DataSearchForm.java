package org.ird.unfepi;

import org.ird.unfepi.GlobalParams.ServiceType;
import org.ird.unfepi.constants.SystemPermissions;

public class DataSearchForm extends DataForm{
	private static final ServiceType serviceType = ServiceType.DATA_SEARCH;

	public DataSearchForm () {
		super(serviceType);
	}
	DataSearchForm(String formName, String formTitle, SystemPermissions permission, boolean isInsertable){
		super(formName, formTitle, permission, serviceType, isInsertable);
	}
}
