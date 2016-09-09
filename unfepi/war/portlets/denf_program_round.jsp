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
	
	if(dateDifference(convertToDate($('#endDate').val()), convertToDate($('#startDate').val())) < 0 ){
		alert('end date sholud be greater than or equal to start date');
		return false;
	}
	
	if(dateDifference(convertToDate($('#endDate').val()), convertToDate($('#startDate').val())) < 0 ){
		alert('end date sholud be greater than or equal to start date');
		return false;
	}	
	var is_end_date_passed = false;
	
	if($('#endDate').val().length > 0 ){
		if (dateDifference(convertToDate($.datepicker.formatDate('dd-mm-yy', new Date())), convertToDate($('#endDate').val())) > 0 ){
			is_end_date_passed = true; 
			alert("end date has passed !");
			return false;
		}
	}
	
	var validName = false;
	
	if ($('#r_name').val().replace(/\s/g, '').length == 0){
		validName = false;
		alert('invalid or empty program name');
	} else {
		validName = true;
	}
// 	rp_ss_date && rp_se_date && rp_es_date && rp_ee_date && 
	if( !isEmptyField && !is_end_date_passed && validName){
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
<tr><td colspan="2">
<spring:hasBindErrors name="command">
	<c:forEach var="error" items="${errors.allErrors}">
		<p style="font-style: italic; color: red;font-size: small;"><spring:message message="${error}" /></p>
	</c:forEach>
</spring:hasBindErrors>
</td></tr>

<tr><td colspan="2"><br></td></tr>
<tr><td>Health Program<span class="mendatory-field">*</span></td><td>
<input type="text" value="${healthprogram.name}" readonly/>
<input type="hidden" name="healthProgramId" value="${healthprogram.programId}" /></td></tr>
<tr><td>Round Name<span class="mendatory-field">*</span></td><td><input type="text" id="r_name" name="name" onkeypress="return isChar(event);" class="requiredField" value="${command.name}" > </td></tr>


<tr><td>Start Date<span class="mendatory-field">*</span></td><td><input id="startDate" name="startDate" class="calendarbox requiredField" onkeypress="return isDateDigit(event)" value="<fmt:formatDate value="${command.startDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>"/></td></tr>
<tr><td>End Date<span class="mendatory-field">*</span></td><td><input id="endDate" name="endDate" class="calendarbox requiredField" onkeypress="return isDateDigit(event)" value="<fmt:formatDate value="${command.endDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>"/></td></tr>
<tr><td>Is Active<span class="mendatory-field">*</span></td>
<td><select name="isActive" class="requiredField">
<option value="" <c:if test="${empty command.isActive}">selected="selected" </c:if>></option>
<option value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>" <c:if test="${command.isActive == true}">selected="selected" </c:if> >Yes</option>
<option value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>" <c:if test="${command.isActive == false}">selected="selected"</c:if> >No</option>
</select></td></tr>
<tr><td colspan="2" height="10px" style="border: none;"></td></tr>
<tr><td colspan="2"><input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();"></td></tr>
</table>
</form>
