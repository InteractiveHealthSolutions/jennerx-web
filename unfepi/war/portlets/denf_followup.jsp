<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineScheduleKey"%>
<%@page import="org.ird.unfepi.model.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.unfepi.utils.validation.REG_EX"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.model.Vaccination"%>

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

#tabs li a:hover {
	color: #e0691a;
/* 	font-size: medium; */
}

#tabs li:hover{
	border-color: #cab99b;
	background-color: #cab99b;
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
<script>
	function centerVisitDateChanged() {
	}
	
	function dateDifference(firstDate, secondDate) {
		var startDay = new Date(firstDate);
		var endDay = new Date(secondDate);
		var millisecondsPerDay = 1000 * 60 * 60 * 24;

		var millisBetween = startDay.getTime() - endDay.getTime();
		var days = millisBetween / millisecondsPerDay;

		return Math.floor(days);
	}
	
	function isDateDigit(e) {
		var charCode = (e.which) ? e.which : e.keyCode;
		if ((charCode >= 48 && charCode <= 57) || (charCode == 45)) {
			return true;
		}
		return false;
	}
	
	function centerChanged() {	
	}
	
	function healthProgramChanged(){
		unSelecteSites();
		getSites();
	}	
	function unSelecteSites(){
		$("#vaccinationCenterId option:selected").removeAttr("selected");
		$("#vaccinationCenterId").val("");
		$("#vaccinationCenterId option").hide();
	}
	
	function getSites(){
		$.get( "addchild/siteList/"+$('#healthProgramId').val()+".htm" , function( data ) {
			
			var centers = $.parseJSON(data);
			console.log(centers);
			$.each(centers, function(index, value){
				$("#vaccinationCenterId option").each(function(){
					 if (value == $(this).attr("id").replace(/\D/g,"")){
						  $('#vc'+value).show();
					  }
				});
			});
			
		});
	}
	
	function subfrm() {
		DWRVaccineService.overrideSchedule(vaccineScheduleList, '${command.uuid}', function(result) {
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
	$(function() {
		
		$("#vaccinationCenterId option").hide();

		if($('#healthProgramId').val().length != 0){
			getSites();	
		}
		
		
		$('.tab-section').hide();
		$('#tabs a').click(function(event){
			if(this.id == 't1'){
//				console.log("t1 clicked");
				$('#tabs a.current').removeClass('current');
				$('.tab-section:visible').hide();
				$(this.hash).show();
				$(this).addClass('current');
				event.preventDefault();
			}
			else if (this.id == 't2' || this.id == 't3'){
				
				var isEmptyField = false;
				$(".requiredField").each(function(index, element) {
					if ((element.value.length == 0) && (element.value.replace(/\s+/g, "").length == 0)) {
						isEmptyField = true ;
					}
				});
				
				var diff = dateDifference(convertToDate($('#centerVisitDate').val()), convertToDate($('#birthdateinh').val()));
				var isValidEnrollmentDate = (diff >= 0) ? true : false;
				
				if (!isValidEnrollmentDate){			
					 if ($('#centerVisitDate').val().length == 0) {
						 alert('select visit date');
					 } else {
						 alert('visit Date should be greater than or equal to Birthdate');
					 }
				} 
				else if(isEmptyField){
					alert('fill all the required fields first');
				}				
				else {
//					console.log('going to next page');
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


<form method="post" id="frm" name="frm" >
<div id="tab1" class="tab-section" >
<table class="denform-h">
    <tr>
    	<td colspan="2"><%@ include file="dg_child_biographic_info.jsp" %></td>
    </tr>
	<tr>
        <td colspan="2" class="headerrow">Immunization Details</td>
    </tr>
    <tr>
    	<td>Center Visit Date<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.visitDate">
        <input id="centerVisitDate" name="visitDate" value="${status.value}" maxDate="+0d" class="calendarbox requiredField" 
        	   onchange="centerVisitDateChanged();" onkeypress="return isDateDigit(event)" placeholder="dd-MM-yyyy"/>
		<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
    	</td>
    </tr>
    <tr>
		<td>Vaccinator ID <span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.vaccinatorId">
            <select id="vaccinatorId" name="vaccinatorId" bind-value="${status.value}" class="requiredField">
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
		<td>Health Program<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.healthProgramId">
	            <select id="healthProgramId" name="healthProgramId" bind-value="${status.value}" onchange="healthProgramChanged();"  class="requiredField">
	               	<option id="" disabled="disabled"></option>
	            	<c:forEach items="${healthprograms}" var="hprog"> 
	            		<option id="hp${hprog.programId}" value="${hprog.programId}" disabled="disabled">${hprog.name}</option>
	            	</c:forEach> 
	            </select>
	            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
		</td>
	</tr>
	<tr>
		<td>Site<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.vaccinationCenterId">
            <select id="vaccinationCenterId" name="vaccinationCenterId" bind-value="${status.value}" onchange="centerChanged();" class="requiredField" >
               	<option id=""></option>
            	<c:forEach items="${vaccinationCenters}" var="vcenter"> 
            	<option id="vc${vcenter.mappedId}" value="${vcenter.mappedId}">${vcenter.name}</option>
            	</c:forEach> 
            </select>
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
		<td><input id="retro_vaccine_in${va.vaccineId}" name="retro_vaccine_in" class="retro_vaccine_in" type="checkbox" /></td>
		<td><input id="retro_date${va.vaccineId}" name="retro_vaccine_date" maxDate="+0d" class="calendarbox retro_vaccine_date" placeholder="dd-MM-yyyy" disabled /></td>  	
	</tr>
	</c:forEach>   
</table>
</div>

<div id="tab3" class="tab-section" style="text-align: center;">

<fieldset>
    <legend> Vaccines history </legend>
    <table class="vaccine_history denform-h" style="table-layout: fixed;">
	</table>
</fieldset>

<br><br>

<fieldset>
    <legend> Current vaccines </legend>
    <table class="current_vaccine denform-h" style="table-layout: fixed;">
	</table>
</fieldset>

<br><br>
<input type="button" id="RealContraindication" value="Real Contraindication" onclick="Contraindication();">
<br><br>
<input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
</div>

<script type="text/javascript">
			
		var vaccineScheduleList;

		function sendVaccinationHistory() {
			jsonArray = [];
// 			$("input[name='retro_date']").each(function(index, element) {
// 				if (element.value.length > 0) {
// 					var id = (element.id).match(/\d+/g);
// 					jsonObject = {};
// 					jsonObject["vaccineId"] = id.toString();
// 					jsonObject["vaccineName"] = $('#retro_vaccine' + id).val();
// 					jsonObject["vaccinationDate"] = element.value;
// 					jsonArray.push(jsonObject);
// 				}
// 			});
			$("input[name ='retro_vaccine_in']:checked").each(function(index, element) {
// 				console.log(index + "  " + element.id);
				
				var id = (element.id).match(/\d+/g);
				jsonObject = {};
				jsonObject["vaccineId"] = id.toString();
				jsonObject["vaccineName"] = $('#retro_vaccine' + id).val();
				
// 				console.log($('#retro_date' + id).val() );
				
				if($('#retro_date' + id).val().length > 0){
					jsonObject["vaccinationDate"] = $('#retro_date' + id).val();
				}				
				
				console.log(jsonObject["vaccineId"] + " " + jsonObject["vaccineName"] + " " + jsonObject["vaccinationDate"] );
				jsonArray.push(jsonObject);
				
			});
			
			DWRVaccineService.getVaccineSchedule(JSON.stringify(jsonArray), convertToDate($('#birthdateinh').val()), convertToDate($('#centerVisitDate').val()), '${command.childId}',  '${command.vaccinationCenterId}', '${command.uuid}', $('#healthProgramId').val(), {callback : function(resultList) {
// 				console.log(resultList);
				vaccineScheduleList = resultList;
// 				$.each(vaccineScheduleList, function(index, element){
// 					console.log(element.vaccine.vaccineId +" : "+ element.vaccine.name  + "   --  " +  element.prerequisiteFor);
					
// 				});
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

				var dateStr = $.datepicker.formatDate('dd-mm-yy', new Date(dueDate));
				
				if(status == "VACCINATED" || status == "RETRO"){
					$(".current_vaccine").append("<tr id='tr"+vid+"'></tr>")
					$("#tr"+vid).append("<td>"+name+"</td><!-- <td>"+status+"</td> <td>"+dateStr+"</td> -->");
					$("#tr"+vid).append("<td><button id='delbtn' onclick='delVaccine("+vid+")'>X</button></td>");
					
					if(status == "RETRO"){
						vaccineScheduleList[index].<%=VaccineScheduleKey.status%> = "VACCINATED";
						vaccineScheduleList[index].<%=VaccineScheduleKey.vaccination_date%> = convertToDate($('#centerVisitDate').val()) ;
						vaccineScheduleList[index].<%=VaccineScheduleKey.center%> = $('#vaccinationCenterId').val()
					}					
				}
				
				if(status == "CURRENT_RETRO" ||  status == "CURRENT_RETRO_DATE_MISSING"){
					$(".vaccine_history").append("<tr id='tr"+vid+"'></tr>")
					$("#tr"+vid).append("<td>"+name+"</td><!-- <td>"+status+"</td> <td>"+dateStr+"</td> -->");
					
					vaccineScheduleList[index].<%=VaccineScheduleKey.status%> = "RETRO";
					vaccineScheduleList[index].<%=VaccineScheduleKey.center%> = $('#vaccinationCenterId').val()
				}
			});
		}
		
		function delVaccine(vid) {
			
			var preReq4 = [];
			
			$.each(vaccineScheduleList, function(index, element) {
				var vaccineId = vaccineScheduleList[index].<%=VaccineScheduleKey.vaccine%>.vaccineId;
				if(vaccineId == vid){
					
					preReq4 = element.prerequisiteFor ;
					
					vaccineScheduleList[index].<%=VaccineScheduleKey.status%> = "SCHEDULED";
					vaccineScheduleList[index].<%=VaccineScheduleKey.vaccination_date%> = null;
					vaccineScheduleList[index].<%=VaccineScheduleKey.center%> = null;
					
					$("#tr"+ vid).remove();
				}
				
				if(preReq4.length > 0){
					$.each(preReq4, function(i, preReq4VaccineId){
						if((element.vaccine.vaccineId == preReq4VaccineId) && (element.status == 'SCHEDULED')){
							
							element.status = 'NOT_ALLOWED';
							element.prerequisite_passed = false ;
							element.assigned_duedate = null;
							element.is_current_suspect = false;
							element.is_retro_suspect = false;
						}
					});
				}
			});			
		}
		
		function Contraindication(){
			if(confirm("do you want to continue ...")){
				$.each(vaccineScheduleList, function(index, element) {
					if(element.<%=VaccineScheduleKey.status%> == "VACCINATED"){
						element.<%=VaccineScheduleKey.status%> = "NOT_VACCINATED";
// 						console.log(element.status +  "  " + element.vaccine.name)
					}
				});
				subfrm();
			}
		}
		
		$("input[name ='retro_vaccine_in']").change(function(){
			var id = (this.id).match(/\d+/g);
			if(this.checked){
				$('#retro_date' + id).prop("disabled", false); 
			} else{
				$('#retro_date' + id).prop("disabled", true);
				$('#retro_date' + id).val("");
			}
		});
		
</script>
</form>