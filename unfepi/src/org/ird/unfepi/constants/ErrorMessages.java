package org.ird.unfepi.constants;

import org.ird.unfepi.GlobalParams;

public class ErrorMessages 
{
	public static final String NOT_EQUAL_TO_LAST_FILLED_VALUE = "Field should be equal to previously filled data value";
	
	public static final String ENROLLMENT_EQUAL_DATES_REQUIRED = "Date enrolled and center visit date must be equal.";
	public static final String ENROLLMENT_MOBILE_BACKDATED_ENTRY = "Date enrolled or date of vaccination should be today`s date only";
	
	public static final String INVALID_EPI_LENGTH = "Epi number should be a numeric sequence of length" + WebGlobals.MAX_EPI_NUMBER_LENGTH;
	public static final String NAME_INVALID = "Name must have valid characters";
	public static final String NAME_AVAILABLE = "Child named or not must be specified";
	public static final String FIRSTNAME_INVALID = "First name must have valid characters";
	public static final String LASTNAME_INVALID = "Last name should have valid characters";
	public static final String VACCINATION_CENTER_MISSING = "Vaccination center must be specified";
	public static final String VACCINATOR_MISSING = "Vaccinator must be specified";
	public static final String NIC_INVALID = "NIC should only be 13 digits numeric sequence";
	public static final String EP_WALLET_INVALID = "EP wallet number should only be 12 digits numeric sequence";
	public static final String GENDER_INVALID = "Gender not specified";
	public static final String CHILD_ID_INVALID = "A valid, numeric and "+GlobalParams.CHILD_PROGRAMID_LENGTH+" digit ID starting with digits 1-5 is required for child.";
	public static final String CHILD_ID_DATE_PART_INVALID = "Child`s program ID should contain date part in format yyMMdd from digit 5";

	public static final String VACCINATION_CENTER_SHOULD_NOT_BE_SPECIFIED = "Vaccination center should not be specified for pending vaccination";
	public static final String VACCINATOR_SHOULD_NOT_BE_SPECIFIED = "Vaccinator should not be specified for pending vaccination";

	public static final String CENTER_NAME_INVALID = "Center name should not contain special characters";
	public static final String CENTER_FULLNAME_INVALID = "Center full name should not contain special characters";
	
	public static final String USERNAME_INVALID = "Username/Login ID must be non-whitespace alphanumeric sequence between 5-20 characters other than words admin or administrator";
	public static final String USERNAME_OCCUPIED = "Username/Login ID given already occupied";

	public static final String PASSWORD_INVALID = "Password must be non-whitespace alphanumeric sequence between 5-20 characters";
	public static final String PASSWORDS_DONOT_MATCH = "Both passwords donot match";

	public static final String EMAIL_INVALID = "A valid email must be specified";
	
	public static final String DATA_ENTRY_SOURCE_INVALID = "Can not be filled using your device";
	
	public static final String ID_INVALID = "A valid program id must be specified";

	public static final String CHILD_ALREADY_EXISTS = "A child with given id already exists";
	public static final String CHILD_DOESNOT_EXISTS = "No child exists with given id";
	
	public static final String STOREKEEPER_ALREADY_EXISTS = "A storekeeper with given id already exists";
	public static final String USERID_ALREADY_EXISTS = "A user with given id already exists";

	public static final String VACCINATOR_ALREADY_EXISTS = "A vaccinator with given id already exists";
	
	public static final String TEAM_USER_ID_INVALID = "Team user id should be numeric and "+GlobalParams.TEAM_USER_PROGRAMID_LENGTH+" digits only";
	public static final String STOREKEEPER_ID_INVALID = "Storekeeper id should be numeric and "+GlobalParams.STOREKEEPER_PROGRAMID_LENGTH+" digits only";
	public static final String VACCINATOR_ID_INVALID = "Vaccinator id should be numeric and "+GlobalParams.VACCINATOR_PROGRAMID_LENGTH+" digits only";
	public static final String VACCINATION_CENTER_ID_INVALID = "Vaccination center id should be numeric and "+GlobalParams.VACCINATION_CENTER_PROGRAMID_LENGTH+" digits only";

