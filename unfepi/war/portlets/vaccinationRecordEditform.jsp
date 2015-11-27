<%@page import="org.ird.unfepi.utils.validation.REG_EX"%>
<%@page import="org.ird.unfepi.model.Vaccination.VACCINATION_STATUS"%>
<%@page	import="org.ird.unfepi.model.Vaccination"%>
<%@page import="org.ird.unfepi.model.Child"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">
<!--
var nextVaccStatus;
var nextVaccine;
var isfirstVaccination;

window.onbeforeunload = function (ev) {
	  var e = ev || window.event;
	  // For IE and Firefox
	  if (e) {
	    e.returnValue = '';
	  }
	  // For Safari
	  return '';
};

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

window.onload = onloadSettingOfControls;

function onloadSettingOfControls() {
	centerChanged();
}
function subfrm(){
	var stPending = '<%=Vaccination.VACCINATION_STATUS.PENDING%>';
	var stVacc = '${command.vaccinationStatus}';

	if(stVacc != stPending && !vaccinationCommonValidations(document)){
		return;
	}
	
	var vaccinationduedate=Date.parseExact(document.getElementById("vaccinationDuedate").value,globalDOf);
	if(vaccinationduedate == null){
		vaccinationduedate=Date.parseExact(document.getElementById("vaccinationDuedate").value,globalDTf);
	}
	
	if(getPrevVaccinationDate() != null && vaccinationduedate.compareTo(getPrevVaccinationDate()) == -1){
		alert('Vaccination due date should not be less than previous vaccination`s vaccination date');
		return;
	}
	
	if(getBirthdate() == null){
		alert('Birthdate not found.');
		return;
	}
	
	var curvacc = getTextSelectedInDD(document.getElementById("vaccineId"));
	if(getVaccinationDate() != null && !validateBirthdateVaccinationGap(getBirthdate(), getVaccinationDate(), curvacc)){
		return;
	}
	
	submitThisForm();
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}

function getBirthdate() {
	try{
		var birthdate=Date.parseExact(document.getElementById("birthdateinh").value,globalDOf);

		if(birthdate == null){
			birthdate=Date.parseExact(document.getElementById("birthdateinh").value,globalDTf);
		}
		return birthdate;
	}
	catch (e) {
		return null;
	}
}

function getPrevVaccinationDuedate() {
	try{
		var vaccinationduedate=Date.parseExact(document.getElementById("prevVaccDuedate").value,globalDOf);
		if(vaccinationduedate == null){
			vaccinationduedate=Date.parseExact(document.getElementById("prevVaccDuedate").value,globalDTf);
		}
		return vaccinationduedate;
	}
	catch (e) {
		return null;
	}
}

function getPrevVaccinationDate() {
	try{
		var vaccinationDate=Date.parseExact(document.getElementById("prevVaccDate").value,globalDTf);
		if(vaccinationDate == null){
			vaccinationDate=Date.parseExact(document.getElementById("prevVaccDate").value,globalDOf);
		}
		
		return vaccinationDate;
	}
	catch (e) {
		return null;
	}
}

function getNxtVaccinationDuedate() {
	try{
		var vaccinationduedate=Date.parseExact(document.getElementById("nextVaccDuedate").value,globalDOf);
		if(vaccinationduedate == null){
			vaccinationduedate=Date.parseExact(document.getElementById("nextVaccDuedate").value,globalDTf);
		}
		return vaccinationduedate;
	}
	catch (e) {
		return null;
	}
}

function getNxtVaccinationDate() {
	try{
		var vaccinationDate=Date.parseExact(document.getElementById("nextVaccDate").value,globalDTf);
		if(vaccinationDate == null){
			vaccinationDate=Date.parseExact(document.getElementById("nextVaccDate").value,globalDOf);
		}
		
		return vaccinationDate;
	}
	catch (e) {
		return null;
	}
}

function getVaccinationDuedate() {
	var vaccinationduedate=Date.parseExact(document.getElementById("vaccinationDuedate").value,globalDOf);
	if(vaccinationduedate == null){
		vaccinationduedate=Date.parseExact(document.getElementById("vaccinationDuedate").value,globalDTf);
	}
	return vaccinationduedate;
}

