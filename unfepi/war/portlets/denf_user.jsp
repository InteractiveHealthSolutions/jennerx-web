<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.model.User"%>

<script type="text/javascript">
<!--

function subfrm(){
	var programId = document.getElementById("projectId").value;
	if(programId == ''){
		alert("Project ID not specified.");
		return;
	}
	
	var reg = /^[0-9]+/;
	if(reg.test(programId)!=true || programId.length < 3 || programId.length > 14){
		alert('Please specify a valid numeric and 3 to 14 digits programId');
		return;
	}
	
	var selrol=document.getElementById("userrole");
	if(selrol.value==''){
		alert("User must have a role");
		return;
	} 
	var pwd1=document.getElementById("password").value;
	var pwd2=document.getElementById("cpassword").value;
	if(pwd1!=pwd2){
		alert("Passwords donot match");
		return;
	}
	DWRUserService.isIdMapperExists(programId,{
		async: false,
		callback: function (res2) {
			if(res2){
				alert('Cannot submit data for given ID. Check if another user is mapped on same ID');
				return;
			}
				
		submitThisForm();
	}});
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}

//-->
</script>

<form method="post" id="frm" name="frm" >
<table class="denform-h">

		<tr><td colspan="2" class="columnHeadingDiv">
		<div class="headingS">ID PATTERNS:</div><br>
		<div>VACCINATOR: </div>vvvvvss     (vaccination center + serial)<br>
		<div>OTHER USER:  </div>1ss        (1 + serial)<br>
		</td></tr>
		
         <tr>
             <td>Project Id : <span class="mendatory-field">*</span></td>
             <td>
             <input type="text" id ="projectId" name="projectId" value="<c:out value="${projectId}"/>"/>
             </td>
         </tr>
         <tr>
		<td>Location <br>(Must specify if user should have location oriented privileges)</td>
        <td>
       <input name="userLocation" id="cc" class="easyui-combotree" style="width:250px;"/>
<script type="text/javascript">
$( document ).ready(function() {
$('#cc').combotree({
    loader: treeDataLoaderLocations
    });
    
});

function treeDataLoaderLocations(parentId){
	//alert(JSON.stringify(parentId));
	DWREntityService.getLocationHierarchy({"parentId": (isNaN(parentId)?"":parentId)}, 
			{callback: function(result) {
				$('#cc').combotree('clear');
				$('#cc').combotree('loadData' ,result);
			}, async: false, timeout: 5000});
}
</script>
    	</td>
	</tr>
         <tr>
             <td>User Name(login id) : <span class="mendatory-field">*</span></td>
             <td>
             <spring:bind path="command.username">
             <input type="text" name="username" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
			</spring:bind>
             </td>
         </tr>
         <tr>
             <td>Password       : <span class="mendatory-field">*</span></td>
             <td>
             <spring:bind path="command.clearTextPassword">
             <input type="password" id="password" name="clearTextPassword" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
             </spring:bind>
			</td>
         </tr>
         <tr>
             <td>Confirm Password       : <span class="mendatory-field">*</span></td>
             <td>
             <input type="password" id="cpassword" name="cpassword" value=""/><br/>
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
        
        <%@ include file="plt_user_status.jsp" %>

		<tr>
			<td></td>
			<td><input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();"></td>
		</tr>
	</table>
</form>
