
<%@include file="/WEB-INF/template/include.jsp"%>

<div class="searchpalette">
	<table>
		<tr>
			<td>Vaccination Calendar 
			<select id="vaccinationCalendar">
				<option></option>
				<c:forEach items="${vaccinationCalendars}" var="calendar">
					<option value="${calendar.calenderId}" <c:if test="${model.calenderId == calendar.calenderId}">selected="selected"</c:if>> ${calendar.shortName}</option>
				</c:forEach>
			</select></td>
			<td><a onclick="searchData()" class="searchButton"></a></td>
		</tr>
	</table>

</div>
<script type="text/javascript">
	function searchData() {
		window.location = "viewVaccinationCalendar.htm?calendarId=" + $('#vaccinationCalendar').val();
	}
</script>
<div class="dvwform">
	<table>
		<tr>
			<th>Id</th>
			<th>vaccine Name</th>
			<th>min Grace<br>Period Days</th>
			<th>max Grace<br>Period Days</th>
			<th>Birthdate Gap</th>
			<th>Previous Vaccine Gap</th>
			<th>Next Vaccine Gap</th>
			<th>Standard Gap</th>
			<th>Vaccine Expiry Gap</th>
			<th>Over Age Gap</th>
			<th>Prerequsites</th>
		</tr>
		
		<c:forEach items="${model.data}" var="data">
		<tr>
				<td>${data.key.vaccineId}</td>
				<td>${data.key.name}</td>
				<td>${data.key.minGracePeriodDays}</td>
				<td>${data.key.maxGracePeriodDays}</td>
				<td>${data.value['Birthdate Gap']}</td>
				<td>${data.value['Previous Vaccine Gap']}</td>
				<td>${data.value['Next Vaccine Gap']}</td>
				<td>${data.value['Standard Gap']}</td>
				<td>${data.value['Vaccine Expiry Gap']}</td>
				<td>${data.value['Over Age Gap']}</td>
				<td>
				<c:forEach items="${model.prerequisites}" var="preq">
					<c:if test="${data.key.vaccineId == preq.vaccineId}">
						${preq.prerequisites}
					</c:if>
				</c:forEach>
				</td>
		</tr>
		</c:forEach>
		
	</table>
</div>
<script>
</script>