function getVaccinationDate() {
	var vaccinationDate = null;
	try{
		vaccinationDate=Date.parseExact(document.getElementById("vaccinationDate").value,globalDTf);
		if(vaccinationDate == null){
			vaccinationDate=Date.parseExact(document.getElementById("vaccinationDate").value,globalDOf);
		}
	}
	catch (e) {
		
	}
	return vaccinationDate;
}

//-->
</script>
<form method="post" id="frm" name="frm">
<span id="pageHeadingTextDiv" style="color: #DE6DF7">Vaccination <span style="font-size: medium;">(Edit)</span></span><br>

<span class="error-message">${errorMessage}</span> 
<span class="error-message">${errorMessagev}</span>
<table class = "mobileForms" style="outline-color : #DE6DF7">
<c:set var="vaccStPen" value="<%=Vaccination.VACCINATION_STATUS.PENDING.toString()%>"></c:set>
    <tr>
    	<td colspan="2"><%@ include file="paletteVaccinationBiographicInfo.jsp" %></td>
    </tr>
	<tr>
        <td colspan="2"><%@ include file="paletteVaccinationPrevious.jsp" %></td>
    </tr>
	<tr>
        <td colspan="2" class="separator-heading">CURRENT VACCINATION DETAILS  <span style="font-size: small;color: maroon;">(${command.vaccinationRecordNum} : ${command.vaccinationStatus})</span></td>
    </tr>
    		
		<%@ include file="plt_global_errors.jsp" %>
		
