package org.ird.unfepi.utils.validation;
/*package org.ird.unfepi.utils.validation;

import org.ird.unfepi.context.Globals;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;

public class ConsistencyValidations {

	public static boolean validateChildInsertion(Child child, ServiceContext sc) throws ChildDataInconsistencyException{
		if(!DataValidation.validate(REG_EX.NUMERIC, Long.toString(child.getChildId()), Globals.CHILD_ID_LEN,  Globals.CHILD_ID_LEN)){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_ID_LEN_INVALID, ChildDataInconsistencyException.CHILD_ID_LEN_INVALID);
		}
		
		if(child.getBirthdate() == null){
			if(!DataValidation.validate(REG_EX.NUMERIC, Long.toString(child.getChildId()), Globals.CHILD_ID_LEN,  Globals.CHILD_ID_LEN)){
				throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_ID_LEN_INVALID, ChildDataInconsistencyException.CHILD_ID_LEN_INVALID);
			}
		}
	}
}
*/