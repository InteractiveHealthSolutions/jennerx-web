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
<%@page import="org.ird.unfepi.web.utils.VaccinationCenterVisit"%>

<style>

fieldset{
 	border:thin solid #b2b869 ; 
 	border-color: gray; 
	display:inline-block;
}

legend{
	font-family: Verdana;
	font-size: small;
	color: #e0691a;
}

.vaccine_history.denform-h, .current_vaccine.denform-h{
	outline: none;
}

#delbtn{
	color: red;
	font-weight: 900;
	border: none;
}

.h5_{
	color: #e0691a;
}
#tabs li {
	/* remove bullets */
	list-style-type: none; 
	/* horizontal list */
	display: inline;
	
	padding: 2px 5px 2px 5px;
	padding-right: 10px;
	padding-left: 10px;
/*  background-color: #ffc966; */
/* 	border: 1px solid #ffaf1a; */
  	border-style: outset; 
	background: transparent;
	
}

#tabs li a {
	text-decoration: none;
 	color: orange; 
	font-family: Verdana;
    font-size: small;
    font-weight: bold;
}

ul{
	text-align: center;
}
 
#tabs li a.current {
	color: #e0691a;
	font-weight: bold;
}

#submitBtn, #RealContraindication{
	width: auto;
    height: 30px;
    cursor: pointer;
    background: transparent;
    border: 1 ridge silver;
    padding: 0;
    margin: 0 10px 10px 0;
    font-size: medium;
    font-weight: bold;
    color: orange;
}

</style>

<script type="text/javascript">

	function isDateDigit(e) {
		var charCode = (e.which) ? e.which : e.keyCode;
		if ((charCode >= 48 && charCode <= 57) || (charCode == 45)) {
			return true;
		}
		return false;
	}

	function isChar(e) {
		var charCode = (e.which) ? e.which : e.keyCode;
		if ((charCode >= 65 && charCode <= 90)
				|| (charCode >= 97 && charCode <= 122) || (charCode == 32)) {
			return true;
		}
		return false;
	}

	function dateDifference(firstDate, secondDate) {
		var startDay = new Date(firstDate);
		var endDay = new Date(secondDate);
		var millisecondsPerDay = 1000 * 60 * 60 * 24;

		var millisBetween = startDay.getTime() - endDay.getTime();
		var days = millisBetween / millisecondsPerDay;

		return Math.floor(days);
	}
	
	function centerChanged() {
		unSelecteHealthProgram();
		getCenterProgram();		
	}
	
	function unSelecteHealthProgram(){
		$("#healthProgramId option:selected").removeAttr("selected");
		$("#healthProgramId").val("");
		$("#healthProgramId option").hide();
	}
	
	function getCenterProgram(){
		$.get( "addchild/programList/"+$('#vaccinationCenterId').val()+".htm" , function( data ) {
			  
			  if(data.replace(/\[|\]|\s/gi,"").split(",").toString().length == 0){
				  alert("no health program found in '" + $('#vaccinationCenterId option:selected').text()+"'");
			  }
			  
			  var current_Prog = data.replace(/\[|\]|\s/gi,"").split(",");
			  
			  $.each(current_Prog, function(index, value){
				  $("#healthProgramId option").each(function(){
					  if (value == $(this).attr("id").replace(/\D/g,"")){
						  $('#hp'+value).show();
					  }
				  });
			  });
		});
	}

	function birthChanged() {
	}

	function centerVisitDateChanged() {
// 		$('#vaccinationCenterId').val('');
		$('#birthdate').val('');
		
		// 	DWRVaccineService.overrideSchedule(null, '${command.centerVisit.uuid}', function(result) {
		// 		console.log(result);
		// 	});
		
	}

	function subfrm() {
		DWRVaccineService.overrideSchedule(vaccineScheduleList, '${command.centerVisit.uuid}', function(result) {
							submitThisForm();
		});
	}

	function submitThisForm() {
		document.getElementById("frm").submit();
	}
</script>



