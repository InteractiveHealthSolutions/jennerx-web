<table class="previousDataDisplay">
	<tr>
   <td colspan="2" class="separator-heading">NEXT VACCINATION DETAILS</td>
    </tr>
    <tr><td colspan="2">
    <c:set var="nextvaccsessvar" value="next_vaccination${command.vaccinationRecordNum}"></c:set>
    <c:set var="nextvacccentersessvar" value="next_vaccination_vcenter${command.vaccinationRecordNum}"></c:set>
    <c:set var="nextvaccvaccinatorsessvar" value="next_vaccination_vaccinator${command.vaccinationRecordNum}"></c:set>
    <c:choose>
	<c:when test="${not empty sessionScope[nextvaccsessvar]}">
    <table class="previousDataDisplay">
	<tr>
				<td>Vaccination Record Num </td>
				<td>${sessionScope[nextvaccsessvar].vaccinationRecordNum}   (${sessionScope[nextvaccsessvar].vaccinationStatus})</td>
			</tr>
			<tr>
				<td>Vaccine</td>
				<td>${sessionScope[nextvaccsessvar].vaccine.name}</td>
			</tr>
			<tr>
				<td>Vaccination Due Date</td>
				<td><div id="currnextvaccinationduedate"><fmt:formatDate value="${sessionScope[nextvaccsessvar].vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></div><div id="editednextvaccinationduedate"></div></td>
			</tr>
			<tr>
				<td>Vaccination Date</td>
				<td><fmt:formatDate value="${sessionScope[nextvaccsessvar].vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
			</tr>
	<tr>
		<td>Vaccination Center , Vaccinator</td>
		<td>${sessionScope[nextvacccentersessvar].idMapper.identifiers[0].identifier} : ${sessionScope[nextvacccentersessvar].name}
		 , ${sessionScope[nextvaccvaccinatorsessvar].idMapper.identifiers[0].identifier} : ${sessionScope[nextvaccvaccinatorsessvar].firstName}</td>
	</tr>
	<tr>
		<td>Epi Number</td>
		<td>${sessionScope[nextvaccsessvar].epiNumber}</td>
	</tr>
	</table>
	</c:when>
	<c:otherwise>
		<div style="font-size: medium;color: maroon;">No next vaccination record exists.</div>
	</c:otherwise>
	</c:choose>
	</td>
	</tr>
</table>