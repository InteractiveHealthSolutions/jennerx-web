<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">
function subfrm(){
	submitThisForm();
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
</script>
<form method="post" id="frm" name="frm" >
<table class="denform-h">
	<tr>
		<td>Parent Location</td>
        <td>
       <input name="parentLocation" id="cc" class="easyui-combotree" style="width:250px;"/>
<script type="text/javascript">
$( document ).ready(function() {
$('#cc').combotree({
    required: true,
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
		<td>Location Type :<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.locationType">
			<select id="locationType" name="locationType" bind-value="${status.value}">
				<c:forEach items="locationTypes" var="locationType_value"  >
					<option>${locationType_value}</option>
				</c:forEach>
			</select>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
    <tr>
        <td>Name(unique and identifiable): <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.name">
             <input type="text" id="name" name="name" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td>Full Name : <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.fullName">
             <input type="text" id="fullName" name="fullName" maxlength="50" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
	<tr>
		<td>Additional Note</td>
		<td><spring:bind path="command.description">
			<textarea name="description" maxlength="255"></textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
    <tr>
        <td>
        <input type="button"  id="submitBtn" value="Submit Data" onclick="subfrm();" align="right" style="width: 300px;">
        </td>
        <td>
        </td>
    </tr>
</table>
</form>
