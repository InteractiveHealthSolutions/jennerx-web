<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.GlobalParams"%>
<%@page import="org.ird.unfepi.model.Model.ContactTeleLineType"%>
<%@page import="org.ird.unfepi.model.Child"%>
<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

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
	lotteryTypeChanged(document.getElementById("lotteryType"));
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

function subfrm(){
	var lotttype = document.getElementById("lotteryType").value;
	if(lotttype == ''){
		alert('Please select Lottery Type');
		return;
	}
	var childid = document.getElementById("childId").value;
	var reg = /^1[0-9]+/;
	if(reg.test(childid)!=true || childid.length < 6){
		alert('Please specify a valid numeric and 6 digits programId of child');
		return;
	}
	
	var vaccine=document.getElementById("vaccineId").value;
	if(vaccine==null || vaccine==''){
		alert('Please specify the vaccine child recieved today');
		return;
	}
	
	var justification=document.getElementById("justification").value;
	if(justification==null || justification==''){
		alert('Please specify the justification of action');
		return;
	}

	if(endsWith(justification, 'Other:')){
		alert('Please specify the other reason of justification of action');
		return;
    }
	
	var epiNumber = document.getElementById("epiNumber").value;
	
	var vacccenter=document.getElementById("vaccinationCenterId").value;

	var vaccinator=document.getElementById("vaccinatorId").value;

	var requestedBy=document.getElementById("requestedBy").value;

	var reg = /^[0-9]+/;
	if(reg.test(epiNumber)!=true || epiNumber.length < 8 || epiNumber.substring(0, 3) != '201'){
		alert('Please specify a valid numeric and 8 digits EPI number of child starting with 201');
		return;
	}
	
	if(lotttype.indexOf("<%=GlobalParams.LotteryType.MISSING_ENROLLMENT%>") != -1
			|| lotttype.indexOf("<%=GlobalParams.LotteryType.PENDING_FOLLOWUP%>") != -1){
		if(vacccenter==null ||vacccenter==''){
			alert('Please select the Vaccination Center child was immunized at');
			return;
		}
		
		if(vaccinator==null ||vaccinator==''){
			alert('Please select the Vaccinator who immunized child');
			return;
		}
		
		if(requestedBy==null || requestedBy==''){
			alert('Please specify the Caller or Person making request');
			return;
		}
	}
	
	if(lotttype.indexOf("<%=GlobalParams.LotteryType.MISSING_ENROLLMENT%>") != -1){
		var birthdateval = document.getElementById("birthdate").value;
		if(birthdateval == ''){
			alert('No birthdate found');
			return;
		}
		
		var birthdate = convertToDate(birthdateval);
		var curvacc = getTextSelectedInDD(document.getElementById("vaccineId"));

		//For Current Vaccine
		if(!validateBirthdateVaccinationGap(birthdate, Date.today(), curvacc)){
			return;
		}
	}
	
	submitThisForm();
	
	/* DWRLotteryGeneratorService.verifyParams(epiNumber, vacccenter, vaccine, childid,timeliness,lotttype,{
		async: false,
		callback: function (res2) {
			if(res2.toLowerCase() != 'ok'){
				alert(res2);
				return;
			}

			submitThisForm();
	}}); */
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
//-->
</script>

<form method="post" id="frm" name="frm" >
<table class="denform-h">
	<tr>
		<td>Lottery Type<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.lotteryType">
			<input type="hidden" id="lotteryTypeinh" name="lotteryTypeinh" value="${status.value}" />
            <select id="lotteryType" name="lotteryType" onchange="lotteryTypeChanged(this);">
               	<option></option>
               	<c:forEach items="<%=GlobalParams.LotteryType.values() %>" var="lortype"> 
            	<option value="${lortype}">${lortype}</option>
            	</c:forEach> 
            </select>
            <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
             <script><!--
            sel = document.getElementById("lotteryType");
			val = document.getElementById("lotteryTypeinh").value;
			makeTextSelectedInDD(sel, val);
            
			function lotteryTypeChanged(obj){
            	 if(obj.value.indexOf("<%=GlobalParams.LotteryType.PENDING_FOLLOWUP%>") != -1){
            		 document.getElementById("vaccinationCenterIdtblrow").style.display="table-row";
            		 document.getElementById("vaccinatorIdtblrow").style.display="table-row";
            		 document.getElementById("epiNumbertblrow").style.display="table-row";
            		 document.getElementById("agetblrow").style.display="none";
            		 document.getElementById("childIdtblrow").style.display="table-row";
            		 document.getElementById("vaccineIdtblrow").style.display="table-row";
            		 document.getElementById("requestedBytblrow").style.display="table-row";
            		 document.getElementById("justificationtblrow").style.display="table-row";
            		 document.getElementById("descriptiontblrow").style.display="table-row";
            		 document.getElementById("imageLotterytblrow").style.display="none";

            		 
            	 }
            	 else if(obj.value.indexOf("<%=GlobalParams.LotteryType.MISSING_ENROLLMENT%>") != -1){
            		 document.getElementById("vaccinationCenterIdtblrow").style.display="table-row";
            		 document.getElementById("vaccinatorIdtblrow").style.display="table-row";
            		 document.getElementById("epiNumbertblrow").style.display="table-row";
            		 document.getElementById("agetblrow").style.display="table-row";
            		 document.getElementById("childIdtblrow").style.display="table-row";
            		 document.getElementById("vaccineIdtblrow").style.display="table-row";
            		 document.getElementById("requestedBytblrow").style.display="table-row";
            		 document.getElementById("justificationtblrow").style.display="table-row";
            		 document.getElementById("descriptiontblrow").style.display="table-row";
            		 document.getElementById("imageLotterytblrow").style.display="none";

            		 
            	 }
            	 else if(obj.value.indexOf("<%=GlobalParams.LotteryType.EXISTING%>") != -1){
            		 document.getElementById("vaccinationCenterIdtblrow").style.display="none";
            		 document.getElementById("vaccinatorIdtblrow").style.display="none";
            		 document.getElementById("epiNumbertblrow").style.display="table-row";
            		 document.getElementById("agetblrow").style.display="none";
            		 document.getElementById("childIdtblrow").style.display="table-row";
            		 document.getElementById("vaccineIdtblrow").style.display="table-row";
            		 document.getElementById("requestedBytblrow").style.display="none";
            		 document.getElementById("justificationtblrow").style.display="table-row";
            		 document.getElementById("descriptiontblrow").style.display="table-row";
            		 document.getElementById("imageLotterytblrow").style.display="none";

            	 }
            	 else{
            		 document.getElementById("vaccinationCenterIdtblrow").style.display="none";
            		 document.getElementById("vaccinatorIdtblrow").style.display="none";
            		 document.getElementById("epiNumbertblrow").style.display="none";
            		 document.getElementById("agetblrow").style.display="none";
            		 document.getElementById("childIdtblrow").style.display="none";
            		 document.getElementById("vaccineIdtblrow").style.display="none";
            		 document.getElementById("requestedBytblrow").style.display="none";
            		 document.getElementById("justificationtblrow").style.display="none";
            		 document.getElementById("descriptiontblrow").style.display="none";
            		 document.getElementById("imageLotterytblrow").style.display="table-row";
            	 }
             }
            //-->
            </script>    
		</td>
	</tr>
	<tr id="imageLotterytblrow">
		<td colspan="2">
		<c:if test="${not empty param.childid || not empty param.editOrUpdateMessage}">
			<div class="separator-heading">${param.childid} : ${param.editOrUpdateMessage}</div>
		</c:if>
           <img src="images/surprisegift.jpg"> 
		</td>
	</tr>
	<tr id="vaccinationCenterIdtblrow">
		<td>Vaccination Center<span class="mendatory-field">*</span></td>
		<td>
            <spring:bind path="command.vaccinationCenterId">
            <select id="vaccinationCenterId" name="vaccinationCenterId" bind-value="${status.value}">
               	<option></option>
            	<c:forEach items="${vaccinationCenters}" var="vcenter"> 
            	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier}:${vcenter.name}</option>
            	</c:forEach> 
            </select>
            <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
		</td>
	</tr>
	<tr id="vaccinatorIdtblrow">
		<td>Vaccinator<span class="mendatory-field">*</span></td>
		<td>
            <spring:bind path="command.vaccinatorId">
            <select id="vaccinatorId" name="vaccinatorId" bind-value="${status.value}">
                <option></option>
                <c:forEach items="${model.vaccinators}" var="vaccinator"> 
                <option value="${vaccinator.mappedId}">${vaccinator.idMapper.identifiers[0].identifier}:${vaccinator.firstName}</option>
            	</c:forEach> 
            </select>
            <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
		</td>
	</tr>
	<tr id="epiNumbertblrow">
		<td>EPI Number <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.epiNumber">
				<input type="text" id="epiNumber" name="epiNumber" maxlength="8" value="<c:out value="${status.value}"/>"/>
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr id="agetblrow">
		<td>Child's Date of Birth or Age<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.birthdate">
        (Date of Birth)<br>
        <input id="birthdate" name="birthdate" value="<c:out value="${status.value}"/>" class="calendarbox" onchange="setEstimatedBD();"/>
        <br><span class="datenote">(ex: 01-Jan-2000)</span>
        <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        (Or age)
        <br>
        <input type="text" name="childageops" size="1" maxlength="1" value="0"/>Years<br>
        <input type="text" name="childageops" size="2" maxlength="2" value="0"/>Months<br>
        <input type="text" name="childageops" size="2" maxlength="2" value="0"/>Weeks<br>
        <input type="text" name="childageops" size="2" maxlength="2" value="0"/>Days<br>
        
        <input type="button" name="childage" value="Ok" onclick="ageChanged();" style="float: right;background: none ;width: 70px;height: 30px;margin-right: 100px;"/>
        <br><div class="mobileFormsInfoDiv" style="width: 200px">(entering age will automatically populate date of birth field)</div>
		<spring:bind path="command.estimatedBirthdate">
		<input type="hidden" id ="estimatedBirthdate" name="estimatedBirthdate" value="${status.value}">
		</spring:bind>
		<script>
			$(function() {
			    $('#birthdate').datepicker({
			    	duration: '',
			        constrainInput: false,
			        maxDate: '+0d',
			        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
			     });
			});
			function setEstimatedBD() {
				document.getElementById("estimatedBirthdate").value = '<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>';
			}
			
			function ageChanged() {
				var dateEnrolled = Date.today()
				
				var reg = /^[0-9]+/;
				
				var ageips = document.getElementsByName("childageops");
				for ( var i = 0; i < ageips.length; i++) {
					if(!reg.test(ageips.item(i).value)){
						alert('invalid age param :'+ageips.item(i).value);
						return;
					}
				}
				//Add years
				dateEnrolled.add(-parseInt(ageips.item(0).value)).years();
				//Add months
				dateEnrolled.add(-parseInt(ageips.item(1).value)).months();
				//Add weeks
				dateEnrolled.add(-parseInt(ageips.item(2).value)).weeks();
				//Add days
				dateEnrolled.add(-parseInt(ageips.item(3).value)).days();

				document.getElementById("birthdate").value=dateEnrolled.toString(globalDOf);
				
				document.getElementById("estimatedBirthdate").value = '<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>';
			}
		</script>
    	</td>
	</tr>
    <tr id="childIdtblrow">
    	<td>Project ID (Program Id) : </td>
        <td><spring:bind path="command.childId">
				<input type="text" id="childId" name="childId" maxlength="6" value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
        </td>
    </tr>
	<tr id="vaccineIdtblrow">
		<td>Which vaccine did the child receive today?<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.vaccineId">
		<input type="hidden" value="${status.value}" id="vaccineIdVal"/>
		<select id="vaccineId" name="vaccineId">
				<option value=""></option>
				<c:forEach items="${model.vaccines}" var="vacc">
					<option value="${vacc.vaccineId}">${vacc.name}</option>
				</c:forEach>
		</select>
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
	<tr id="requestedBytblrow">
		<td>Caller/Requested By</td>
		<td><spring:bind path="command.requestedBy">
				<input type="text" id="requestedBy" name="requestedBy" maxlength="50" value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr id="justificationtblrow">
		<td>Justification</td>
		<td><spring:bind path="command.justification">
			<input type="hidden" id="justification" name="justification">
            <select id="justificationlist" name="justificationlist" onchange="otherJustificationSelected();">
                <option></option>
                <option>Call from field</option>
                <option>Call from caregiver</option>
                <option>LotteryNotRun Csv</option>
                <option>Other</option>
			</select>
            <br>Other:<input id="justificationOther" name="justificationOther" maxlength="30" onchange="otherJustificationSelected();" />
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script><!--
              sel = document.getElementById("justificationlist");
              val=document.getElementById("justification").value;
              
              if(val.indexOf('Other') == -1){
                  makeTextSelectedInDD(sel,val);
              }
              else {
                  makeTextSelectedInDD(sel,"Other");
                  document.getElementById("justificationOther").value = document.getElementById("justification").value.substring(document.getElementById("justification").value.indexOf(":"));
              }
              
              function otherJustificationSelected() {
					var v = getTextSelectedInDD(document.getElementById("justificationlist"));
					if(!endsWith(v, 'Other')){
						document.getElementById("justificationOther").value = '';
						document.getElementById("justificationOther").disabled = true;
					}
					else{
						document.getElementById("justificationOther").disabled = false;
					}
					
					if(endsWith(v, 'Other')){
	                    document.getElementById("justification").value = 'Other:'+ document.getElementById("justificationOther").value;
	                }
	                else {
	                  document.getElementById("justification").value = document.getElementById("justificationlist").value;
	                }
				}
              //-->
             </script>
		</td>
	</tr>
	<tr id="descriptiontblrow">
		<td>Additional Note</td>
		<td><spring:bind path="command.description">
				<textarea id="description" name="description" maxlength="255">${status.value}</textarea>
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
    <tr>
     	<td></td>
        <td><input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();"></td>
    </tr>
</table>
</form>