<tr>
<td>Due date of vaccination</td>
<td>
	 <spring:bind path="command.vaccinationDuedate">
       <input id="vaccinationDuedate" name="vaccinationDuedate" readonly="readonly" value="<fmt:formatDate value="${command.vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>"  class="calendarbox"/>
	   <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
	</spring:bind>
<c:choose>	
<c:when test="${command.vaccinationStatus == vaccStPen}"><!-- is editable for pending vaccinations only -->
	<div class="mobileFormsInfoDiv">(Warning: editing this date could reschedule all pending reminders)</div>
	<script>
		$(function() {
		    $('#vaccinationDuedate').datepicker({
		    	duration: '',
		        constrainInput: false,
		        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
		     });
		    
		    document.getElementById("vaccinationDuedate").readOnly = false;
		});
	</script>
</c:when>
<c:otherwise>
	<div class="mobileFormsInfoDiv">Editable only for status ${vaccStPen}</div>
	<script>document.getElementById("vaccinationDuedate").readOnly = true;</script>
</c:otherwise>
</c:choose>
</td>
</tr>
	<tr>
		<td>Vaccination Center<span class="mendatory-field">*</span></td>
		<td>
		<c:choose>
		<c:when test="${command.vaccinationStatus != vaccStPen}">
		<spring:bind path="command.vaccinationCenterId">
			<input type="hidden" id="vaccinationCenterIdinh" name="vaccinationCenterIdinh" value="${status.value}"/>
			<input type="hidden" id="previousVaccinationCenterinh" name="previousVaccinationCenterinh" value="${sessionScope[prevvaccsessvar].vaccinationCenterId}">
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
               
               function centerChanged(){
            	   var prevEpiNum = document.getElementById("previousEpiNumberinh").value;
            		
            	   if(document.getElementById("vaccinationCenterId").value != document.getElementById("previousVaccinationCenterinh").value){
            		   document.getElementById("epiNumber").value = '';
            		   document.getElementById("epiNumber").readOnly = false;
            	   }
            	   else if(trim(prevEpiNum) != ''){
            		   document.getElementById("epiNumber").value = prevEpiNum;
            		   document.getElementById("epiNumber").readOnly = true;
            	   }
            	   else{
            		   document.getElementById("epiNumber").readOnly = false;
            	   }
               }
            //-->
            </script>
        </c:when>
        <c:otherwise><div class="mobileFormsInfoDiv">N/A for status ${vaccStPen}</div></c:otherwise>
        </c:choose>
		</td>
	</tr>
	<tr>
		<td>Vaccinator<span class="mendatory-field">*</span></td>
		<td>
		<c:choose>
		<c:when test="${command.vaccinationStatus != vaccStPen}">
		<spring:bind path="command.vaccinatorId">
			<input type="hidden" id="vaccinatorIdinh" name="vaccinatorIdinh" value="${status.value}" /> 
			<input type="hidden" id="previousVaccinatorinh" name="previousVaccinatorinh" value="${sessionScope[prevvaccsessvar].vaccinatorId}">
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
        </c:when>
        <c:otherwise><div class="mobileFormsInfoDiv">N/A for status ${vaccStPen}</div></c:otherwise>
        </c:choose>
		</td>
	</tr>
	<tr>
		<td>If center changed, please provide newly assigned EPI card Number<span class="mendatory-field">*</span></td>
		<td>
		<c:choose>
		<c:when test="${command.vaccinationStatus != vaccStPen}">
			<spring:bind path="command.epiNumber">
				<input type="hidden" id="previousEpiNumberinh" name="previousEpiNumberinh" value="${sessionScope[prevvaccsessvar].epiNumber}">
				<input type="text" id="epiNumber" name="epiNumber" maxlength="8" value="<c:out value="${status.value}"/>" readonly="readonly"/>
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</c:when>
		<c:otherwise><div class="mobileFormsInfoDiv">N/A for status ${vaccStPen}</div>
        </c:otherwise>
        </c:choose>
		</td>
</tr>
<tr>
	<td>Which vaccine scheduled for the day?<span class="mendatory-field">*</span></td>
	<td>
	<spring:bind path="command.vaccineId">
	<select id="vaccineId" name="vaccineId">
		<option value="${vaccine.vaccineId}">${vaccine.name}</option>
	</select>
	<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
	</spring:bind>
	</td>
</tr>
<tr>
	<td>Date of Vaccination/Visit<span class="mendatory-field">*</span></td>
    <td>
    <c:choose>
	<c:when test="${command.vaccinationStatus != vaccStPen}">
	 <div class="mobileFormsInfoDiv" style="width: 200px">
		(Be very careful if editing this date as if next vaccination 
		is pending then the duedate of next vaccination would need to be updated as well as 
		reminders will be rescheduled. Otherwise if next vaccination 
		have incurred only this date will be updated.)</div>
        <spring:bind path="command.vaccinationDate">
        <input type="hidden" id="vaccinationDateinh" value="<fmt:formatDate value="${command.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>" >
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
		        onClose: handleVaccinationDate
		     });
		});
	function handleVaccinationDate() 
	{
   		if(getVaccinationDate() == null){
			return;
		}
   		
   		var curVaccine = getTextSelectedInDD(document.getElementById("vaccineId"));
   		if(!validateBirthdateVaccinationGap(getBirthdate(), getVaccinationDate(), curVaccine)){
			document.getElementById("vaccinationDate").value = document.getElementById("vaccinationDateinh").value;
   			return;
   		}
   		
   		if(getNxtVaccinationDuedate() != null && (getVaccinationDate()).compareTo(getNxtVaccinationDuedate()) == 1){
			if(!confirm('Vaccination date can not be after next vaccination`s duedate. Ignore anyway?')){
   				document.getElementById("vaccinationDate").value = document.getElementById("vaccinationDateinh").value;
				return;
			}
   		}
   		
   		if(getVaccinationDate().compareTo(Date.parse('June 20th 2012')) == -1){
			document.getElementById("vaccinationDate").value = document.getElementById("vaccinationDateinh").value;
			alert('Vaccination date can not be before start date of project(June 20th, 2012)');
			return;
		}

   		var prevMileStoneDate=getVaccinationDate();
   		var vaccCenterID = document.getElementById("vaccinationCenterId").value;

		if(getNxtVaccinationDuedate() != null && nextVaccStatus == '<%=VACCINATION_STATUS.PENDING%>'){
			if(nextVaccine.toLowerCase().indexOf('measles2') != -1)
			{
				DWRVaccineService.calculateMeasles2DateFromMeasles1(
						getBirthdate()
						,getVaccinationDate()
						,vaccCenterID
						,curVaccine
						,nextVaccine
						,{
						async: false,
						callback: nextDateCallback
						});
			}
			else 
			{
				if(nextVaccine.toLowerCase().indexOf('measles1') != -1
						|| nextVaccine.toLowerCase().indexOf('penta1') != -1)
				{
					prevMileStoneDate = getBirthdate();
				}

				DWRVaccineService.calculateNextVaccinationDateFromPrev(
					prevMileStoneDate
					,getVaccinationDate()
					,vaccCenterID
					,nextVaccine
					,false
					,{
					async: false,
					callback: nextDateCallback
					});
			}
		}
	}
	
	function nextDateCallback (recievedvcdate) {
		if('${command.isFirstVaccination}'){
			document.getElementById("vaccinationDuedate").value = document.getElementById("vaccinationDate").value;
		}
		
		alert('Donot forget to change next vaccintion duedate to '+recievedvcdate.toString(globalDOf));
		document.getElementById("currnextvaccinationduedate").innerHTML=
			"<span style=\"text-decoration: line-through\">"+document.getElementById("currnextvaccinationduedate").innerHTML+"</span>";
		document.getElementById("editednextvaccinationduedate").innerHTML="***"+recievedvcdate.toString(globalDOf);
	}
	</script>
