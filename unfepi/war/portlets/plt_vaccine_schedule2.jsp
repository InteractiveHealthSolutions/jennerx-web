<%@page import="org.ird.unfepi.web.utils.VaccineSchedule"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineScheduleKey"%>
<div id="vaccineScheduleContainerDiv">
<table id="vaccineScheduleContainerTbl" class="denform2" >
</table>

</div>
<script type="text/javascript">
<!--
var vaccineSchedule;

function vaccineScheduleGenerator (birthdate, centerVisitDate, childId, vaccinerules,uuid) {
	
	$('#vaccineScheduleContainerTbl').find("tr").remove();
	$('#vaccineScheduleContainerTbl').append('<tr class="headerrow-compact"><th>Vaccine</th><th>Status</th><th>Center</th><th>Vaccine Date</th></tr>');

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
			$('#vaccineScheduleContainerTbl').append('<tr class="headerrow-lighter"><td colspan="10"><div class="left">'+birthGapCurrentValue+' <span class="datenote">('+scheduleduedatestring+')</span></div></td></tr>');
			birthGapDivValue = birthGapCurrentValue;
		}
		else if(birthdategap == null && birthGapDivValue != birthGapCurrentValue){
			$('#vaccineScheduleContainerTbl').append('<tr class="headerrow-lighter"><td colspan="10"><div class="left">Supplementary Vaccines</div></td></tr>');
			birthGapDivValue = birthGapCurrentValue;
		}
		else {
			$('#vaccineScheduleContainerTbl').append('<tr><th colspan="10" class="separatorline"></th></tr>');
		}
		//alert(vaccinename+":"+assignedduedate + ":" + scheduleduedate);
		var comboSelectedCenter = '';
		var combostatus = '';
		// ONLY if vaccine is ALLOWED
		if(status != '<%=VaccineStatusType.NOT_ALLOWED%>'){
			comboSelectedCenter = $("#vaccinationCenterList option[value='"+center+"']").text();
			
			if(status == ''){
				combostatus = 'Not Given';
			}
			else if(!isCurrentSuspect && !isRetroSuspect && status == '<%=VaccineStatusType.SCHEDULED%>'){
				combostatus= 'Scheduled';
			}
			else if(isRetroSuspect || isCurrentSuspect){
				combostatus = 'Due';
			}
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
			$('#vaccineScheduleContainerTbl').append(makeVaccineRow(vaccinename, status, isCurrentSuspect, isRetroSuspect, vaccinationdate, assignedduedate, zone, combostatus, comboSelectedCenter, vaccinationdatestring, assignedduedatestring));
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
	row.append('<td class="'+zone+'"> <div class="left" style="padding: 2px"><span class="headingS">'+vaccinename+' </span><div> </td> ');

	if(status == '<%=VaccineStatusType.NOT_ALLOWED%>') {
		row.append($('<td class="'+zone+'">').append('N/A'));
	} 
	else if(status == '<%=VaccineStatusType.VACCINATED_EARLIER%>'){
		row.append($('<td class="'+zone+'"> ').append("Vaccinated"));
		row.append($('<td> ').append(comboSelectedCenter));
		row.append($('<td> ').append(getDateString(vaccinationdate)));
	}
	else if(status == ''){
		row.append($('<td> ').append(combostatus));
		row.append($('<td> ').append(comboSelectedCenter));
		row.append($('<td> ').append(ipCurrentDate));
	}
	else if(!isCurrentSuspect && !isRetroSuspect && status == '<%=VaccineStatusType.SCHEDULED%>'){
		row.append($('<td class="'+zone+'">').append(combostatus));
		row.append($('<td> ').append(''));
		row.append($('<td colspan="9">').append(ipNextDate));
	}
	else {
		row.append($('<td class="'+zone+'"> ').append(combostatus));
		row.append($('<td> ').append(comboSelectedCenter));
		//alert(vaccinename+":"+ipCurrentDate+":"+ipNextDate);
		row.append($('<td> ').append(ipCurrentDate, ipNextDate));
		//row.append($('<td> ').append(ipNextDate));
	}
	
	return row;
}
//-->
</script>