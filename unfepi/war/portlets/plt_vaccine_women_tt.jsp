<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS"%>

<script type="text/javascript">

 	$( document ).ready(function(){
		/*document.getElementById("tt1Date").style.display = "none";
		document.getElementById("tt2Date").style.display = "none";
		document.getElementById("tt3Date").style.display = "none";
		document.getElementById("tt4Date").style.display = "none";
		document.getElementById("tt5Date").style.display = "none";*/
	});

	
	function vaccineStatus(object){
		var today = document.getElementById("centerVisitDate").value;
		//var dd = today.getDate();
		//var mm = today.getMonth()+1; //January is 0!
		//var yyyy = today.getFullYear();

		//if(dd<10) {
		//    dd='0'+dd
		//} 

		//if(mm<10) {
		//    mm='0'+mm
		//} 

		//today = dd +'-'+ mm +'-'+ yyyy;
		var id = object.id.substring(0,3);
		var value = object.options[object.selectedIndex].value;
		
		$('#'+id+"Date").datepicker( "option", "minDate", null);
		$('#'+id+"DueDate").datepicker( "option", "minDate", today);

		if(value == "VACCINATED"){
			var dateId = id + "Date";
			document.getElementById( id + "Date").style.display = "block";
			document.getElementById( id + "DueDate").style.display = "none";
			document.getElementById(dateId).classList.remove("calendarbox");
			document.getElementById(dateId).classList.remove("hasDatepicker");
			$('#'+id+"Date").datepicker( "option", "minDate", today);
			document.getElementById(dateId).value = today;
			document.getElementById(dateId).readOnly = "true";
		}
		
		if(value == "RETRO"){
			document.getElementById( id + "Date").style.display = "block";
			document.getElementById( id + "DueDate").style.display = "none";
			document.getElementById( id + "Date").value = "";
		}
		
		if(value == "RETRO_DATE_MISSING"){
			document.getElementById( id + "Date").style.display = "none";
			document.getElementById( id + "DueDate").style.display = "none";
			document.getElementById( id + "Date").value = "";
			document.getElementById( id + "DueDate").value = "";
		}
		if(value == "SCHEDULED"){
			document.getElementById( id + "Date").style.display = "none";
			document.getElementById( id + "DueDate").style.display = "block";
			document.getElementById( id + "Date").value = "";
			$('#'+id+"DueDate").datepicker( "option", "maxDate", null);
		}
		
		if(value == "NOT_VACCINATED" || value == ""){
			document.getElementById( id + "Date").style.display = "block";
			document.getElementById( id + "DueDate").style.display = "none";
			document.getElementById( id + "Date").value = "";
			document.getElementById( id + "DueDate").value = "";
		}
	}

