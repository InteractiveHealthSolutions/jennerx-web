
<%@page import="org.directwebremoting.json.types.JsonArray"%>
<%@page import="java.util.List"%>
<%@page import="org.ird.unfepi.beans.EnrollmentWrapper"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule"%>
<%@page import="org.ird.unfepi.web.controller.AddChildController"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="org.ird.unfepi.constants.FormType"%>
<%@page import="org.ird.unfepi.utils.validation.REG_EX"%>
<%@page import="org.ird.unfepi.model.Vaccination"%>
<%@page import="org.ird.unfepi.model.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.unfepi.model.Model.ContactTeleLineType"%>

<%@page import="org.ird.unfepi.model.Child"%>
<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<% boolean lotteryGeneratorForm = false;// TODO request.getAttribute("formType").equals(FormType.LOTTERY_GENERATOR_FORM_FILL);
boolean enableBirthdateEdit = !lotteryGeneratorForm;%>

<script type="text/javascript">
<!--

window.onload = onloadSettingOfControls;

function onloadSettingOfControls() 
{
	$('#birthdate').change(function() {
		birthChanged($(this));
	});
	
	DWRVaccineService.getSchedule('${command.centerVisit.uuid}', function(result) {
		//alert(result);
		vaccineScheduleGenerator(convertToDate($('#birthdate').val()), convertToDate($('#centerVisitDate').val()), '${command.centerVisit.childId}', result, '${command.centerVisit.uuid}',true);
	});
}

function birthChanged(jqControl){
	if($('#centerVisitDate').val() == '' || $('#vaccinationCenterId').val() == ''){
		//reset age inputs
		$('input[name^="childage"]').val('');
		//reset birthdate input
		$('#birthdate').val('');
		alert('Tareekh pedaish say pehlay Enrollment ki tareekh aur Center ka indraj zaroori hy.');
	}
	else{
		vaccineScheduleGenerator(convertToDate(jqControl.val()), convertToDate($('#centerVisitDate').val()), '${command.centerVisit.childId}', null,'${command.centerVisit.uuid}',true);
	}
}
//reset schedule
function resetSchedule(jqControl){
	vaccineScheduleGenerator('', '','${command.centerVisit.childId}', null,'${command.centerVisit.uuid}');
}
function subfrm(){
	<%-- if(!vaccinationCommonValidations(document,/^<%=REG_EX.CELL_NUMBER%>$/)){
		return;
	} --%>

	if($('#centerVisitDate').val() == ''){
		alert('Pehlay Enrollment ki Tareekh ka indraj karen');
		return;
	}
	else if(convertToDate($('#birthdate').val()) == null){
		alert('Pehlay Tareekh Pedaish ya Umr ka indraj karen');
		return;
		 
	}
	
	
	DWRVaccineService.validateVaccineScheduleDates(vaccineSchedule, function(result) {
		//alert(JSON.stringify(result));
		if(result.SUCCESS != null){
			DWRVaccineService.overrideSchedule(vaccineSchedule, '${command.centerVisit.uuid}', function(result) {
				submitThisForm();
			});
		}
		else if(result.ERROR != null
				&& confirm("Following are the problems detected with child vaccine schedule. " + result.MESSAGE + "\n\nIgnore and continue with data submit?")){
			DWRVaccineService.overrideSchedule(vaccineSchedule, '${command.centerVisit.uuid}', function(result) {
				submitThisForm();
			});
		}
		
});
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}

//-->
</script>

<form method="post" id="frm" name="frm" >
<table class="denform-h">
	<tr>
    	<td>Enrollment ki Tareekh <span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.centerVisit.visitDate">
        <!-- MUST be named as centerVisitDate: used in plt_vaccine_schedule for autopopulating date incase of status VACCINATED -->
        <input id="centerVisitDate" name="centerVisit.visitDate" value="${status.value}" maxDate="+0d" class="calendarbox" onclosehandler="centerVisitDateChanged"/>
		<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
