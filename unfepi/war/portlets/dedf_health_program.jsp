<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
<!--

function isChar(e) {
	var charCode = (e.which) ? e.which : e.keyCode;
	if ((charCode >= 65 && charCode <= 90)
			|| (charCode >= 97 && charCode <= 122) || (charCode == 32)) {
		return true;
	}
	return false;
}

function isDigit(e) {
	var charCode = (e.which) ? e.which : e.keyCode;
	if ((charCode >= 48 && charCode <= 57)) {
		return true;
	}
	return false;
}

function subfrm(){	
	
	if($.isEmptyObject($('#vaccinationCenters').val())){
		alert('select vaccination center\(s\) for the program');
		return;
	}
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
		<tr><td colspan="2">
		<spring:hasBindErrors name="command">
			<c:forEach var="error" items="${errors.allErrors}">
				<p style="font-style: italic; color: red; font-size: small;text-align: center;"><spring:message message="${error}" /></p>
			</c:forEach>
		</spring:hasBindErrors>
		</td></tr>
		
		<tr><td colspan="2" height="10px" style="border: none;"></td></tr>
		<tr>
			<td>Name<span class="mendatory-field">*</span></td>
			<td><input type="text" name="name" onkeypress="return isChar(event);" value="${command.name}"/></td>
		</tr>
		<tr>
			<td>Description</td>
			<td><textarea rows="2" cols="20" name="description">${command.description}</textarea></td>
		</tr>
		<tr>
			<td>Enrollment Limit</td>
			<td><input type="number" name="enrollmentLimit" min="0" onkeypress="return isDigit(event);" value="${command.enrollmentLimit}"/></td>
		</tr>
		
		<tr><td colspan="2" height="10px" style="border: none;"></td></tr>

		<tr>
			<td>Vaccination Centers<span class="mendatory-field">*</span></td>
			<td><select multiple="multiple" id="vaccinationCenters" name="vaccinationCenters" class="requiredField">
					<c:forEach items="${vaccinationCenters}" var="center">
						<c:forEach items="${centerProgram}" var="prog">
							<c:if test="${prog.vaccinationCenter.mappedId == center.mappedId && prog.isActive == true}">
								<c:set var="found" value="true" />
							</c:if>
						</c:forEach>
						<option value="${center.mappedId}" <c:if test="${found}">selected="selected"</c:if>>
							${center.idMapper.identifiers[0].identifier} : ${center.name}</option>
							<c:remove var="found"/>
					</c:forEach>
			</select></td>
			<script type="text/javascript">
				$(function() {
					$('#vaccinationCenters').multiselect({
					// 				selectedList: '${fn:length(vaccinationCenters)}'
					});
				});
			</script>
		</tr>
		<tr><td colspan="2" height="10px" style="border: none;"></td></tr>
		<tr>
			<td colspan="2"><input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();"></td>
		</tr>
	</table>
	
</form>
