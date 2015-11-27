package org.ird.unfepi.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.utils.reporting.ExceptionUtil;
import org.ird.unfepi.utils.reporting.LoggerUtil;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;

public class IRSettingValidator {
	public String	ERROR_MESSAGE	= "";

	public boolean validateSettingValue( String name , String value ) {
		ERROR_MESSAGE = "";
		if (name.trim().equalsIgnoreCase( "cellnumber.number-length-without-zero" )) {
			try {
				int v = Integer.parseInt( value );
				if (v >= 4 && v <= 20) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must lie in the range 4-20";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "cellnumber.user-num-length" )) {
			try {
				int v = Integer.parseInt( value );
				if (v >= 4 && v <= 15) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must lie in the range 4-15";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "cellnumber.prefix-length" )) {
			try {
				int v = Integer.parseInt( value );
				if (v >= 1 && v <= 10) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must lie in the range 4-10";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "daily-rescheduling.hour" )) {
			try {
				int v = Integer.parseInt( value );
				if (v >= 0 && v <= 23) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must lie in the range 0-23";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "daily-rescheduling.minute" )) {
			try {
				int v = Integer.parseInt( value );
				if (v >= 0 && v <= 59) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must lie in the range 0-59";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "daily-rescheduling.second" )) {
			try {
				int v = Integer.parseInt( value );
				if (v >= 4 && v <= 59) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must lie in the range 0-59";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "response.max-response-length" )) {
			try {
				int v = Integer.parseInt( value );
				if (v >= 1 && v <= 500) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must lie in the range 1-500";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "reminder.default-reminder-text" )) {
			try {
				if (value.trim().length() > 0) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must be valid string of length greater than 0";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "admin.email-address" )) {
			try {
				if (DataValidation.validate( REG_EX.EMAIL , value.trim() )) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must be a valid email address";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "reminder.default-verified-response-text" )) {
			try {
				if (value.trim().length() > 0) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must be valid string of length greater than 0";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "reminder.default-invalid-response-text" )) {
			try {
				if (value.trim().length() > 0) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must be valid string of length greater than 0";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "reminder.daily-rem-resp.updates" )) {
			try {
				if (value.trim().length() > 0) {
					String[] emails = value.split( "," );
					for ( String e : emails ) {
						if (!DataValidation.validate( REG_EX.EMAIL , e )) {
							ERROR_MESSAGE = "property value must be valid email addresses separated by comma (,)";
							return false;
						}
					}

				}
				return true;
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must be valid email addresses separated by comma (,)";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "reminder.missed-vacc.updates" )) {
			try {
				if (value.trim().length() > 0) {
					String[] emails = value.split( "," );
					for ( String e : emails ) {
						if (!DataValidation.validate( REG_EX.EMAIL , e )) {
							ERROR_MESSAGE = "property value must be valid email addresses separated by comma (,)";
							return false;
						}
					}
				}
				return true;
			}
			catch ( Exception e ) {
				LoggerUtil.logIt( "Error while updating setting:(" + name
						+ value + ") " + ExceptionUtil.getStackTrace( e ) );
			}
			ERROR_MESSAGE = "property value must be valid email addresses separated by comma (,)";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "child.child-id.min-length" )) {
			try {
				int v = Integer.parseInt( value );
				int propertyMaxln = Integer.parseInt( Context.getSetting(
						"child.child-id.max-length" , "20" ) );
				if (v >= 4 && v <= propertyMaxln) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must lie in the range 4-20 and less than value of property child.child-id.max-length";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "child.child-id.max-length" )) {
			try {
				int v = Integer.parseInt( value );
				int propertyMinln = Integer.parseInt( Context.getSetting(
						"child.child-id.min-length" , "4" ) );
				if (v >= propertyMinln && v <= 20) {
					return true;
				}
			}
			catch ( Exception e ) {
			}
			ERROR_MESSAGE = "property value must lie in the range 4-20 and greater than value of property child.child-id.min-length";
			return false;
		}
		else if (name.trim().equalsIgnoreCase( "logs.root-path" )) {
			String regx = "";
			if (System.getProperty( "os.name" ).toLowerCase().indexOf( "linux" ) != -1) {
				regx = "(/[\\w\\-]+)+";
			}
			else {
				regx = "[a-zA-Z]:(\\\\[\\w-]+)+";
			}
			Pattern p = Pattern.compile( regx );
			Matcher m = p.matcher( value );
			if (!m.matches()) {
				ERROR_MESSAGE = "property value must be a valid file path like /../../../";
				return false;
			}
			return true;
		}
		return false;
	}
}