	public static final String EPI_MISSING = "Epi number must be specified";
	
	public static final String REGISTRATION_DATE_MISSING = "Registration date must be specified";
	public static final String REGISTRATION_DATE_IN_FUTURE = "Registration date can not be in future";
	public static final String BIRTHDATE_INVALID = "Date of birth must be valid, past and before enrollment date";
	public static final String IS_ESTIMATED_BIRTHDATE_MISSING = "Specify if birthdate is estimated or accurate";

	public static final String QUALIFICATION_MISSING = "Qualification must be specified";

	public static final String STORENAME_MISSING = "Store Name must be specified";

	public static final String LOTTERY_TYPE_MISSING = "A valid lottery type must be specified";
	public static final String VACCINE_MISSING = "A valid and registered vaccine must be specified";
	public static final String JUSTIFICATION_MISSING = "A justification of action must be specified";
	public static final String REQUESTER_MISSING = "Requested by must be specified";
	public static final String SPECIFIED_VACCINE_NOT_RECEIVED_FOR_CHILD = "Vaccine specified was not found vaccinated for given child";
	public static final String SPECIFIED_VACCINE_NOT_PENDING_FOR_CHILD = "Vaccine specified was not found pending for given child";
	public static final String LOTTERY_APPROVAL_MISSING = "Lottery approval for vaccination is missing. Change lottery approval status first";
	public static final String LOTTERY_NOT_APPROVED = "Child has not approved lottery for the vaccination.";
	public static final String LOTTERY_ALREADY_EXISTS = "A lottery was already done for the vaccination.";
	public static final String VACCINATION_TOO_EARLY = "Vaccination date gap should not be 4 or more days before vaccination due date";
	public static final String EPI_NUMBER_DIFFERENT_ON_LAST_VISIT = "Child on its last vaccine at given center was assigned different EPI number";
	public static final String LOTTERY_NOT_APPLICABLE_FOR_MEASLES2_ENROLLMENT = "Lottery can not be performed on enrollment on Measles2.";

	public static final String SCREENING_CHILD_HEALTH_PROBLEM_MISSING = "Child is not healthy. Plz specify the health problem child is having";
	public static final String SCREENING_SCREENING_DATE_MISSING = "Screening date must be specified";
	public static final String SCREENING_SCREENING_DATE_IN_FUTURE = "Screening date can not be in future";

	public static final String APPROVAL_LOTTERY_MISSING = "Lottery approval missing";
	public static final String APPROVAL_REMINDERS_MISSING = "Reminders approval missing";
	public static final String APPROVAL_REMINDERS_NOT_APPLICABLE = "Reminders approval should not be specified";

	public static final String USER_STATUS_MISSING = "User status missing";
	public static final String USER_ROLE_MISSING = "User role missing";
	public static final String USER_STATUS_ADMIN_INVALID = "User status can only be ACTIVE for default admin user";
	public static final String USER_ADMIN_EDITOR_NOT_AUTHORIZED = "Default admin can only be edited by default admin user.";
	public static final String USER_ROLE_EDIT_NOT_ALLOWED = "User can not be assigned the role specified. Role might be more privileged.";

	public static final String COMPLETE_COURSE_FROM_CENTER_MISSING = "Complete course from center parameter must be specified.";

	public static final String BIRTHDATE_OR_AGE_PARAM_MISSING = "Birthdate or age parameter must be specified.";
	public static final String BIRTHDATE_OR_AGE_PARAM_INVALID = "Birthdate or age parameter must only be AGE or BIRTHDATE.";

	public static final String INVALID_YEARS_OF_AGE = "Invalid years of age";
	public static final String INVALID_MONTHS_OF_AGE = "Invalid months of age";
	public static final String INVALID_WEEKS_OF_AGE = "Invalid weeks of age";
	public static final String INVALID_DAYS_OF_AGE = "Invalid days of age";

