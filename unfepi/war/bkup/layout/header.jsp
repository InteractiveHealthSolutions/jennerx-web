<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<html>
<head>
<title>UNF-EPI</title>
<meta name="description" content="website description">
<meta name="keywords" content="website keywords, website keywords">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="themes/default/easyui.css?v=<%=WebGlobals.VERSION_CSS_JS %>">
<link rel="stylesheet" href="themes/icon.css?v=<%=WebGlobals.VERSION_CSS_JS %>">
<link rel="stylesheet" href="css/themes/base/jquery.ui.all.css?v=<%=WebGlobals.VERSION_CSS_JS %>">
<link rel="stylesheet" type="text/css" href="css/style.css?v=<%=WebGlobals.VERSION_CSS_JS %>">

<!-- modernizr enables HTML5 elements and feature detects -->
<script type="text/javascript" src="js/modernizr-1.5.min.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script type="text/javascript" src="gen_validatorv31.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script type="text/javascript" src="date.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script type="text/javascript" src="immunization.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script type="text/javascript" src="js/jquery-1.7.1.min.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script type="text/javascript" src="js/jquery-ui-timepicker-addon.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script type="text/javascript" src="js/jquery-ui-sliderAccess.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script src="css/ui/jquery.ui.core.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script src="css/ui/jquery.ui.widget.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>
<script type="text/javascript" src="dwrloader.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>  
<script type="text/javascript" src="galleria/galleria-1.2.9.min.js?v=<%=WebGlobals.VERSION_CSS_JS %>"></script>

  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRAdminTaskService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRChildService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRDailySummaryService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWREntityService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRLotteryGeneratorService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRCommunicationService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRStorekeeperService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRUserService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRVaccinationCenterService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRVaccinatorService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRVaccineService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRValidationService.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>

  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  <script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js?v=<%=WebGlobals.VERSION_CSS_JS %>'></script>
  
<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/WEB-INF/layout/include.jsp"%>
</head>
<script type="text/javascript">
<!--
var pwdpopupWindow=null;
var centerWidth = (window.screen.width - 600) / 2;
var centerHeight = (window.screen.height - 400) / 2;

function changepwdpopup()
{
	$('#winpwd').window('open');
	return;
	pwdpopupWindow = window.open('changepwd.htm','change password','width=600,height=400,left='+centerWidth+',top='+centerHeight+',resizable=no,toolbar=no,location=no,scrollbars=no,directories=no,status=no,menubar=no,copyhistory=no');
	pwdpopupWindow.focus();
}

function parent_disable() {
if(pwdpopupWindow && !pwdpopupWindow.closed)
	pwdpopupWindow.focus();
}

//-->
</script>
<body onFocus="parent_disable();" onclick="parent_disable();">
<!-- start page container (main) -->
  <div id="main">

<!-- start header -->
    <header>
	<div id="topcornermenu">
	<!-- <span style="color:red;font-size:40px;font-weight: bolder; ">TEST APP</span> -->
			<a onclick="changepwdpopup();">ChangePassword</a> |
			<a href="logout">Logout</a>
			<p style="color: white;font: medium bolder wider"><c:out value="${sessionScope.fullname}" /></p>
			<div id="winpwd"><%@ include file="/portlets/plt_change_pwd.jsp.jsp"%></div>
<script type="text/javascript"><!--
$('#winpwd').window({
	title: 'Change password',
	width: 600,  
    height: 400,
    minimizable: false,
    maximizable: false,
    draggable: false,
    closed: true,
    modal:true
}); 
//--></script>
	</div>
	<div id="logo">
        <div id="logo_text">
          <h1><span class="logo_simple" >UNF</span><span class="logo_decorated">EPI</span></h1>
          <h4>get every child fortified</h4>
        </div>
      </div>
      
<%@ include file="/WEB-INF/layout/navigation.jsp"%>
</header>
<!-- end header -->

<!-- start body div (site_content) -->
<div id="site_content">
