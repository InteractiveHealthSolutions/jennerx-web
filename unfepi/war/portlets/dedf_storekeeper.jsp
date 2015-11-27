<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">
<!--
function subfrm(){
	submitThisForm();
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
    	<td>Project ID (Program Id) : </td>
        <td>${command.idMapper.identifiers[0].identifier}
        </td>
    </tr>
	<tr>
		<td>Closest Vaccination Center<span class="mendatory-field">*</span></td>
		<td>
            <select id="vaccinationCenterId" name="vaccinationCenterId">
            	<option value="${vaccinationCenter.mappedId}">${vaccinationCenter.idMapper.identifiers[0].identifier} : ${vaccinationCenter.name}</option>
            </select>
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
    <tr>
        <td>Storekeeper`s Name  : <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.firstName">
        	 (first)<br>
             <input type="text" id="firstName" name="firstName" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
             <spring:bind path="command.lastName">
             (last)<br>
             <input type="text" id="lastName" name="lastName" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
		<td>Store Name</td>
		<td>
			<spring:bind path="command.storeName">
				<input type="text" id="storeName" name="storeName" maxlength="50" value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
        <td>Storekeeper`s NIC  : <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.nic">
             <input type="text" id="nic" name="nic" maxlength="13" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
    <tr>
        <td>Storekeeper`s EP Account Number  : <span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.epAccountNumber">
             <input type="text" id="epAccountNumber" name="epAccountNumber" maxlength="20" value="<c:out value="${status.value}"/>"/>
             <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
             </spring:bind>
		</td>
    </tr>
	<tr>
		<td>Storekeeper`s Gender :<span class="mendatory-field">*</span></td>
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
		<td>Storekeeper`s Age or Date of Birth<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.birthdate">
        (Date of Birth)(<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA %>)<br>
        <input id="birthdate" name="birthdate" value="<c:out value="${status.value}"/>" class="calendarbox" readonly="readonly" onchange="setEstimatedBD();"/>
        <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
        <br>(Or age)
        <br>
        <input type="text" name="storekeeperageops" size="2" maxlength="2" value="0"/>Years<br>
        <input type="text" name="storekeeperageops" size="2" maxlength="2" value="0"/>Months<br>
        
        <input type="button" name="storekeeperage" value="Ok" onclick="ageChanged();" style="float: right;background: none ;width: 70px;height: 30px;margin-right: 100px;"/>
        <div class="mobileFormsInfoDiv">(entering age will automatically populate date of birth field)</div>
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
			        onClose:setEstimatedBD
			     });
			});
			function setEstimatedBD() {
				document.getElementById("estimatedBirthdate").value = '<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>';
			}
			function ageChanged() {
				var todaydate = Date.today();
				var reg = /^[0-9]+/;
				
				var ageips = document.getElementsByName("storekeeperageops");
				for ( var i = 0; i < ageips.length; i++) {
					if(!reg.test(ageips.item(i).value)){
						alert('invalid age param :'+ageips.item(i).value);
						return;
					}
				}
				//Add years
				todaydate.add(-parseInt(ageips.item(0).value)).years();
				//Add months
				todaydate.add(-parseInt(ageips.item(1).value)).months();

				document.getElementById("birthdate").value=todaydate.toString(globalDOf);
				document.getElementById("estimatedBirthdate").value = '<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>';
			}
		</script>
    	</td>
	</tr>
	<tr>
		<td>Qualification <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.qualification">
			<input type="hidden" id="qualificationinh" name="qualificationinh" value="${status.value}" />
			<select id="qualification" name="qualification" onchange="otherQualification();">
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
			</spring:bind>
			<br>
			
			<input type="text" id="qualificationOther" name="qualificationOther" maxlength="50" onchange="qualificationOtherChanged();" value="<c:out value="${status.value}"/>" />
			<script type="text/javascript">
			<!--
			sel = document.getElementById("qualification");
			val = document.getElementById("qualificationinh").value;
			makeTextSelectedInDD(sel, val);
			
			if(val!='' && sel.value==''){
				var opt = document.createElement("option");
		        
		        opt.text = val;
		        opt.value = val;
		        // Add an Option object to Drop Down/List Box
		        try{
		        document.getElementById("qualification").options.add(opt,null);
		        }catch (e) {
		        	document.getElementById("qualification").options.add(opt);
				}
			}
			function qualificationOtherChanged() {
				var qualSel = document.getElementById("qualification");
				
				var v = getTextSelectedInDD(qualSel);
				if(endsWith(v.toLowerCase(), 'other:') || endsWith(v.toLowerCase(), 'other :')){
					qualSel.remove(qualSel.options.length-1);//remove last one which must be the added one
				}
				
				var otherval = document.getElementById("qualificationOther").value;
				
				var opt = document.createElement("option");
		        
		        opt.text = "Other:"+otherval;
		        opt.value = "Other:"+otherval;
		        // Add an Option object to Drop Down/List Box
		        try{
		        document.getElementById("qualification").options.add(opt,null);
		        }catch (e) {
		        	document.getElementById("qualification").options.add(opt);
				}
		        
		        document.getElementById("qualificationOther").value='';
			}
			function otherQualification() {
				var v = getTextSelectedInDD(document.getElementById("qualification"));
				if(!endsWith(v, 'Other')){
					document.getElementById("qualificationOther").value = '';
					document.getElementById("qualificationOther").disabled = true;
				}
				else{
					document.getElementById("qualificationOther").disabled = false;
				}
			}
			//-->
			</script>
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
        <td>
        <input type="button"  id="submitBtn" value="Submit Data" onclick="subfrm();">
        </td>
        <td>
        </td>
    </tr>
</table>
</form>