	public static final String SCREENING_LOTTERY_REJECTION_REASON_MISSING = "Plz specify the reason of lottery rejection";
	public static final String SCREENING_REMINDERS_REJECTION_REASON_MISSING = "Plz specify the reason of reminders rejection";
	public static final String SCREENING_CELL_NUMBER_MISSING = "A valid and non empty cell number must be specified";
	public static final String SCREENING_CELL_NUMBER_OWNER_MISSING = "Cell number owner should be a non empty valid name";
	public static final String SCREENING_CELL_PHONE_ACCESS_MISSING = "Cell phone access must be specified";
	public static final String SCREENING_REFERRED_CENTER_MISSING = "Referred vaccination center must be specified";
	public static final String SCREENING_BROUGHTBY_MISSING = "Relationship of person who brought child must be specified";
	public static final String SCREENING_OTHER_BROUGHTBY_MISSING = "Other relationship of person who brought child must be specified";

	public static final String CHILD_DATE_ENROLLED_INVALID = "A valid and past date must be specified as date enrolled for child";
	public static final String CHILD_AGE_LIMIT_EXCEEDED = "Child should not be more than 2 years of age on vaccination (only exception for Measles2 is 3 years)";

	public static final String CHILD_RELIGION_MISSING = "Religion must be specified";
	public static final String CHILD_ETHNICITY_MISSING = "Ethnicity must be specified";
	public static final String CHILD_LANGUAGE_MISSING = "Language must be specified";
	public static final String CHILD_OTHER_RELIGION_MISSING = "Other religion must be specified";
	public static final String CHILD_OTHER_ETHNICITY_MISSING = "Other ethnicity must be specified";
	public static final String CHILD_OTHER_LANGUAGE_MISSING = "Other language must be specified";
	public static final String CHILD_ENROLLMENT_VACCINE_MISSING = "Child`s enrollment vaccine ID must be populated";
	public static final String CHILD_STATUS_MISSING = "Child program status must be specified";
	public static final String CHILD_TERMINATION_DATE_INVALID = "A valid past date must be specified when child terminated from program";
	public static final String CHILD_TERMINATION_REASON_MISSING = "A reason must be specified for child`s termination from program";

	public static final String VACCINATION_BROUGHTBY_MISSING = "Relationship of person who brought child must be specified";
	public static final String VACCINATION_BROUGHTBY_SHOULD_NOT_BE_SPECIFIED = "Relationship of person who brought child should not be specified for pendin vaccination";
	public static final String VACCINATION_OTHER_BROUGHTBY_MISSING = "Other relationship of person who brought child must be specified";
	public static final String VACCINATION_VACCINE_MISSING = "Vaccine recieved/scheduled must be specified";
	public static final String VACCINATION_VISIT_DATE_INVALID = "Visit date must be a valid, past date and ideally should not be 4 or more days before vaccination due date";
	public static final String VACCINATION_VACCINATION_DATE_SHOULD_NOT_BE_SPECIFIED = "Vaccination date should not be specified for pending vaccination";
	public static final String VACCINATION_POLIO_VACCINE_SHOULD_NOT_BE_SPECIFIED = "Polio vaccine given should not be specified for pending vaccination";
	public static final String VACCINATION_PCV_SHOULD_NOT_BE_SPECIFIED = "PCV given should not be specified for pending vaccination";
	public static final String VACCINATION_LOTTERY_APPROVAL_SHOULD_NOT_BE_SPECIFIED = "Lottery approval should not be specified for pending or Mealses2 enrollment vaccination";
	public static final String VACCINATION_NEXT_VACCINES_SHOULD_NOT_BE_SPECIFIED = "Next vaccines should not be specified for Measles2 vaccination";
	public static final String VACCINATION_EPI_SHOULD_NOT_BE_SPECIFIED = "Epi number should not be specified for pending vaccination";
	public static final String VACCINATION_CHILD_ID_MISSING = "Child Id for vaccination cannot be missing";
	public static final String VACCINATION_STATUS_MISSING = "Vaccination status missing";
	public static final String VACCINATION_STATUS_ENROLLMENT_INVALID = "Enrollment vaccination`s status must be VACCINATED";
	public static final String VACCINATION_STATUS_PENDING_REQUIRED = "Vaccination`s status must be PENDING";

