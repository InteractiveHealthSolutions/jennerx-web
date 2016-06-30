
<%@page import="org.directwebremoting.json.types.JsonArray"%>
<%@page import="java.util.List"%>
<%@page import="org.ird.unfepi.beans.EnrollmentWrapper"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule"%>
<%@page import="org.ird.unfepi.web.controller.AddChildController"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="java.util.Date"%>
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

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
$( document ).ready(function() {
    console.log( "ready!" );
    $(function(){
        $('.tab-section').hide();
        $('#tabs a').bind('click', function(e){
            $('#tabs a.current').removeClass('current');
            $('.tab-section:visible').hide();
            $(this.hash).show();
            $(this).addClass('current');
            e.preventDefault();
        }).filter(':first').click();
    });
});
</script>


<ul id="tabs">
    <li><a href="#tab1">Biodata</a></li>
    <li><a href="#tab2">Vaccines history</a></li>
    <li><a href="#tab3">current vaccines</a></li>
</ul>


<form method="post" id="frm" name="frm" >
<div id="tab1" class="tab-section">
<table class="denform-h">
	<tr>
    	<td><spring:message code="label.enrollmentdate"/> <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.centerVisit.visitDate">
       			<!-- MUST be named as centerVisitDate: used in plt_vaccine_schedule for autopopulating date incase of status VACCINATED -->
       			<input id="centerVisitDate" name="centerVisit.visitDate" value="${status.value}" maxDate="+0d" class="calendarbox" onclosehandler="centerVisitDateChanged"/>
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script type="text/javascript">
			</script>
    	</td>
    </tr>
    <tr>
    	<td><spring:message code="label.vaccinatorId"/><span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.centerVisit.vaccinatorId">
	            <select id="vaccinatorId" name="centerVisit.vaccinatorId" bind-value="${status.value}">
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
	            <select id="vaccinationCenterId" name="centerVisit.vaccinationCenterId" bind-value="${status.value}" onchange="centerChanged();">
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
				<input type="text" id="childIdentifier" name="childIdentifier" maxlength="14" value="<c:out value="${status.value}"/>" class="numbersOnly" />
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
    	<td><input id="retro_date${va.vaccineId}" name="retro_date"  maxDate="+0d" class="calendarbox" placeholder="select date"/></td>  	
    	
    	</tr>
    </c:forEach>   
    <tr><td><button onclick="sendVaccinationHistory()" type="button">Next!</button></td></tr>
     <script>
     function sendVaccinationHistory(){
    	 
    	 jsonObject = [];
    	 
    	 $("input[name='retro_date']").each( function( index, element ){
    		 if(element.value.length > 0){
    			 var id = (element.id).match(/\d+/g);
    			 
    			 item = {};
    			 item["vaccineId"] = id.toString() ;
    			 item["vaccineName"] = $('#retro_vaccine' + id).val();
    			 item["vaccinationDate"] = element.value;
    			 
    			 jsonObject.push(item);
    		 }
    	 });
    	 
		 jsonString = JSON.stringify(jsonObject);
		 console.log(jsonString);
    	 
    	 DWRVaccineService.getTested(jsonString, function(response){
    			alert(response);
    	});
     }
     
     </script>
</table>
</div>
<div id="tab3" class="tab-section">
</div>
</form>

