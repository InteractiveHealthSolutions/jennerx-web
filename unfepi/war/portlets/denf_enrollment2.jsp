<%@page import="org.directwebremoting.json.types.JsonArray"%>
<%@page import="java.util.List"%>
<%@page import="org.ird.unfepi.beans.EnrollmentWrapper"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule"%>
<%@page import="org.ird.unfepi.web.controller.AddChildController"%>
<%@include file="/WEB-INF/template/include.jsp"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.ird.unfepi.constants.FormType"%>
<%@page import="org.ird.unfepi.utils.validation.REG_EX"%>
<%@page import="org.ird.unfepi.model.Vaccination"%>
<%@page import="org.ird.unfepi.model.Vaccine"%>
<%@page import="org.ird.unfepi.model.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.unfepi.model.Model.ContactTeleLineType"%>
<%@page import="org.ird.unfepi.model.Child"%>
<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineScheduleKey"%>
<script type="text/javascript">
<!--
function isDateDigit(e){
	var charCode = (e.which) ? e.which : e.keyCode ; 
	if ((charCode >= 48 && charCode <=57) || (charCode == 45))
    {
    	return true;
    }
    return false;
}

function centerChanged(){}

//-->
</script>
<script type="text/javascript">
	$(function(){
		$('.tab-section').hide();
		
		$('#tabs a').click(function(event){
				
				if(this.id == 't1'){
					console.log("t1 clicked");
					$('#tabs a.current').removeClass('current');
					$('.tab-section:visible').hide();
					$(this.hash).show();
					$(this).addClass('current');
					event.preventDefault();
				}
				else if (this.id == 't2' || this.id == 't3'){
					var isEmptyFields = false;
					
					$(".requiredField").each(function(index, element) {
						if ((element.value.length == 0) && (element.value.replace(/\s+/g, "").length == 0)) {
							isEmptyFields = true ;
						}
					});

					if(isEmptyFields){
						alert('fill all the required fields first');
					}
					else{
						console.log('going to next page');
						
						if (this.id == 't3') {
							$(".current_vaccine").empty();
							sendVaccinationHistory();
						}
						
						$('#tabs a.current').removeClass('current');
						$('.tab-section:visible').hide();
						$(this.hash).show();
						$(this).addClass('current');
						event.preventDefault(); 
					}
				}		
				
				
			
		}).filter(':first').click();

	});
</script>

<ul id="tabs">
    <li><a id="t1" href="#tab1">biodata</a></li>
    <li><a id="t2" href="#tab2">vaccines history</a></li>
    <li><a id="t3" href="#tab3">current vaccines</a></li>
</ul>

<form method="post" id="frm" name="frm" >
<div id="tab1" class="tab-section" >
<table class="denform-h">
	<tr>
    	<td><spring:message code="label.enrollmentdate"/> <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.centerVisit.visitDate">
       	<!-- MUST be named as centerVisitDate: used in plt_vaccine_schedule for autopopulating date incase of status VACCINATED -->
       			<input id="centerVisitDate" name="centerVisit.visitDate" value="${status.value}" 
       				   maxDate="+0d" class="calendarbox requiredField" onclosehandler="centerVisitDateChanged" 
       				   onkeypress="return isDateDigit(event)" placeholder="dd-MM-yyyy"/>
       				   
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script type="text/javascript">
			</script>
    	</td>
    </tr>
    <tr>
    	<td><spring:message code="label.vaccinatorId"/><span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.centerVisit.vaccinatorId">
	            <select id="vaccinatorId" name="centerVisit.vaccinatorId" bind-value="${status.value}" class="requiredField">
	                <option></option>
	                <c:forEach items="${vaccinators}" var="vaccinator"> 
	                <option value="${vaccinator.mappedId}">${vaccinator.idMapper.identifiers[0].identifier} : ${vaccinator.firstName}</option>
	            	</c:forEach> 
	            </select>
            	<span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
		</td>
	</tr>
	<tr>
		<td><spring:message code="label.vaccinationCenter"/><span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.centerVisit.vaccinationCenterId">
	            <select id="vaccinationCenterId" name="centerVisit.vaccinationCenterId" bind-value="${status.value}" onchange="centerChanged();" class="requiredField">
	               	<option></option>
	            	<c:forEach items="${vaccinationCenters}" var="vcenter"> 
	            	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier} : ${vcenter.name}</option>
	            	</c:forEach> 
	            </select>
	            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
			<script type="text/javascript">
			</script>
		</td>
	</tr>
	<tr>
    	<td><spring:message code="label.childIdentifier"></spring:message><span class="mendatory-field">*</span></td>
        <td>
        	<spring:bind path="command.childIdentifier">
				<input type="text" id="childIdentifier" name="childIdentifier" maxlength="14" value="<c:out value="${status.value}"/>" 
					   class="numbersOnly requiredField" />
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
        </td>
    </tr>

    <c:set var="commandAdditionalPathStr" value="child."></c:set>
	<%@ include file="plt_child_biographic_info.jsp" %>
	
	<c:set var="commandAdditionalPathStr" value="address."></c:set>
	<%@ include file="plt_address.jsp" %> 
   
 	<tr>
		<td><spring:message code="label.contactNumber"/></td>
		<td><spring:bind path="command.centerVisit.contactPrimary">
			<input type="text" id="contactPrimary" name="centerVisit.contactPrimary" maxlength="13" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr> 
</table>
</div>
<div id="tab2" class="tab-section">
<table  class="denform-h">
	<c:forEach var="va" items="${vaccineList}">
	<tr>
		<td><input id="retro_vaccine${va.vaccineId}" name="retro_vaccine" value="${va.name}" readonly="readonly" style="border: hidden;"/></td>
		<td><input id="retro_date${va.vaccineId}" name="retro_date"  maxDate="+0d" class="calendarbox" placeholder="dd-MM-yyyy"/></td>  	
	</tr>
	</c:forEach>   
   		<tr><td><button onclick="sendVaccinationHistory()" type="button">Next!</button></td></tr>
		<script type="text/javascript">
			
		var vaccineScheduleList;

		function sendVaccinationHistory() {
			jsonArray = [];
			$("input[name='retro_date']").each(function(index, element) {
				if (element.value.length > 0) {
					var id = (element.id).match(/\d+/g);
					jsonObject = {};
					jsonObject["vaccineId"] = id.toString();
					jsonObject["vaccineName"] = $('#retro_vaccine' + id).val();
					jsonObject["vaccinationDate"] = element.value;
					jsonArray.push(jsonObject);
				}
			});

			DWRVaccineService.getTested(JSON.stringify(jsonArray), {callback : function(resultList) {
				console.log(resultList);
				vaccineScheduleList = resultList;
				displaySchedule();
				}
			});
		}
		
		function displaySchedule(){
			$.each(vaccineScheduleList, function(index, element) {	
				var vid = vaccineScheduleList[index].<%=VaccineScheduleKey.vaccine%>.vaccineId;
				var name = vaccineScheduleList[index].<%=VaccineScheduleKey.vaccine%>.name;
				var status = vaccineScheduleList[index].<%=VaccineScheduleKey.status%>;
				var dueDate = vaccineScheduleList[index].<%=VaccineScheduleKey.assigned_duedate%>;
						
// 				console.log($.type(dueDate));	

				$(".current_vaccine").append("<tr id='tr"+vid+"'></tr>")
				$("#tr"+vid).append("<td>"+name+"</td><td>"+status+"</td><td>"+dueDate+"</td>");
			});
		}

		</script>
</table>
</div>
<div id="tab3" class="tab-section">
<table class="current_vaccine denform-h">
</table>

<input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
</div>

</form>

