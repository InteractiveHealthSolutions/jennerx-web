
package org.ird.unfepi.web.validator;

import java.util.List;

import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LocationValidator implements Validator{

	@Override
	public boolean supports(Class arg0) {
		return Location.class.equals(arg0);
	}

	@Override
	public void validate(Object command, Errors error) {
		Location loc = (Location) command;
		
		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, loc.getName())){
			error.rejectValue("name" , "" , ErrorMessages.LOCATION_NAME_INVALID);
		}
		else {
			ServiceContext sc = Context.getServices();
			try{
				List el = sc.getCustomQueryService().getDataByHQL("FROM Location WHERE name='"+loc.getName()+"' AND locationId <> "+loc.getLocationId());
				if(el.size() > 0){
					error.rejectValue("name" , "" , "Location with given name already exists");
				}
			}
			finally {
				sc.closeSession();
			}
			
		}
		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, loc.getFullName())){
			error.rejectValue("fullName" , "" , ErrorMessages.LOCATION_FULLNAME_INVALID);
		}

		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, loc.getFullName())){
			error.rejectValue("fullName" , "" , ErrorMessages.LOCATION_FULLNAME_INVALID);
		}
		
		// if location is city and other id is null then reject
		if(loc.getLocationType().getLocationTypeId() == 1 && !DataValidation.validate(REG_EX.NUMERIC, loc.getOtherIdentifier())){
			error.rejectValue("otherIdentifier" , "" , "A valid numeric ID must be specified for Cities. This is appended with Center ID");
		}
		
		if(loc.getLocationType() == null || 
				loc.getLocationType().getLocationTypeId() == null 
				|| loc.getLocationType().getLocationTypeId() <= 0){
			error.rejectValue("locationType.locationTypeId" , "" , "A location type must be specified");
		}
		if(loc.getParentLocation() == null || 
				loc.getParentLocation().getLocationId() == null || loc.getParentLocation().getLocationId() <= 0){
			error.rejectValue("parentLocation.locationId" , "" , "A parent location must be specified");
		}
	}
}