	public static final String VACCINATION_NEXT_VACCINE_MISSING = "Next scheduled vaccine missing";
	public static final String VACCINATION_NEXT_VACCINE_RECEIVED = "Vaccine next scheduled, has already been received";
	public static final String VACCINATION_ELSE_WHERE_VACCINE_EXISTS = "Vaccine received, has already been recorded in application";

	public static final String VACCINATION_DUEDATE_INVALID = "A valid date must be specified as vaccination due date";

	public static final String VACCINATION_DATE_BEFORE_BIRTHDATE = "Vaccination date cannot be before date of birth";

	public static final String VACCINATION_POLIO_VACCINE_MISSING = "Polio vaccine given not specified";
	public static final String VACCINATION_PCV_MISSING = "PCV given not specified";
	public static final String VACCINATION_LOTTERY_MISSING = "Lottery approval not specified";
	public static final String VACCINATION_NEXT_ASSIGNED_DATE_MISSING = "Next assigned date must be populated for all vaccinations other than Mealses2";

	public static final String VACCINATION_WEIGHT_MISSING = "child`s weight must be specified";
	public static final String VACCINATION_HEIGHT_MISSING = "child`s height must be specified";
	public static final String VACCINATION_WEIGHT_SHOULD_NOT_BE_SPECIFIED = "child`s weight should not be specified for pending vaccination";
	public static final String VACCINATION_HEIGHT_SHOULD_NOT_BE_SPECIFIED = "child`s height should not be specified for pending vaccination";
	public static final String VACCINATION_WEIGHT_INVALID = "weight must be between "+WebGlobals.MIN_WEIGHT+" - "+WebGlobals.MAX_WEIGHT+" kg";
	public static final String VACCINATION_HEIGHT_INVALID = "height must be between "+WebGlobals.MIN_HEIGHT+" - "+WebGlobals.MAX_HEIGHT+" cms";

	public static final String PREFERENCE_REMINDER_MISSING = "Reminder`s approval must be specified";

	public static final String CONTACT_OWNER_MISSING = "Contact number owner missing";
	public static final String CONTACT_OTHER_OWNER_MISSING = "Other contact number owner must be specified";
	public static final String CONTACT_CELL_NUMBER_INVALID = "A valid cell number must be specified for Contact";
	public static final String CONTACT_LANDLINE_NUMBER_INVALID = "A valid cell number must be specified for Contact";
	public static final String CONTACT_NUMBER_OCCUPIED = "Cell number occupied";
	public static final String CONTACT_PRIMARY_NUMBER_EXISTS = "A primary number already exists for specified id";
	public static final String CONTACT_NUMBER_NUMBER_TYPE_INVALID = "A valid number type must be specified";
	public static final String CONTACT_NUMBER_TELELINE_TYPE_INVALID = "A valid teleline type must be specified";
	public static final String CONTACT_NUMBER_MAPPEDID_MISSING = "A valid entity id must be specified whom number is being assigned to";
	public static final String CONTACT_NUMBER_ALREADY_ASSIGNED = "Cell number already assigned to the id specified. A number should not be assigned twice";

	public static final String CONTACT1_OWNER_MISSING = "Primary contact number owner missing";
	public static final String CONTACT1_OTHER_OWNER_MISSING = "Other primary contact number owner must be specified";
	public static final String CONTACT1_NUMBER_INVALID = "A valid cell number must be specified for primary contact for reminders";
	public static final String CONTACT1_NUMBER_OCCUPIED = "Cell number occupied";
	
