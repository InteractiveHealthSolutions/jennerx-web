<%@include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
<!--
$(function(){
	$('#isActive').keydown(false);
});

function isCharOrDigit(e) {
	var charCode = (e.which) ? e.which : e.keyCode;
	if ((charCode >= 65 && charCode <= 90)
			|| (charCode >= 97 && charCode <= 122) 
			|| (charCode == 32)
		    || (charCode >= 48 && charCode <= 57)) {
		return true;
	}
	return false;
}

function isDateDigit(e) {
	var charCode = (e.which) ? e.which : e.keyCode;
	if ((charCode >= 48 && charCode <= 57) || (charCode == 45)) {
		return true;
	}
	return false;
}

function dateDifference(firstDate, secondDate) {
	var startDay = new Date(firstDate);
	var endDay = new Date(secondDate);
	var millisecondsPerDay = 1000 * 60 * 60 * 24;
	var millisBetween = startDay.getTime() - endDay.getTime();
	var days = millisBetween / millisecondsPerDay;
	return Math.floor(days);
}

function validateFields(){
	
	var isEmptyField = false;
	
	$(".requiredField").each(function(index, element) {
		if ((element.value.length == 0) && (element.value.replace(/\s+/g, "").length == 0)) {
			isEmptyField = true ;
		}
	});
	
	if(isEmptyField){
		alert('fill all the required fields first');
		return false;
	}
	
	var rp_ss_date;
	var rp_se_date;
	
	if( dateDifference(convertToDate($('#startDate').val()), '${command.centerProgram.startDate}') >= 0 ){
		rp_ss_date = true;
	}else{
		rp_ss_date = false;
		alert("round start date should be greater than or equal to program start date");
		return false;
	}	
	if( dateDifference('${command.centerProgram.endDate}', convertToDate($('#startDate').val())) >= 0 ){
		rp_se_date = true;
	}else{
		rp_se_date = false;
		alert("round start date should be less than or equal to program end date");
		return false;
	}
	
	var rp_es_date;
	var rp_ee_date;
	
	if( dateDifference(convertToDate($('#endDate').val()), '${command.centerProgram.startDate}') >= 0 ){
		rp_es_date = true;
	}else{
		rp_es_date = false;
		alert("round end date should be greater than or equal to program start date");
		return false;
	}	
	if( dateDifference('${command.centerProgram.endDate}', convertToDate($('#endDate').val())) >= 0 ){
		rp_ee_date = true;
	}else{
		rp_ee_date = false;
		alert("round end date should be less than or equal to program end date");
		return false;
	}
	
	var is_end_date_passed = false;
	
// 	var dateStr = $.datepicker.formatDate('dd-mm-yy', new Date());
	
	if (dateDifference(convertToDate($.datepicker.formatDate('dd-mm-yy', new Date())), convertToDate($('#endDate').val())) > 0 ){
		is_end_date_passed = true; 
		alert("end date has passed !");
		return false;
	}
	
	if(dateDifference(convertToDate($('#endDate').val()), convertToDate($('#startDate').val())) < 0 ){
		alert('end date sholud be greater than or equal to start date');
		return false;
	}
	
	var validName = false;
	
	if ($('#r_name').val().replace(/\s/g, '').length == 0){
		validName = false;
		alert('invalid or empty program name');
	} else {
		validName = true;
	}
	
	if(rp_ss_date && rp_se_date && rp_es_date && rp_ee_date && !isEmptyField && !is_end_date_passed && validName){
		return true;
	}
	
	return false;	
}

function subfrm(){	
	if(validateFields()){
		submitThisForm();
	}
}

function submitThisForm() {
	document.getElementById("frm").submit();
}
//-->
</script>

<form method="post" id="frm" name="frm">
<input type="hidden" name="roundId" value="${command.roundId}" />
<input type="hidden" name="centerProgramId" value="${command.centerProgramId}" />
<input type="hidden" name="vaccinationCenterId" value="${command.centerProgram.vaccinationCenterId}" />
<input type="hidden" name="healthProgramId" value="${command.centerProgram.healthProgramId}" />
<br>
<table class="denform-h">
<tr><td colspan="2"><br></td></tr>
<tr><td>Vaccination Center</td><td><b>${command.centerProgram.vaccinationCenter.name}</b></td></tr>
<tr><td>Health Program</td><td><b>${command.centerProgram.healthProgram.name}</b></td></tr>
<tr><td></td><td>[<fmt:formatDate value="${command.centerProgram.startDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> : <fmt:formatDate value="${command.centerProgram.endDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>]</td></tr>
<tr><td>Round Name</td><td><input type="text" name="name" value="${command.name}" onkeypress="return isCharOrDigit(event)" class="requiredField" > </td></tr>
<tr><td>Start Date</td><td><input id="startDate" name="startDate" value="<fmt:formatDate value="${command.startDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>" class="calendarbox requiredField" onkeypress="return isDateDigit(event)"/></td></tr>
<tr><td>End Date</td><td><input id="endDate" name="endDate" value="<fmt:formatDate value="${command.endDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>" class="calendarbox requiredField" onkeypress="return isDateDigit(event)"/></td></tr>
<tr><td>Is Active</td>
<td><select id="isActive" name="isActive" class="requiredField">
<option value="" <c:if test="${empty command.isActive}">selected="selected" </c:if>></option>
<option value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>" <c:if test="${command.isActive == true}">selected="selected" </c:if>>Yes</option>
<option value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>" <c:if test="${command.isActive == false}">selected="selected"</c:if>>No</option>
</select></td></tr>
<tr><td colspan="2" height="10px" style="border: none;"></td></tr>
<tr><td colspan="2"><input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();"></td></tr>
</table>
</form>