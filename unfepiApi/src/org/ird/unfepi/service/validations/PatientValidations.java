package org.ird.unfepi.service.validations;
/*package org.ird.immunizationreminder.service.validations;

import ird.xoutTB.context.Context;
import ird.xoutTB.db.entity.Child;
import ird.xoutTB.reporting.exceptions.ChildDataException;

public class ChildValidations {

	public static boolean validateChild(Child child) throws ChildDataException {
		if(child.getChildId()==null || child.getChildId().compareTo("")==0){
			throw new ChildDataException(ChildDataException.CHILD_ID_MISSING_OR_NULL,ChildDataException.CHILD_ID_MISSING_OR_NULL);
		}
		if(child.getFirstName()==null || child.getFirstName().compareTo("")==0){
			throw new ChildDataException(ChildDataException.CHILD_NAME_EMPTY,ChildDataException.CHILD_NAME_EMPTY);
		}
		
		validateCellNumber(child.getCellNoLatest());
		return false;
		
	}
	public static void validateCellNumber(String cellNumber) throws ChildDataException{
		if(cellNumber==null || cellNumber.compareTo("")==0){
			throw new ChildDataException(ChildDataException.CHILD_CURRENT_CELL_MISSING,ChildDataException.CHILD_CURRENT_CELL_MISSING);
		}
		try{
		Double.parseDouble(cellNumber);
		}
		catch (Exception e) {
			throw new ChildDataException(ChildDataException.INVALID_CELL_NUMBER,ChildDataException.INVALID_CELL_NUMBER);
		}
		if(cellNumber.length() < 
				Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"))){
			throw new ChildDataException(ChildDataException.CHILD_CELL_NUMBER_INVALID_DIGIT_RANGE,ChildDataException.CHILD_CELL_NUMBER_INVALID_DIGIT_RANGE);
		}
	}
}*/