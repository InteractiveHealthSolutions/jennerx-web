<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<div class="dvwform">
	<table >
	<thead>
        <tr>
        	<th></th>
            <th>Name</th>
            <th>Short Name</th>
            <th>Full Name</th>
            <th>Gap From Birthdate</th>
            <th>Gap From Previous Milestone</th>
            <th>Gap From Next Milestone</th>
            <th>Description</th>
<%-- <%
boolean permcr=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_CREATEDBY_INFO, request);
boolean permed=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_EDITEDBY_INFO, request);
boolean permuid=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_USERID, request);
boolean permuname=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_USERNAME, request);

if(permcr){ 
%>            
           <th>Created By(<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th>
<%
}
%>    
            <th>Date created</th>

<%
if(permed){ 
%>            
           <th>Last Editor(<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th>
<%
}
%>        
            <th>Last updated</th>
--%>
        </tr>
    </thead>
      <tbody>
         <c:forEach items="${model.datalist}" var="vacc">
   		 <tr>
   		 	<td><a href="editVaccine.htm?editRecord=${vacc.name}" class="linkiconS iconedit"></a></td>
            <td><c:out value="${vacc.name}"></c:out></td>
            <td><c:out value="${vacc.shortName}"></c:out></td>
            <td><c:out value="${vacc.fullName}"></c:out></td>
            <td><c:out value="${vacc.gapFromBirthdate} ${vacc.birthdateGapUnit}"></c:out></td>
            <td><c:out value="${vacc.gapFromPreviousMilestone} ${vacc.prevGapUnit}"></c:out></td>
            <td><c:out value="${vacc.gapFromNextMilestone} ${vacc.nextGapUnit}"></c:out></td>   
            <td><c:out value="${vacc.description}"></c:out></td>
<%-- <%if(permcr){ %>   
<td><%if(permuid){%>${vacc.createdByUserId.username},<%}%><%if(permuname){%>${vacc.createdByUserId.firstName}<%}%></td>
<%} %> 
            <td><fmt:formatDate value="${vacc.createdDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
 <%if(permed){ %>   
<td><%if(permuid){%>${vacc.lastEditedByUserId.username},<%}%><%if(permuname){%>${vacc.lastEditedByUserId.firstName}<%}%></td>
<%} %> 
            <td><fmt:formatDate value="${vacc.lastEditedDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
--%>       </tr>
  </c:forEach>
  </tbody>
</table>
</div>
