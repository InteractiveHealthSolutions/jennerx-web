<%@page import="org.ird.unfepi.model.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.unfepi.utils.validation.REG_EX"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.model.Vaccination"%>

<script type="text/javascript">
<!--
window.onbeforeunload = function (e) {
	  var e = e || window.event;
	  // For IE and Firefox
	  if (e) {
	    e.returnValue = '';
	  }
	  // For Safari
	  return '';
	};

window.onload = onloadSettingOfControls;

function onloadSettingOfControls() {
	vaccinationStatusCheckChanged(document.getElementsByName("vaccinationStatusChk").item(0), false);
	nextvaccinationStatusCheckChanged(document.getElementsByName("nextvaccinationStatusChk").item(0));
}


var globalDTf="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>";
var globalDOf="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>";

function convertToDate(stringDate) {
	try{
		var datec=Date.parseExact(stringDate,globalDTf);
		if(datec == null){
			datec=Date.parseExact(stringDate,globalDOf);
		}
		
		return datec;
	}
	catch (e) {
		return null;
	}
}

function subfrm()
{
	if(!vaccinationCommonValidations(document)){
		return;
	}

	if(!followupReminderPreferenceValidation(document,/^<%=REG_EX.CELL_NUMBER%>$/)){
		return;
	}
	
	var programId = document.getElementById("programId").value;
	var vaccinationdateval = document.getElementById("vaccinationDate").value;
	var vaccinationduedateval = document.getElementById("vaccinationDuedate").value;
	var nextassigneddateval = document.getElementById("nextAssignedDate").value;
	var birthdateval = document.getElementById("birthdateinh").value;
	var curvacc = getTextSelectedInDD(document.getElementById("vaccineId"));
	var nextvacc = document.getElementById("nextVaccine").value;

	if(birthdateval == ''){
		alert('no birthdate found');
		return;
	}
	if(curvacc == ''){
		alert('current vaccine not specified');
		return;
	}
	if(vaccinationdateval == ''){
		alert('no vaccination date defined');
		return;
	}
	if(vaccinationduedateval == ''){
		alert('no vaccination due date defined');
		return;
	}
	
	if(convertToDate(vaccinationdateval).compareTo(convertToDate(vaccinationduedateval).add(-3).days()) == -1){
		alert('vaccination date can only be 3 days before assigned vaccination due date');
		return;
	}
	
	//For Current Vaccine
	if(!validateBirthdateVaccinationGap(convertToDate(birthdateval), convertToDate(vaccinationdateval), curvacc)){
		return;
	}
	
	if(document.getElementById("nextvaccinationStatusChk").checked != true)
	{
		if(nextvacc == null || nextvacc == ''){
			alert('no next vaccine specified');
			return;
		}
		if(nextvacc == curvacc){
			alert('next vaccine should not be equal to current vaccine');
			return;
		}
		
		if(nextassigneddateval == null || nextassigneddateval == ''){
			alert('no next vaccination date specified');
			return;
		}
	
		//For Next Vaccine
		if(!validateNextVaccinationGap(convertToDate(birthdateval), convertToDate(vaccinationdateval), convertToDate(nextassigneddateval), nextvacc)){
			return false;
		}
		
		if(convertToDate(nextassigneddateval).compareTo(convertToDate(vaccinationdateval)) == -1){
			alert('next vaccination date cannot be before current vaccine`s date');
			return;
		}
	}
	
	var selSt=document.getElementById("vaccinationStatus").value;
	if(selSt == "<%=Vaccination.VACCINATION_STATUS.PENDING.name()%>"){
		alert('Vaccination status should be changed from pending to a new one');
		return;
	}	

	DWRVaccineService.validatePrivilegedEntryNextVaccine(
			programId
			,curvacc
			,nextvacc
			,{
			async: false,
			callback: function (res) {
				if(res.toLowerCase()!='ok'){
					alert(res);
					return;
				}
				submitThisForm();
			}});
	
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
//-->
</script>
<form method="post" id="frm" name="frm" >
<span id="pageHeadingTextDiv" style="color: #DE6DF7">Followup <span style="font-size: medium;">(Admin)</span></span><br>

<span class="warning-message">${errorMessage}</span>
<span class="error-message">${errorMessagev}</span>
<table class = "mobileForms" style="outline-color : #DE6DF7">
    <tr>
    	<td colspan="2"><%@ include file="paletteVaccinationBiographicInfo.jsp" %></td>
    </tr>
    <c:set var="pndStatus" value="<%=Vaccination.VACCINATION_STATUS.PENDING %>"></c:set>
    <c:set var="prevvacclistsessvar" value="prevVaccList${sessionScope[childsessvar].idMapper.identifiers[0].identifier}"></c:set>
    <tr>
        <td colspan="2" class="separator-heading">OTHER VACCINATION DETAILS</td>
    </tr>
    <c:forEach items="${sessionScope[prevvacclistsessvar]}" var="prevvaccmap"> 
    <tr><td colspan="2">
   <table class="previousDataDisplay">
   	<tr>
		<td colspan="2" class="separator-heading">${prevvaccmap.previous_vaccination.vaccine.name}</td>
	</tr>
	<tr>
		<td>Vaccination Record Num </td>
		<td>${prevvaccmap.previous_vaccination.vaccinationRecordNum}  (${prevvaccmap.previous_vaccination.vaccinationStatus})</td>
	</tr>
	<tr>
		<td>Vaccination Due Date</td>
		<td><fmt:formatDate value="${prevvaccmap.previous_vaccination.vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
	</tr>
	<c:if test="${prevvaccmap.previous_vaccination.vaccinationStatus!=pndStatus}">
	<tr>
		<td>Vaccination Date</td>
		<td><fmt:formatDate value="${prevvaccmap.previous_vaccination.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>
	</tr>
	<tr>
		<td>Vaccination Center , Vaccinator</td>
		<td>${prevvaccmap.previous_vaccination_vcenter.idMapper.identifiers[0].identifier} : ${prevvaccmap.previous_vaccination_vcenter.name}
		 , ${prevvaccmap.previous_vaccination_vaccinator.idMapper.identifiers[0].identifier} : ${prevvaccmap.previous_vaccination_vaccinator.firstName}</td>
	</tr>
	<tr>
		<td>Epi Number</td>
		<td>${prevvaccmap.previous_vaccination.epiNumber}</td>
	</tr>
	</c:if>
	</table>
	</td>
	</tr>
    </c:forEach> 
	<tr>
        <td colspan="2" class="separator-heading">CURRENT VACCINATION DETAILS</td>
    </tr>
    		
		<%@ include file="plt_global_errors.jsp" %>
		
    <tr><td colspan="2"><spring:bind path="command">
    	<c:forEach items="${status.errorMessages}" var="error">
    	<span class="error-message" style="width: auto;">~ <c:out value="${error}"/><br>
    	</c:forEach></spring:bind></td>
	</tr>
    <tr>
	<td>Due date of vaccination<span class="mendatory-field">*</span></td>
	<td>
		<spring:bind path="command.vaccinationDuedate">
	    <input id="vaccinationDuedate" name="vaccinationDuedate" value="<fmt:formatDate value="${command.vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>"  class="calendarbox"/>
		<br><span class="datenote">(ex: 01-Jan-2000)</span>
		<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		<script>
			$(function() {
			    $('#vaccinationDuedate').datepicker({
			    	duration: '',
			        constrainInput: false,
			        maxDate: '+0d',
			        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
			     });
			});
		</script>
	</td>
	</tr>
	<tr>
		<td>Vaccination Center<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.vaccinationCenterId">
			<input type="hidden" id="vaccinationCenterIdinh" name="vaccinationCenterIdinh" value="${status.value}"/>
            <select id="vaccinationCenterId" name="vaccinationCenterId" onchange="centerChanged();">
               	<option></option>
            	<c:forEach items="${vaccinationCenters}" var="vcenter"> 
            	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier}:${vcenter.name}</option>
            	</c:forEach> 
            </select>
            <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
            <script><!--
               sel = document.getElementById("vaccinationCenterId");
               val=document.getElementById("vaccinationCenterIdinh").value;
               makeValueSelectedInDD(sel,val);
            //-->
            </script>    
		</td>
	</tr>
	<tr>
		<td>Vaccinator<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.vaccinatorId">
			<input type="hidden" id="vaccinatorIdinh" name="vaccinatorIdinh" value="${status.value}" /> 
            <select id="vaccinatorId" name="vaccinatorId">
                <option></option>
                <c:forEach items="${vaccinators}" var="vaccinator"> 
                <option value="${vaccinator.mappedId}">${vaccinator.idMapper.identifiers[0].identifier}:${vaccinator.firstName}</option>
            	</c:forEach> 
            </select>
            <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
            <script><!--
               sel = document.getElementById("vaccinatorId");
               val=document.getElementById("vaccinatorIdinh").value;
               makeValueSelectedInDD(sel,val);
            //-->
            </script>
		</td>
	</tr>
	<tr>
		<td>EPI card Number<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.epiNumber">
				<input type="text" id="epiNumber" name="epiNumber" maxlength="8" value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
</tr>
<tr>
	<td>What vaccine did the child recieve today? <span class="mendatory-field">*</span></td>
	<td>
	<spring:bind path="command.vaccineId">
	<input type="hidden" value="${status.value}" id="vaccineIdVal"/>
	<select id="vaccineId" name="vaccineId" >
		<option value=""></option>
		<c:forEach items="${vaccines}" var="vacc">
			<option value="${vacc.vaccineId}">${vacc.name}</option>
		</c:forEach>
	</select>
	<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
	</spring:bind>
	<spring:bind path="command.vaccinationStatus">
	<input type="hidden" id="vaccinationStatus" name="vaccinationStatus" value="<%=VACCINATION_STATUS.VACCINATED%>" />
	<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
	</spring:bind>
	<script type="text/javascript">
		<!--
		 	sel = document.getElementById("vaccineId");
       	 	val=document.getElementById("vaccineIdVal").value;
        	makeValueSelectedInDD(sel,val);
		//-->
		</script>
	</td>
</tr>
<tr>
	<td>Date of Vaccine<span class="mendatory-field">*</span></td>
       <td><spring:bind path="command.vaccinationDate">
       <input id="vaccinationDate" name="vaccinationDate" value="${status.value}" class="calendarbox"/>
       <br><span class="datenote">(ex: 01-Jan-2000)</span>
		<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
	<script>
		$(function() {
		   $('#vaccinationDate').datepicker({
		    	duration: '',
		        constrainInput: false,
		        maxDate: '+0d',
		        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
		     });
		});
	</script>
   	</td>
</tr>
<tr>
		<td>Was Polio vaccine given today? <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.polioVaccineGiven">
			<input type="hidden" id="polioVaccineGiveninh" name="polioVaccineGiveninh" value="${polioVaccineGiveninh}" /> 
			<select id="polioVaccineGiven" name="polioVaccineGiven" onchange="polioVaccineGivenChanged(this);">
				<option></option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>">Yes</option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>">No</option>
				<option value="">Don`t Know</option>
			</select>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		<script type="text/javascript">
			<!--
			sel = document.getElementById("polioVaccineGiven");
            val=document.getElementById("polioVaccineGiveninh").value;
           
            makeTextSelectedInDD(sel,val);
            
            function polioVaccineGivenChanged(sel) {
				document.getElementById("polioVaccineGiveninh").value = getTextSelectedInDD(sel);
			}
		//-->
		</script>
		</td>
</tr>
<tr>
		<td>Was PCV given today? <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.pcvGiven">
			<input type="hidden" id="pcvGiveninh" name="pcvGiveninh" value="${pcvGiveninh}" /> 
			<select id="pcvGiven" name="pcvGiven" onchange="pcvGivenChanged(this);">
				<option></option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>">Yes</option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>">No</option>
				<option value="">Don`t Know</option>
			</select>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		<script type="text/javascript">
			<!--
			sel = document.getElementById("pcvGiven");
            val=document.getElementById("pcvGiveninh").value;
           
            makeTextSelectedInDD(sel,val);
            
            function pcvGivenChanged(sel) {
				document.getElementById("pcvGiveninh").value = getTextSelectedInDD(sel);
			}
		//-->
		</script>		
		</td>
</tr>
<tr>
	<td>Next scheduled vaccine?<span class="mendatory-field">*</span></td>
	<td>
	<input type="hidden" value="${nextVaccine}" id="nxtvaccineIdVal"/>
		<select id="nextVaccine" name="nextVaccine">
				<option value=""></option>
				<c:forEach items="${vaccines}" var="vacc">
					<option value="${vacc.name}">${vacc.name}</option>
				</c:forEach>
		</select>
		<br><input type="checkbox" id="nextvaccinationStatusChk"  name="nextvaccinationStatusChk" onchange="nextvaccinationStatusCheckChanged(this);" <c:if test="${nextvaccinationStatusChk == 'false'}">checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>None/Already Scheduled
		<script type="text/javascript">
		<!--
		 	sel = document.getElementById("nextVaccine");
       	 	val=document.getElementById("nxtvaccineIdVal").value;
        	makeTextSelectedInDD(sel,val);
        	
        	function nextvaccinationStatusCheckChanged(radioChecked) {
				if(radioChecked.checked){
					document.getElementById("nextVaccine").value = '';
					document.getElementById("nextVaccine").disabled = true;
					document.getElementById("nextAssignedDate").value = '';
					document.getElementById("nextAssignedDate").disabled = true;
				}
				else{
					document.getElementById("nextVaccine").disabled = false;
					document.getElementById("nextAssignedDate").disabled = false;
				}
			}
		//-->
		</script>
	</td>
</tr>
<tr>
	<td>Allotted date of next visit<span class="mendatory-field">*</span></td>
       <td><spring:bind path="command.nextAssignedDate">
       <br><input id="nextAssignedDate" name="nextAssignedDate" value="${status.value}" class="calendarbox"/>
       <br><span class="datenote">(ex: 01-Jan-2000)</span>
		<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
	<script>
		$(function() {
		    $('#nextAssignedDate').datepicker({
		    	duration: '',
		        constrainInput: false,
		        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
		     });
		});
	</script>
   	</td>
</tr>
	<tr>
		<td>SMS reminders?<span class="mendatory-field">*</span></td>
		<td>
			<input type="radio" name="reminderPreference" <c:if test='${not empty reminderPreference && reminderPreference == true}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"/>Yes<br>
			<input type="radio" name="reminderPreference" <c:if test='${not empty reminderPreference && reminderPreference == false}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>No
	<!-- previous disabled preference -->		
<%-- 	<input type="radio" name="reminderPreference" <c:if test='${not empty reminderPreference && reminderPreference == false}'>checked = "checked"</c:if> <c:if test='${not empty prevreminderPreference && prevreminderPreference == true}'> disabled="disabled"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>No
 --%>		</td>
	</tr>
	<tr>
		<td>Cell Number</td>
		<td>
		<input type="text" id="reminderCellNumber" name="reminderCellNumber" maxlength="15" value="${reminderCellNumber}"/>
		<!-- previous disabled preference -->		
<%-- 	<input type="text" id="reminderCellNumber" name="reminderCellNumber" maxlength="15" value="${reminderCellNumber}" <c:if test='${not empty prevreminderPreference && prevreminderPreference == true}'> readonly="readonly"</c:if>/>
 --%>		
		</td>
	</tr>
	<tr>
		<td>Lottery?<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.hasApprovedLottery">
			<input type="hidden" id="hasApprovedLotteryinh" name="hasApprovedLotteryinh" value="${hasApprovedLotteryinh}" /> 
			<select id="hasApprovedLottery" name="hasApprovedLottery" onchange="hasApprovedLotteryChanged(this);">
				<option></option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>">Yes</option>
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>">No</option>
				<option value="">Don`t Know</option>
			</select>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		<script type="text/javascript">
			<!--
			sel = document.getElementById("hasApprovedLottery");
            val=document.getElementById("hasApprovedLotteryinh").value;
           
            makeTextSelectedInDD(sel,val);
            
            function hasApprovedLotteryChanged(sel) {
				document.getElementById("hasApprovedLotteryinh").value = getTextSelectedInDD(sel);
			}
		//-->
		</script>
		</td>
	</tr>
<%-- <tr>
		<td>Additional Note</td>
		<td><spring:bind path="command.description">
			<textarea name="description"></textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
</tr> --%>
<tr>
        <td></td>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
        <%
        	boolean shouldEnablevacc = true;
        	Object shouldEnable = request.getAttribute("shouldenableVaccination");
        	if (shouldEnable != null) {
        		shouldEnablevacc = (Boolean) shouldEnable;
        	}
        %>
        <script type="text/javascript">
        <!--
			if(<%=shouldEnablevacc%>){
				document.getElementById("submitBtn").disabled=false;
			} 
        //-->
        </script>
        </td>
</tr>
</table>
</form>