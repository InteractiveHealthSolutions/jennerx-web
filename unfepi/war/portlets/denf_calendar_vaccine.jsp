
<%@include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.model.Model.TimeIntervalUnit"%>

<script type="text/javascript">
function isDigit(e) {
	var charCode = (e.which) ? e.which : e.keyCode;
	if ((charCode >= 48 && charCode <= 57)) {
		return true;
	}
	return false;
}

function subfrm() {
	
	if($('#vaccinationCalendarId').val().length <= 0 || $('#vaccineId').val().length <= 0){
		alert("fill all the mendatory fields");
		return false;
	}
	
	var emptyGapCount = 0;
	var emptyGapFieldCount = 0;
	
	$("input[id^='gap_in'").each(function(index, element){		
		if($(this).prop('checked')){
			var id = (element.id).match(/\d+/g);
			if($('#gap_value'+id).val().length <= 0 || $('#gap_unit'+id).val().length <= 0 ){
				emptyGapFieldCount++;
				emptyGapCount++ ;
			}
		}
	});
	
	if(emptyGapCount > 0 && $('#vaccinePrerequisites').val() == null){
		alert("fill/select atleast one gap or prerequisite");
		return false;
	}
	
	if(emptyGapCount > 0 && emptyGapFieldCount > 0 ){
		alert("fill the selected gap values ");
		return false;
	}
	
	submitThisForm();
}
function submitThisForm() {
	document.getElementById("frm").submit();
}

function enableGapValueTimeUnit(element) {

	var id = (element.id).match(/\d+/g);
	if (element.checked) {
		$('.gap' + id).prop("disabled", false);
		$('.const_gap' + id).prop("disabled", false);
		
		$("#vaccineId_Id_"+id).val($("#vaccineId").val());
		$("#vaccinationcalendarId_Id_"+id).val($("#vaccinationCalendarId").val());
		
	} else {
		$('.gap' + id).prop("disabled", true).val("");
		$('.const_gap' + id).prop("disabled", true);
	}
}

$(function(){
	
	$("input[class^='gap'], select[class^='gap'], input[class^='const_gap']").prop("disabled", true);
	$('input[type="checkbox"]').prop('checked', false); 
	$("input[class^='gap']", "select[class^='gap']").val("");
	$("input[class^='gap']", "select[class^='gap']").removeAttr('value');
		
		$("#vaccinationCalendarId , #vaccineId").change(
				function() {
					$("input[class^='gap'], select[class^='gap']").val("");
					$("input[class^='gap'], select[class^='gap'], input[class^='const_gap']").prop("disabled", true);
					$('input[type="checkbox"]').prop('checked', false); 
					
					$("#vaccinePrerequisites").multiselect("uncheckAll");
				});
		
		var selected_pr = ${preReq_selected}
		console.log($.type(selected_pr));
		if($.type(selected_pr) != 'undefined'){
			$.each(selected_pr, function(index, value){
				$("#vaccinePrerequisites option").each(function(){
					 if (value == $(this).attr("id")){
						 $(this).attr('selected', 'selected');
					  }
					});
				});
		}
});

</script>

<form method="post" id="frm" name="frm">
	<table class="denform-h">
		<tr>
			<td>Vaccination Calendar<span class="mendatory-field">*</span></td>
			<td colspan="3">
			<spring:bind path="command.vaccinationCalendarId">
				<select id="vaccinationCalendarId" name="vaccinationCalendarId" bind-value="${status.value}">
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
			<td colspan="3">
			<spring:bind path="command.vaccineId">
				<select id="vaccineId" name="vaccineId"	bind-value="${status.value}">
					<option></option>
					<c:forEach items="${vaccineList}" var="vaccine">
						<option value="${vaccine.vaccineId}">${vaccine.name}</option>
					</c:forEach>
				</select>
				<span class="error-message"><c:out value="${status.errorMessage}" /></span>
			</spring:bind></td>
		</tr>
		
		<tr>
			<td colspan="4" class="headerrow"></td>
		</tr>
		
		<c:forEach items="${vaccineGapTypeList}" var="vaccineGapType" varStatus="varstatus">
		<tr>
			<td><span>${vaccineGapType.name}</span></td>
			
			<td>
				<input type="checkbox" id="gap_in${varstatus.index}" onclick="enableGapValueTimeUnit(this)" />
			</td>
			
			<td>
			<input type="text" id="gap_value${varstatus.index}" name="vaccineGapList[${varstatus.index}].value" class="gap${varstatus.index}"
			 	   pattern="\d{2}" maxlength="2" onkeypress="return isDigit(event);" value=""/></td>
			
			<td>
			<select id="gap_unit${varstatus.index}" name="vaccineGapList[${varstatus.index}].gapTimeUnit" class="gap${varstatus.index}">
				<option></option>
				<c:forEach items="<%=TimeIntervalUnit.values()%>" var="timeInterval">
					<option>${timeInterval}</option>
				</c:forEach></select></td>		
		</tr>
		
		<input type="hidden" name="vaccineGapList[${varstatus.index}].id.vaccineGapTypeId" class="const_gap${varstatus.index}" value="${vaccineGapType.vaccineGapTypeId}"/>
		<input type="hidden" name="vaccineGapList[${varstatus.index}].id.vaccineId" class="gap${varstatus.index}" id="vaccineId_Id_${varstatus.index}" value=""/>
		<input type="hidden" name="vaccineGapList[${varstatus.index}].id.vaccinationcalendarId" class="gap${varstatus.index}" id="vaccinationcalendarId_Id_${varstatus.index}" value="" />
		
		</c:forEach>
		
		<tr>
			<td colspan="4" class="headerrow"></td>
		</tr>
		
		<tr>
			<td>Prerequisites</td>
			<td colspan="3"><spring:bind path="command.vaccinePrerequisites">
			<select multiple="multiple" id="vaccinePrerequisites" name="vaccinePrerequisites" >
					<c:forEach items="${vaccineList}" var="prereq">
						<option id="${prereq.vaccineId}" value="${prereq.vaccineId}">${prereq.name}</option>
					</c:forEach>
			</select>
			<span class="error-message"><c:forEach items="${status.errorMessages}" var="m_error">
			- ${m_error}<br></c:forEach></span>
			</spring:bind></td>
			<script type="text/javascript">
				$(function() {
					$('#vaccinePrerequisites').multiselect({});
				});
			</script>
		</tr>
		
		<tr>
			<td colspan="4" class="headerrow"></td>
		</tr>
		
		<tr>
			<td colspan="3"></td>
			<td><input type="button" id="submitBtn" value="Submit Data" onclick="return subfrm();"></td>
		</tr>
	</table>
</form>