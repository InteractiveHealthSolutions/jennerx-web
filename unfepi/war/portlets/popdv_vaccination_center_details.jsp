<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<%@ include file="/WEB-INF/template/include.jsp"%>

<c:catch var="catchException">
<c:out value="${lmessage}"></c:out>
<div class="datalistform">
<table>
<tr>
            <td>ID</td><td>${model.vaccinationCenter.idMapper.identifiers[0].identifier}</td>
</tr>
<tr>
            <td>Date registered</td><td><fmt:formatDate value="${model.vaccinationCenter.dateRegistered}" /></td>
</tr>
<tr>
            <td>Name</td><td><c:out value="${model.vaccinationCenter.name}"></c:out></td>
</tr>  
<tr>            
            <td>Full name</td><td><c:out value="${model.vaccinationCenter.fullName}"></c:out></td>
</tr>
<tr>            
            <td>Short name</td><td><c:out value="${model.vaccinationCenter.shortName}"></c:out></td>
</tr>
<tr>
            <td>Vaccine days</td><td>
			<c:forEach items="${model.vaccineDayMapList}" var="vdml">
            	<span style="font-weight: bolder;">${vdml['vaccine'].name}: </span>${vdml['daylist']}<br>
            </c:forEach>
            </td>
</tr>
<tr>
            <td>Center type</td><td><c:out value="${model.vaccinationCenter.centerType}"></c:out></td>
</tr>
<tr>
            <td>Additional note</td><td><c:out value="${model.vaccinationCenter.description}"></c:out></td>
</tr>
<%
boolean permcr=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CREATEDBY_INFO.name());
boolean permed=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_EDITEDBY_INFO.name());
boolean permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERID.name());
boolean permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERNAME.name());

if(permcr){ 
%>
<tr>
            <td>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${model.vaccinationCenter.createdByUserId.username},<%}%><%if(permuname){%>${model.vaccinationCenter.createdByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <td>Created on</td><td><c:out value="${model.vaccinationCenter.createdDate}" /></td>
</tr>
<%
if(permed){ 
%>
<tr>
            <td>Last editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${model.vaccinationCenter.lastEditedByUserId.username},<%}%><%if(permuname){%>${model.vaccinationCenter.lastEditedByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <td>Last updated</td><td><c:out value="${model.vaccinationCenter.lastEditedDate}" /></td>    
</tr>
<c:forEach items="${model.contacts}" var="con" >
	<tr>
        <td colspan="2" class="headerrow-light">${con.numberType} Contact Number</td>
    </tr>
<tr>
            <td>Contact number</td><td><c:out value="${con.number}"></c:out></td>
</tr>
<tr>
            <td>Line type</td><td><c:out value="${con.telelineType}"></c:out></td>
</tr>
</c:forEach>

<c:forEach items="${model.addresses}" var="addr" varStatus="i">
	<tr>
        <td colspan="2" class="headerrow-light">${addr.addressType} Address</td>
    </tr>
<%-- <tr>
            <td>Address Type</td><td><c:out value="${addr.addressType}"></c:out></td>
</tr> --%>
<tr>
            <td>Address line 1</td><td><c:out value="${addr.address1}"></c:out></td>
</tr>
<tr>
            <td>Town, UC</td><td><c:out value="${addr.town}"/>, UC: <c:out value="${addr.uc}"/></td>
</tr>
<tr>
            <td>Landmark</td><td><c:out value="${addr.landmark}"></c:out></td>
</tr>
<tr>
            <td>City</td><td><c:out value="${addr.city.cityName}"/> <c:out value="${addr.cityName}"/></td>
</tr>
</c:forEach>
</table>
</div>
</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>
