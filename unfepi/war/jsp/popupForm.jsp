<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.DataForm"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.GlobalParams.ServiceType"%>
<%@page import="org.ird.unfepi.service.exception.UserServiceException" %>
<%@page import="org.ird.unfepi.utils.UserSessionUtils" %>
<%@page import="org.ird.unfepi.constants.SystemPermissions" %>

<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css?v=<%=WebGlobals.VERSION_CSS_JS %>">
<script type="text/javascript" src="js/jquery-1.7.1.min.js?v=${version_css_js}"></script>

</head>
<body>
<div class="sideline">
<div id="headercompact">
<div id="compactlogo">
</div>
<div class="windowheadingM">${model.dataFormObject.formTitle}</div>
</div>
<div id="content">
<c:set var="dataForm" value="${model.dataFormObject}"></c:set>
<c:choose>
<c:when test="${not empty dataForm}">
<%
boolean perm=false;
DataForm formReq = null;
try{
	formReq = (DataForm)pageContext.getAttribute("dataForm");
	perm=UserSessionUtils.hasActiveUserPermission(formReq.getPermission(),request);
}catch(UserServiceException e){
%>
<c:out value="${e}"></c:out>
<%
}
System.out.println(perm);
if(perm){ 
%>

<jsp:include page="/portlets/popdv_${fn:toLowerCase(dataForm.formName)}.jsp"/>


<%
}else{
%>
<%@ include file="pageDeniedAccess.jsp" %>
<%
}
%>
</c:when>
<c:otherwise><%@ include file="pageError.jsp" %></c:otherwise>
</c:choose>
</div>
</div>
</body>
</html>