<ul id="tabs">
    <li><a id="t1" href="#tab1">biodata</a></li>
    <li><a id="t2" href="#tab2">vaccines history</a></li>
    <li><a id="t3" href="#tab3">current vaccines</a></li>
</ul>
<script type="text/javascript">
	$(function(){
		
		$("#healthProgramId option").hide();
		
		if($('#vaccinationCenterId').val().length != 0){
			getCenterProgram();	
		}
		
		$('.tab-section').hide();
		
		$('#tabs a').click(function(event){
				
				if(this.id == 't1'){
// 					console.log("t1 clicked");
					$('#tabs a.current').removeClass('current');
					$('.tab-section:visible').hide();
					$(this.hash).show();
					$(this).addClass('current');
					event.preventDefault();
				}
				else if (this.id == 't2' || this.id == 't3'){
					var isEmptyField = false;
					var isValidChildIdentifier = false;
					
					$(".requiredField").each(function(index, element) {
						if ((element.value.length == 0) && (element.value.replace(/\s+/g, "").length == 0)) {
							isEmptyField = true ;
						}
					});
					
					if($('#childIdentifier').val().length > 0){
						isValidChildIdentifier =  /^[567]\d{7}$/.test($('#childIdentifier').val());
					}
// 					str.split(' ').length; //count words in name
					
					var diff = dateDifference(convertToDate($('#centerVisitDate').val()), convertToDate($('#birthdate').val()));
					var isValidEnrollmentDate;
					
					if( diff >= 0 ){
						isValidEnrollmentDate = true;
					}else{
						isValidEnrollmentDate = false;
					}
					
					if(isEmptyField){
						alert('fill all the required fields first');
					}
					else if(!isValidChildIdentifier){
						alert('"Child Identifier" should start with 5, 6 or 7 and length should be of 8-digits');
					}
					else if(!isValidEnrollmentDate){
						alert('Enrollment Date should be greater than or equal to Birthdate')
					}
					else{
// 						console.log('going to next page');
						
						if (this.id == 't3') {
							$(".current_vaccine").empty();
							$(".vaccine_history").empty();
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


<spring:hasBindErrors name="command">
	<c:forEach var="error" items="${errors.allErrors}">
		<p style="font-style: italic; color: red;font-size: small;"><spring:message message="${error}" /></p>
	</c:forEach>
</spring:hasBindErrors>


<form method="post" id="frm" name="frm" >
<div id="tab1" class="tab-section" >
<table class="denform-h">
	<tr>
    	<td><spring:message code="label.enrollmentdate"/> <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.centerVisit.visitDate">
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
		<td>Health Program<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.centerVisit.healthProgramId">
	            <select id="healthProgramId" name="centerVisit.healthProgramId" bind-value="${status.value}" class="requiredField">
	               	<option id=""></option>
	            	<c:forEach items="${healthprograms}" var="hprog"> 
	            		<option id="hp${hprog.programId}" value="${hprog.programId}">${hprog.name}</option>
	            	</c:forEach> 
	            </select>
	            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
		</td>
	</tr>
	<tr>
    	<td><spring:message code="label.childIdentifier"></spring:message><span class="mendatory-field">*</span></td>
        <td>
        	<spring:bind path="command.childIdentifier">
				<input type="text" id="childIdentifier" name="childIdentifier" maxlength="8" value="<c:out value="${status.value}"/>" 
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
		<td><input id="retro_vaccine${va.vaccineId}" name="retro_vaccine" value="${va.fullName}" readonly="readonly" style="border: hidden;"/></td>
		<td><input id="retro_vaccine_in${va.vaccineId}" name="retro_vaccine_in" type="checkbox" /></td>
		<td><input id="retro_date${va.vaccineId}" name="retro_date"  maxDate="+0d" class="calendarbox" placeholder="dd-MM-yyyy" disabled /></td>  	
	</tr>
	</c:forEach>   
</table>
</div>

<%@ include file="plt_vaccine_schedule_den2.jsp" %>

</form>