</c:when>
<c:otherwise><div class="mobileFormsInfoDiv">N/A for status ${vaccStPen}</div></c:otherwise>
</c:choose>
   	</td>
</tr>
<tr>
	<td>Polio vaccine given? <span class="mendatory-field">*</span></td>
	<td>
	<c:choose>
	<c:when test="${command.vaccinationStatus != vaccStPen}">
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
	</c:when>
	<c:otherwise><div class="mobileFormsInfoDiv">N/A for status ${vaccStPen}</div></c:otherwise>
    </c:choose>
	</td>
</tr>
<tr>
	<td>Was PCV given today? <span class="mendatory-field">*</span></td>
	<td>
	<c:choose>
	<c:when test="${command.vaccinationStatus != vaccStPen}">
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
	</c:when>
	<c:otherwise><div class="mobileFormsInfoDiv">N/A for status ${vaccStPen}</div></c:otherwise>
    </c:choose>
	</td>
</tr>
<%--     <tr>
		<td>Cell Number</td>
		<td>
			<input type="text" id="reminderCellNumber" name="reminderCellNumber" maxlength="15" value="${reminderCellNumber}" />
		</td>
	</tr>
	<tr>
		<td>SMS reminders?<span class="mendatory-field">*</span></td>
		<td>
			<input type="radio" name="reminderPreference" <c:if test='${not empty reminderPreference && reminderPreference == true}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"/>Yes<br>
			<input type="radio" name="reminderPreference" <c:if test='${not empty reminderPreference && reminderPreference == false}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>No
		</td>
	</tr>--%>
	<tr>
		<td>Lottery?<span class="mendatory-field">*</span></td>
		<td>
	<c:choose>
	<c:when test="${command.vaccinationStatus != vaccStPen}">
			<select id="hasApprovedLottery" name="hasApprovedLottery" onchange="hasApprovedLotteryChanged(this);">
			<c:if test="${command.hasApprovedLottery == true}">
			<option value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>">Yes</option>
			</c:if>
			<c:if test="${command.hasApprovedLottery == false}">
				<option value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>">No</option>
			</c:if>
			<c:if test="${empty command.hasApprovedLottery}">
				<option value="">Don`t Know</option>
			</c:if>
			</select>
	</c:when>
	<c:otherwise><div class="mobileFormsInfoDiv">N/A for status ${vaccStPen}</div>
    </c:otherwise>
    </c:choose>
		</td>
	</tr>
<tr>
		<td>Additional Note</td>
		<td><spring:bind path="command.description">
			<textarea name="description" maxlength="255">${status.value}</textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
</tr>
<tr>
    <td colspan="2"><%@ include file="paletteVaccinationNext.jsp" %>
<input id = "prevVaccDate" type="hidden" value="<fmt:formatDate value="${sessionScope[prevvaccsessvar].vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>">
<input id = "prevVaccDuedate" type="hidden" value="<fmt:formatDate value="${sessionScope[prevvaccsessvar].vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>">
<input id = "nextVaccDate" type="hidden" value="<fmt:formatDate value="${sessionScope[nextvaccsessvar].vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>">
<input id = "nextVaccDuedate" type="hidden" value="<fmt:formatDate value="${sessionScope[nextvaccsessvar].vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>">
<script type="text/javascript">
<!--
nextVaccStatus = '${sessionScope[nextvaccsessvar].vaccinationStatus}';
nextVaccine = '${sessionScope[nextvaccsessvar].vaccine.name}';
//-->
</script>
    
</td>
</tr>
<tr>
        <td></td>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
<c:if test="${shouldenableVaccination == false}">
	<script type="text/javascript"><!-- 
	document.getElementById("submitBtn").disabled=true; 
	//-->
	</script>
</c:if>
        </td>
</tr>
</table>
</form>
