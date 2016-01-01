<%@page import="org.ird.unfepi.web.utils.VaccineSchedule"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineScheduleKey"%>
<div id="vaccineScheduleContainerDiv">
<table id="vaccineScheduleContainerTbl" class="denform2">
</table>

</div>
<script type="text/javascript"> 
<!--
var vaccineSchedule;
var uuid;
var birthdate;
var centerVisitDate;
var childId;
var centerId;
function vaccineScheduleGenerator (cbirthdate, ccenterVisitDate, cchildId, ccenterId, vaccinerules,uuidd) {
	uuid=uuidd;
	birthdate=cbirthdate;
	centerVisitDate=ccenterVisitDate;
	childId=cchildId;
	centerId = ccenterId;
	
	$('#vaccineScheduleContainerTbl').find("tr").remove();
	$('#vaccineScheduleContainerTbl').append('<tr class="headerrow"><th>Schedule</th><th>Vaccine</th><th>Status</th><th>Center</th><th>Due On</th><th>Vaccine Date</th></tr>');

	var birthGapDivValue = "";
	if(birthdate && centerVisitDate){
	
	if(!vaccinerules){
		DWRVaccineService.generateDefaultSchedule(birthdate, centerVisitDate, childId, centerId, true, uuid, 
			{callback: function(result) {
				//alert(result);
				vaccineSchedule = result;
			}, async: false, timeout: 5000});
	}
	else {
		//alert(JSON.stringify(vaccinerules));
		vaccineSchedule = vaccinerules;
	}
	
	for ( var i = 0; i < vaccineSchedule.length; i++) {
		var row = $('<tr></tr>');

		var birthdategap = vaccineSchedule[i].<%=VaccineScheduleKey.birthdate_gap%>;
		var birthdategapval = birthdategap == null?"":birthdategap.value.toString();
		var birthdategapunit = birthdategap == null?"":birthdategap.gapTimeUnit;

		var scheduleduedate = vaccineSchedule[i].<%=VaccineScheduleKey.schedule_duedate%>;
		var scheduleduedatestring = (scheduleduedate==null?'':scheduleduedate.toString('<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>'));

		var vaccinename = vaccineSchedule[i].<%=VaccineScheduleKey.vaccine%>.name;

		var status = vaccineSchedule[i].<%=VaccineScheduleKey.status%>;
		var center = vaccineSchedule[i].<%=VaccineScheduleKey.center%>;

		var isCurrentSuspect = vaccineSchedule[i].<%=VaccineScheduleKey.is_current_suspect%>;
		var isRetroSuspect = vaccineSchedule[i].<%=VaccineScheduleKey.is_retro_suspect%>;

		var isExpired = vaccineSchedule[i].<%=VaccineScheduleKey.expired%>;
		var prerequisitePassed = vaccineSchedule[i].<%=VaccineScheduleKey.prerequisite_passed%>;
		
		
		var assignedduedate = vaccineSchedule[i].<%=VaccineScheduleKey.assigned_duedate%>;
		var assignedduedatestring = (assignedduedate==null?'':assignedduedate.toString('<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>'));

		var vaccinationdate = vaccineSchedule[i].<%=VaccineScheduleKey.vaccination_date%>;
		var vaccinationdatestring = (vaccinationdate==null?'':vaccinationdate.toString('<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>'));

		var vzone = getZone(status, isCurrentSuspect, isRetroSuspect, isExpired);
		var currentDateInputId = makeControlId('<%=VaccineScheduleKey.vaccination_date%>', vaccinename, i);
		var nextDateInputId = makeControlId('<%=VaccineScheduleKey.assigned_duedate%>', vaccinename, i);
		var currentStatusComboId = makeControlId('<%=VaccineScheduleKey.status%>', vaccinename, i);
		var selectedCenterComboId = makeControlId('<%=VaccineScheduleKey.center%>', vaccinename, i);
		
		var birthGapCurrentValue = birthdategapval+' '+birthdategapunit;
		if(birthdategap && birthGapDivValue != birthGapCurrentValue){
			birthGapDivValue = birthGapCurrentValue;
			row.append(appendVaccineGapColumn(birthdategapval, birthdategapunit, scheduleduedatestring));
		}
		else if(!birthdategap && birthGapDivValue != birthGapCurrentValue){
			birthGapDivValue = birthGapCurrentValue;
			row.append(appendVaccineGapColumn('Supplementary'));
		}
		else {
			row.append('<td></td>');
		}
		
		row.append(appendVaccineNameColumn(vaccinename, vzone));
		row.append(appendStatusColumn(currentStatusComboId, status, isCurrentSuspect, isRetroSuspect, scheduleduedatestring, prerequisitePassed, isExpired, vzone)); 
		row.append(appendCenterColumn(selectedCenterComboId, status, center, prerequisitePassed, vzone)); 
		row.append(appendDueDateNoteColumn(assignedduedatestring, vzone)); 
		row.append(appendDateColumn(currentDateInputId, nextDateInputId, status, vaccinationdatestring, assignedduedatestring, prerequisitePassed, vzone)); 

		//alert(vaccinename+":"+status+":"+vaccinationdate+":"+center);
		
		$('#vaccineScheduleContainerTbl').append(row);
	}//end for
	}
	// if followup childid is null in centervisit
	else if(childId){
		$('#vaccineScheduleContainerTbl').append('<tr><td colspan="10"><div class="emptydataset">Select center visit date first to generate schedule</div></td></tr>');
	}
	// if Birthdate is null or empty
	else {
		$('#vaccineScheduleContainerTbl').append('<tr><td colspan="10"><div class="emptydataset">Select birthdate first to generate schedule</div></td></tr>');
	}
}
function getZone(status, isCurrentSuspect, isRetroSuspect, isExpired){
	var zone = "";
	try{
		if(status == '<%=VaccineStatusType.VACCINATED_EARLIER%>'){
			zone = "greenZone";
		}
		else if(isExpired){
			zone = "";
		}
		else if(isCurrentSuspect){
			zone = "okZone";
		}
		else if(isRetroSuspect){
			zone = "alarmZone";
		}
		else if(status == '<%=VaccineStatusType.NOT_ALLOWED%>'){
			zone = "grayZone";
		}
		if(status == '<%=VaccineStatusType.SCHEDULED%>'){
			zone = "yellowZone";
		}
	}
	catch (e) {
		alert(e);
	}
	return zone;
}
function appendVaccineGapColumn(birthdateGap, gapUnit, scheduleduedate, zone) {
	$('#vaccineScheduleContainerTbl').append('<tr><td class="headerrow-compact separatorline" colspan="10"></td></tr>');
	var col = $("<th class='left'></th>");
	col.append(birthdateGap,' ', gapUnit, ' <span style="margin-left:15px"></span> <span class="datenote right rightalign"> '+scheduleduedate+' </span>');
	return col;
}
function appendVaccineNameColumn(vaccinename, zone) {
	var col = $('<th class="'+zone+' left" style="padding-left:15px;">'+vaccinename+'</th> ');
	return col;
}
function appendDueDateNoteColumn(assignedDate, zone) {
	var col = $('<th class="'+zone+'-light"></th>');
	col.append('<span class="datenote"> '+assignedDate+' </span>');
	return col;
}
function appendDateColumn(currentDateInputId, nextDateInputId, status, vaccinationdate, assignedduedate, prerequisitePassed, zone) {
	var ipCurrentDate;
	var ipNextDate;
	
	// if vaccinated earlier or prerequisite not passed dates should not be displayed
	if(status == '<%=VaccineStatusType.VACCINATED_EARLIER%>'){
		ipCurrentDate=vaccinationdate;
		ipNextDate='';
	}
	else if(!prerequisitePassed){
		ipCurrentDate='';
		ipNextDate='';
	}
	else{
		ipCurrentDate = $('<input id="'+currentDateInputId+'" name="'+currentDateInputId+'" style="width: 80px" class="calendarbox" value="'+vaccinationdate+'"/>');
		ipNextDate = $('<input id="'+nextDateInputId+'" name="'+nextDateInputId+'" style="width: 80px" class="calendarbox" value="'+assignedduedate+'"/>');
	
		ipCurrentDate.datepicker({
		  	duration: '',
		    constrainInput: false,
		    dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
		    maxDate: '+0d',
		    onSelect: function(dateText) {
		    	if($('#'+getPeerControlId(this, '<%=VaccineScheduleKey.center%>')).val() == ''){
		    		alert('Select center first');
		    		this.value = '';
		    		return;
		    	}
		    	index = getScheduleIndexFromControl(this);
				vaccineSchedule[index].<%=VaccineScheduleKey.vaccination_date%> = convertToDate(this.value);
				updateScheduleGrid();
		    }
		});
		
		ipNextDate.datepicker({
		  	duration: '',
		    constrainInput: false,
		    dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
		    minDate: '+0d',
		    onSelect: function(dateText) {
		    	index = getScheduleIndexFromControl(this);
		    	vaccineSchedule[index].<%=VaccineScheduleKey.assigned_duedate%> = convertToDate(this.value);
		    }
		});
		
		if(status == '<%=VaccineStatusType.SCHEDULED%>'){
			ipNextDate.show();
			ipCurrentDate.hide();
		}
		else if(status == '<%=VaccineStatusType.RETRO_DATE_MISSING%>'){
			ipNextDate.hide();
			ipCurrentDate.hide();
		}
		else {
			ipNextDate.hide();
			ipCurrentDate.show();
		}
	}
	var col = $('<td class="'+zone+'-light"></td> ');
	col.append(ipNextDate,ipCurrentDate);
	return col;
}
function appendCenterColumn(selectedCenterComboId, status, center, prerequisitePassed, zone){
	var comboSelectedCenter;
	// if vaccinated earlier or prerequisite not passed combo should not be displayed
	if(status == '<%=VaccineStatusType.VACCINATED_EARLIER%>' 
			|| status == '<%=VaccineStatusType.SCHEDULED%>' 
			|| !prerequisitePassed){
		comboSelectedCenter = '';
	}
	else{
		comboSelectedCenter = $("<select></select>").attr("id", selectedCenterComboId).attr("name", selectedCenterComboId);
	
		if(status == '<%=VaccineStatusType.VACCINATED%>'){
			comboSelectedCenter.append($("#vaccinationCenterId > option:selected").clone());
		}
		else {
			comboSelectedCenter.append($("#vaccinationCenterId > option").clone());
		}

		comboSelectedCenter.find('option').each(function(){
			if ($(this).val() == center+''){
				$(this).attr("selected","selected");
			}
		});

		comboSelectedCenter.change(function() {
			index = getScheduleIndexFromControl(this);
			vaccineSchedule[index].<%=VaccineScheduleKey.center%> = this.value;
		});	
	}
	var col = $('<td class="'+zone+'-light"></td> ');
	col.append(comboSelectedCenter);
	return col;
}
function appendStatusColumn(currentStatusComboId, status, isCurrentSuspect, isRetroSuspect, scheduleduedate, prerequisitePassed, isexpired, zone) {
	var combostatus;
	// if vaccinated earlier or prerequisite not passed combo should not be displayed
	if(status == '<%=VaccineStatusType.VACCINATED_EARLIER%>'){
		combostatus = 'RECEIVED';
	}
	else if(!prerequisitePassed){
		combostatus = '';
	}
	else{
		combostatus = $("<select></select>").attr("id", currentStatusComboId).attr("name", currentStatusComboId);
		if(!scheduleduedate || isexpired){
			combostatus.append("<option value=''></option>");
			combostatus.append("<option value='<%=VaccineStatusType.RETRO%>'>Retro</option>");
			combostatus.append("<option value='<%=VaccineStatusType.RETRO_DATE_MISSING%>'>Retro (Date Missing)</option>");
		}
		else if(isCurrentSuspect || isRetroSuspect){
			combostatus.append("<option value='<%=VaccineStatusType.RETRO%>'>Retro</option>");
			combostatus.append("<option value='<%=VaccineStatusType.VACCINATED%>'>Vaccinated</option>");
			combostatus.append("<option value='<%=VaccineStatusType.RETRO_DATE_MISSING%>'>Retro (Date Missing)</option>");
			combostatus.append("<option value='<%=VaccineStatusType.SCHEDULED%>'>Schedule</option>");
		}
		else {
			combostatus.append("<option value='<%=VaccineStatusType.SCHEDULED%>'>Schedule</option>");
		}
	
		combostatus.find('option').each(function(){
			if ($(this).val() == status){
				$(this).attr("selected","selected");
			}
		});
		
		combostatus.change(function() {
			index = getScheduleIndexFromControl(this);
			vaccdatesel = $('#'+getPeerControlId(this, '<%=VaccineScheduleKey.vaccination_date%>'));
			assigndatesel = $('#'+getPeerControlId(this, '<%=VaccineScheduleKey.assigned_duedate%>'));
	
			vaccdatesel.val('');
	
			centerSel = $('#'+getPeerControlId(this, '<%=VaccineScheduleKey.center%>'));
			centerSel.find("option").remove();
			
			if($(this).val() == '<%=VaccineStatusType.RETRO_DATE_MISSING%>'){
				vaccdatesel.hide();
				assigndatesel.hide();
				centerSel.append($("#vaccinationCenterId > option").clone());
				centerSel.show();
			}
			else if($(this).val() == '<%=VaccineStatusType.VACCINATED%>'){
				vaccdatesel.show();
				vaccdatesel.val($('#centerVisitDate').val());
				assigndatesel.hide();
				centerSel.append($("#vaccinationCenterId > option:selected").clone());
				centerSel.show();
			}
			else if($(this).val() == '<%=VaccineStatusType.RETRO%>'){
				vaccdatesel.show();
				assigndatesel.hide();
				centerSel.append($("#vaccinationCenterId > option").clone());
				centerSel.show();
			}
			else if($(this).val() == '<%=VaccineStatusType.SCHEDULED%>'){
				vaccdatesel.hide();
				assigndatesel.show();
				centerSel.hide();
			}
			
			vaccineSchedule[index].<%=VaccineScheduleKey.status%> = this.value;
			vaccineSchedule[index].<%=VaccineScheduleKey.center%> = $(centerSel).val();
			vaccineSchedule[index].<%=VaccineScheduleKey.assigned_duedate%> = convertToDate(assigndatesel.val());
			vaccineSchedule[index].<%=VaccineScheduleKey.vaccination_date%> = convertToDate(vaccdatesel.val());
			
			if(vaccineSchedule[index].<%=VaccineScheduleKey.status%> == '<%=VaccineStatusType.RETRO_DATE_MISSING%>'
					|| vaccineSchedule[index].<%=VaccineScheduleKey.status%> == '<%=VaccineStatusType.VACCINATED%>'){
				updateScheduleGrid();
			}
		});
	}
	
	var col = $('<td class="'+zone+'-light"></td> ');
	col.append(combostatus);
	return col;
}
function updateScheduleGrid(){
	DWRVaccineService.updateSchedule(vaccineSchedule, uuid, function(result) {
		//alert(result);
		vaccineScheduleGenerator(birthdate, centerVisitDate, childId, centerId, result, uuid);
	});
}
function makeControlId(fieldNameOrKey, vaccinename, index){
	return vaccinename+'__'+fieldNameOrKey+'__'+index;
}

function getPeerControlId(element, peerFieldNameOrKey) {
	try{
		if(element.id == undefined || element.id == null || element.id == ''){
			alert('Error: No ID specified for control. Contact program vendor.');
			return;
		}
		
		vname = element.id.substring(0, element.id.indexOf('__'));
		index = element.id.substring(element.id.lastIndexOf('__')+2);
		
		return vname+'__'+peerFieldNameOrKey+'__'+index;
	}
	catch(e){
		alert(e);
	}
	
	return null;
}

function getScheduleIndexFromControl(element) {
	try{
		if(element.id == undefined || element.id == null || element.id == ''){
			alert('Error: No ID specified for control. Contact program vendor.');
			return;
		}
		return element.id.substring(element.id.lastIndexOf('__')+2);
	}
	catch(e){
		alert(e);
	}
	
	return null;
}
//-->
</script>