package org.ird.unfepi.web.validator;

import java.util.Calendar;
import java.util.Date;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;

import com.mysql.jdbc.StringUtils;

public class EntityValidation {

	public String	ERROR_MESSAGE	= "";

/*	public boolean validateMrNumber( Child ch ) {
		ERROR_MESSAGE = "";

		if (StringUtils.isEmptyOrWhitespaceOnly( ch.getMrNumber() )
				|| !DataValidation.validate( REG_EX.NUMERIC , ch.getMrNumber().trim() )) {
			ERROR_MESSAGE = "Epi Number is empty or non numeric.";
			return false;
		}
		else {
			if (ch.getDateEnrolled() == null) {
				ERROR_MESSAGE = "Epi Number cannot be determined without enrollment date.";
				return false;
			}
			final long dateenr = ch.getDateEnrolled().getTime();
			Calendar caldemin = Calendar.getInstance();
			caldemin.setTime(new Date(dateenr));
			caldemin.add(Calendar.MONTH, -3);
			
			Calendar caldemax = Calendar.getInstance();
			caldemax.setTime(new Date(dateenr));
			
			if (!ch.getMrNumber().trim().startsWith(Integer.toString( caldemin.get( Calendar.YEAR ) ) )
					&& !ch.getMrNumber().trim().startsWith(Integer.toString( caldemax.get( Calendar.YEAR ) ) )) {
				ERROR_MESSAGE = "Epi Number should start with year of enrollment date ("
						+ caldemin.get( Calendar.YEAR ) + " OR " + caldemax.get(Calendar.YEAR) + ").";
				return false;
			}
			ServiceContext sc = Context.getServices();
			try {
				List<Child> p2 = sc.getChildService().findByEpiOrMrNumber(ch.getMrNumber().trim() );

				if (p2.size() == 0)
					return true;

				if (p2.size() > 1
						|| ( p2.size() == 1 && !p2.get( 0 ).getChildId()
								.equalsIgnoreCase( ch.getChildId().trim() ) )) {
					ERROR_MESSAGE = "Epi Reg number occupied by " + p2.size() + " child(ren).";
					return false;
				}
			}
			catch ( Exception e ) {
				ERROR_MESSAGE = "Getting Epi threw error :" + e.getMessage();
				return false;
			}
			finally {
				sc.closeSession();
			}
		}
		return true;
	}*/
	
	public boolean validateMrNumber(String epiOrMrNum, Date enrollmentDate ) {
		ERROR_MESSAGE = "";

		if (StringUtils.isEmptyOrWhitespaceOnly( epiOrMrNum )
				|| !DataValidation.validate( REG_EX.NUMERIC , epiOrMrNum.trim(), WebGlobals.MIN_EPI_NUMBER_LENGTH, WebGlobals.MAX_EPI_NUMBER_LENGTH)) {
			ERROR_MESSAGE = "Epi Number should be a numeric sequence of digits"+ WebGlobals.MIN_EPI_NUMBER_LENGTH;
			return false;
		}
		else {
			if (enrollmentDate == null) {
				ERROR_MESSAGE = "Epi Number cannot be determined without screening or enrollment date.";
				return false;
			}
			final long dateenr = enrollmentDate.getTime();
			Calendar caldemin = Calendar.getInstance();
			caldemin.setTime(new Date(dateenr));
			caldemin.add(Calendar.MONTH, -3);
			
			Calendar caldemax = Calendar.getInstance();
			caldemax.setTime(new Date(dateenr));
			
			if (!epiOrMrNum.trim().startsWith(Integer.toString( caldemin.get( Calendar.YEAR ) ) )
					&& !epiOrMrNum.trim().startsWith(Integer.toString( caldemax.get( Calendar.YEAR ) ) )) {
				ERROR_MESSAGE = "Epi Number should start with year of enrollment/screening date ("
						+ caldemin.get( Calendar.YEAR ) + " OR " + caldemax.get(Calendar.YEAR) + ").";
				return false;
			}
		}
		return true;
	}
	
