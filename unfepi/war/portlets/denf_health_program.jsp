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
		
<!-- 		<tr><td colspan="2" height="10px" style="border: none;"></td></tr> -->
		<tr>
			<td>Name<span class="mendatory-field">*</span></td>
			<td><input type="text" name="name" value="${command.name}" style="width:219px" onkeypress="return isChar(event);"/></td>
		</tr>
		<tr>
			<td>Description</td>
			<td><textarea rows="2" cols="29" name="description">${command.description}</textarea></td>
		</tr>
<!-- 		<tr> -->
<!-- 			<td>Enrollment Limit</td> -->
<!-- 			<td><input type="number" name="enrollmentLimit" min="0" onkeypress="return isDigit(event);"/></td> -->
<!-- 		</tr> -->
		
<!-- 		<tr><td colspan="2" height="10px" style="border: none;"></td></tr> -->

		<tr>
			<td>Sites<span class="mendatory-field">*</span></td>
			<td><select multiple="multiple" id="vaccinationCenters" name="vaccinationCenters" class="requiredField">
					<c:forEach items="${vaccinationCenters}" var="center">
						<option id="${center.mappedId}" value="${center.mappedId}"> ${center.name}</option>
					</c:forEach>
			</select></td>
		<script type="text/javascript">
		
			$(function() {
				var selected_vc = ${centers_selected}
				console.log($.type(selected_vc));
				if($.type(selected_vc) != 'undefined'){
	 				$.each(selected_vc, function(index, value){
					$("#vaccinationCenters option").each(function(){
						 if (value == $(this).attr("id").replace(/\D/g,"")){
							 $(this).attr('selected', 'selected');
						  }
						});
					});
				}
				
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
