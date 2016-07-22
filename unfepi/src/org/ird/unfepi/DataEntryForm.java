package org.ird.unfepi;

import org.ird.unfepi.GlobalParams.ServiceType;
import org.ird.unfepi.constants.SystemPermissions;

public class DataEntryForm extends DataForm{
	private static final ServiceType serviceType = ServiceType.DATA_ENTRY;

	public DataEntryForm () {
		super(serviceType);
	}
	public DataEntryForm(String formName, String formTitle, SystemPermissions permission){
		super(formName, formTitle, permission, serviceType, false);
	}
}
