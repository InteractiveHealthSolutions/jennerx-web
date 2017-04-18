
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.model.Model.VaccineEntity"%>
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
	<table class="denform-h">

		<tr>
			<td>Name<span class="mendatory-field">*</span></td>
			<td colspan="2"><spring:bind path="command.name">
					<input id="vaccineName" name="name" value="${status.value}" maxlength="15" style="width: 4.94cm" /> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>Short Name<span class="mendatory-field">*</span></td>
			<td colspan="2"><spring:bind path="command.shortName">
					<input id="vaccineShortName" name="shortName" value="${status.value}" maxlength="10" style="width: 4.94cm" /> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>Full Name</td>
			<td colspan="2"><spring:bind path="command.fullName">
					<input id="vaccineFullName" name="fullName" value="${status.value}" maxlength="30" style="width: 4.94cm" /> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>min Grace Period Days</td>
			<td colspan="2"><spring:bind path="command.minGracePeriodDays">
					<input id="minGracePeriodDays" name="minGracePeriodDays" value="${status.value}" maxlength="2" 
					onkeypress="return isDigit(event);" style="width: 4.94cm" /> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>max Grace Period Days</td>
			<td colspan="2"><spring:bind path="command.maxGracePeriodDays">
					<input id="maxGracePeriodDays" name="maxGracePeriodDays" value="${status.value}" maxlength="2" 
					onkeypress="return isDigit(event);" style="width: 4.94cm" /> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		
		<tr>
		<td>Description</td>
		<td><spring:bind path="command.description">
			<textarea name="description" maxlength="255"style="width: 4.95cm">${status.value}</textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</tr>
		<tr>
		<td>Vaccine Entity<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.vaccine_entity">
			<select id="vaccine_entity" name="vaccine_entity" style="width: 5.14cm">
				<c:forEach items="<%=VaccineEntity.values()%>" var="vac_entity">
				<c:if test="${fn:contains(vac_entity, 'CHILD')}">
					<option value="${vac_entity}">${vac_entity}</option>
				</c:if>
				</c:forEach>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		
		</td>
		</tr>
		
		<!--  -->
		
		<tr>
		<td>Is Attenuated<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.isAttenuated">
			<select id="isAttenuated" name="isAttenuated" style="width: 5.14cm">
				<option></option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>" >Yes</option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>">No</option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</tr>
		
		<tr>
		<td>Number of doses<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.numberOfDoses">
			<input type="number" id="numberOfDoses" name="numberOfDoses" style="width: 5cm" min="0"/>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</tr>
		
		<tr>
			<td></td>
        	<td><input type="button"  id="submitBtn" value="Submit Data" onclick="subfrm();"></td>
        </tr>
		<script type="text/javascript">
			$(function() {
				$('#vaccinePrerequisites').multiselect({
				});
			});
		</script>

	</table>
</form>