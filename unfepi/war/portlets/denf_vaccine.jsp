
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
	<table class="denform-h">

		<tr>
			<td>Name<span class="mendatory-field">*</span></td>
			<td colspan="2"><spring:bind path="command.name">
					<input id="vaccineName" name="name" value="${status.value}" maxlength="15"/> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>Short Name<span class="mendatory-field">*</span></td>
			<td colspan="2"><spring:bind path="command.shortName">
					<input id="vaccineShortName" name="shortName" value="${status.value}" maxlength="10"/> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>Full Name</td>
			<td colspan="2"><spring:bind path="command.fullName">
					<input id="vaccineFullName" name="fullName" value="${status.value}" maxlength="30"/> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>min Grace Period Days</td>
			<td colspan="2"><spring:bind path="command.minGracePeriodDays">
					<input id="minGracePeriodDays" name="minGracePeriodDays" value="${status.value}" maxlength="2" onkeypress="return isDigit(event);"/> <br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind></td>
		</tr>
		<tr>
			<td>max Grace Period Days</td>
			<td colspan="2"><spring:bind path="command.maxGracePeriodDays">
					<input id="maxGracePeriodDays" name="maxGracePeriodDays" value="${status.value}" maxlength="2" 
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
		
<!-- 		<tr> -->
<!-- 	    	<td>Vaccination Calendar<span class="mendatory-field">*</span></td> -->
<%-- 			<td colspan="2"><spring:bind path="command.vaccinationCalendar.calenderId"> --%>
<%-- 		            <select id="calenderId" name="vaccinationCalendar.calenderId" bind-value="${status.value}" class="requiredField"> --%>
<!-- 		                <option></option> -->
<%-- 		                <c:forEach items="${vaccinationCalendarList}" var="calendar">  --%>
<%-- 		                <option value="${calendar.calenderId}">${calendar.shortName}</option> --%>
<%-- 		            	</c:forEach>  --%>
<!-- 		            </select> -->
<%-- 	            	<span class="error-message"><c:out value="${status.errorMessage}" /></span>  --%>
<%-- 	            </spring:bind></td> --%>
<!-- 		</tr> -->
		
<!-- 		<tr><td colspan="3" class="headerrow"></td></tr> -->
        
<%-- 		<c:forEach items="${vaccineGapTypeList}" var="vaccineGapType"> --%>
<!-- 			<tr> -->
<%-- 			<td>${vaccineGapType.name}</td> --%>
<!-- 			<td><input maxlength="2" onkeypress="return isDigit(event);" /></td> -->
<!-- 			<td><select> -->
<!-- 				<option></option> -->
<!-- 				<option>DAYS</option> -->
<!-- 				<option>WEEKS</option> -->
<!-- 				<option>MONTHS</option> -->
<!-- 				<option>YEARS</option> -->
<!-- 				</select></td> -->
<!-- 			</tr>	     -->
<%-- 	    </c:forEach>  --%>
<!-- 		<tr><td colspan="3" class="headerrow"></td></tr> -->
		
		<tr>
			<td></td>
        	<td><input type="button"  id="submitBtn" value="Submit Data" onclick="subfrm();"></td>
        </tr>
       
<!--         <tr> -->
<!-- 			<td>Prerequisites</td> -->
<!-- 			<td colspan="2"> -->
<!-- 			<select multiple="multiple" id="vaccinePrerequisites" name="vaccinePrerequisites"> -->
<%-- 				<c:forEach items="${vaccineList}" var="prereq">		 --%>
<%-- 					<option value="${prereq.vaccineId }">${prereq.name }</option> --%>
<%-- 				</c:forEach> --%>
<!-- 			</select></td> -->
		<script type="text/javascript">
		
			$(function() {
				
				$('#vaccinePrerequisites').multiselect({
				});
			});
		</script>
<!-- 		</tr> -->

	</table>
</form>