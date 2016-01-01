<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<div class="dvwform">
<table >
	<tr>
		<td>Enrollment Date</td>
		<td>
		<c:set var="childsessvar" value="childfollowup"></c:set>
		<fmt:formatDate value="${sessionScope[childsessvar].dateEnrolled}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>
		</td>
	</tr>
	<tr>
		<td>Assigned Id (Program Id) : Name </td>
   		<td>
   		<input type="hidden" id="programId" value="${sessionScope[childsessvar].idMapper.identifiers[0].identifier}"/>
      		<a onclick="viewChildDetails(this.title)" title="${sessionScope[childsessvar].idMapper.identifiers[0].identifier}"><span class="hltext1"><c:out value="${sessionScope[childsessvar].idMapper.identifiers[0].identifier}"/> : ${sessionScope[childsessvar].firstName} ${sessionScope[childsessvar].middleName} ${sessionScope[childsessvar].lastName}</span></a>
  		(${sessionScope[childsessvar].gender})
  		</td>
	</tr>
	<tr>
		<td>Father`s Name</td>
		<td>${sessionScope[childsessvar].fatherFirstName} ${sessionScope[childsessvar].fatherLastName}</td>
	</tr>
	<tr>
		<td>DOB , Age</td>
		<td>
			<input class="readonlyLabelInput" style="width: 65px" readonly="readonly" id="birthdateinh" name="birthdateinh" value="<fmt:formatDate value="${sessionScope[childsessvar].birthdate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>">
		 , 
			<c:if test="${sessionScope[childsessvar].age < 20}">${sessionScope[childsessvar].age} Weeks</c:if>
			<c:if test="${sessionScope[childsessvar].age >= 20}">
			<fmt:formatNumber value="${sessionScope[childsessvar].age/4.34}" maxFractionDigits="0"></fmt:formatNumber> Months
			</c:if>
		</td>
	</tr>
</table>
</div>