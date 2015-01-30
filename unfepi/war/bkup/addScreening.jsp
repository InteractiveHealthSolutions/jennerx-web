<%@ include file="/WEB-INF/layout/header.jsp" %>
<%@ include file="/WEB-INF/layout/sidebarChildren.jsp" %>
<%@ include file="/WEB-INF/layout/pagebody.jsp" %>

<input type="hidden" id="page_nav_id" value="_li6">


<%@page import="org.ird.unfepi.service.exception.UserServiceException" %>
<%@page import="org.ird.unfepi.utils.UserSessionUtils" %>
<%@page import="org.ird.unfepi.constants.SystemPermissions" %>
<%@page import="org.ird.unfepi.constants.WebGlobals" %>
<%
boolean perm=false;
try{
perm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.ADD_SCREENING,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perm){ 
%> 
<%@ include file="/portlets/screeningAddform.jsp" %>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to add screening. Contact your system administrator."></c:out>
</span>
<%
}
%>
<%@ include file="/WEB-INF/layout/endpagebody.jsp" %>
<%@ include file="/WEB-INF/layout/footer.jsp" %>