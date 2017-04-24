<%@ include file="/WEB-INF/template/header.jsp"%>

<br>	
<span class="formheadingWRule">Round Vaccines</span>
<br>

<div class="dvwform">
<table>
	<tr>
	<th>Health Program</th>
	<th>Round</th>
	<c:forEach var="vac" items="${model.vaccinesList}">
	<th>${vac}</th>
	</c:forEach>
	</tr>

	<c:forEach var="data" items="${model.data}">

		<c:forEach var="round" items="${data.value}">
			<tr>
				<td>${data.key.name}</td>
				<td>${round.key.name}</td>
				<c:forEach var="vac" items="${model.vaccinesList}">
				<td width="50" height="30" style="text-align: center;">
					<c:forEach var="vaccine" items="${round.value}">
					<c:if test="${vaccine == vac}"> 
					<img alt="" src="images/check-black.png" height="15" width="15">
					</c:if>
					</c:forEach> 
				</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</c:forEach>
</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>