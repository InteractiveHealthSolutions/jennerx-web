<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<style>
<!--
.divtd {
width: 130px;
display: inline-block !important;
vertical-align: text-top;
}
.divtd span{
width: auto !important;
}
.divtd select {
width: auto !important;
}


-->
</style>
        <div class="divtd"> Child name : </div>
        <div class="divtd">
        <spring:bind path="command.${commandAdditionalPathStr}firstName">
             <input type="text" id="childfirstName" name="${commandAdditionalPathStr}firstName" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <span class="error-message" style="display: inline;"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        <spring:bind path="command.childNamed">
   		<input onchange="childNamedChanged(this);" type="checkbox" name="childNamed" <c:if test='${not empty status.value && status.value == false}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>Not named
        <input style="visibility: hidden;" onchange="childNamedChanged(this);" type="checkbox" name="childNamed" <c:if test='${not empty status.value && status.value == true}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"/>
		<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		<script type="text/javascript">
            function childNamedChanged(sel) {
            	if(document.getElementById("childNamed").value == '<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>'){
    				document.getElementById("childNamedtr").style.display = 'table-row';
            	}
            	else{
            		document.getElementById("childfirstName").value = '';
    				document.getElementById("childNamedtr").style.display = 'none';
            	}
			}
			
			childNamedChanged(document.getElementById("childNamed"));
		</script>
		</div>
<br>
        <div class="divtd">Father name <span class="mendatory-field">*</span></div>
        <div class="divtd"><spring:bind path="command.${commandAdditionalPathStr}fatherFirstName">
             <input type="text" id="childfatherFirstName" name="${commandAdditionalPathStr}fatherFirstName" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</div>
<br>
		<div class="divtd">Sex <span class="mendatory-field">*</span></div>
		<div class="divtd"><spring:bind path="command.${commandAdditionalPathStr}gender">
			<select id="gender" name="${commandAdditionalPathStr}gender" bind-value="${status.value}" style="text-transform: capitalize">
				<c:forEach items="<%=Gender.values()%>" var="gen_value">
					<option value="${gen_value}">${fn:toLowerCase(gen_value)}</option>
				</c:forEach>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</div>
<br>
        <div class="divtd"><spring:bind path="command.birthdateOrAge">
			<select style="width:auto" id="birthdateOrAge" name="birthdateOrAge" onchange="birthdateOrAgeChanged(this);" bind-value="${status.value}">
				<option></option>
				<option value="birthdate">Birthdate</option>
				<option value="age">Age</option>
			</select>
			<span class="mendatory-field">*</span>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		<script type="text/javascript">
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
			
			function resetBirthdateAndAge() {
				var ageips = $('input[name^="childage"]');
				for ( var i = 0; i < ageips.length; i++) {
					ageips[i].value = '';
				}
				document.getElementById("birthdate").value = '';
			}
			
			birthdateOrAgeChanged(document.getElementById("birthdateOrAge"));
		</script>
		</div>
		<div class="divtd">
        <div id="birthdatetr" <c:if test="${command.birthdateOrAge != 'birthdate'}">style="display: none"</c:if>>
        <spring:bind path="command.${commandAdditionalPathStr}birthdate">
        <input id="birthdate" name="${commandAdditionalPathStr}birthdate" maxDate="+0d" value="${status.value}" class="calendarbox"/>
        <span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        </div>
        <div id="agetr" <c:if test="${command.birthdateOrAge != 'age'}">style="display: none"</c:if>>
        <spring:bind path="command.childagey">
        <input type="text" name="childagey" size="1" maxlength="1" value="${status.value}" onchange="ageChanged();" class="numbersOnly"/>Years<br>
        </spring:bind>
        <spring:bind path="command.childagem">
        <input type="text" name="childagem" size="2" maxlength="2" value="${status.value}" onchange="ageChanged();" class="numbersOnly"/>Months<br>
        </spring:bind>
        <spring:bind path="command.childagew">
        <input type="text" name="childagew" size="2" maxlength="2" value="${status.value}" onchange="ageChanged();" class="numbersOnly"/>Weeks<br>
        </spring:bind>
        <spring:bind path="command.childaged">
        <input type="text" name="childaged" size="2" maxlength="2" value="${status.value}" onchange="ageChanged();" class="numbersOnly"/>Days<br>
        </spring:bind>
		<spring:bind path="command.${commandAdditionalPathStr}estimatedBirthdate">
		<input type="hidden" id ="estimatedBirthdate" name="${commandAdditionalPathStr}estimatedBirthdate" value="${status.value}" class="numbersOnly">
		</spring:bind>
		<script>
			function ageChanged() {
				var dateEnrolled=convertToDate(document.getElementById("dateEnrolled").value);
				
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
			}
		</script>
    	</div>
	</div>
	