<%@page import="org.ird.unfepi.model.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineScheduleKey"%>

<div id="vaccinelistContainerDiv">
	<c:set var="vaccStPen" value="<%=VACCINATION_STATUS.SCHEDULED.toString()%>"></c:set>
	<table id="vaccinelistContainerTbl" class="denform2">
		<tr style="background-color: #F3F3F3">
			<td>Vaccine</td>
			<td>Vaccination due date</td>
			<td>Vaccination status</td>
			<td>Vaccination date</td>
			<td>Center</td>
			<td>Vaccinator</td>
		</tr>
		<c:forEach items="${command.vaccinations}" var="vacc" varStatus="vst">
			<tr>
				<td>${vacc.vaccine.name}</td>
				<td><c:choose>
						<c:when test="${vacc.vaccinationStatus == vaccStPen}">
							<!-- is editable for scheduled vaccinations only -->
							<spring:bind path="command.vaccinations[${vst.index}].vaccinationDuedate">
								<input id="vaccinationDuedate${vst.index}" name="${status.expression}" value="${status.value}" class="smallbox" />
								<br>
								<span class="error">${status.errorMessage}</span>
							</spring:bind>
							<script>
								$(function() {
								    $('#vaccinationDuedate${vst.index}').datepicker({
								    	duration: '',
								        constrainInput: false,
								        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
								     });
								});
							</script>
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${command.vaccinations[vst.index].vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>" />
						</c:otherwise>
					</c:choose></td>
				<td>${vacc.vaccinationStatus}</td>
				<td><c:choose>
						<c:when test="${vacc.vaccinationStatus != vaccStPen}">
							<!-- is editable for vaccinated vaccinations only -->
							<spring:bind path="command.vaccinations[${vst.index}].vaccinationDate">
								<input id="vaccinationDate${vst.index}" name="${status.expression}" readonly="readonly" value="${status.value}" class="smallbox" />
								<br>
								<span class="error">${status.errorMessage}</span>
							</spring:bind>
							<script>
								$(function() { 
									$('#vaccinationDate${vst.index}').datepicker({ 
										duration: '', 
										constrainInput: false, 
										dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
										});
									});
							</script>
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${command.vaccinations[vst.index].vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>" />
						</c:otherwise>
					</c:choose></td>

				<td><c:choose>
						<c:when test="${vacc.vaccinationStatus != vaccStPen}">
							<spring:bind path="command.vaccinations[${vst.index}].vaccinationCenterId">
								<select id="vaccinationCenterId${vst.index}" name="${status.expression}" bind-value="${status.value}">
									<option></option>
									<c:forEach items="${vaccinationCenters}" var="vcenter">
										<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier}:${vcenter.name}</option>
									</c:forEach>
								</select>
								<br>
								<span class="error"><c:out value="${status.errorMessage}" /></span>
							</spring:bind>
						</c:when>
					</c:choose></td>
				<td><c:choose>
						<c:when test="${vacc.vaccinationStatus != vaccStPen}">
							<spring:bind path="command.vaccinations[${vst.index}].vaccinatorId">
								<select id="vaccinatorId${vst.index}" name="${status.expression}" bind-value="${status.value}">
									<option></option>
									<c:forEach items="${vaccinators}" var="vtor">
										<option value="${vtor.mappedId}">${vtor.idMapper.identifiers[0].identifier}:${vtor.firstName}</option>
									</c:forEach>
								</select>
								<br>
								<span class="error">${status.errorMessage}</span>
							</spring:bind>
						</c:when>
					</c:choose></td>
			</tr>
		</c:forEach>
	</table>

</div>
