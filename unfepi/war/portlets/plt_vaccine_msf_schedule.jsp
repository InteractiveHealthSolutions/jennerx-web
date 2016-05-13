<%@page import="org.ird.unfepi.model.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType"%>
<%@page import="org.ird.unfepi.model.Vaccine"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineScheduleKey"%>
<div id="vaccinelistContainerDiv">
<table id="vaccinelistContainerTbl" class="denform2" >
</table>
</div>
<script type="text/javascript">
var id;
var birthDate;
var allvaccines;
function vaccineMSFScheduleGenerator(childId, cBirthDate, allVaccines){
	id=childId;
	birthDate=cBirthDate;
	allvaccines=allVaccines;
	
	if(cBirthDate){
	var table=$('#vaccineScheduleContainerTbl').children(); 
	var content="<tr class='headerrow'> <th> Date </th>";
	
/* 	for (i = 0; i < allvaccines.length; i++) {
			content += ' <th> ' + allvaccines[i].name + ' </th> ';
		}
		content += "</tr>"
		table.append(content); */
		
	$('#vaccinelistContainerDiv').append('<table></table>');
	var table = $('#dynamictable').children();    
	table.append("<tr><th>a</th><th>b</th></tr>");
	table.append("<tr><td>c</td><td>d</td></tr>");
	}else if(childId){
		$('#vaccineScheduleContainerTbl').append('<tr><td colspan="10"><div class="emptydataset">Select center visit date first to generate schedule</div></td></tr>');
	}
	// if Birthdate is null or empty
	else {
		$('#vaccineScheduleContainerTbl').append('<tr><td colspan="10"><div class="emptydataset">Select birthdate first to generate schedule</div></td></tr>');
	}
	}
	function generateDueVaccineRow() {

		$('#vaccineScheduleContainerTbl').append('<tr> <input ');

		$('#vaccineScheduleContainerTbl').append('</tr>');
	}

	function validateBirthDateGap(cBirthDate, gapType, gapValue) {
	}
</script>