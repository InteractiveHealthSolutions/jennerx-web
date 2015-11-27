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

function vaccineScheduleGenerator (birthdate, centerVisitDate, childId, vaccinerules,uuid) {
	
	$('#vaccineScheduleContainerTbl').find("tr").remove();
	$('#vaccineScheduleContainerTbl').append('<tr class="headerrow"><th>Vaccine</th><th>Status</th><th>Center</th><th>Vaccine Date</th></tr>');

	var birthGapDivValue = "";
	if(birthdate != null && birthdate != ''
			&& centerVisitDate != null && centerVisitDate != ''){
	
	if(vaccinerules == null || vaccinerules == undefined){
		DWRVaccineService.generateDefaultSchedule(birthdate, centerVisitDate, childId, 0, true, uuid, 
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
		var birthdategap = vaccineSchedule[i].<%=VaccineScheduleKey.birthdate_gap%>;
		var birthdategapval = birthdategap == null?"":birthdategap.value.toString();
		var birthdategapunit = birthdategap == null?"":birthdategap.gapTimeUnit;
		var vaccinename = vaccineSchedule[i].<%=VaccineScheduleKey.vaccine%>.name;
		var center = vaccineSchedule[i].<%=VaccineScheduleKey.center%>;
		var status = vaccineSchedule[i].<%=VaccineScheduleKey.status%>;

		var scheduleduedate = vaccineSchedule[i].<%=VaccineScheduleKey.schedule_duedate%>;
		var scheduleduedatestring = (scheduleduedate==null?'':scheduleduedate.toString('<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>'));

		var assignedduedate = vaccineSchedule[i].<%=VaccineScheduleKey.assigned_duedate%>;
		var assignedduedatestring = (assignedduedate==null?'':assignedduedate.toString('<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>'));

		var minGracePeriod = vaccineSchedule[i].<%=VaccineScheduleKey.vaccine%>.minGracePeriodDays;
		var maxGracePeriod = vaccineSchedule[i].<%=VaccineScheduleKey.vaccine%>.maxGracePeriodDays;

		var vaccinationdate = vaccineSchedule[i].<%=VaccineScheduleKey.vaccination_date%>;
		var vaccinationdatestring = (vaccinationdate==null?'':vaccinationdate.toString('<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>'));

		var isCurrentSuspect = vaccineSchedule[i].<%=VaccineScheduleKey.is_current_suspect%>;
		var isRetroSuspect = vaccineSchedule[i].<%=VaccineScheduleKey.is_retro_suspect%>;
	
		var birthGapCurrentValue = birthdategapval+' '+birthdategapunit;
		if(birthdategap != null && birthGapDivValue != birthGapCurrentValue){
			$('#vaccineScheduleContainerTbl').append('<tr><th colspan="10"><div class="headerrow-compact">'+birthGapCurrentValue+' <span class="datenote">('+scheduleduedatestring+')</span></div></th></tr>');
			birthGapDivValue = birthGapCurrentValue;
		}
		else if(birthdategap == null && birthGapDivValue != birthGapCurrentValue){
			$('#vaccineScheduleContainerTbl').append('<tr><th colspan="10"><div class="headerrow-compact">Supplementary Vaccines</div></th></tr>');
			birthGapDivValue = birthGapCurrentValue;
		}
		else {
			$('#vaccineScheduleContainerTbl').append('<tr><th colspan="10" class="separatorline"></th></tr>');
		}
		
		var currentDateInputId = makeControlId('<%=VaccineScheduleKey.vaccination_date%>', vaccinename, i);
		var nextDateInputId = makeControlId('<%=VaccineScheduleKey.assigned_duedate%>', vaccinename, i);
		var currentStatusComboId = makeControlId('<%=VaccineScheduleKey.status%>', vaccinename, i);
		var selectedCenterComboId = makeControlId('<%=VaccineScheduleKey.center%>', vaccinename, i);
		
		
		var comboSelectedCenter = $("<select></select>").attr("id", selectedCenterComboId).attr("name", selectedCenterComboId);
		var combostatus = $("<select></select>").attr("id", currentStatusComboId).attr("name", currentStatusComboId);
		var ipCurrentDate = $('<input id="'+currentDateInputId+'" name="'+currentDateInputId+'" style="width: 80px" class="calendarbox" value="'+vaccinationdatestring+'"/>');
		var ipNextDate = $('<input id="'+nextDateInputId+'" name="'+nextDateInputId+'" style="width: 80px" class="calendarbox" value="'+assignedduedatestring+'"/>');
		
		// ONLY if vaccine is ALLOWED
		if(status != '<%=VaccineStatusType.NOT_ALLOWED%>'){
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
			    	//alert(index);
					vaccineSchedule[index].<%=VaccineScheduleKey.vaccination_date%> = convertToDate(this.value);
					DWRVaccineService.updateSchedule(vaccineSchedule, uuid, function(result) {
						//alert(result);
						vaccineScheduleGenerator(birthdate, centerVisitDate, childId, result, uuid);
					});
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
			
			if(status == ''){
				comboSelectedCenter.append($("#vaccinationCenterId > option").clone());
			}
			else if(status == '<%=VaccineStatusType.RETRO_DATE_MISSING%>'){
				comboSelectedCenter.append($("#vaccinationCenterId > option").clone());
			}
			else if(status == '<%=VaccineStatusType.RETRO%>'){
				comboSelectedCenter.append($("#vaccinationCenterId > option").clone());
			}
			else if(status == '<%=VaccineStatusType.VACCINATED%>'){
				comboSelectedCenter.append($("#vaccinationCenterId > option:selected").clone());
				ipCurrentDate.val($('#centerVisitDate').val());

				vaccineSchedule[i].<%=VaccineScheduleKey.center%> = comboSelectedCenter.val();
				vaccineSchedule[i].<%=VaccineScheduleKey.vaccination_date%> = convertToDate(ipCurrentDate.val());
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
			
			
			combostatus.change(function() {
				index = getScheduleIndexFromControl(this);
				vaccdatesel = $('#'+getPeerControlId(this, '<%=VaccineScheduleKey.vaccination_date%>'));
				assigndatesel = $('#'+getPeerControlId(this, '<%=VaccineScheduleKey.assigned_duedate%>'));
	
				vaccdatesel.val('');
				assigndatesel.val('');
	
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
					DWRVaccineService.updateSchedule(vaccineSchedule, uuid, function(result) {
						//alert(result);
						vaccineScheduleGenerator(birthdate, centerVisitDate, childId, result, uuid);
					});
				}
			});
			
			if(status == ''){
				combostatus.append("<option value=''></option>");
				<%-- combostatus.append("<option value='<%=VaccineStatusType.VACCINATED%>'>Vaccinated</option>"); --%>
				combostatus.append("<option value='<%=VaccineStatusType.RETRO%>'>Retro</option>");
				combostatus.append("<option value='<%=VaccineStatusType.RETRO_DATE_MISSING%>'>Retro (Date Missing)</option>");
			}
			else if(!isCurrentSuspect && !isRetroSuspect && '<%=VaccineStatusType.SCHEDULED%>'){
				combostatus.append("<option value='<%=VaccineStatusType.SCHEDULED%>'>Schedule</option>");
			}
			else {
				combostatus.append("<option value='<%=VaccineStatusType.VACCINATED%>'>Vaccinated</option>");
				combostatus.append("<option value='<%=VaccineStatusType.RETRO%>'>Retro</option>");
				combostatus.append("<option value='<%=VaccineStatusType.RETRO_DATE_MISSING%>'>Retro (Date Missing)</option>");
				combostatus.append("<option value='<%=VaccineStatusType.SCHEDULED%>'>Schedule</option>");
			}
	
			combostatus.find('option').each(function(){
				if ($(this).val() == status){
					$(this).attr("selected","selected");
				}
			});
		
		}
		var zone = "";
		try{
			if(status == ''){
				zone = '';
			}
			else if(status == '<%=VaccineStatusType.VACCINATED_EARLIER%>'){
				zone = "greenZone";
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
		}
		catch (e) {
			alert(e);
		}
		
		try{
			$('#vaccineScheduleContainerTbl').append(makeVaccineRow(vaccinename, status, isCurrentSuspect, isRetroSuspect, vaccinationdate, assignedduedate, zone, combostatus, comboSelectedCenter, ipCurrentDate, ipNextDate));
		}
		catch (e) {
			alert(e);
		}
	}
	}
	// if followup childid is null in centervisit
	else if(childId != null){
		$('#vaccineScheduleContainerTbl').append('<tr><td colspan="10"><div class="emptydataset">Select center visit date first to generate schedule</div></td></tr>');
	}
	// if Birthdate is null or empty
	else {
		$('#vaccineScheduleContainerTbl').append('<tr><td colspan="10"><div class="emptydataset">Select birthdate first to generate schedule</div></td></tr>');
	}
}

function getDateString(givenDate){
	return (givenDate==null?'':givenDate.toString('<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>'));
}

function makeVaccineRow(vaccinename, status, isCurrentSuspect, isRetroSuspect, vaccinationdate, assignedduedate, zone, combostatus, comboSelectedCenter, ipCurrentDate, ipNextDate){
	var row = $('<tr></tr>');
	//var assignDateHtml = assignedduedate!=null?'Due ('+getDateString(assignedduedate)+')<div>':'';
	row.append('<th class="'+zone+'"> <div class="leftaligned" style="padding: 1px"><span class="">'+vaccinename+' </span><div> </td> ');

	if(status == '<%=VaccineStatusType.NOT_ALLOWED%>') {
		row.append($('<th class="'+zone+'">').append(''));
	} 
	else if(status == '<%=VaccineStatusType.VACCINATED_EARLIER%>'){
		row.append($('<th class="'+zone+'"> ').append("Vaccinated"));
		row.append($('<td> ').append(''));
		row.append($('<td> ').append(getDateString(vaccinationdate)));
	}
	else if(status == ''){
		row.append($('<th> ').append(combostatus));
		row.append($('<td> ').append(comboSelectedCenter));
		row.append($('<td> ').append(ipCurrentDate));
	}
	else if(!isCurrentSuspect && !isRetroSuspect && status == '<%=VaccineStatusType.SCHEDULED%>'){
		row.append($('<th class="'+zone+'">').append(combostatus));
		row.append($('<td> ').append(''));
		row.append($('<td colspan="9">').append(ipNextDate));
	}
	else {
		if(status == '<%=VaccineStatusType.SCHEDULED%>'){
			comboSelectedCenter.hide();
			ipCurrentDate.hide();
			ipNextDate.show();
		}
		else if(status == '<%=VaccineStatusType.RETRO_DATE_MISSING%>'){
			ipCurrentDate.hide();
			ipNextDate.hide();
		}
		else if(status == '<%=VaccineStatusType.VACCINATED%>'){
			ipCurrentDate.show();
			ipNextDate.hide();
		}
		else {
			ipNextDate.hide();
		}
		
		row.append($('<th class="'+zone+'"> ').append(combostatus));
		row.append($('<td> ').append(comboSelectedCenter));
		row.append($('<td> ').append(ipCurrentDate, ipNextDate));
		//row.append($('<td> ').append(ipNextDate));
	}
	
	return row;
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

function getVaccineNameFromControl(element) {
	try{
		if(element.id == undefined || element.id == null || element.id == ''){
			alert('Error: No ID specified for control. Contact program vendor.');
			return;
		}
		return element.id.substring(0, element.id.indexOf('__'));
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