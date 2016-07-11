<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

    <c:set var="trueStrval" value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"></c:set>
	<tr>
        <td>First Name<span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}firstName">
             <input type="text" id="childfirstName" name="${commandAdditionalPathStr}firstName" maxlength="30" 
             		value="<c:out value="${status.value}"/>" class="requiredField"/>
             <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td>Last Name<span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}lastName">
             <input type="text" id="childlastName" name="${commandAdditionalPathStr}lastName" maxlength="30" 
             		value="<c:out value="${status.value}"/>" class="requiredField"/>
             <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td><spring:message code="label.motherName"/></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}motherFirstName">
             <input type="text" id="childfatherFirstName" name="${commandAdditionalPathStr}fatherFirstName" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
	<tr>
		<td>Gender</td>
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
        <td>Child birthdate or age<span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.birthdateOrAge">
			<select id="birthdateOrAge" name="birthdateOrAge" onchange="birthdateOrAgeChanged(this);" bind-value="${status.value}" class="requiredField">
				<option></option>
				<option value="birthdate">birthdate</option>
				<option value="age">Age</option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		<script type="text/javascript">
			<!--
            function birthdateOrAgeChanged(sel) {
            	if(document.getElementById("birthdateOrAge").value == 'birthdate'){
    				document.getElementById("birthdatetr").style.display = 'table-row';
    				document.getElementById("agetr").style.display = 'none';
    				document.getElementById("estimatedBirthdate").value = '<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>';
    				var ageips = $('input[name^="childage"]');
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
    				var ageips = $('input[name^="childage"]');
    				for ( var i = 0; i < ageips.length; i++) {
    					ageips[i].value = '';
    				}
            	}
			}
			
			$( document ).ready(function() {
				birthdateOrAgeChanged(document.getElementById("birthdateOrAge"));
			});
		//-->
		</script>
		</td>
    </tr>
	<tr id="birthdatetr" <c:if test="${command.birthdateOrAge != 'birthdate'}">style="display: none"</c:if> >
		<td>Birth Date <span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.${commandAdditionalPathStr}birthdate">
        <input id="birthdate" name="${commandAdditionalPathStr}birthdate" 
        	   maxDate="+0d" value="${status.value}" class="calendarbox requiredFieldBirthDate"
        	   onkeypress="return isDateDigit(event)" placeholder="mm-dd-yyyy"/>
        <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        </td>
    </tr>
    <tr id="agetr" <c:if test="${command.birthdateOrAge != 'age'}">style="display: none"</c:if> >
        <td>Age (insert 0 if not applicable)<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.childagey">
        <input type="text" id="childagey" name="childagey" size="1" maxlength="1" value="${status.value}" 
        	   onchange="ageChanged();" class="numbersOnly requiredFieldAge"/>Years<br>
        </spring:bind>
        <spring:bind path="command.childagem">
        <input type="text" id="childagem" name="childagem" size="2" maxlength="2" value="${status.value}" 
        	   onchange="ageChanged();" class="numbersOnly requiredFieldAge"/>Months<br>
        </spring:bind>
        <spring:bind path="command.childagew">
        <input type="text" id="childagew" name="childagew" size="2" maxlength="2" value="${status.value}" 
        	   onchange="ageChanged();" class="numbersOnly requiredFieldAge"/>Weeks<br>
        </spring:bind>
        <spring:bind path="command.childaged">
        <input type="text" id="childaged" name="childaged" size="2" maxlength="2" value="${status.value}" 
        	   onchange="ageChanged();" class="numbersOnly requiredFieldAge"/>Days<br>
        </spring:bind>
		<spring:bind path="command.${commandAdditionalPathStr}estimatedBirthdate">
		<input type="hidden" id ="estimatedBirthdate" name="${commandAdditionalPathStr}estimatedBirthdate" value="${status.value}" class="numbersOnly">
		</spring:bind>
		<script>
			function ageChanged() {
				var dateEnrolled=null;
				try{
				dateEnrolled = convertToDate(document.getElementById("dateEnrolled").value);
				}
				catch (e) {
					dateEnrolled = convertToDate(document.getElementById("centerVisitDate").value);
				}
				if(dateEnrolled == null){
					 $('input[name^="childage"]').val('');
					alert('Select date enrolled first');
					return;
				}
				var reg = /^[0-9]+/;
				var ageips = $('input[name^="childage"]');
				for ( var i = 0; i < ageips.length; i++) {
					if(ageips[i].value != '' && !reg.test(ageips[i].value)){
						alert('invalid age param :'+ageips[i].value);
						ageips[i].value ='';
						return;
					}
				}
				
				//Add years
				dateEnrolled.add(-parseInt($('input[name^="childagey"]').val())).years();
				//Add months
				dateEnrolled.add(-parseInt($('input[name^="childagem"]').val())).months();
				//Add weeks
				dateEnrolled.add(-parseInt($('input[name^="childagew"]').val())).weeks();
				//Add days
				dateEnrolled.add(-parseInt($('input[name^="childaged"]').val())).days();

				document.getElementById("birthdate").value=dateEnrolled.toString(globalDOf);
				
				var allset = true;
				for ( var i = 0; i < ageips.length; i++) {
					if(ageips[i].value == '' || !reg.test(ageips[i].value)){
						allset = false;
					}
				}
				if(allset){
					birthChanged($('#birthdate'));
				}
			}
		</script>
    	</td>
	</tr>