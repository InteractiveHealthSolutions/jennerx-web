<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

    <c:set var="trueStrval" value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"></c:set>
	<tr>
        <td>First Name<span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}firstName">
             <input type="text" id="childfirstName" name="${commandAdditionalPathStr}firstName" maxlength="30" 
             		value="<c:out value="${status.value}"/>" class="requiredField" onkeypress="return isChar(event);" />
             <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td>Last Name<span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}lastName">
             <input type="text" id="childlastName" name="${commandAdditionalPathStr}lastName" maxlength="30" 
             		value="<c:out value="${status.value}"/>" class="requiredField" onkeypress="return isChar(event);" />
             <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td><spring:message code="label.motherName"/></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}motherFirstName">
             <input type="text" id="childmotherFirstName" name="${commandAdditionalPathStr}motherFirstName" maxlength="30" 
             		value="<c:out value="${status.value}"/>"  onkeypress="return isChar(event);" />
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
	<tr>
		<td>Gender<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.${commandAdditionalPathStr}gender">
			<select id="gender" name="${commandAdditionalPathStr}gender" bind-value="${status.value}" style="text-transform: capitalize" class="requiredField">
				<option value=""></option>
				<c:forEach items="<%=Gender.values()%>" var="gen_value">
					<c:if test="${!fn:containsIgnoreCase(gen_value, 'unknown')}">
						<option value="${gen_value}">${fn:toLowerCase(gen_value)}</option>
					</c:if>
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
            		
//             		$('#birthdate').val('');
            		$('.requiredFieldAge').val('');
            		
            		$('.requiredFieldBirthDate').addClass("requiredField");
            		$('.requiredFieldAge').removeClass("requiredField");
            		
    				document.getElementById("birthdatetr").style.display = 'table-row';
    				document.getElementById("agetr").style.display = 'none';
    				document.getElementById("estimatedBirthdate").value = '<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>';
    				var ageips = $('input[name^="childage"]');
    				for ( var i = 0; i < ageips.length; i++) {
    					ageips[i].value = '';
    				}
            	}
            	else if(document.getElementById("birthdateOrAge").value == 'age'){
            		
            		$('#birthdate').val('');
//             		$('.requiredFieldAge').val('');
            		
            		$('.requiredFieldBirthDate').removeClass("requiredField");
            		$('.requiredFieldAge').addClass("requiredField");
            		document.getElementById("birthdatetr").style.display = 'none';
    				document.getElementById("agetr").style.display = 'table-row';
    				document.getElementById("estimatedBirthdate").value = '<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>';
            	}
            	else {
            		
            		$('#birthdate').val('');
            		$('.requiredFieldAge').val('');
            		
            		$('.requiredFieldBirthDate').removeClass("requiredField");
            		$('.requiredFieldAge').removeClass("requiredField");
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
        	   maxDate="+0d" value="${status.value}" class="requiredFieldBirthDate calendarbox"
        	   onkeypress="return isDateDigit(event)" placeholder="dd-MM-yyyy"
        	   onclosehandler="birthDate_Changed" />
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
        	   <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        <spring:bind path="command.childagem">
        <input type="text" id="childagem" name="childagem" size="2" maxlength="2" value="${status.value}" 
        	   onchange="ageChanged();" class="numbersOnly requiredFieldAge"/>Months<br>
        	   <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        <spring:bind path="command.childagew">
        <input type="text" id="childagew" name="childagew" size="2" maxlength="2" value="${status.value}" 
        	   onchange="ageChanged();" class="numbersOnly requiredFieldAge"/>Weeks<br>
        	   <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        <spring:bind path="command.childaged">
        <input type="text" id="childaged" name="childaged" size="2" maxlength="2" value="${status.value}" 
        	   onchange="ageChanged();" class="numbersOnly requiredFieldAge"/>Days<br>
        </spring:bind>
		<spring:bind path="command.${commandAdditionalPathStr}estimatedBirthdate">
		<input type="hidden" id ="estimatedBirthdate" name="${commandAdditionalPathStr}estimatedBirthdate" value="${status.value}" class="numbersOnly">
		<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
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
				
// 				var max = dateDifference(new Date(), convertToDate($('#centerVisitDate').val()));
				var min = dateDifference(new Date(), convertToDate($('#birthdate').val()));
				
				$(".retro_vaccine_date").each(function(index, element) {
//		 			$(this).datepicker("option", "maxDate", '-'+max+'d');
					$(this).datepicker("option", "minDate", '-'+min+'d');
				});
				
				
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