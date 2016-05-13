<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">
<!--
function subfrm(){
	var vacccenter=document.getElementById("vaccinationCenterId").value;
	if(vacccenter==null ||vacccenter==''){
		alert('Please select the Vaccination Center vaccinator belongs to');
		return;
	}
	
/* 	var programId;
	var asgnsno = document.getElementById("vaccinatorIdAssigned").value;
	var reg = /^[0-9]+/;
	if(reg.test(asgnsno)!=true || asgnsno.length < 4){
		alert('Please specify a valid numeric and 4 digits serial number part of programId assigned to vaccinator');
		return;
	}

	programId = document.getElementById("autogenIdPart").value+""+document.getElementById("vaccinatorIdAssigned").value;
				
	DWRVaccinatorService.isIdExists(programId,{
		async: false,
		callback: function (res2) {
			if(res2){
				alert('Cannot submit registration for id. Check if another vaccinator have been submitted with same id');
				return;
			}
	}}); */
	
	var username = document.getElementById("usernamegiven").value;
	var usernamereg = /^[0-9A-Za-z]+/;
	if(usernamereg.test(username)!=true || username.length < 5){
		alert('Username must be an alphanumeric word of minimum 5 characters');
		return;
	}
	
	var password = document.getElementById("passwordgiven").value;
	var passwordconfirm = document.getElementById("passwordconfirm").value;

	if(password != passwordconfirm){
		alert('Both passwords donot match');
		return;
	}
	
	var passwordreg = /^[0-9A-Za-z]+/;
	if(passwordreg.test(password)!=true || password.length < 6){
		alert('Password must be an alphanumeric word of minimum 6 characters');
		return;
	}
	
	DWRUserService.isUsernameExists(username,{
		async: false,
		callback: function (res2) {
			if(res2){
				alert('Given username has already been assigned to another user');
				return;
			}
			
			var qv = document.getElementById('qualificationOther').value;
			if(qv != ''){
				qv = 'Other:'+qv;
				addNewOption(qv, '');
				makeTextSelectedInDD(document.getElementById('qualification'), qv);
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
<table class="denform-h">
	<tr>
		<td>Vaccination Center <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.vaccinationCenterId">
            <select id="vaccinationCenterId" name="vaccinationCenterId" bind-value="${status.value}">
               	<option></option>
            	<c:forEach items="${vaccinationCenters}" var="vcenter"> 
            	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier} : ${vcenter.name}</option>
            	</c:forEach> 
            </select>
            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
		</td>
	</tr>
    <tr>
		<td>Date Registered<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.dateRegistered">
        <input id="dateRegistered" name="dateRegistered" value="${status.value}" maxDate="+0d" class="calendarbox"/>
      	<br><span class="error-message"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
    	</td>
	</tr>
	<%-- <tr>
        <td>Vaccinator`s NIC  : <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.nic">
             <input id="nic" name="nic" class="numbersOnly" maxlength="13" value="${status.value}"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td>Vaccinator`s EP Account Number  : <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.epAccountNumber">
             <input id="epAccountNumber" name="epAccountNumber" class="numbersOnly" maxlength="20" value="${status.value}"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr> --%>
    <tr>
    	<td>User name<span class="mendatory-field">*</span></td>
        <td><input id="usernamegiven" name="usernamegiven" maxlength="15" value="${usernamegiven}" /></td>
    </tr>
    <tr>
    	<td>Password<span class="mendatory-field">*</span></td>
        <td><input type="password" id="passwordgiven" name="passwordgiven" maxlength="15"/></td>
    </tr>
     <tr>
    	<td>Confirm password<span class="mendatory-field">*</span></td>
        <td><input type="password" id="passwordconfirm" name="passwordconfirm" maxlength="15" /></td>
    </tr>
    <tr>
        <td>Vaccinator`s Name  : <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.firstName">
        	 (first)<br>
             <input type="text" id="firstName" name="firstName" maxlength="30" value="${status.value}"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
             <spring:bind path="command.lastName">
             (last)<br>
             <input type="text" id="lastName" name="lastName" maxlength="30" value="${status.value}"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
	<tr>
		<td>Vaccinator`s Gender :<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.${commandAdditionalPathStr}gender">
			<select id="gender" name="${commandAdditionalPathStr}gender" bind-value="${status.value}" style="text-transform: capitalize">
				<c:forEach items="<%=Gender.values()%>" var="gen_value">
					<option value="${gen_value}">${fn:toLowerCase(gen_value)}</option>
				</c:forEach>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
        <td>Birthdate or Age <span class="mendatory-field">*</span></td>
        <td><select id="birthdateOrAge" name="birthdateOrAge" onchange="birthdateOrAgeChanged(this);" bind-value="${birthdateOrAge}">
				<option></option>
				<option value="birthdate">Birthdate</option>
				<option value="age">Age</option>
			</select>
		<script type="text/javascript">
			<!--
            function birthdateOrAgeChanged(sel) {
            	if(document.getElementById("birthdateOrAge").value == 'birthdate'){
    				document.getElementById("birthdatetr").style.display = 'table-row';
    				document.getElementById("agetr").style.display = 'none';
    				document.getElementById("estimatedBirthdate").value = '<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>';
    				var ageips = $('input[name^="vaccinatorage"]');
    				for ( var i = 0; i < ageips.length; i++) {
    					ageips[i].value = '';
    				}
            	}
            	else if(document.getElementById("birthdateOrAge").value == 'age'){
            		document.getElementById("birthdatetr").style.display = 'none';
    				document.getElementById("agetr").style.display = 'table-row';
    				document.getElementById("estimatedBirthdate").value = '<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>';
            	}
            	else {
            		document.getElementById("birthdatetr").style.display = 'none';
    				document.getElementById("agetr").style.display = 'none';
    				document.getElementById("estimatedBirthdate").value = '';
    				var ageips = $('input[name^="vaccinatorage"]');
    				for ( var i = 0; i < ageips.length; i++) {
    					ageips[i].value = '';
    				}
            	}
			}
			
			function resetBirthdateAndAge() {
				var ageips = $('input[name^="vaccinatorage"]');
				for ( var i = 0; i < ageips.length; i++) {
					ageips[i].value = '';
				}
				document.getElementById("birthdate").value = '';
			}
		//-->
		</script>
		</td>
    </tr>
	<tr id="birthdatetr" <c:if test="${birthdateOrAge != 'birthdate'}">style="display: none"</c:if> >
		<td>Birthdate <span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.${commandAdditionalPathStr}birthdate">
        <input id="birthdate" name="${commandAdditionalPathStr}birthdate" maxDate="+0d" value="${status.value}" class="calendarbox"/>
        <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        </td>
    </tr>
    <tr id="agetr" <c:if test="${birthdateOrAge != 'age'}">style="display: none"</c:if> >
        <td>Umr <span class="mendatory-field">*</span></td>
        <td>
        <input type="text" name="vaccinatoragey" maxlength="2" value="${status.value}" onchange="ageChanged();" class="numbersOnly"/>Years<br>
		<spring:bind path="command.${commandAdditionalPathStr}estimatedBirthdate">
		<input type="hidden" id ="estimatedBirthdate" name="${commandAdditionalPathStr}estimatedBirthdate" value="${status.value}" class="numbersOnly">
		</spring:bind>
		<script>
			function ageChanged() {
				var dateRegistered=convertToDate(document.getElementById("dateRegistered").value);
				
				if(dateRegistered == null){
					 $('input[name^="vaccinatorage"]').val('');
					alert('Select date registered first');
					return;
				}
				var reg = /^[0-9]+/;
				
				var ageips = $('input[name^="vaccinatorage"]');
				for ( var i = 0; i < ageips.length; i++) {
					if(ageips[i].value != '' && !reg.test(ageips[i].value)){
						alert('invalid age param :'+ageips[i].value);
						ageips[i].value ='';
						return;
					}
				}
				//Add years
				dateRegistered.add(-parseInt($('input[name^="vaccinatoragey"]').val())).years();

				document.getElementById("birthdate").value=dateRegistered.toString(globalDOf);
			}
		</script>
    	</td>
	</tr>
	<tr>
		<td>Qualification <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.qualification">
			<select id="qualification" name="qualification" onchange="resetOtherQualification();" bind-value="${status.value}">
				<option></option>
				<option>Primary</option>
				<option>Middle</option>
				<option>Metric</option>
				<option>Intermediate</option>
				<option>Diploma</option>
				<option>Bachelors</option>
				<option>Masters</option>
				<option>Other</option>
				<option>Refused</option>
				<option>Don`t Know</option>
			</select>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			<input type="text" id="qualificationOther" name="qualificationOther" maxlength="50"/>
			<script type="text/javascript">
			function addNewOption(val,prefix){
				var opt = document.createElement("option");
		        opt.text = prefix+val;
		        opt.value = prefix+val;
		        // Add an Option object to Drop Down/List Box
		        try{
		        document.getElementById("qualification").options.add(opt,null);
		        }catch (e) {
		        document.getElementById("qualification").options.add(opt);
				}
			}
			function resetOtherQualification() {
				var v = getTextSelectedInDD(document.getElementById("qualification"));
				if(v.toLowerCase() != 'other'){
					document.getElementById("qualificationOther").value = '';
					document.getElementById("qualificationOther").disabled = true;
				}
				else{
					document.getElementById("qualificationOther").disabled = false;
				}
			}
			
			sel = document.getElementById("qualification");
			val = '${status.value}';
			
			if(val!='' && sel.value==''){
				addNewOption(val, '');
				makeTextSelectedInDD(sel, val);
			}

			resetOtherQualification();
			</script>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Additional Note</td>
		<td><spring:bind path="command.description">
			<textarea name="description" maxlength="255"></textarea>
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
