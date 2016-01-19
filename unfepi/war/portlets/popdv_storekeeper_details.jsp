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
            <td>ID</td><td>${model.storekeeper.idMapper.identifiers[0].identifier}</td>
</tr>
<tr>
            <td>Date registered</td><td><fmt:formatDate value="${model.storekeeper.dateRegistered}" /></td>
</tr>
<tr>
            <td>First name</td><td><c:out value="${model.storekeeper.firstName}"></c:out></td>
</tr>  
<tr>            
            <td>Last name</td><td><c:out value="${model.storekeeper.lastName}"></c:out></td>
</tr>
<tr>
            <td>Date of birth</td><td><fmt:formatDate value="${model.storekeeper.birthdate}"  type="date"/> 
            , (<fmt:formatNumber value="${model.storekeeper.age/52}" maxFractionDigits="0"></fmt:formatNumber> Y)
            </td>
</tr>
<tr>
            <td>Is date of birth estimated ?</td><td><c:out value="${model.storekeeper.estimatedBirthdate}"></c:out></td>
</tr>
<tr>
            <td>Gender</td><td><c:out value="${model.storekeeper.gender}"></c:out></td>
</tr>
<tr>
            <td>Store</td><td>${model.storekeeper.storeName}</td>
</tr>
<tr>
	        <td>Closest vaccination center</td><td><c:out value="${model.vcenter.idMapper.identifiers[0].identifier} : ${model.vcenter.name}"/></td>
</tr>
<%
boolean nicPerm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_STOREKEEPER_NIC, request);
boolean epWalletPerm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_STOREKEEPER_EP_WALLET_NUMBER, request);

if(nicPerm){
%>          
<tr> 
            <td>NIC</td><td><c:out value="${model.storekeeper.nic}"></c:out></td>
</tr>
<%
}
if(epWalletPerm){
%>  
<tr> 
            <td>EP wallet number</td><td><c:out value="${model.storekeeper.epAccountNumber}"></c:out></td>
</tr>            
<%
}
%> 
<tr> 
            <td>Qualification</td><td><c:out value="${model.storekeeper.qualification}"></c:out></td>
</tr>
<tr>
            <td>Additional note</td><td><c:out value="${model.storekeeper.description}"></c:out></td>
</tr>
<%
boolean permcr=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CREATEDBY_INFO.name());
boolean permed=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_EDITEDBY_INFO.name());
boolean permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERID.name());
boolean permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERNAME.name());

if(permcr){ 
%>
<tr>
            <td>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${model.storekeeper.createdByUserId.username},<%}%><%if(permuname){%>${model.storekeeper.createdByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <td>Created on</td><td><c:out value="${model.storekeeper.createdDate}" /></td>
</tr>
<%
if(permed){ 
%>
<tr>
            <td>Last editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</td><td><%if(permuid){%>${model.storekeeper.lastEditedByUserId.username},<%}%><%if(permuname){%>${model.storekeeper.lastEditedByUserId.firstName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <td>Last updated</td><td><c:out value="${model.storekeeper.lastEditedDate}" /></td>    
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
