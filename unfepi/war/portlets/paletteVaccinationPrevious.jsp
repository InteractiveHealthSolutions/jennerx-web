<table class="previousDataDisplay">
	<tr>
        <td colspan="2" class="separator-heading">PREVIOUS VACCINATION DETAILS</td>
    </tr>
    <tr><td colspan="2">
    <c:set var="prevvaccsessvar" value="previous_vaccination${command.vaccinationRecordNum}"></c:set>
    <c:set var="prevvacccentersessvar" value="previous_vaccination_vcenter${command.vaccinationRecordNum}"></c:set>
    <c:set var="prevvaccvaccinatorsessvar" value="previous_vaccination_vaccinator${command.vaccinationRecordNum}"></c:set>
    <c:choose>
	<c:when test="${not empty sessionScope[prevvaccsessvar]}">
   <table class="previousDataDisplay">
	<tr>
		<td>Vaccination Record Num </td>
		<td>${sessionScope[prevvaccsessvar].vaccinationRecordNum}  (${sessionScope[prevvaccsessvar].vaccinationStatus})</td>
	</tr>
	<tr>
		<td>Vaccine</td>
		<td>${sessionScope[prevvaccsessvar].vaccine.name}</td>
	</tr>
<%-- 	<tr>
		<td>Vaccination Due Date</td>
		<td><fmt:formatDate value="${sessionScope[prevvaccsessvar].vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
	</tr> --%>
	<tr>
		<td>Vaccination Date</td>
		<td><fmt:formatDate value="${sessionScope[prevvaccsessvar].vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>
	</tr>
	<tr>
		<td>Vaccination Center , Vaccinator</td>
		<td>${sessionScope[prevvacccentersessvar].idMapper.identifiers[0].identifier} : ${sessionScope[prevvacccentersessvar].name}
		 , ${sessionScope[prevvaccvaccinatorsessvar].idMapper.identifiers[0].identifier} : ${sessionScope[prevvaccvaccinatorsessvar].firstName}</td>
	</tr>
	<%-- <tr>
		<td>Vaccinator</td>
		<td>${sessionScope[prevvaccvaccinatorsessvar].idMapper.identifiers[0].identifier} : ${sessionScope[prevvaccvaccinatorsessvar].firstName}</td>
	</tr> --%>
	<tr>
		<td>Epi Number</td>
		<td>${sessionScope[prevvaccsessvar].epiNumber}</td>
	</tr>
	</table>
	</c:when>
	<c:otherwise>
		<div style="font-size: medium;color: maroon;">No previous vaccination record exists.</div>
	</c:otherwise>
	</c:choose>
	</td>
	</tr>
</table>