<%@page import="org.ird.unfepi.model.Model"%>
<%@page import="org.ird.unfepi.model.Model.ContactType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
<!--
// window.onload = onloadSettingOfControls;

function onloadSettingOfControls() {
	// cellNumberOwnerRelationshipOtherSelected();
}

function subfrm(){
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
        <td colspan="2" class="headerrow">${entityRole}</td>
    </tr>
	<tr>
    	<td>Project ID : </td>
        <td>
   			<input type="hidden" id="programId" name="programId" value="${programId}" />
   			<input type="hidden" id="entityRole" name="entityRole" value="${entityRole}" />
   			
           	<span class="hltext1"><c:out value="${programId}"/></span>
        </td>
    </tr>
	<tr><td colspan="2"><span class="datenote">(donot assign Contact type PRIMARY if one already exists)</span></td></tr>
	<tr>
		<td>Number Type<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.numberType">
			<select	id="numberType" name="numberType" bind-value="${status.value}">
					<c:forEach items="<%=ContactType.values()%>" var="cty">
						<option >${cty}</option>
					</c:forEach>
			</select>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Number</td>
		<td><spring:bind path="command.telelineType">
			<input type="radio" name="telelineType" <c:if test="${status.value == 'MOBILE'}">checked = "checked"</c:if> value="MOBILE"/>Mobile<br>
			<input type="radio" name="telelineType" <c:if test="${status.value == 'LANDLINE'}">checked = "checked"</c:if> value="LANDLINE"/>Landline
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<spring:bind path="command.number">
			<input type="text" id="number" name="number" maxlength="15" value="${status.value}" />
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
        <td></td>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
        </td>
    </tr>
</table>
</form>