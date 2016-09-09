<%@ include file="/WEB-INF/template/include.jsp"%>

<div class="dvwform">
	<table>
		<thead>
			<tr>
				<th></th>
				<th>Name</th>
				<th>Description</th>
<!-- 				<th>Enrollment Limit</th> -->
<!-- 				<th>current Enrollment count</th> -->
				<th>Vaccination Center</th>
				<th>edit</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="map" items="${model.healthprograms}">
				<tr>
					<td>${map.key.programId}</td>
					<td>${map.key.name}</td>
					<td>${map.key.description}</td>
<%-- 					<td>${map.key.enrollmentLimit}</td> --%>
<!-- 					<td></td> -->
					<td><c:forEach var="centerpro" items="${map.value}">
							<c:choose>
								<c:when test="${centerpro.isActive == 'true'}">
									<span style="color: green;">${centerpro.vaccinationCenter.name}</span>
									<br>
								</c:when>
								<c:otherwise>
									<span style="color: red;">${centerpro.vaccinationCenter.name}</span>
									<br>
								</c:otherwise>
							</c:choose>
						</c:forEach></td>
					<td><a href="edithealthprogram.htm?programId=${map.key.programId}" class="icon"><img alt="edit" src="images/edit-icon.png" class="icon"></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>