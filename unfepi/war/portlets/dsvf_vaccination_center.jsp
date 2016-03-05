
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

	<div class="dvwform">
	<table >
	<thead>
        <tr>
<%
boolean edit=UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_VACCINATION_CENTERS, request);

if(edit){
%>
        	<th>edit</th>
<%
}
%> 
        	<!-- <th>---</th> -->
			<th>Assigned Id</th>
			<th>Date Registered</th>
			<th>Name</th>
			<th>Location</th>
            <th>Full Name</th>
			<th>Center Type</th>
			<th>Vaccine Days</th>
        </tr>
    </thead>
    <tbody>
   <c:forEach items="${model.datalist}" var="centermap">
   	 <tr>
<%
if(edit){
%>    	 
			<td><a href="editvaccinationCenter.htm?rid=${centermap.center.mappedId}"  class="icon"><img alt="edit" src ="images/edit-icon.png" class="icon"></a></td>
<%
}
%> 
		  	<td><a onclick="viewVaccinationCenterDetails(this.text);" class="anchorCustom">${centermap.center.idMapper.identifiers[0].identifier}</a></td>
            <td><fmt:formatDate value="${centermap.center.dateRegistered}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td>${centermap.center.name}</td>
            <td>${centermap.center.idMapper.identifiers[0].location.name}</td>
            <td><c:out value="${centermap.center.fullName}"></c:out></td>
            <td class="lowercase"><c:out value="${centermap.center.centerType}"></c:out></td>
            <td>
            <a href="editvaccinationCenter.htm?rid=${centermap.center.mappedId}&editvaccinedays=true" class="anchorCustom">clear all and re-enter</a><br>
            <c:forEach items="${centermap.vaccdaymaplist}" var="vdml">
            	<span style="font-weight: bolder;">${vdml['vaccine'].name}: </span>${vdml['daylist']}<br>
            </c:forEach>
            </td>
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>