<script type="text/javascript">
<!--
function centerVisitDateChanged() {
	$('#vaccinationCenterId').val('');
	$('#birthdate').val('');
	resetSchedule($('#birthdate'));
}
//-->
</script>
    	</td>
    </tr>
    <tr>
    	<td>Vaccinator ID <span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.centerVisit.vaccinatorId">
            <select id="vaccinatorId" name="centerVisit.vaccinatorId" bind-value="${status.value}">
                <option></option>
                <c:forEach items="${vaccinators}" var="vaccinator"> 
                <option value="${vaccinator.mappedId}">${vaccinator.idMapper.identifiers[0].identifier} : ${vaccinator.firstName}</option>
            	</c:forEach> 
            </select>
            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
		</td>
	</tr>
	<tr>
		<td>Vaccination Center <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.centerVisit.vaccinationCenterId">
            <select id="vaccinationCenterId" name="centerVisit.vaccinationCenterId" bind-value="${status.value}" onchange="centerChanged();">
               	<option></option>
            	<c:forEach items="${vaccinationCenters}" var="vcenter"> 
            	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier} : ${vcenter.name}</option>
            	</c:forEach> 
            </select>
            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
<script type="text/javascript">
<!--
function centerChanged() {
	$('#birthdate').val('');
	resetSchedule($('#birthdate'));
}
//-->
</script>
		</td>
	</tr>
	<tr>
    	<td>Project ID <span class="mendatory-field">*</span></td>
        <td>
        	<spring:bind path="command.projectId">
				<input type="text" id="programId" name="projectId" maxlength="14" value="<c:out value="${status.value}"/>" class="numbersOnly" />
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
        </td>
    </tr>
	<tr>
		<td>EPI Register Number <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.centerVisit.epiNumber">
				<input type="text" id="epiNumber" name="centerVisit.epiNumber" maxlength="8" class="numbersOnly" 
						value="<c:out value="${status.value}"/>" <%if(lotteryGeneratorForm){%>readonly="readonly"<%}%>/>
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
        <td colspan="2" class="headerrow">Basic Info</td>
    </tr>
    <c:set var="commandAdditionalPathStr" value="child."></c:set>
	<%@ include file="plt_child_biographic_info.jsp" %>
	
	
	<tr>
        <td colspan="2" class="headerrow">Address</td>
    </tr>
	<c:set var="commandAdditionalPathStr" value="address."></c:set>
	<%@ include file="plt_address.jsp" %>
	
	<tr>
        <td colspan="2" class="headerrow">Immunization Details</td>
    </tr>
    <tr>
		<td>Kya vaccines ka course iss hee center se mukamal kareinge? <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.completeCourseFromCenter">
			<select id="completeCourseFromCenter" name="completeCourseFromCenter" bind-value="${status.value}">
				<option></option>
				<option>Yes</option>
				<option>No</option>
				<option>Don`t Know</option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		</td>
	</tr>
	<tr>
        <td colspan="2">
        <%@ include file="plt_vaccine_schedule.jsp" %>
        </td>
    </tr>
	<tr>
        <td colspan="2" class="headerrow">Programme Details</td>
    </tr>
    <tr>
		<td>Kya aap SMS reminder chahtay hein? <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.centerVisit.preference.hasApprovedReminders">
				<input type="radio" name="centerVisit.preference.hasApprovedReminders" <c:if test='${not empty status.value && status.value == true}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"/>Yes<br>
				<input type="radio" name="centerVisit.preference.hasApprovedReminders" <c:if test='${not empty status.value && status.value == false}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>No
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>SMS reminder ke liye Mobile Number</td>
		<td><spring:bind path="command.centerVisit.contactPrimary">
			<input type="text" id="contactPrimary" name="centerVisit.contactPrimary" maxlength="13" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Raabtay ke liye koi aur number</td>
		<td><spring:bind path="command.centerVisit.contactSecondary">
			<input type="text" id="contactSecondary" name="centerVisit.contactSecondary" maxlength="13" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Kya aap targheebi inaami scheme mein hisa lena chahtay hen? <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.centerVisit.preference.hasApprovedLottery">
				<input type="radio" name="centerVisit.preference.hasApprovedLottery" <c:if test='${not empty status.value && status.value == true}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"/>Yes<br>
				<input type="radio" name="centerVisit.preference.hasApprovedLottery" <c:if test='${not empty status.value && status.value == false}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>No
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr> 
	<tr>
        <td>NIC Number</td>
        <td><spring:bind path="command.child.nic">
             <input type="text" id="childnic" name="child.nic" maxlength="13"  class="numbersOnly" value="<c:out value="${status.value}" />"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td></td>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
        </td>
    </tr>
</table>
</form>

