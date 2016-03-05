<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
<!--
function addPerm(){
	var allPer=document.getElementById("allPerms");
	var selPer=document.getElementById("selectedPerms");
	for (i=0; i<allPer.options.length; i++) {
		if (allPer.options[i].selected) {
	        var opt = document.createElement("option");
	        
	        opt.text = allPer.options[i].text;
	        opt.value = allPer.options[i].value;
	        // Add an Option object to Drop Down/List Box
	        try{
	        document.getElementById("selectedPerms").options.add(opt,null);
	        allPer.remove(i,null);//stndrd
	        }catch (e) {
	        	document.getElementById("selectedPerms").options.add(opt);
		        allPer.remove(i);//ie only
			}
	        i--;
		}
	}
}
function removePerm(){
	var allPer=document.getElementById("allPerms");
	var selPer=document.getElementById("selectedPerms");
	for (i=0; i<selPer.options.length; i++) {
		if (selPer.options[i].selected) {
	        var opt = document.createElement("option");
	        
	        opt.text = selPer.options[i].text;
	        opt.value = selPer.options[i].value;
	        // Add an Option object to Drop Down/List Box
	        try{
	        document.getElementById("allPerms").options.add(opt,null);
	        selPer.remove(i,null);//stndrd
	        }catch (e) {
	        	document.getElementById("allPerms").options.add(opt);
		       	selPer.remove(i);//ie only
			}
	        i--;
		}
	}
}
function frmSubmit(){
	var r=document.getElementById("rolename").value.toLowerCase();
	if(r =="admin"||r=="administrator"||r=="role_administrator"){
		alert("Role can not have name '"+r+"'. Please choose some other name.");
		return;
	}
	var selPer=document.getElementById("selectedPerms");
	if(selPer.options.length<1){
		alert("Role must have atleast one permission");
		return;
	}

	for(var count=0; count < selPer.options.length; count++) {
		selPer.options[count].selected = true;
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
         <td>Role Name : <span class="mendatory-field">*</span></td>
         <td>
         <spring:bind path="command.rolename">
         <input type="text" id="rolename" name="rolename" maxlength="30" value="<c:out value="${status.value}"/>"/>
         <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
		 </spring:bind>
         </td>
     </tr>
     <tr>
         <td>Description : </td>
         <td>    
         <spring:bind path="command.description">
         <textarea name="description" maxlength="255" rows="4" cols="25"></textarea>
		<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
		</spring:bind>
		</td>
     </tr>
     <tr>
        <td colspan="2">
            <table>
            <tr>
            <td>
				<select id="selectedPerms" name="selectedPerms" multiple="multiple" class="listMedium"></select>
			</td>
			<td>
						<input type="text" readonly="readonly" value="&lt" class="expandDataButton" onclick="addPerm();"/>
						<br/>
						<input type="text" readonly="readonly" value="&gt" class="expandDataButton" onclick="removePerm();"/>
			</td>
			<td>
				<select id="allPerms" multiple="multiple" class="listMedium">
					<c:forEach items="${permissions}" var="perm">
						<option>${perm.permissionname}</option>
					</c:forEach>
				</select>
			</td>
			</tr>
			</table>
			</td>
        </tr>

        <tr>
        <td>
        <input type="button"  id="submitBtn" value="Submit" title="submit" onclick="frmSubmit();"/>
        </td>
        </tr>
</table>
</form>