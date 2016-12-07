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

function isDigitCharHyphenSpace(e){
	var charCode = (e.which) ? e.which : e.keyCode;
	if ((charCode >= 48 && charCode <= 57) ||
			(charCode >= 65 && charCode <= 90) || 
			(charCode >= 97 && charCode <= 122) || 
			(charCode == 32) || (charCode == 45)) {
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
		
				<spring:bind path="command.itemRecordNum">
					<input id="itemRecordNum" name="itemRecordNum" value="${status.value}" type="hidden"/><br>
				</spring:bind>
		
		<tr>
			<td>Name<span class="mendatory-field">*</span></td>
			<td><spring:bind path="command.name">
					<input id="itemName" name="name" value="${status.value}" maxlength="30" onkeypress="return isDigitCharHyphenSpace(event);"/><br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		
		<tr>
			<td>Quantity/Pack<span class="mendatory-field">*</span></td>
			<td><spring:bind path="command.unit_per_pack">
					<input id="item_per_pack" name="unit_per_pack" value="${status.value}" maxlength="6" onkeypress="return isDigit(event);"/><br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		
		<tr>
			<td>Quantity<span class="mendatory-field">*</span></td>
			<td><spring:bind path="command.quantity">
					<input id="itemQuantity" name="quantity" value="${status.value}" maxlength="6" onkeypress="return isDigit(event);"/><br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>

		<tr>
			<td></td>
			<td><input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();"></td>
		</tr>
	</table>

</form>