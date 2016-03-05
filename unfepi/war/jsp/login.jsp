
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@ include file="/WEB-INF/template/headersimple.jsp"%>

<%
	if (UserSessionUtils.isUserSessionActive(request)) {
%>
<c:redirect url="mainpage.htm"></c:redirect>
<%
	}
%>
<div class="centerdivh" style="margin-top: 15%;">
<form method="post">
    <h4 class="formheadingL" style="margin-bottom: 50px">Welcome to Zindagi Mehfooz Study</h4>
		<table class="denform-s" style="width: 35%">
			<tr>
				<td class="headerrow" colspan="2">Log in</td>
			</tr>
			<tr>
				<td colspan="2"><span class="error" style="font-size: x-small; color: red"> <c:out value="${logmessage}"/>
				</span></td>
			</tr>
			<tr>
				<td >Username:</td>
				<td>
				<spring:bind path="credentials.username">
					<input type="text" name="username" value="" />
					<span class="error" style="font-size: x-small; color: red"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
			</tr>
			<tr>
				<td >Password:</td>
				<td >
				<spring:bind path="credentials.password">
					<input type="password" name="password" />
					<span class="error" style="font-size: x-small; color: red"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
			</tr>
			<tr>
	        <td></td>
	        <td>
	        <input type="submit" id="submitBtn" value="Login" style="width: 100px">
	        </td>
	    </tr>
		</table>
    <!-- <div style="padding:0px; margin:0px; margin-top:-60px; margin-right: 300px;float: right; transform: rotate(60deg);-ms-transform: rotate(60deg); /* IE 9 */ -webkit-transform: rotate(60deg); /* Safari and Chrome */">
    <div style="margin: 0 auto; background: url(gallery/unfepi01.jpg) no-repeat; background-size: 250px 120px; width: 150px;height: 60px;"></div>
    <div style="margin: 0 auto; background: url(gallery/unfepi13.jpg) no-repeat; background-size: 100px 80px; width: 100px;height: 70px;"></div>
    <div style="margin: 0 auto; background: url(images/unfepi44.jpg) no-repeat; background-size: 200px 500px; width: 180px;height: 500px;"></div>
    <div style="margin: 0 auto; background: url(gallery/unfepi12.jpg) no-repeat; background-size: 100px 70px; width: 80px;height: 70px;"></div>
    <div style="margin: 0 auto; width: 2px;height: 70px; background: black;"></div>
 -->  
</form>
</div>
<script type="text/javascript">
 document.getElementById('username').focus();
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>