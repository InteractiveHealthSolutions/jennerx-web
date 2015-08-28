<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

	<div class="dvwform">
	<table >
	<thead>
        <tr>
<%
boolean edit=UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_LOCATIONS, request);

if(edit){
%>
        	<th>edit</th>
<%
}
%> 
			<th>Name</th>
            <th>Full Name</th>
			<th>Type</th>
			<th>Parent Location</th>
        </tr>
    </thead>
    <tbody>
   <c:forEach items="${model.datalist}" var="loc">
   	 <tr>
<%
if(edit){
%>    	 
			<td><a href="editlocation.htm?rid=${loc.locationId}"  class="icon"><img alt="edit" src ="images/edit-icon.png" class="icon"></a></td>
<%
}
%> 
            <td>${loc.name}</td>
            <td><c:out value="${loc.fullName}"></c:out></td>
            <td class="lowercase"><c:out value="${loc.locationType.typeName}"></c:out></td>
            <td>${loc.parentLocation.name}</td>
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>


