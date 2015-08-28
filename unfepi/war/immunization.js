function viewChildDetails(programId){
	var win;

	win=window.open('childDetails.htm?programId='+programId,'ChildDetails:'+programId,'width=600,height=700,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}

function viewImmunizationDetails(programId){
	var win;

	win=window.open('immunizationDetails.htm?programId='+programId,'ImmunizationDetails:'+programId,'width=600,height=700,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}

function viewEncounterDetails(ids){
	var win;

	win=window.open('encounterDetails.htm?'+ids,'EncounterDetails:'+ids,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}
function viewVaccinatorDetails(ids){
	var win;

	win=window.open('vaccinatorDetails.htm?programId='+ids,'VaccinatorDetails:'+ids,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}

function viewVaccinationCenterDetails(ids){
	var win;

	win=window.open('vaccinationCenterDetails.htm?programId='+ids,'VaccinationCenterDetails:'+ids,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}
function viewStorekeeperDetails(ids){
	var win;

	win=window.open('storekeeperDetails.htm?programId='+ids,'StorekeeperDetails:'+ids,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}
function viewScreeningDetails(screeningId){
	var win;

	win=window.open('viewScreeningwindow.htm?screening_id='+screeningId,'ScreeningDetails:'+screeningId,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}
function viewDailySummaryDetails(dailysummaryId){
	var win;

	win=window.open('viewDailySummarywindow.htm?dsId='+dailysummaryId,'DailySummaryDetails:'+dailysummaryId,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}
function viewReminderSmsDetails(remindersmsId){
	var win;

	win=window.open('viewReminderSmswindow.htm?rsId='+remindersmsId,'ReminderSmsDetails:'+remindersmsId,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}
function viewVaccinationDetails(vaccinationRecordNumber){
	var win;

	win=window.open('viewVaccinationwindow.htm?vaccId='+vaccinationRecordNumber,'VaccinationDetails:'+vaccinationRecordNumber,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}

function validateBirthdateVaccinationGap(dateOfBirth, vaccineDate, vaccine) {
	var birthdate = dateOfBirth.clone();
	var vaccinationDate = vaccineDate.clone();
	
	if(birthdate.compareTo(vaccinationDate) == 1){
		alert('Vaccination Date can not be before date of birth.');
		return false;
	}
	if(vaccine.toLowerCase().indexOf('penta1') != -1 
			&& birthdate.add(6*7).days().compareTo(vaccinationDate) == 1){
		return confirm('Vaccination for Penta1 can not be given before 6 weeks of age. Continue and ignore ?');
	}
	else if(vaccine.toLowerCase().indexOf('penta2') != -1 
			&& birthdate.add(10*7).days().compareTo(vaccinationDate) == 1){
		return confirm('Vaccination for Penta2 can not be given before 10 weeks of age. Continue and ignore ?');
	}
	else if(vaccine.toLowerCase().indexOf('penta3') != -1 
			&& birthdate.add(14*7).days().compareTo(vaccinationDate) == 1){
		return confirm('Vaccination for Penta3 can not be given before 14 weeks of age. Continue and ignore ?');
	}
	else if(vaccine.toLowerCase().indexOf('measles1') != -1 
			&& birthdate.add(9).months().compareTo(vaccinationDate) == 1){
		return confirm('Vaccination for Measles1 can not be given before 9 months of age. Continue and ignore ?');
	}
	else if(vaccine.toLowerCase().indexOf('measles2') != -1 
			&& birthdate.add(12).months().compareTo(vaccinationDate) == 1){
		return confirm('Vaccination for Measles2 can not be given before 12 months of age. Continue and ignore ?');
	}
	
	return true;
}

function validateNextVaccinationGap(dateOfBirth, currentVaccineDate, nextDateAssigned, nextVaccine) 
{
	var birthdate = dateOfBirth.clone();
	var currentVaccinationDate = currentVaccineDate.clone();
	var nextAssignedDate = nextDateAssigned.clone();

	if(birthdate.compareTo(nextAssignedDate) == 1){
		alert('Vaccination Date for '+nextVaccine+' can not be before date of birth.');
		return false;
	}
	else if(nextVaccine.toLowerCase().indexOf('penta1') != -1 
			&& birthdate.add(6*7).days().compareTo(nextAssignedDate) == 1){
		return confirm('Vaccination for Penta1 can not be given before 6 weeks of age. Continue and ignore ?');
	}
	else if(nextVaccine.toLowerCase().indexOf('penta2') != -1 
			&& currentVaccinationDate.add(4*7).days().compareTo(nextAssignedDate) == 1){
		return confirm('Vaccination for Penta2 must be given after 4 weeks of Penta1 date. Continue and ignore ?');
	}
	else if(nextVaccine.toLowerCase().indexOf('penta3') != -1 
			&& currentVaccinationDate.add(4*7).days().compareTo(nextAssignedDate) == 1){
		return confirm('Vaccination for Penta3 must be given after 4 weeks of Penta2 date. Continue and ignore ?');
	}
	else if(nextVaccine.toLowerCase().indexOf('measles1') != -1 
			&& birthdate.add(9).months().compareTo(nextAssignedDate) == 1){
		return confirm('Vaccination for Measles1 can not be given before 9 months of age/birthdate. Continue and ignore ?');
	}
	else if(nextVaccine.toLowerCase().indexOf('measles2') != -1 )
	{
		if(birthdate.add(12).months().compareTo(nextAssignedDate) == 1){
			return confirm('Vaccination for Measles2 can not be given before 12 months of age/birthdate. Continue and ignore ?');
		}
		 
		if( currentVaccinationDate.add(4*7).days().compareTo(nextAssignedDate) == 1){
			return confirm('Vaccination for Measles2 can not be given before 4 weeks of Measles1. Continue and ignore ?');
		}
	}
	
	if(currentVaccinationDate.compareTo(nextAssignedDate) == 1){
		alert('Next assigned date can not be before current vaccination date');
		return false;
	}
	
	return true;
}
function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

String.prototype.startsWith = function(str) {
	return (this.match("^"+str)==str);
};

function datediff(datesmaller,dategreater,interval) {
    var second=1000, minute=second*60, hour=minute*60, day=hour*24, week=day*7;
    var date1=datesmaller;
    var date2=dategreater;
    var timediff = date2 - date1;
    if (isNaN(timediff)) return NaN;
    switch (interval) {
        case "years": return date2.getFullYear() - date1.getFullYear();
        case "months": return (
            ( date2.getFullYear() * 12 + date2.getMonth() )
            -
            ( date1.getFullYear() * 12 + date1.getMonth() )
        );
        case "weeks"  : return Math.floor(timediff / week);
        case "days"   : return Math.floor(timediff / day); 
        case "hours"  : return Math.floor(timediff / hour); 
        case "minutes": return Math.floor(timediff / minute);
        case "seconds": return Math.floor(timediff / second);
        default: return undefined;
    }
}

function isNumber (o) {
	return ! isNaN (o-0);
}

function makeDateFromString(dategiven) {
    var str1 = dategiven;
    var dt1  = parseInt(str1.substring(0,2),10);
    var mon1 = parseInt(str1.substring(3,5),10)-1;
    var yr1  = parseInt(str1.substring(6,10),10);
    
    var date1 = new Date(yr1, mon1, dt1);
    return date1;
}
function trim(s)
{
	if(s.length==0) return '';
	var l=0; var r=s.length -1;
	while(l < s.length && s[l] == ' ')
	{	l++; }
	while(r > l && s[r] == ' ')
	{	r-=1;	}
	return s.substring(l, r+1);
}
var afterDateSelected=function (obj) {
	var date1=Date.parseExact(obj.value,globalDf);

	if(date1.is().sunday()){
		alert('selected date is sunday, moving date to monday.');
		date1.add(1).days();
		obj.value=date1.toString(globalDf);
	}
};
function makeTextSelectedInDD(selectControl,textToSelect){
	var sel = selectControl;
    var val = textToSelect;
	for (var i=0; i<sel.options.length; i++) {
		if (sel.options[i].text == val) {
			sel.selectedIndex = i;
		}
	}
}
function makeValueSelectedInDD(selectControl,valueToSelect){
	var sel = selectControl;
    var val = valueToSelect;
	for (var i=0; i<sel.options.length; i++) {
		if (sel.options[i].value == val) {
			sel.selectedIndex = i;
		}
	}
}
function getTextSelectedInDD(selectControl){
	var sel = selectControl;
	var selIndex = sel.selectedIndex;
	return sel.options[selIndex].text;
}

function getValueSelectedInChoiceGroup(choiceControls){
     for(var k=0 ; k < choiceControls.length ; k++){
       if(choiceControls[k].checked){
         return choiceControls[k].value;
       }
     }
	return "";
}
//return the value of the radio button that is checked
//return an empty string if none are checked, or
//there are no radio buttons
function getCheckedValue(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked)
			return radioObj.value;
		else
			return "";
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}
function getGapFromChecked(radioObj) {
	if(!radioObj) return -10;
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked) return parseInt(radioObj.title);
		else return -10;
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return parseInt(radioObj[i].title);
		}
	}
	return -10;
}


function verifyTimeValues() {
	var inputs = document.getElementsByTagName('input');
	for (var i=0; i < inputs.length; i++)
	{
	   if (inputs[i].getAttribute('type') == 'text')
	   {
		   var reg = /^time([0-9_\-\.])+/;
		   if(reg.test(inputs[i].name)==true){
			   var reg2 = /^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]/;
				if(reg2.test(inputs[i].value)==false){
					alert(inputs[i].name+" is not a valid time value");
					return false;
				}
		   }
	   }
	}
	return true;
}

function vaccinationCommonValidations(dobj)
{
 	if(dobj.getElementById("hasApprovedLottery").disabled==false && getTextSelectedInDD(dobj.getElementById("hasApprovedLottery")) == ''){
		alert("Specify approval of caregiver for lottery");
		return false;
	}

	return true;
}

function followupReminderPreferenceValidation(dobj,cellNumberRegex) 
{
	if(getValueSelectedInChoiceGroup(dobj.getElementsByName("reminderPreference")) == ''){
		alert("Specify approval of caregiver for reminders");
		return false;
	}
	
	try{
		if(getValueSelectedInChoiceGroup(dobj.getElementsByName("reminderPreference")) == 'true'
				&& cellNumberRegex.test(dobj.getElementById("reminderCellNumber").value) != true){
			alert("A valid cell number must be provided for reminders");
			return false;
		}
	}catch (e) {
		alert(e);
		return false;
	}
	
	return true;
}

function verifyAndHandleVaccinationDate(currentVaccineNAME, nextVaccineNAME, vaccinationDuedateTIP, vaccinationDateTIP, birthdateTIP, 
		vaccCenterID, nextAssignedDateTIP, systemCalculatedDateTIP) 
{
	try{
		var vaccinationdateval = vaccinationDateTIP.value;
		var vaccinationdate=Date.parseExact(vaccinationdateval,globalDOf);
		if(vaccinationdate == null){
			vaccinationdate=Date.parseExact(vaccinationdateval,globalDTf);
		}
		var birthdateval = birthdateTIP.value;
		var birthdate = Date.parseExact(birthdateval, globalDOf);
		if(birthdate == null){
			birthdate=Date.parseExact(birthdateval,globalDTf);
		}

		if(vaccinationdate == null){
			return;
		}
		
		var prevMileStoneDate=vaccinationdate;
		
		if(currentVaccineNAME == ''){
			vaccinationDateTIP.value = '';
			alert('First select vaccine given today');
			return;
		}
		if(birthdateval == ''){
			vaccinationDateTIP.value = '';
			alert('Please choose date of birth first');
			return;
		}
		if(nextVaccineNAME == ''){
			alert('Next vaccine found to be null, contact program vendor');
			return;
		}
		
		if(vaccinationdate.compareTo(Date.parse('June 20th 2012')) == -1){
			vaccinationDateTIP.value = '';
			alert('Vaccination date can not be before start date of project(June 20th, 2012)');
			return;
		}
		
		if(nextVaccineNAME == currentVaccineNAME){
			vaccinationdate.add(3).days();
			try{
			var recievedvcdate = vaccinationdate.toString(globalDOf);
			nextAssignedDateTIP.value = recievedvcdate;
			systemCalculatedDateTIP.value = recievedvcdate;
			}catch (e) {
				alert(e);
				var recievedvcdate = vaccinationdate.toString(globalDTf);
				nextAssignedDateTIP.value = recievedvcdate;
				systemCalculatedDateTIP.value = recievedvcdate;
			}
			
			return;
		}
		
		var bdtest = Date.parseExact(birthdateval, globalDOf);
		var vdtest = Date.parseExact(vaccinationdateval, globalDOf);
		//For Current Vaccine
		if(!validateBirthdateVaccinationGap(bdtest, vdtest, currentVaccineNAME)){
			vaccinationDateTIP.value = '';
			return;
		}
		
		if(nextVaccineNAME.toLowerCase().indexOf('n/a') != -1){
			nextAssignedDateTIP.value = '';
		}
		else {
			if(nextVaccineNAME.toLowerCase().indexOf('measles2') != -1)
			{
				DWRVaccineService.calculateMeasles2DateFromMeasles1(
						birthdate
						,vaccinationdate
						,vaccCenterID
						,currentVaccineNAME
						,nextVaccineNAME
						,{
						async: false,
						callback: function (recievedvcdate) {
							nextAssignedDateTIP.value=recievedvcdate;
							systemCalculatedDateTIP.value = recievedvcdate;
						}});
			}
			else 
			{
				if(nextVaccineNAME.toLowerCase().indexOf('measles1') != -1
						|| nextVaccineNAME.toLowerCase().indexOf('penta1') != -1)
				{
					prevMileStoneDate = birthdate;
				}
				 
				DWRVaccineService.calculateNextVaccinationDateFromPrev(
					prevMileStoneDate
					,vaccinationdate
					,vaccCenterID
					,nextVaccineNAME
					,false
					,{
					async: false,
					callback: function (recievedvcdate) {
						nextAssignedDateTIP.value=recievedvcdate;
						systemCalculatedDateTIP.value = recievedvcdate;
					}});
			}
		 }
	}
	catch (e) {
		alert(e);
	}
} 
