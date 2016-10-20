
<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
<!--
function isDigit(e) {
	var charCode = (e.which) ? e.which : e.keyCode;
	if ((charCode >= 48 && charCode <= 57)) {
		return true;
	}
	return false;
}
function subfrm(){
	submitThisForm();
}

function submitThisForm() {
	document.getElementById("frm").submit();
}
//-->
</script>

<form method="post" id="frm" name="frm">
<br>
	<table class="denform-h">
		<tr>
			<td>Name<span class="mendatory-field">*</span></td>
			<td colspan="2"><spring:bind path="command.name">
					<input id="vaccineName" name="vaccine.name" value="${status.value}" maxlength="15" readonly="readonly"/> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>Short Name<span class="mendatory-field">*</span></td>
			<td colspan="2"><spring:bind path="command.shortName">
					<input id="vaccineShortName" name="vaccine.shortName" value="${status.value}" maxlength="15"/> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>Full Name</td>
			<td colspan="2"><spring:bind path="command.fullName">
					<input id="vaccineFullName" name="vaccine.fullName" value="${status.value}" maxlength="30"/> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>min Grace Period Days</td>
			<td colspan="2"><spring:bind path="command.minGracePeriodDays">
					<input id="minGracePeriodDays" name="vaccine.minGracePeriodDays" value="${status.value}" maxlength="2" onkeypress="return isDigit(event);"/> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>max Grace Period Days</td>
			<td colspan="2"><spring:bind path="command.maxGracePeriodDays">
					<input id="maxGracePeriodDays" name="vaccine.maxGracePeriodDays" value="${status.value}" maxlength="2" 
					onkeypress="return isDigit(event);" /> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
		<td>Description</td>
		<td><spring:bind path="command.description">
			<textarea name="description" maxlength="255"></textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</tr>
		<tr>
			<td></td>
        	<td><input type="button"  id="submitBtn" value="Submit Data" onclick="subfrm();"></td>
        </tr>
	</table>
</form>