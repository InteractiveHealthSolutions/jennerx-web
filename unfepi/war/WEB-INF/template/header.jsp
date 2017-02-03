<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<!DOCTYPE html>
<html>
<head>
<title>MSF - JennerX</title>
<meta name="description" content="website description">
<meta name="keywords" content="website keywords, website keywords">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<%
	request.setAttribute("version_css_js", WebGlobals.VERSION_CSS_JS);
%>

<link rel="stylesheet" href="themes/gray/easyui.css?v=${version_css_js}">
<link rel="stylesheet" href="themes/icon.css?v=${version_css_js}">
<script type="text/javascript" src="js/jquery-1.7.1.min.js?v=${version_css_js}"></script>

<link rel="stylesheet" href="css/ui-lightness/jquery-ui-1.8.18.custom.css?v=${version_css_js}">
<link rel="stylesheet" type="text/css" href="css/style.css?v=${version_css_js}">

<script type="text/javascript" src="date.js?v=${version_css_js}"></script>
<script type="text/javascript" src="immunization.js?v=${version_css_js}"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js?v=${version_css_js}"></script>

<link rel="stylesheet" type="text/css" href="js/jquery.multiselect.css?v=${version_css_js}">
<script type="text/javascript" src="js/jquery.multiselect.min.js?v=${version_css_js}"></script>

<script type="text/javascript" src="js/jquery.easyui.min.js?v=${version_css_js}"></script>
<%-- <script type="text/javascript" src="dwrloader.js?v=${version_css_js}"></script> --%>

<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRAdminTaskService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRChildService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRDailySummaryService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWREntityService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRLotteryGeneratorService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRReportService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRCommunicationService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRStorekeeperService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRUserService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRVaccinationCenterService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRVaccinatorService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRVaccineService.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/DWRValidationService.js?v=${version_css_js}'></script>

<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js?v=${version_css_js}'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js?v=${version_css_js}'></script>

<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">

var pwdpopupWindow=null;
var centerWidth = (window.screen.width - 600) / 2;
var centerHeight = (window.screen.height - 400) / 2;

function changepwdpopup()
{
	jQuery('#winpwd').window('open');
	return;
	// pwdpopupWindow = window.open('changepwd.htm','change  password','width=600,height=400,left='+centerWidth+',top='+centerHeight+',resizable=no,toolbar=no,location=no,scrollbars=no,directories=no,status=no,menuba// r=no,copyhistory=no');
	// pwdpopupWindow.focus();
}

function parent_disable() {
if(pwdpopupWindow && !pwdpopupWindow.closed)
	pwdpopupWindow.focus();
}

var globalDTf="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>";
var globalDOf="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>";
	function convertToDate(stringDate) {
		try {
			var datec = Date.parseExact(stringDate, globalDTf);
			if (datec == null) {
				datec = Date.parseExact(stringDate, globalDOf);
			}

			return datec;
		} catch (e) {
			return null;
		}
	}
</script>

</head>
<body>
	<div id="wrapper">
		<div id="header">
			<div id="logo">
				<!--  <h4>get every child fortified</h4> -->
				<img alt="logo" src="./images/logo.png">
			</div>
			<div id="topcornermenu">
				<!-- <span style="color:red;font-size:40px;font-weight: bolder; ">TEST APP</span> -->
				<a onclick="changepwdpopup();">ChangePassword</a> | <a href="logout">Logout</a>
				| <span><c:out value="${sessionScope.fullname}" /></span>
				<div id="winpwd"><%@ include
						file="/portlets/plt_change_pwd.jsp"%></div>
				<script type="text/javascript">
				<!--
					jQuery('#winpwd').window({
						title : 'Change password',
						width : 600,
						height : 400,
						minimizable : false,
						maximizable : false,
						draggable : false,
						closed : true,
						modal : true
					});
				//-->
				</script>
			</div>
			<%@ include file="/WEB-INF/template/navigation.jsp"%>
		</div>
		<!--  End Header -->
		<div id="content">

			<div id="contentarea">