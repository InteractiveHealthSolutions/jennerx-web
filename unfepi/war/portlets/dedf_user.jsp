<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.model.User"%>

<script type="text/javascript">
<!--

function subfrm(){
	var isAdmin = <%=UserSessionUtils.getActiveUser(request).isDefaultAdministrator()%>;
	if(!isAdmin){
		var selrol=document.getElementById("userrole").value;
		if(selrol==''){
			alert("User must have a role");
			return;
		} 
	}
				
	submitThisForm();
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}

//-->
</script>

<form method="post" id="frm" name="frm" >
<table class="denform-h">
         <tr>
             <td>Project Id : <span class="mendatory-field">*</span></td>
             <td>${command.idMapper.identifiers[0].identifier}
             </td>
         </tr>
         <tr>
             <td>First Name  : <span class="mendatory-field">*</span></td>
             <td>
             <spring:bind path="command.firstName">
             <input type="text" name="firstName" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
			 </spring:bind>
			</td>
		</tr>
         <tr>
             <td>Last Name   : </td>
             <td>
             <spring:bind path="command.lastName">
             <input type="text" name="lastName" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
			</spring:bind>
			</td>
        </tr>
        <tr>
            <td>Email: <span style="font-size: small;">(Your passwords will be sent to this email.)</span><span class="mendatory-field">*</span></td>
            <td>
            <spring:bind path="command.email">
            <input type="text" name="email" value="<c:out value="${status.value}"/>"/>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
			</spring:bind>
			</td>
        </tr>
<c:if test="${isUserAllowedToEdit == true}">
        <%@ include file="plt_user_status.jsp" %>
		<tr>
			<td></td>
			<td><input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();"></td>
		</tr>
</c:if>
<c:if test="${not isUserAllowedToEdit}">
<tr>
<td colspan="2" class="error">The user has more permissions than yours. Ask your manager to edit the user</td>
</tr>
</c:if>
	</table>

</form>
