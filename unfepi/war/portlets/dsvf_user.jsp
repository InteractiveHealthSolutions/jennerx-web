<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<script type="text/javascript"><!--

function resetUserPwd(obj){
	var id=obj.title;
    var pwd=getRandomPwd();
	if(confirm("The password will be reset to \""+pwd+"\" for user with id \""+id+"\". You will not be able to undo the action.")){
	document.getElementById("udiv").style.display="block";
	document.getElementById("utable").style.display="none";
	
	DWRUserService.resetPassword(id, pwd,true,{
		async: false,
		callback: function (res2) {
			alert(res2);
			document.getElementById("udiv").style.display="none";
			document.getElementById("utable").style.display="block";
		}});
	}
}
function getRandomPwd(){
	var str="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	var randomnumber=Math.floor(Math.random()*4);
	var pstr="";
	for (i = 0; i < 6+randomnumber; i++) {
		pstr= pstr+str.charAt(Math.floor(Math.random()*str.length));
	}
	return pstr;
}
//-->
</script>
<div id="udiv" style="font-size: large;display: none;">
<br>
.<br>
.<br>
.<br>
<br><br>
.................... Wait !!!!     while sending email ..................
<br><br>
<br>
.<br>
.<br>
.<br>
</div>

<div class="dvwform" id="utable" style="display: block;">

	<table>
	<thead>
        <tr>
<%
boolean edituser=UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_USERS, request);
if(edituser){ 
%>      		
       		<th></th>
<%} %>       		
<%
boolean resetpwd=UserSessionUtils.hasActiveUserPermission(SystemPermissions.RESET_USER_PASSWORD, request);
if(resetpwd){ 
%>      		
       		<th>reset<br>pwd</th>
<%} %>
			<th>Program Id</th>
			<th>Location</th>
            <th>Login Id</th>
            <th>Name</th>
            <th>Role</th>
            <th>Status</th>
            <th>Email</th>
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
        </tr>
    </thead>
      <tbody>
      <c:forEach items="${model.datalist}" var="u">
   		 <tr>	
<%
if(edituser){ 
%>	
<c:choose>
<c:when test='${fn:toLowerCase(u.username) == "admin" || fn:toLowerCase(u.username) == "administrator"}'>
<% 
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){
%>
<td><a href="edituser.htm?editRecord=${u.idMapper.identifiers[0].identifier}" class="linkiconS iconedit"></a></td>
<%
}else{
%>
<td></td>
<%} %>
</c:when>
<c:otherwise>
<td><a href="edituser.htm?editRecord=${u.idMapper.identifiers[0].identifier}" class="linkiconS iconedit"></a></td>
</c:otherwise>
</c:choose>
<%
}
%>            
<%
if(resetpwd){ 
%>	
<c:choose>
<c:when test='${fn:toLowerCase(u.username) == "admin" || fn:toLowerCase(u.username) == "administrator"}'>
<% 
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){
%>
   	 		<td><a onclick="resetUserPwd(this);" title="${u.username}" class="linkiconS iconresetpwd"></a></td>
<%
}else{
	%>
	<td></td>
	<%} %>
</c:when>
<c:otherwise>
   	 		<td><a onclick="resetUserPwd(this);" title="${u.username}" class="linkiconS iconresetpwd"></a></td>
</c:otherwise>
</c:choose>
<%
}
%>
			<td><c:out value="${u.idMapper.identifiers[0].identifier}"></c:out></td>
			<td><c:out value="${u.idMapper.identifiers[0].location.name}"></c:out></td>
            <td><c:out value="${u.username}"></c:out></td>
            <td><c:out value="${u.firstName} ${u.middleName} ${u.lastName}"></c:out></td>
            <td class="lowercase"><c:out value="${u.idMapper.role.rolename}"></c:out></td>
            <td><c:out value="${u.status}"></c:out></td>
            <td><c:out value="${u.email}"></c:out></td>
<%if(permuid||permuname){ %>   
<td><%if(permuid){%>${u.createdByUserId.username}, <%}%><%if(permuname){%>${u.createdByUserId.firstName}<%}%></td>
<%} %>
            <td><fmt:formatDate value="${u.createdDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
       </tr>
  </c:forEach>
  </tbody>
</table>
</div>


