<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.model.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.unfepi.utils.validation.REG_EX"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.model.Vaccination"%>

<c:set var="childsessvar" value="childfollowup"></c:set>
<script type="text/javascript">
<!--
window.onload = onloadSettingOfControls;

function onloadSettingOfControls() 
{
	loadSchedule();
}

function loadSchedule(){
	DWRVaccineService.getSchedule('${command.uuid}', function(result) {
		//alert(result);
		vaccineScheduleGenerator(convertToDate($('#birthdateinh').val()), convertToDate($('#centerVisitDate').val()), '${command.childId}', result, '${command.uuid}');
	});
}
function subfrm()
{
	if(convertToDate($('#centerVisitDate').val()) == null){
		alert('No center visit date specified');
		return;
	}
	 DWRVaccineService.overrideSchedule(vaccineSchedule, '${command.uuid}', function(result) {
			
			submitThisForm();
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
    	<td colspan="2"><%@ include file="dg_child_biographic_info.jsp" %></td>
    </tr>
<%--     <c:set var="pndStatus" value="<%=Vaccination.VACCINATION_STATUS.PENDING %>"></c:set>
    <c:set var="prevvacclistsessvar" value="prevVaccList${sessionScope[childsessvar].idMapper.mappedId}"></c:set>
	<tr>
        <td colspan="2" class="headerrow">Previous Vaccination Details</td>
    </tr>
    <tr><td colspan="2">
    <table class="previousDataDisplay">
    <c:forEach items="${sessionScope[prevvacclistsessvar]}" var="prevvaccmap"> 
   	<tr>
		<td colspan="1"><span class="separator-heading">${prevvaccmap.previous_vaccination.vaccine.name}</span> - 
		${prevvaccmap.previous_vaccination.epiNumber} : ${prevvaccmap.previous_vaccination.vaccinationStatus}  (${prevvaccmap.previous_vaccination.vaccinationRecordNum})</td>
		<td>${prevvaccmap.previous_vaccination_vcenter.idMapper.identifiers[0].identifier} : ${prevvaccmap.previous_vaccination_vcenter.name}</td>
		<th colspan="2"><fmt:formatDate value="${prevvaccmap.previous_vaccination.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>
	<c:if test="${prevvaccmap.previous_vaccination.vaccinationStatus==pndStatus}">
		<fmt:formatDate value="${prevvaccmap.previous_vaccination.vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>
	</c:if>
		</th>
	</tr>
	<c:if test="${prevvaccmap.previous_vaccination.vaccinationStatus!=pndStatus}">
	<tr>
		<td>Vaccination Center , Vaccinator</td>
		<td>${prevvaccmap.previous_vaccination_vcenter.idMapper.identifiers[0].identifier} : ${prevvaccmap.previous_vaccination_vcenter.name}
		 , ${prevvaccmap.previous_vaccination_vaccinator.idMapper.identifiers[0].identifier} : ${prevvaccmap.previous_vaccination_vaccinator.firstName}</td>
	</tr>
	<tr>	
		<td>Epi Number</td>
		<td>${prevvaccmap.previous_vaccination.epiNumber}</td>
	</tr>
	</c:if>
    </c:forEach>
    	</table>
    </td>
	</tr> --%>
	<tr>
        <td colspan="2" class="headerrow">Immunization Details</td>
    </tr>
    <tr>
    	<td>Center Visit Date<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.visitDate">
        <input id="centerVisitDate" name="visitDate" value="${status.value}" maxDate="+0d" class="calendarbox" onchange="centerVisitDateChanged();"/>
		<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
<script type="text/javascript">
function centerVisitDateChanged() {
	loadSchedule();
}
</script>
    	</td>
    </tr>
    <tr>
		<td>Vaccinator ID <span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.vaccinatorId">
            <select id="vaccinatorId" name="vaccinatorId" bind-value="${status.value}">
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
			<spring:bind path="command.vaccinationCenterId">
            <select id="vaccinationCenterId" name="vaccinationCenterId" bind-value="${status.value}" onchange="centerChanged();">
               	<option></option>
            	<c:forEach items="${vaccinationCenters}" var="vcenter"> 
            	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier} : ${vcenter.name}</option>
            	</c:forEach> 
            </select>
            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
<script type="text/javascript">
function centerChanged() {
	loadSchedule();
}
</script>
		</td>
	</tr>
	<tr>
		<td>EPI Register Number <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.epiNumber">
				<input type="number" id="epiNumber" name="epiNumber" maxlength="8"  class="numbersOnly" 
						value="<c:out value="${status.value}"/>"/>
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
			<spring:bind path="command.preference.hasApprovedReminders">
				<input type="radio" name="preference.hasApprovedReminders" <c:if test='${not empty status.value && status.value == true}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"/>Yes<br>
				<input type="radio" name="preference.hasApprovedReminders" <c:if test='${not empty status.value && status.value == false}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>No
				<input type="radio" name="preference.hasApprovedReminders" value="" disabled="disabled"/>N/A<br>
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>SMS reminder ke liye Mobile Number</td>
		<td><spring:bind path="command.contactPrimary">
			<input type="number" id="contactPrimary" name="contactPrimary" maxlength="15" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Raabtay ke liye koi aur number</td>
		<td><spring:bind path="command.contactSecondary">
			<input type="number" id="contactSecondary" name="contactSecondary" maxlength="15" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<%-- <tr>
		<td>Kya aap kurandazi mein hisa lena chahtay hen? <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.hasApprovedLottery">
			<input type="hidden" id="hasApprovedLotteryinh" name="hasApprovedLotteryinh"/> 
			<select id="hasApprovedLottery" name="hasApprovedLottery" bind-text="${hasApprovedLotteryinh}" onchange="hasApprovedLotteryChanged(this);">
				<option></option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>">Yes</option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>">No</option>
				<option value="">Don`t Know</option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		<script type="text/javascript">
            function hasApprovedLotteryChanged(sel) {
				document.getElementById("hasApprovedLotteryinh").value = getTextSelectedInDD(sel);
			}
		</script>
		</td>
	</tr> --%>
	<tr>
        <td></td>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
        </td>
    </tr>
</table>
</form>
