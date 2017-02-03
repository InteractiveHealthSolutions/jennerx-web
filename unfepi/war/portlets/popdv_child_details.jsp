<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<%@ include file="/WEB-INF/template/include.jsp"%>

<c:out value="${lmessage}"></c:out>
<div class="dvwform">
<table>
<%
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){%>
<tr>
      		<td>Mapped ID</td><td><c:out value="${model.child.mappedId}"></c:out></td>
</tr>
<%}%>
<tr>
      		<td>Program ID</td><td><c:out value="${model.programId}"></c:out></td>
</tr>
<tr>
            <td>Date enrolled</td><td><fmt:formatDate value="${model.child.dateEnrolled}" /></td>
</tr>
<tr>
        <td>Enrollment Vaccination</td><td>${model.child.enrollmentVaccine.name}</td>
</tr>
<tr>
            <td>First name</td><td><c:out value="${model.child.firstName}"></c:out></td>
</tr>  
<tr>            
            <td>Last name</td><td><c:out value="${model.child.lastName}"></c:out></td>
</tr>
<tr>
            <td>Mother`s first name</td><td><c:out value="${model.child.motherFirstName}"></c:out></td>
</tr>
<tr>
            <td>Mother`s last name</td><td><c:out value="${model.child.motherLastName}"></c:out></td>
</tr>
<tr>
</tr>
<tr>
            <td>Date of birth</td><td><fmt:formatDate value="${model.child.birthdate}"  type="date"/> 
            , ${model.child.age} W <c:if test="${model.child.age >= 20}">(<fmt:formatNumber value="${model.child.age/4.34}" maxFractionDigits="1"></fmt:formatNumber> M)</c:if>
            </td>
</tr>
<tr>
            <td>Is date of birth estimated ?</td><td><c:out value="${model.child.estimatedBirthdate}"></c:out></td>
</tr>
<tr>
            <td>Gender</td><td><c:out value="${model.child.gender}"></c:out></td>
</tr>
<tr>
            <td>Program status</td><td><c:out value="${model.child.status}"></c:out></td>
</tr>
<c:if test="${fn:containsIgnoreCase(model.child.status,'TERMINATED')}">
<tr>
            <td>Termination date</td><td><fmt:formatDate value="${model.child.terminationDate}" /></td>
</tr>
<tr>
            <td>Termination reason</td><td><c:out value="${model.child.terminationReason}"></c:out></td>
</tr>
</c:if>
<tr><td></td><td><a onclick="showHide(this)" hideshow="hideshow1" class="anchorCustom" ><< less</a></td></tr>
<tr class="hideshow1">
            <td>Additional note</td><td><c:out value="${model.child.description}"></c:out></td>
</tr>
<%
boolean permcr=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CREATEDBY_INFO.name());
boolean permed=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_EDITEDBY_INFO.name());
boolean permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERID.name());
boolean permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERNAME.name());

