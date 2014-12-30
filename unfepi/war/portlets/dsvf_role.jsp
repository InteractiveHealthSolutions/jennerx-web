<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<div class="dvwform">
<table >
	<thead>
        <tr>
       		<th></th>
<%
boolean permcor=UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_USER_ROLES, request);
if(permcor){ 
%>       		
       		<th></th>
<%
}
%>        		
            <th>Role Name</th>
            <th>Description</th>
<%
boolean permuid=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_USERID, request);
boolean permuname=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_USERNAME, request);
if(permuid||permuname){ 
%>            
            <th>Creator (<%if(permuid){%>Id, <%}%><%if(permuname){%>Name<%}%>)</th>
<%
}
%>            
            <th>Created On</th>
<%
if(permuid||permuname){ 
%>            
           <th>Last Editor (<%if(permuid){%>Id, <%}%><%if(permuname){%>Name<%}%>)</th>
<%
}
%>             
            <th>Last Updated</th>
        </tr>
    </thead>
      <tbody>
         <c:forEach items="${model.datalist}" var="role">
   		 <tr>	
   	 		<td><input type="text" readonly="readonly" value="+" title="${role.roleId}rl" class="expandDataButton" onclick="expandRecord(this);"/></td>
<%
if(permcor){ 
%>  
<c:choose>
<c:when test='${fn:toLowerCase(role.rolename) == "admin" || fn:toLowerCase(role.rolename) == "administrator" || fn:toLowerCase(role.rolename) == "role_administrator"}'>
<td></td>
</c:when>
<c:otherwise>
            <td><a href="editrole.htm?editRecord=${role.rolename}" class="linkiconS iconedit"></a></td>
</c:otherwise>
</c:choose>
<%
}
%> 
            <td><c:out value="${role.rolename}"></c:out></td>
            <td><c:out value="${role.description}"></c:out></td>
<%if(permuid||permuname){ %>            
<td><%if(permuid){%>${role.createdByUserId.username}, <%}%><%if(permuname){%>${role.createdByUserId.firstName}<%}%></td>
<%} %>
            <td><fmt:formatDate value="${role.createdDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
<%if(permuid||permuname){ %>              
<td><%if(permuid){%>${role.lastEditedByUserId.username}, <%}%><%if(permuname){%>${role.lastEditedByUserId.firstName}<%}%></td>
<%} %>
            <td><fmt:formatDate value="${role.lastEditedDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td> 
        </tr>
       <tr id="${role.roleId}rl" style="display: none;" >
       <td></td>
       <td colspan="80" class="dvwinner">
       	<table>
           		 <thead>
				       <tr>
				           <th>Permission</th>
				           <th>Description</th>
				       </tr>
				   </thead>
				   <c:forEach items="${role.permissions}" var="perm">
	           		<tr>
	           		 <td><c:out value="${perm.permissionname}"></c:out></td>
	            	 <td><c:out value="${perm.description}"></c:out></td>
	           		</tr>
	           		</c:forEach>
           		</table>
       </td></tr>
  </c:forEach>
  </tbody>
</table>
</div>


