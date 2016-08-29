<%@include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">

function isChar(e) {
	var charCode = (e.which) ? e.which : e.keyCode;
	if ((charCode >= 65 && charCode <= 90)
			|| (charCode >= 97 && charCode <= 122) || (charCode == 32)) {
		return true;
	}
	return false;
}

function isDigit(e) {
	var charCode = (e.which) ? e.which : e.keyCode;
	if ((charCode >= 48 && charCode <= 57)) {
		return true;
	}
	return false;
}

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
	
// 	console.log('firstDate-secondDate' + firstDate + '  ' + secondDate);
	var startDay = new Date(firstDate);
	var endDay = new Date(secondDate);
// 	console.log('startDay-endDay' + startDay + '  ' + endDay);
	var millisecondsPerDay = 1000 * 60 * 60 * 24;
	var millisBetween = startDay.getTime() - endDay.getTime();
	var days = millisBetween / millisecondsPerDay;
	
// 	console.log(Math.floor(days));
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
	
	if( dateDifference(convertToDate($('#startDate').val()), '${centerprogram.startDate}') >= 0 ){
		rp_ss_date = true;
	}else{
		rp_ss_date = false;
		alert("round start date should be greater than or equal to program start date");
	}	
	if( dateDifference('${centerprogram.endDate}', convertToDate($('#startDate').val())) >= 0 ){
		rp_se_date = true;
	}else{
		rp_se_date = false;
		alert("round start date should be less than or equal to program end date");
	}
	
	var rp_es_date;
	var rp_ee_date;
	
	if( dateDifference(convertToDate($('#endDate').val()), '${centerprogram.startDate}') >= 0 ){
		rp_es_date = true;
	}else{
		rp_es_date = false;
		alert("round end date should be greater than or equal to program start date");
	}	
	if( dateDifference('${centerprogram.endDate}', convertToDate($('#endDate').val())) >= 0 ){
		rp_ee_date = true;
	}else{
		rp_ee_date = false;
		alert("round end date should be less than or equal to program end date");
	}
	
	
	if(dateDifference(convertToDate($('#endDate').val()), convertToDate($('#startDate').val())) < 0 ){
		alert('end date sholud be greater than or equal to start date');
		return false;
	}
	
	if(dateDifference(convertToDate($('#endDate').val()), convertToDate($('#startDate').val())) < 0 ){
		alert('end date sholud be greater than or equal to start date');
		return false;
	}
	
	var is_end_date_passed = false;
	
	if (dateDifference(convertToDate($.datepicker.formatDate('dd-mm-yy', new Date())), convertToDate($('#endDate').val())) > 0 ){
		is_end_date_passed = true; 
		alert("end date has passed !");
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
</script>

<form method="post" id="frm" name="frm">
<br>

<table class="denform-h">
<tr><td colspan="2"><br></td></tr>
<tr><td>Vaccination Center</td><td><b>${centerprogram.vaccinationCenter.name}</b></td></tr>
<tr><td>Health Program</td><td><b>${centerprogram.healthProgram.name}</b></td></tr>
<tr><td></td><td>[<fmt:formatDate value="${centerprogram.startDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> : <fmt:formatDate value="${centerprogram.endDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>]</td></tr>
<tr><td>Round Name</td><td><input type="text" id="r_name" name="name" onkeypress="return isChar(event);" class="requiredField"> </td></tr>
<tr><td>Start Date</td><td><input id="startDate" name="startDate" class="calendarbox" class="requiredField" onkeypress="return isDateDigit(event)"/></td></tr>
<tr><td>End Date</td><td><input id="endDate" name="endDate" class="calendarbox" class="requiredField" onkeypress="return isDateDigit(event)"/></td></tr>
<tr><td>Is Active</td>
<td><select name="isActive" class="requiredField">
<option value=""></option>
<option value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>" >Yes</option>
<option value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>">No</option>
</select></td></tr>
<tr><td colspan="2" height="10px" style="border: none;"></td></tr>
<tr><td colspan="2"><input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();"></td></tr>
</table>
</form>