	public static final String CONTACT2_OWNER_MISSING = "Secondary contact number owner missing";
	public static final String CONTACT2_OTHER_OWNER_MISSING = "Other secondary contact number owner must be specified";
	public static final String CONTACT2_NUMBER_INVALID = "A valid cell or phone number must be specified for secondary contact";
	public static final String CONTACT2_NUMBER_OCCUPIED = "Cell number occupied";

	public static final String ADDRESS_HOUSE_NUMBER_INVALID = "House number in address should not contain special characters";
	public static final String ADDRESS_HOUSE_NUMBER_MISSING = "House number must be specified";
	public static final String ADDRESS_STREET_INVALID = "Street in address should not contain special characters";
	public static final String ADDRESS_STREET_MISSING = "Street must be specified";
	public static final String ADDRESS_SECTOR_INVALID = "Sector in address should not contain special characters";
	public static final String ADDRESS_COLONY_INVALID = "Colony in address should not contain special characters";
	public static final String ADDRESS_TOWN_INVALID = "Town in address should not contain special characters";
	public static final String ADDRESS_TOWN_MISSING = "Town must be specified";
	public static final String ADDRESS_UC_INVALID = "UC in address should only be numeric";
	public static final String ADDRESS_UC_MISSING = "UC must be specified";
	public static final String ADDRESS_LANDMARK_INVALID = "Landmark in address should not contain special characters";
	public static final String ADDRESS_CITY_INVALID = "City in address should not contain special characters";
	public static final String ADDRESS_CITY_MISSING = "City must be specified";
	public static final String ADDRESS_OTHER_CITY_MISSING = "Other city must be specified";

	public static final String VACCINE_NAME_INVALID = "Vaccine name should be a non empty non-whitespace word (a-z0-9_) of length 3-15";
	public static final String VACCINE_SHORTNAME_INVALID = "Vaccine short name should be a non empty non-whitespace word (a-z0-9_) of length 3-10";
	public static final String VACCINE_FULLNAME_INVALID = "Vaccine full name should be a non empty word (a-z0-9_space) of length 3-30";
	public static final String VACCINE_GAP_INVALID = "Gap between vaccine should be between -1 to 60 , if needs greater choose proper interval";

	public static final String DAILY_SUMMARY_DATE_MISSING = "Daily summary date must be specified";
	public static final String DAILY_SUMMARY_BCG_VISITED_INVALID = "BCG visited must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_BCG_ENROLLED_INVALID = "BCG enrolled must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_PENTA1_VISITED_INVALID = "Penta1 visited must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_PENTA1_ENROLLED_INVALID = "Penta1 enrolled must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_PENTA1_FOLLOWUPED_INVALID = "Penta1 Followed up must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_PENTA2_VISITED_INVALID = "Penta2 visited must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_PENTA2_FOLLOWUPED_INVALID = "Penta2 Followed up must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_PENTA3_VISITED_INVALID = "Penta3 visited must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_PENTA3_FOLLOWUPED_INVALID = "Penta3 Followed up must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_MEASLES1_VISITED_INVALID = "Measles1 visited must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_MEASLES1_FOLLOWUPED_INVALID = "Measles1 followed up must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_MEASLES2_VISITED_INVALID = "Measles2 visited must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_MEASLES2_FOLLOWUPED_INVALID = "Measles2 followed up must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_BCG_ENROLLED_LOTTERY_INVALID = "BCG enrolled with lottery must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_PENTA1_ENROLLED_LOTTERY_INVALID = "Penta1 enrolled with lottery must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_BCG_ENROLLED_REMINDERS_INVALID = "BCG enrolled with reminders must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_PENTA1_ENROLLED_REMINDERS_INVALID = "Penta1 enrolled with reminders must be valid integer greater than or equal to 0";
	public static final String DAILY_SUMMARY_BCG_TOTAL_ENROLLED_INVALID = "BCG enrollments into lottery or reminders should not exceed total enrollemnts into BCG";
	public static final String DAILY_SUMMARY_PENTA1_TOTAL_ENROLLED_INVALID = "Penta1 enrollments into lottery or reminders should not exceed total enrollemnts into Penta1";
}