	/*public boolean validateChildId( Child ch ) {
		ERROR_MESSAGE = "";

		int pidMinln = Integer.parseInt( Context.getIRSetting( "child.child-id.min-length" , "1" ) );
		int pidMaxln = Integer.parseInt( Context.getIRSetting( "child.child-id.max-length" , "10" ) );
		
		if (StringUtils.isEmptyOrWhitespaceOnly( ch.getChildId() )
				|| !DataValidation.validate( REG_EX.NUMERIC , ch.getChildId().trim() )) {
			ERROR_MESSAGE = "Child id is empty or non numeric";
			return false;
		}
		else if (ch.getChildId().length() < pidMinln
				|| ch.getChildId().length() > pidMaxln) {
			ERROR_MESSAGE = "Child id length is not between " + pidMinln + "-" + pidMaxln + " digits";
			return false;
		}
		else {
			ServiceContext sc = Context.getServices();
			try {
				Child c = sc.getChildService().getChildbyChildId( ch.getChildId().trim() , true );
				if (c != null
						&& !c.getChildId().equalsIgnoreCase( ch.getChildId().trim() )) {
					ERROR_MESSAGE = "Child id exists";
					return false;
				}
			}
			catch ( ChildDataException e ) {
				ERROR_MESSAGE = "Getting child id threw exception :" + e.getMessage();
				return false;
			}
			finally {
				sc.closeSession();
			}
		}
		return true;
	}

	public boolean validateChildId( String childId , boolean verifyExisting ) {
		ERROR_MESSAGE = "";

		int pidMinln = Integer.parseInt( Context.getIRSetting( "child.child-id.min-length" , "1" ) );
		int pidMaxln = Integer.parseInt( Context.getIRSetting( "child.child-id.max-length" , "10" ) );
		
		if (StringUtils.isEmptyOrWhitespaceOnly( childId )
				|| !DataValidation.validate( REG_EX.NUMERIC , childId.trim() )) {
			ERROR_MESSAGE = "Child id is empty or non numeric";
			return false;
		}
		else if (childId.length() < pidMinln || childId.length() > pidMaxln) {
			ERROR_MESSAGE = "Child id length should be between " + pidMinln
					+ "-" + pidMaxln + " digits";
			return false;
		}
		
		if (verifyExisting) {
			ServiceContext sc = Context.getServices();

			try {
				Child c = sc.getChildService().getChildbyChildId( childId.trim() , true );
				if (c != null
						&& !c.getChildId().equalsIgnoreCase( childId.trim() )) {
					ERROR_MESSAGE = "Child id exists";
					return false;
				}
			}
			catch ( ChildDataException e ) {
				ERROR_MESSAGE = "Getting child id threw exception :" + e.getMessage();
				return false;
			}
			finally {
				sc.closeSession();
			}
		}
		return true;
	}*/

	/*public boolean validateChildContactNumber( Child ch ) {
		if (StringUtils.isEmptyOrWhitespaceOnly( ch.getCurrentCellNo().trim())
				|| (!DataValidation.validate( REG_EX.CELL_NUMBER , ch.getCurrentCellNo().trim())
						&& !DataValidation.validate( REG_EX.PTCL_WIRELESS_NUMBER , ch.getCurrentCellNo().trim()) )){
			ERROR_MESSAGE = "Cell number is empty or invalid";
			return false;
		}
		else {
			ServiceContext sc = Context.getServices();
			try {
				Child p2 = sc.getChildService().getChildbyCurrentCell( ch.getCurrentCellNo().trim() , true );
				if (p2 != null
						&& !p2.getChildId().equalsIgnoreCase( ch.getChildId().trim() )) {
					ERROR_MESSAGE = "Cell number occupied";
					return false;
				}
			}
			catch ( ChildDataException e ) {
				ERROR_MESSAGE = e.getMessage();
				return false;
			}
			finally {
				sc.closeSession();
			}
		}
		return true;
	}*/

	/*public boolean validateChildContactNumber( String cellNumber , boolean verifyExisting, boolean validtePattern) {
		if(validtePattern){
			if (StringUtils.isEmptyOrWhitespaceOnly( cellNumber.trim() )
				|| (!DataValidation.validate( REG_EX.CELL_NUMBER , cellNumber.trim())
						&& !DataValidation.validate( REG_EX.PTCL_WIRELESS_NUMBER , cellNumber.trim()) )){
				ERROR_MESSAGE = "Cell number is empty or invalid";
				return false;
			}
		}
		if (verifyExisting) {
			ServiceContext sc = Context.getServices();
			try {
				Child p2 = sc.getChildService().getChildbyCurrentCell( cellNumber.trim() , true );
				if (p2 != null
						&& !p2.getChildId().equalsIgnoreCase( cellNumber.trim() )) {
					ERROR_MESSAGE = "Cell number occupied";
					return false;
				}
			}
			catch ( ChildDataException e ) {
				ERROR_MESSAGE = e.getMessage();
				return false;
			}
			finally {
				sc.closeSession();
			}
		}
		return true;
	}*/
}
