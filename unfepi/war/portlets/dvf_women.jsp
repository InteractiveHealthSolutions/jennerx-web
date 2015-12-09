<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<form method="post" id="frm" name="frm" >
<table class="denform-h">
	<tr>
		<td> Women Id </td>
		<td> Name </td>
		<td> Father Name </td>
		<td> Husband Name </td>
		<td> Birthdate </td>
		<td> TT1 </td>
		<td> TT2 </td>
		<td> TT3 </td>
		<td> TT4 </td>
		<td> TT5 </td>
	</tr>
	
	<c:forEach items="${model.women}" var="women">
	<tr>
	
		<td> <a href="womenFollowup.htm?women_id=${women.mappedId}"> ${women.idMapper.identifiers[0].identifier} </a></td>
		<td> ${women.firstName} </td>
		<td> ${women.fatherFirstName} </td>
		<td> ${women.husbandFirstName} </td>
		<td> <fmt:formatDate value="${women.birthdate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> </td>
		<%-- <td> ${vaccine.vaccineId} </td> --%>
		<c:forEach items="${model.vaccine}" var="vaccination">
			<c:if test="${vaccination.womenId == women.mappedId}">
				<c:if test="${vaccination.vaccine.name == 'TT1'}">
					<td> <fmt:formatDate value="${vaccination.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> </td>
				</c:if>
			</c:if> 
		</c:forEach>
		<c:forEach items="${model.vaccine}" var="vaccination">
			<c:if test="${vaccination.womenId == women.mappedId}">
				<c:if test="${vaccination.vaccine.name == 'TT2'}">
					<td> <fmt:formatDate value="${vaccination.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> </td>
				</c:if>
			</c:if> 
		</c:forEach>
		<c:forEach items="${model.vaccine}" var="vaccination">
			<c:if test="${vaccination.womenId == women.mappedId}">
				<c:if test="${vaccination.vaccine.name == 'TT3'}">
					<td> <fmt:formatDate value="${vaccination.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> </td>
				</c:if>
			</c:if> 
		</c:forEach>
		<c:forEach items="${model.vaccine}" var="vaccination">
			<c:if test="${vaccination.womenId == women.mappedId}">
				<c:if test="${vaccination.vaccine.name == 'TT4'}">
					<td> <fmt:formatDate value="${vaccination.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> </td>
				</c:if>
			</c:if> 
		</c:forEach>
		<c:forEach items="${model.vaccine}" var="vaccination">
			<c:if test="${vaccination.womenId == women.mappedId}">
				<c:if test="${vaccination.vaccine.name == 'TT5'}">
					<td> <fmt:formatDate value="${vaccination.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> </td>
				</c:if>
			</c:if> 
		</c:forEach>
	</tr>
	</c:forEach>
	
</table>
</form>
