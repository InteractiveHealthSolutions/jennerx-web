<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<div class="dvwform">

<table>
	<tr>
		<td>Enrollment Date</td>
		<td>
		<c:set var="womensessvar" value="womenfollowup"></c:set>
		<fmt:formatDate value="${sessionScope[womensessvar].dateEnrolled}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>
		</td>
	</tr>
	<tr>
		<td>Assigned Id (Program Id) : Name </td>
   		<td>
   		<input type="hidden" id="programId" value="${sessionScope[womensessvar].idMapper.identifiers[0].identifier}"/>
      		<a  title="${sessionScope[womensessvar].idMapper.identifiers[0].identifier}"><span class="hltext1"><c:out value="${sessionScope[womensessvar].idMapper.identifiers[0].identifier}"/> : ${sessionScope[womensessvar].firstName} ${sessionScope[womensessvar].middleName} ${sessionScope[womensessvar].lastName}</span></a>
  		</td>
	</tr>
	<tr>
		<td>Father`s Name</td>
		<td>${sessionScope[womensessvar].fatherFirstName} ${sessionScope[womensessvar].fatherLastName}</td>
	</tr>
	<tr>
		<td>Husband`s Name</td>
		<td>${sessionScope[womensessvar].husbandFirstName} ${sessionScope[womensessvar].husbandLastName}</td>
	</tr>
	<tr>
		<td>DOB , Age</td>
		<td>
			<input class="readonlyLabelInput" readonly="readonly" id="birthdateinh" name="birthdateinh" value="<fmt:formatDate value="${sessionScope[womensessvar].birthdate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>">
		 , 
			<%-- <c:if test="${women.age < 20}">${sessionScope[womensessvar].age} Weeks</c:if>
			<c:if test="${women.age >= 20}">
			<fmt:formatNumber value="${sessionScope[womensessvar].age/4.34}" maxFractionDigits="0"></fmt:formatNumber> Months
			</c:if> --%>
		</td>
	</tr>
</table>
</div>