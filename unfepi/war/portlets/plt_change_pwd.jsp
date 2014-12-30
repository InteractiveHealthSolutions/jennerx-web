<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<div class="headerwindow"></div>
<script type="text/javascript">
<!--
function change() {

	showMsg("Please wait while changing password....");
	opwd=document.getElementById("opwd").value;
	npwd=document.getElementById("npwd").value;
	cpwd=document.getElementById("cpwd").value;
	if(opwd!='' && npwd!='' && cpwd !=''){
		if(confirm("Are you sure you want to change password ?")){
			document.getElementById("changebtn").style.disabled=true;
			DWRUserService.changePassword(opwd,npwd,cpwd,success);
		}else{
			return;
		}
	}else{
		alert('No value specified in either of password boxes');
	}
}
var success=function (msg) {
	document.getElementById("opwd").value="";
	document.getElementById("npwd").value="";
	document.getElementById("cpwd").value="";
	
	showMsg(msg);
	document.getElementById("changebtn").style.disabled=false;
	
};
function showMsg(msg){
	document.getElementById("messageDiv").innerHTML="<p><span style=\"color:green\">"+msg+"</span></p>";
}
//-->
</script>
<div id="messageDiv"></div>
<table class="denform-h">
<tr>
   <td colspan="2" class="headerrow">Change your password</td>
</tr>
  <tr>
    <td>Old Password</td>
    <td><input id="opwd" name="opwd" type="password" maxlength="15"/></td>
  </tr>
  <tr>
    <td>New Password</td>
    <td><input id="npwd" name="npwd" type="password" maxlength="15"/></td>
  </tr>
  <tr>
    <td>Confirm New Password</td>
    <td><input id="cpwd" name="cpwd" type="password" maxlength="15"/></td>
  </tr>
  <tr>
    <td></td>
    <td><input id="changebtn" type="button" value="Change" onclick="change();"/></td>
  </tr>
</table>
