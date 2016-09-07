<%@include file="/WEB-INF/template/include.jsp"%>

<style type="text/css">
input[readonly]
{
    background-color:#bfbfbf;
}
</style>
<script type="text/javascript">
<!--
$(function(){
	$('#isActive').keydown(false);
});

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

function subfrm(){		
	var isEmptyField = false;
	var isValidEnrollmentDate = false;
	
	//end date should be greater than start date 
	var diff = dateDifference(convertToDate($('#endDate').val()), convertToDate($('#startDate').val()));

	if( diff >= 0 ){
		isValidEnrollmentDate = true;	
	}else{
		isValidEnrollmentDate = false;
	}
	
	$(".requiredField").each(function(index, element) {
		if (element.value.length == 0 ) {
			isEmptyField = true ;
		}
	});
	
	if(!isEmptyField && isValidEnrollmentDate ){
		submitThisForm();
	}else{
		alert('empty or invalid fields');
		return;
	}
}

function submitThisForm() {
	document.getElementById("frm").submit();
}
//-->
</script>
<body>
<form method="post" id="frm" name="frm">
<br>
<input type="hidden" name="centerProgramId" value="${command.centerProgramId}" />
<input type="hidden" name="vaccinationCenterId" value="${command.vaccinationCenterId}" />
<input type="hidden" name="healthProgramId" value="${command.healthProgramId}" />
<table class="denform-h">
<tr><td colspan="2" height="10px" style="border: none;"></td></tr>
<tr><td>Vaccination Center</td><td><input type="text" value="${command.vaccinationCenter.name}" readonly="readonly"/></td></tr>
<tr><td>Health Program</td><td><input type="text" value="${command.healthProgram.name}" readonly="readonly"/></td></tr>
<tr><td>Start Date</td><td><input id="startDate" name="startDate" value="<fmt:formatDate value="${command.startDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>" class="calendarbox requiredField" onkeypress="return isDateDigit(event)"/></td></tr>
<tr><td>End Date</td><td><input id="endDate" name="endDate" value="<fmt:formatDate value="${command.endDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>" class="calendarbox requiredField" onkeypress="return isDateDigit(event)"/></td></tr>
<%-- <tr><td>Is Active</td><td><input type="text" name="isActive" value="${command.isActive}"> </td></tr> --%>
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
