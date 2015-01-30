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
            <td>ID</td><td>${model.vaccinator.idMapper.identifiers[0].identifier}</td>
</tr>
<tr>
            <td>Date registered</td><td><fmt:formatDate value="${model.vaccinator.dateRegistered}" /></td>
</tr>
<tr>
            <td>First name</td><td><c:out value="${model.vaccinator.firstName}"></c:out></td>
</tr>  
<tr>            
            <td>Last name</td><td><c:out value="${model.vaccinator.lastName}"></c:out></td>
</tr>
<tr>
            <td>Date of birth</td><td><fmt:formatDate value="${model.vaccinator.birthdate}"  type="date"/> 
            , (<fmt:formatNumber value="${model.vaccinator.age/52}" maxFractionDigits="0"></fmt:formatNumber> Y)
            </td>
</tr>
<tr>
            <td>Is date of birth estimated ?</td><td><c:out value="${model.vaccinator.estimatedBirthdate}"></c:out></td>
</tr>
<tr>
            <td>Gender</td><td><c:out value="${model.vaccinator.gender}"></c:out></td>
</tr>
<tr>
	        <td>Vaccination center</td><td><c:out value="${model.vcenter.idMapper.identifiers[0].identifier} : ${model.vcenter.name}"/></td>
</tr>
<%
boolean nicPerm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_STOREKEEPER_NIC, request);
boolean epWalletPerm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_STOREKEEPER_EP_WALLET_NUMBER, request);

if(nicPerm){
%>          
<tr> 
            <td>NIC</td><td><c:out value="${model.vaccinator.nic}"></c:out></td>
</tr>
<%
}
if(epWalletPerm){
%>  
<tr> 
            <td>EP wallet number</td><td><c:out value="${model.vaccinator.epAccountNumber}"></c:out></td>
</tr>            
<%
}
%> 
<tr> 
            <td>Qualification</td><td><c:out value="${model.vaccinator.qualification}"></c:out></td>
</tr>
<tr>
            <td>Additional note</td><td><c:out value="${model.vaccinator.description}"></c:out></td>
</tr>
<%
boolean permcr=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CREATEDBY_INFO.name());
boolean permed=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_EDITEDBY_INFO.name());
boolean permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERID.name());
boolean permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERNAME.name());

if(permcr){ 
%>
<tr>
            <td>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${model.vaccinator.createdByUserId.username},<%}%><%if(permuname){%>${model.vaccinator.createdByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <td>Created on</td><td><c:out value="${model.vaccinator.createdDate}" /></td>
</tr>
<%
if(permed){ 
%>
<tr>
            <td>Last editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${model.vaccinator.lastEditedByUserId.username},<%}%><%if(permuname){%>${model.vaccinator.lastEditedByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <td>Last updated</td><td><c:out value="${model.vaccinator.lastEditedDate}" /></td>    
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
            <td>House number</td><td><c:out value="${addr.addHouseNumber}"></c:out></td>
</tr>
<tr>
            <td>Street number</td><td><c:out value="${addr.addStreet}"></c:out></td>
</tr>
<tr>            
			<td>Sector</td><td><c:out value="${addr.addSector}"></c:out></td>
</tr>
<tr>
            <td>Colony</td><td><c:out value="${addr.addColony}"></c:out></td>
</tr>
<tr>
            <td>Town, UC</td><td><c:out value="${addr.addtown}"/>, UC: <c:out value="${addr.addUc}"/></td>
</tr>
<tr>
            <td>Landmark</td><td><c:out value="${addr.addLandmark}"></c:out></td>
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