</script>

		<tr id="tt1">
			<td>TT1 ki tareekh</td>
			<spring:bind path="command.${commandAdditionalPathStr}tt1.vaccinationRecordNum">
				<c:set var="tt1_recordNum" value="${status.value}" />
			</spring:bind>
			<spring:bind path="command.${commandAdditionalPathStr}tt1.vaccinationStatus">
			<c:set var="tt1_status" value="${status.value}" />
			
			<c:if test="${tt1_status == 'SCHEDULED' || tt1_status == null || tt1_recordNum == 0}">
			<td>
			<select id="tt1Status" name="${status.expression}" bind-value="${status.value}"  onChange="vaccineStatus(this);">
					<option value="NOT_VACCINATED"></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO_DATE_MISSING">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</td>
			
			</c:if>
			<td>
			<c:if test="${tt1_status != 'SCHEDULED' &&  not empty tt1_status && tt1_recordNum > 0}">
			<input name="${status.expression}" value="${status.value}" readonly/>
			</c:if>
			</spring:bind>
			
			<c:if test="${tt1_status == 'SCHEDULED' || tt1_recordNum == 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt1.vaccinationDuedate">
			
			<input id="tt1DueDate" name="${status.expression}" maxDate="+0d" value="${status.value}" class="calendarbox"/>
			</spring:bind>
			</c:if>
			
			<!-- followup not changeable if not scheduled or given in previou visit -->
			<c:if test="${tt1_status != 'SCHEDULED' && not empty tt1_status && tt1_recordNum > 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt1.vaccinationDate">
			<input id="tt1Date" name="${status.expression}" value="${status.value}" readonly="readonly"/>
			</spring:bind>
			</c:if>
			<c:if test="${tt1_recordNum == 0 || tt1_status == 'SCHEDULED'}">
			<spring:bind path="command.${commandAdditionalPathStr}tt1.vaccinationDate">
			<input id="tt1Date" name="${status.expression}" maxDate="+0d" class="calendarbox"  value="${status.value}" />
			</spring:bind>
			</c:if>
			
			<script>
				vaccineStatus(document.getElementById("tt1Status"));
			</script>
			</td>
		</tr>

		<tr id="tt2">
			<td>TT2 ki tareekh</td>
			<spring:bind path="command.${commandAdditionalPathStr}tt2.vaccinationRecordNum">
				<c:set var="tt2_recordNum" value="${status.value}" />
			</spring:bind>
			<spring:bind path="command.${commandAdditionalPathStr}tt2.vaccinationStatus">
			<c:set var="tt2_status" value="${status.value}" />
			
			<c:if test="${tt2_status == 'SCHEDULED' || tt2_status == null || tt2_recordNum == 0}">
			<td>
			<select id="tt2Status" name="${status.expression}" bind-value="${status.value}" onChange="vaccineStatus(this);">
					<option value="NOT_VACCINATED"></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO_DATE_MISSING">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</td>
			</c:if>
			<td>
			<c:if test="${tt2_status != 'SCHEDULED' &&  not empty tt2_status && tt2_recordNum > 0}">
			<input name="${status.expression}" value="${status.value}" readonly/>
			</c:if>
			</spring:bind>
			
			<c:if test="${tt2_status == 'SCHEDULED' || tt2_recordNum == 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt2.vaccinationDuedate">
			
			<input id="tt2DueDate" name="${status.expression}" maxDate="+0d" value="${status.value}" class="calendarbox"/>
			</spring:bind>
			</c:if>
			
			<!-- followup not changeable if not scheduled or given in previou visit -->
			<c:if test="${tt2_status != 'SCHEDULED' && not empty tt2_status && tt2_recordNum > 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt2.vaccinationDate">
			<input id="tt2Date" name="${status.expression}" value="${status.value}" readonly="readonly"/>
			</spring:bind>
			</c:if>
			<c:if test="${tt2_recordNum == 0 || tt2_status == 'SCHEDULED'}">
			<spring:bind path="command.${commandAdditionalPathStr}tt2.vaccinationDate">
			<input id="tt2Date" name="${status.expression}" maxDate="+0d" class="calendarbox"  value="${status.value}" />
			</spring:bind>
			</c:if>
			
			<script>
				vaccineStatus(document.getElementById("tt2Status"));
			</script>
			</td>
		</tr>
	
		<tr id="tt3">
			<td>TT3 ki tareekh</td>
			<spring:bind path="command.${commandAdditionalPathStr}tt3.vaccinationRecordNum">
				<c:set var="tt3_recordNum" value="${status.value}" />
			</spring:bind>
			<spring:bind path="command.${commandAdditionalPathStr}tt3.vaccinationStatus">
			<c:set var="tt3_status" value="${status.value}" />
			
			<c:if test="${tt3_status == 'SCHEDULED' || tt3_status == null || tt3_recordNum == 0}">
			<td>
			<select id="tt3Status" name="${status.expression}" bind-value="${status.value}" onChange="vaccineStatus(this);">
					<option value="NOT_VACCINATED"></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO_DATE_MISSING">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</td>
			</c:if>
			<td>
			<c:if test="${tt3_status != 'SCHEDULED' &&  not empty tt3_status && tt3_recordNum > 0}">
			<input name="${status.expression}" value="${status.value}" readonly/>
			</c:if>
			</spring:bind>
			
			<c:if test="${tt3_status == 'SCHEDULED' || tt3_recordNum == 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt3.vaccinationDuedate">
			
			<input id="tt3DueDate" name="${status.expression}" maxDate="+0d" value="${status.value}" class="calendarbox"/>
			</spring:bind>
			</c:if>
			
			<!-- followup not changeable if not scheduled or given in previou visit -->
			<c:if test="${tt3_status != 'SCHEDULED' && not empty tt3_status && tt3_recordNum > 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt3.vaccinationDate">
			<input id="tt3Date" name="${status.expression}" value="${status.value}" readonly="readonly"/>
			</spring:bind>
			</c:if>
			<c:if test="${tt3_recordNum == 0 || tt3_status == 'SCHEDULED'}">
			<spring:bind path="command.${commandAdditionalPathStr}tt3.vaccinationDate">
			<input id="tt3Date" name="${status.expression}" maxDate="+0d" class="calendarbox"  value="${status.value}" />
			</spring:bind>
			</c:if>
			
			<script>
				vaccineStatus(document.getElementById("tt3Status"));
			</script>
			</td>
		</tr>
	

		<tr id="tt4">
			<td>TT4 ki tareekh</td>
			<spring:bind path="command.${commandAdditionalPathStr}tt4.vaccinationRecordNum">
				<c:set var="tt4_recordNum" value="${status.value}" />
			</spring:bind>
			<spring:bind path="command.${commandAdditionalPathStr}tt4.vaccinationStatus">
			<c:set var="tt4_status" value="${status.value}" />
			
			<c:if test="${tt4_status == 'SCHEDULED' || tt4_status == null || tt4_recordNum == 0}">
			<td>
			<select id="tt4Status" name="${status.expression}" bind-value="${status.value}" onChange="vaccineStatus(this);">
					<option value="NOT_VACCINATED"></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO_DATE_MISSING">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</td>
			</c:if>
			<td>
			<c:if test="${tt4_status != 'SCHEDULED' &&  not empty tt4_status && tt4_recordNum > 0}">
			<input name="${status.expression}" value="${status.value}" readonly/>
			</c:if>
			</spring:bind>
			<c:if test="${tt4_status == 'SCHEDULED' || tt4_recordNum == 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt4.vaccinationDuedate">
			
			<input id="tt4DueDate" name="${status.expression}" maxDate="+0d" value="${status.value}" class="calendarbox"/>
			</spring:bind>
			</c:if>
			
			<!-- followup not changeable if not scheduled or given in previou visit -->
			<c:if test="${tt4_status != 'SCHEDULED' && not empty tt4_status && tt4_recordNum > 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt4.vaccinationDate">
			<input id="tt4Date" name="${status.expression}" value="${status.value}" readonly="readonly"/>
			</spring:bind>
			</c:if>
			<c:if test="${tt4_recordNum == 0 || tt4_status == 'SCHEDULED'}">
			<spring:bind path="command.${commandAdditionalPathStr}tt4.vaccinationDate">
			<input id="tt4Date" name="${status.expression}" maxDate="+0d" class="calendarbox"  value="${status.value}" />
			</spring:bind>
			</c:if>
			
			<script>
				vaccineStatus(document.getElementById("tt4Status"));
			</script>
			</td>
		</tr>


		<tr id="tt5">
			<td>TT5 ki tareekh</td>
			<spring:bind path="command.${commandAdditionalPathStr}tt5.vaccinationRecordNum">
				<c:set var="tt5_recordNum" value="${status.value}" />
			</spring:bind>
			<spring:bind path="command.${commandAdditionalPathStr}tt5.vaccinationStatus">
			<c:set var="tt5_status" value="${status.value}" />
			
			<c:if test="${tt5_status == 'SCHEDULED' || tt5_status == null || tt5_recordNum == 0}">
			<td>
			<select id="tt5Status" name="${status.expression}" bind-value="${status.value}" onChange="vaccineStatus(this);">
					<option value="NOT_VACCINATED"></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO_DATE_MISSING">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</td>
			</c:if>
			<td>
			<c:if test="${tt5_status != 'SCHEDULED' &&  not empty tt5_status && tt5_recordNum > 0}">
			<input name="${status.expression}" value="${status.value}" readonly/>
			</c:if>
			</spring:bind>
			<c:if test="${tt5_status == 'SCHEDULED' || tt5_recordNum == 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt5.vaccinationDuedate">
			
			<input id="tt5DueDate" name="${status.expression}" maxDate="+0d" value="${status.value}" class="calendarbox"/>
			</spring:bind>
			</c:if>
			
			<!-- followup not changeable if not scheduled or given in previou visit -->
			<c:if test="${tt5_status != 'SCHEDULED' && not empty tt5_status && tt5_recordNum > 0}">
			<spring:bind path="command.${commandAdditionalPathStr}tt5.vaccinationDate">
			<input id="tt5Date" name="${status.expression}" value="${status.value}" readonly="readonly"/>
			</spring:bind>
			</c:if>
			<c:if test="${tt5_recordNum == 0 || tt5_status == 'SCHEDULED'}">
			<spring:bind path="command.${commandAdditionalPathStr}tt5.vaccinationDate">
			<input id="tt5Date" name="${status.expression}" maxDate="+0d" class="calendarbox"  value="${status.value}" />
			</spring:bind>
			</c:if>
			
			<script>
				vaccineStatus(document.getElementById("tt5Status"));
			</script>
			</td>
		</tr>


