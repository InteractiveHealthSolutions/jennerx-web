
<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
$(function(){
	
	
	
	if($('#calenderId').val().length != 0 && $('#vaccineId').val().length != 0){
		getSites();	
	}
	
	function getSites(){
		$.get( "addchild/siteList/"+$('#healthProgramId').val()+".htm" , function( data ) {
			
		});
	}
});

</script>


<form method="post" id="frm" name="frm">
<br>
	<table class="denform-h">
			<tr>
	    	<td>Vaccination Calendar<span class="mendatory-field">*</span></td>
			<td colspan="2"><spring:bind path="command.vaccinationCalendar.calenderId">
		            <select id="calenderId" name="vaccinationCalendar.calenderId" bind-value="${status.value}" class="requiredField">
		                <option></option>
		                <c:forEach items="${vaccinationCalendarList}" var="calendar"> 
		                <option value="${calendar.calenderId}">${calendar.shortName}</option>
		            	</c:forEach> 
		            </select>
	            	<span class="error-message"><c:out value="${status.errorMessage}" /></span> 
	            </spring:bind></td>
		</tr>
		
			<tr>
	    	<td>Vaccine<span class="mendatory-field">*</span></td>
			<td colspan="2"><spring:bind path="command.vaccine.vaccineId">
		            <select id="vaccineId" name="vaccine.vaccineId" bind-value="${status.value}" class="requiredField">
		                <option></option>
		                <c:forEach items="${vaccineList}" var="vaccine"> 
		                <option value="${vaccine.vaccineId}">${vaccine.name}</option>
		            	</c:forEach> 
		            </select>
	            	<span class="error-message"><c:out value="${status.errorMessage}" /></span> 
	            </spring:bind></td>
		</tr>
		
		<tr><td colspan="3" class="headerrow"></td></tr>

		<c:forEach items="${vaccineGapTypeList}" var="vaccineGapType">
			<tr>
				<td>${vaccineGapType.name}</td>
				<td><input maxlength="2" onkeypress="return isDigit(event);" /></td>
				<td><select>
						<option></option>
						<option>DAYS</option>
						<option>WEEKS</option>
						<option>MONTHS</option>
						<option>YEARS</option>
				</select></td>
			</tr>
		</c:forEach>
		
		<tr><td colspan="3" class="headerrow"></td></tr>

		<tr>
			<td>Prerequisites</td>
			<td colspan="2"><select multiple="multiple"
				id="vaccinePrerequisites" name="vaccinePrerequisites">
					<c:forEach items="${vaccineList}" var="prereq">
						<option value="${prereq.vaccineId }">${prereq.name }</option>
					</c:forEach>
			</select></td>
			<script type="text/javascript">
			$(function() {
				
				$('#vaccinePrerequisites').multiselect({
				});
			});
		</script>
		</tr>
		
		<tr><td colspan="3" class="headerrow"></td></tr>
		
		<tr>
			<td colspan="2"></td>
        	<td><input type="button"  id="submitBtn" value="Submit Data" onclick="subfrm();"></td>
        </tr>
	</table>
</form>