if(permcr){ 
%>
<tr class="hideshow1">
            <td>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${model.child.createdByUserId.username},<%}%><%if(permuname){%>${model.child.createdByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr class="hideshow1">
            <td>Created on</td><td><c:out value="${model.child.createdDate}" /></td>
</tr>
<%
if(permed){ 
%>
<tr class="hideshow1">
            <td>Last editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${model.child.lastEditedByUserId.username},<%}%><%if(permuname){%>${model.child.lastEditedByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr class="hideshow1">
            <td>Last updated</td><td><c:out value="${model.child.lastEditedDate}" /></td>    
</tr>
<c:forEach items="${model.contacts}" var="con" >
	<tr>
        <td colspan="2" class="headerrow">${con.numberType} Contact Number</td>
    </tr>
<tr>
            <td>Contact number</td><td><c:out value="${con.number}"></c:out></td>
</tr>
<tr>
            <td>Line type</td><td><c:out value="${con.telelineType}"></c:out></td>
</tr>
<tr><td></td><td><a onclick="showHide(this)" hideshow="hideshow1" class="anchorCustom" ><< less</a></td></tr>
<%
if(permcr){ 
%>
<tr class="hideshow1">
            <td>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${con.createdByUserId.username},<%}%><%if(permuname){%>${con.createdByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr class="hideshow1">
            <td>Created on</td><td><fmt:formatDate value="${con.createdDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
</tr>
<%
if(permed){ 
%>
<tr class="hideshow1">
            <td>Last editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${con.lastEditedByUserId.username},<%}%><%if(permuname){%>${con.lastEditedByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr class="hideshow1">
            <td>Last updated</td><td><fmt:formatDate value="${con.lastEditedDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>    
</tr>

</c:forEach>

<c:forEach items="${model.addresses}" var="addr" varStatus="i">
	<tr>
        <td colspan="2" class="headerrow">${addr.addressType} Address</td>
    </tr>

<tr>
            <td>Village</td><td><c:out value="${addr.address1}"></c:out></td>
</tr>
<tr>
            <td>Location</td><td><c:out value="${addr.address1}"></c:out></td>
</tr>

<tr><td></td><td><a onclick="showHide(this)" hideshow="hideshow1" class="anchorCustom" ><< less</a></td></tr>

<%
if(permcr){ 
%>
<tr class="hideshow1">
            <td>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${addr.createdByUserId.username},<%}%><%if(permuname){%>${addr.createdByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr class="hideshow1">
            <td>Created on</td><td><fmt:formatDate value="${addr.createdDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
</tr>
<%
if(permed){ 
%>
<tr class="hideshow1">
            <td>Last editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${addr.lastEditedByUserId.username},<%}%><%if(permuname){%>${addr.lastEditedByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr class="hideshow1">
            <td>Last updated</td><td><fmt:formatDate value="${addr.lastEditedDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>    
</tr>
</c:forEach>
</table>
<table>
<tr>
     <td colspan="2" class="headerrow">Vaccinations Quick Overview</td>
</tr>
<tr>
<td colspan="2">
<table class="dvwform">
<!--     <thead >
        <tr>
            <td>Vaccine</td>
            <td>Status</td>
            <td>Reminders pending</td>
            <td>Reminder late?/ Max days</td>
            <td>Due date</td>
            <td>Vaccine date</td>
            <td>Record number</td> 
            <td>Approved lottery ?</td>          
        </tr>
    </thead> -->
    <c:forEach items="${model.vaccinations.record}" var="rec">
   	 <tr><td class="headerrow-light" colspan="2"><c:out value="${rec.vaccination.vaccine.name}"></c:out></td></tr>
 	 <tr><td>Vaccination record dum</td><td><c:out value="${rec.vaccination.vaccinationRecordNum}"></c:out></td></tr>
     <tr><td>Vaccination status</td><td><c:out value="${rec.vaccination.vaccinationStatus}"></c:out></td></tr>
     <tr><td>Vaccination due date</td><td><fmt:formatDate value="${rec.vaccination.vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td></tr>
     <tr><td>Vaccination date</td><td><fmt:formatDate value="${rec.vaccination.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td></tr>
   	 <tr><td>Vaccination center</td><td><c:out value="${rec.center.idMapper.identifiers[0].identifier} : ${rec.center.name}"></c:out></td></tr> 
	 <tr><td>Vaccinator</td><td><c:out value="${rec.vaccinator.idMapper.identifiers[0].identifier} : ${rec.vaccinator.firstName}"></c:out></td></tr>
<tr><td></td><td><a onclick="showHide(this)" hideshow="hideshow1" class="anchorCustom" ><< less</a></td></tr>
	 <tr class="hideshow1"><td>Additional note</td><td><c:out value="${rec.vaccination.description}"></c:out></td></tr>
<%
	if(permcr){ 
	%>
	<tr class="hideshow1"><td>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${rec.vaccination.createdByUserId.username},<%}%><%if(permuname){%>${rec.vaccination.createdByUserId.firstName}<%}%></td></tr>
	<%
	}
	%>
	<tr class="hideshow1"><td>Created on</td><td><fmt:formatDate value="${rec.vaccination.createdDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td></tr>
	<%
	if(permed){ 
	%>
	<tr class="hideshow1"><td>Last editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${rec.vaccination.lastEditedByUserId.username},<%}%><%if(permuname){%>${rec.vaccination.lastEditedByUserId.firstName}<%}%></td></tr>
	<%
	}
	%>
	<tr class="hideshow1"><td>Last updated</td><td><fmt:formatDate value="${rec.vaccination.lastEditedDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td></tr>
	<c:forEach items="${rec.reminderSms}" var="sms">
	<tr><td class="datahighlight center" colspan="2">Reminder D(${sms.dayNumber})</td></tr>
    <tr><td>Due date</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${sms.dueDate}" /></td></tr>
    <tr><td>Sent date</td><td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${sms.sentDate}"/></td></tr>
    <tr><td>Status</td><td><c:out value="${sms.reminderStatus}"/></td></tr>
    <tr><td>Sms cancel reason</td><td>${sms.smsCancelReason}</td></tr>
    <tr><td>Reminder type</td><td>${sms.reminder.reminderType}</td></tr>
<tr><td></td><td><a onclick="showHide(this)" hideshow="hideshow1" class="anchorCustom" ><< less</a></td></tr>
    <tr class="hideshow1"><td>Day number</td><td>${sms.dayNumber}</td></tr>
    <tr class="hideshow1"><td>Recipient</td><td><c:out value="${sms.recipient}"></c:out></td></tr>
    <tr class="hideshow1"><td>Reference number</td><td><c:out value="${sms.referenceNumber}"></c:out></td></tr>
    <tr class="hideshow1"><td>Text</td><td style="width: 50%;"><div>${sms.text}</div></td></tr>

    <%
	if(permcr){ 
	%>
	<tr class="hideshow1"><td>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${sms.createdByUserId.username},<%}%><%if(permuname){%>${sms.createdByUserId.firstName}<%}%></td></tr>
	<%
	}
	%>
	<tr class="hideshow1"><td>Created on</td><td><fmt:formatDate value="${sms.createdDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td></tr>
	<%
	if(permed){ 
	%>
	<tr class="hideshow1"><td>Last editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${sms.lastEditedByUserId.username},<%}%><%if(permuname){%>${sms.lastEditedByUserId.firstName}<%}%></td></tr>
	<%
	}
	%>
	<tr class="hideshow1"><td>Last updated</td><td><fmt:formatDate value="${sms.lastEditedDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td></tr>
  </c:forEach>
  <c:forEach items="${rec.response}" var="resp" varStatus="vst">
  	<tr><td class="datahighlight center" colspan="2">Response ${vst.index}</td></tr>
    <tr><td>Sender</td><td><c:out value="${resp.originatorCellNumber}"></c:out></td></tr>
    <tr><td>Receive date</td><td><fmt:formatDate value="${resp.recievedDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td></tr>
    <tr><td>Response type</td><td><c:out value="${resp.responseType}"></c:out></td></tr>
    <tr><td>Text</td><td><c:out value="${resp.responseText}"></c:out></td></tr>
    <tr><td>Reference number</td><td><c:out value="${resp.referenceNumber}"></c:out></td></tr>
    <tr><td>Project phone number</td><td><c:out value="${resp.recipientCellNumber}"></c:out></td></tr>
  </c:forEach>
  
</c:forEach>
</table>
</td></tr>
</tbody>
</table>
</div>
<% ServiceContext sc = (ServiceContext)request.getAttribute("sc");
 	//must close session here; or do it in finally of respective controller else can create memory leak
 	sc.closeSession();%>
<%-- </c:catch> --%>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>
