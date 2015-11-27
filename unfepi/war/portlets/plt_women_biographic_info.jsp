<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">

	function marriage() {
		if(document.getElementById("maritalStatus").value == "Married" || document.getElementById("maritalStatus").value == "Widowed"){
			document.getElementById("husbandName").style.display = "block";
		} else {
			document.getElementById("husbandName").style.display = "none";
		}
	}

</script>
	
    <c:set var="trueStrval" value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"></c:set>
	<tr id="womenNamedtr" >
        <td>Aap ka poora naam kya hai? <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}firstName">
             <input type="text" id="womenfirstName" name="${commandAdditionalPathStr}firstName" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td>Walid ka Poora Naam <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}fatherFirstName">
             <input type="text" id="womenfatherFirstName" name="${commandAdditionalPathStr}fatherFirstName" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
	<tr>
        <td>Tareekh pedaish ya umr <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.birthdateOrAge">
			<select id="birthdateOrAge" name="birthdateOrAge" onchange="birthdateOrAgeChanged(this);" bind-value="${status.value}">
				<option></option>
				<option value="birthdate">Tareekh Pedaish</option>
				<option value="age">Umr</option>
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
    				var ageips = $('input[name^="womenage"]');
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
    				var ageips = $('input[name^="womenage"]');
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
		<td>Tareekh Pedaish <span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.${commandAdditionalPathStr}birthdate">
        <input id="birthdate" name="${commandAdditionalPathStr}birthdate" maxDate="+0d" value="${status.value}" class="calendarbox"/>
        <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        </td>
    </tr>
    <tr id="agetr" <c:if test="${command.birthdateOrAge != 'age'}">style="display: none"</c:if> >
        <td>Umr (insert 0 if not applicable)<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.womenagey">
        <input type="text" id="womenagey" name="womenagey" size="1" maxlength="1" value="${status.value}" onchange="ageChanged();" class="numbersOnly"/>Years<br>
        </spring:bind>
        <spring:bind path="command.womenagem">
        <input type="text" id="womenagem" name="womenagem" size="2" maxlength="2" value="${status.value}" onchange="ageChanged();" class="numbersOnly"/>Months<br>
        </spring:bind>
        <spring:bind path="command.womenagew">
        <input type="text" id="womenagew" name="womenagew" size="2" maxlength="2" value="${status.value}" onchange="ageChanged();" class="numbersOnly"/>Weeks<br>
        </spring:bind>
        <spring:bind path="command.womenaged">
        <input type="text" id="womenaged" name="womenaged" size="2" maxlength="2" value="${status.value}" onchange="ageChanged();" class="numbersOnly"/>Days<br>
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
					 $('input[name^="womenage"]').val('');
					alert('Select date enrolled first');
					return;
				}
				var reg = /^[0-9]+/;
				var ageips = $('input[name^="womenage"]');
				for ( var i = 0; i < ageips.length; i++) {
					if(ageips[i].value != '' && !reg.test(ageips[i].value)){
						alert('invalid age param :'+ageips[i].value);
						ageips[i].value ='';
						return;
					}
				}
				
				//Add years
				dateEnrolled.add(-parseInt($('input[name^="womenagey"]').val())).years();
				//Add months
				dateEnrolled.add(-parseInt($('input[name^="womenagem"]').val())).months();
				//Add weeks
				dateEnrolled.add(-parseInt($('input[name^="womenagew"]').val())).weeks();
				//Add days
				dateEnrolled.add(-parseInt($('input[name^="womenaged"]').val())).days();

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
	<tr>
			<td>Kya aap shaadi shuda hain?</td>
			<td><select id="maritalStatus" onchange="marriage();">
				<option></option>
				<option value="Single">Single</option>
				<option value="Married">Married</option>
				<option value="Widowed">Widowed</option>
				<option value="Divorced">Divorced</option>
				<option value="Engaged">Engaged</option>
				<option value="Refused">Refused</option>
			</select></td>
		</tr>
		
	<tr id="husbandName" style="display:none">
        <td>Shohar ka Poora Naam <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}husbandFirstName">
             <input type="text" id="womenHusbandFirstName" name="${commandAdditionalPathStr}husbandFirstName" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>