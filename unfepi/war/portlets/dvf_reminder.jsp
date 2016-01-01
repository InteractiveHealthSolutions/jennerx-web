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
boolean editremperm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_REMINDERS, request);
if(editremperm){ 
%>        		
       		<th></th>
<%
}
%>        		
            <th>Reminder Name</th>
            <th>Reminder Type</th>
            <th>Description</th>
<%-- <%
boolean permuid=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_USERID, request);
boolean permuname=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_USERNAME, request);
if(permuid||permuname){ 
%>            
            <th>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th>
<%
}
%>  --%>           
            <th>Created On</th>
<%-- <%
if(permuid||permuname){ 
%>            
           <th>Last Editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th>
<%
}
%>   --%>           
            <th>Last Updated</th>
        </tr>
    </thead>
      <tbody>
         <c:forEach items="${model.datalist}" var="rem">
   		 <tr>	
   	 		<td><input type="text" readonly="readonly" value="+" title="${rem.reminderId}r" class="expandDataButton" onclick="expandRecord(this);"/></td>
<%
if(editremperm){ 
%> 
            <td><a href="editreminder.htm?editRecord=${rem.reminderId}" class="linkiconS iconedit"></a></td>
<%}
%>            
            <td><c:out value="${rem.remindername}"></c:out></td>
            <td><c:out value="${rem.reminderType}"></c:out></td>
            <td><c:out value="${rem.description}"></c:out></td>
<%-- <%if(permuid||permuname){ %>              
<td><%if(permuid){%>${rem.createdByUserId.username},<%}%><%if(permuname){%>${rem.createdByUserId.firstName}<%}%></td>
<%} %> --%>
            <td><fmt:formatDate value="${rem.createdDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
<%-- <%if(permuid||permuname){ %>   
<td><%if(permuid){%>${rem.lastEditedByUserId.username},<%}%><%if(permuname){%>${rem.lastEditedByUserId.firstName}<%}%></td>
<%} %> --%>
            <td><fmt:formatDate value="${rem.lastEditedDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td> 
       </tr>
       <tr id="${rem.reminderId}r" style="display: none;" >
       <td colspan="80" class="dvwinner">
		   <c:forEach items="${rem.reminderText}" var="rtext">
	           		 <div>${rtext}</div>
       	</c:forEach>
      </td>
      </tr>
  </c:forEach>
  </tbody>
</table>
</div>


