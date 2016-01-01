<%@page import="org.ird.unfepi.model.Child.STATUS"%>
<%@page import="org.directwebremoting.json.types.JsonArray"%>
<%@page import="java.util.List"%>
<%@page import="org.ird.unfepi.beans.EnrollmentWrapper"%>
<%@page import="org.ird.unfepi.web.utils.VaccineSchedule"%>
<%@page import="org.ird.unfepi.web.controller.AddChildController"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="org.ird.unfepi.constants.FormType"%>
<%@page import="org.ird.unfepi.utils.validation.REG_EX"%>
<%@page import="org.ird.unfepi.model.Vaccination"%>
<%@page import="org.ird.unfepi.model.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.unfepi.model.Model.ContactTeleLineType"%>

<%@page import="org.ird.unfepi.model.Child"%>
<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<% boolean lotteryGeneratorForm = false;// TODO request.getAttribute("formType").equals(FormType.LOTTERY_GENERATOR_FORM_FILL);
boolean enableBirthdateEdit = !lotteryGeneratorForm;%>

<script type="text/javascript">
window.onload = onloadSettingOfControls;

function onloadSettingOfControls() 
{
	$('#birthdate').change(function() {
		birthChanged($(this));
	});
}

function loadVaccineSchedule() {
	var birthd = convertToDate($('#birthdate').val());
	var centerVisitd = new Date()
	var childid = '${command.child.mappedId}';
	var uuid = '${command.uuid}';

	DWRVaccineService.generateDefaultSchedule(birthd, centerVisitd, childid, 0, true, uuid, 
			{callback: function(result) {
				//alert(result);
				vaccineScheduleGenerator(birthd, centerVisitd, childid, result, uuid);
			}, async: false, timeout: 5000});
}
function birthChanged(jqControl){
	alert('Tareekh Pedaish tabdeel karna bachay kay Vaccine Schedule pay asar andaaz hoga. Koi bhi Vaccination ya Reminder Date khud bakhud tabdeel nahi ki jaegi.');
}
</script>

<style>
<!--
table.compactde {
padding: 0px !important;
margin: 0px !important;
}
-->
</style>
<script type="text/javascript" src="js/formchanges.js?v=${version_css_js}"></script>

<div align="center">
ID : <a onclick="viewChildDetails(this.text);" class="anchorCustom headingS">${command.child.idMapper.identifiers[0].identifier}</a>
</div>
<script type="text/javascript">
var initial_values = new Array();
var all_form_elements = new Array();
var curSelectedTab = '${editSection}';

$(window).load(function() 
{
   // executes when complete page is fully loaded, including all frames, objects and images
   //setTimeout( setInitialFormValues, 1*1000 );
   curSelectedTab = curSelectedTab==''?$('#tt').tabs('getSelected').panel('options').title:curSelectedTab;
   $('#tt').tabs('select', curSelectedTab);
   setInitialFormValues();
});  

$( document ).ready(function() {
	$('#tt').tabs({
		tabPosition: 'left',
		toolPosition: 'left',
		tools:'#tab-tools',
		onSelect:function(title){
			if(initial_values.length != 0){
				if(hasFormChanged() && curSelectedTab != title){
					alert('Data in previous tab has been changed. Submit changes first.');
					$('#tt').tabs('select', curSelectedTab);
					return;
				}
			curSelectedTab = title;
			}
		}
	});
	
});

// Gets all form elements from the entire document.
function initAllFormElements() {
    // Return variable.

    // The form.
    var form_activity_report = document.getElementById('frm');

    // Different types of form elements.
    var inputs = form_activity_report.getElementsByTagName('input');
    var textareas = form_activity_report.getElementsByTagName('textarea');
    var selects = form_activity_report.getElementsByTagName('select');

    // We do it this way because we want to return an Array, not a NodeList.
    var i;
    for (i = 0; i < inputs.length; i++) {
        all_form_elements.push(inputs[i]);
    }
    for (i = 0; i < textareas.length; i++) {
        all_form_elements.push(textareas[i]);
    }
    for (i = 0; i < selects.length; i++) {
        all_form_elements.push(selects[i]);
    }

}

// Sets the initial values of every form element.
function setInitialFormValues() {
	initAllFormElements();
	
    var inputs = all_form_elements;
    for (var i = 0; i < inputs.length; i++) {
    	if(inputs[i].id == ''){
    		alert("ERROR: "+inputs[i].tagName+" - "+i+inputs[i].name+" MUST be assigned id");
    	}
    	var selattval = jQuery('#'+inputs[i].id).attr('bind-value');
    	console.log(inputs[i].id+":"+selattval);
    	if(inputs[i].tagName.toLowerCase() == 'select' && selattval != ''){
    		initial_values.push(selattval);
    	}
    	else {
            initial_values.push(inputs[i].value);
    	}
    }
}

function hasFormChanged() {
    var has_changed = false;
    var elements = all_form_elements;

    for (var i = 0; i < elements.length; i++) {
    	if (elements[i].value != initial_values[i]) {
        	//alert(elements[i].id+";now:"+elements[i].value +";old:"+ initial_values[i]);
        	elements[i].parentNode.style.backgroundColor = '#F2F5A9';
            has_changed = true;
        }
    }

    return has_changed;
}

function changeReportingPeriod() {
    alert(hasFormChanged());
}

function submitFrm() {
	var tabname = $('#tt').tabs('getSelected').panel('options').title;
	$('#editSection').val(tabname);
	
	if(!hasFormChanged()){
		alert('Can not submit! Nothing edited on form.');
	}
	else{
		document.getElementById('frm').submit();
	}
}

</script>
<select id="vaccinationCenterList" style="visibility: hidden;">
	<option></option>
	<c:forEach items="${vaccinationCenters}" var="vcenter">
		<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier}: ${vcenter.name}</option>
	</c:forEach>
</select>
<form method="post" id="frm" name="frm" >
<input type="hidden" id="editSection" name="editSection" value="${editSection}" />
<div id="tab-tools">
</div>
	<div id="tt" class="easyui-tabs">
    <div title="Biographic" >
    <table class="denform2">
    	<c:set var="commandAdditionalPathStr" value="child."></c:set>
		<%@ include file="plt_child_biographic_info.jsp" %>
    </table>
    </div>
    <div title="Address">
    <table class="denform2">
    	<c:set var="commandAdditionalPathStr" value="address."></c:set>
        <%@ include file="plt_address.jsp" %>
	</table>
    </div>
    <div title="Program">
    <table class="denform2">
    <tr>
    	<td>Enrollment date <span class="mendatory-field">*</span> </td>
    	<td><fmt:formatDate value="${command.child.dateEnrolled}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
    </tr>
    <tr>
    	<td>Enrollment vaccine</td>
    	<td>${command.child.enrollmentVaccine.name}</td>
    </tr>
    	<tr>
		<td>Child`s status :<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.child.status" >
			<select id="status" name="child.status" onchange="childStatusChanged();" bind-value="${status.value}" >
			<option <c:if test="${fn:containsIgnoreCase(command.child.status,'TERMINATED')}">disabled="disabled"</c:if>><%=STATUS.FOLLOW_UP%></option>
			<option><%=STATUS.TERMINATED%></option>
			<option disabled="disabled"><%=STATUS.COMPLETED%></option>
			<option disabled="disabled"><%=STATUS.UNENROLLED%></option>
			<option disabled="disabled"><%=STATUS.UNRESPONSIVE%></option>
			</select>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script><!--
              function childStatusChanged() {
            	  var st = document.getElementById("status").value;
            	  if(st == '<%=STATUS.TERMINATED%>'){
            		  document.getElementById("terminationdiv").style.display = 'inline-block';
            	  }
            	  else{
            		  document.getElementById("terminationdiv").style.display = 'none';
            	  }
            	  
            	  alert('Changing child status would Cancel all scheduled reminders.');
              }
              //-->
             </script>
            
            <div id="terminationdiv" style="<c:if test="${fn:containsIgnoreCase(command.child.status,'FOLLOW_UP')}">display:none</c:if>">
			<spring:bind path="command.child.terminationReason">
				<textarea id="terminationReason" name="child.terminationReason" rows="3" cols="16">${status.value}</textarea> Reason
				<br><span class="error-message"><c:out value="${status.errorMessage}" /></span>
			</spring:bind>
			<spring:bind path="command.child.terminationDate">
				<input id="terminationDate" name="child.terminationDate" value="${status.value}" class="calendarbox"/>  Date
       			<br><span class="datenote">(ex: 01-Jan-2000)</span>
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		<script>
		$(function() {
		    $('#terminationDate').datepicker({
		    	duration: '',
		    	maxDate: '+0d',
		        constrainInput: false,
		        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
		     });
		});
		</script>
			</div> 
		</td>
	</tr>
    <tr>
		<td>Reminders approved? <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.preference.hasApprovedReminders">
				<input type="radio" id="hasApprovedRemindersYes"name="preference.hasApprovedReminders" <c:if test='${not empty status.value && status.value == true}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"/>Yes<br>
				<input type="radio" id="hasApprovedRemindersNo" name="preference.hasApprovedReminders" <c:if test='${not empty status.value && status.value == false}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>No
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Reminder mobile number</td>
		<td><spring:bind path="command.contactPrimary">
			<input type="text" id="contactPrimary" name="contactPrimary" maxlength="15" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Other contact number</td>
		<td><spring:bind path="command.contactSecondary">
			<input type="text" id="contactSecondary" name="contactSecondary" maxlength="15" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>CNIC</td>
		<td><spring:bind path="command.child.nic">
			<input type="text" id="nic" name="child.nic" maxlength="13" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Additional Note</td>
		<td><spring:bind path="command.child.description">
			<textarea id="childDescription" name="child.description" maxlength="255">${status.value}</textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	</table>
    </div>
    <div title="Vaccination">
    <%@ include file="plt_vaccination_list.jsp" %>
    <div class="error-message" style="width: 100%">* Warning: Vaccination will NOT be validated. Modifying this data may result in updates to scheduled reminders or discrepencies in incentive dates for all entities or discrepencies in incentive amounts if vaccinator is changed for any vaccinator</div>
    </div>
    <input id="submitButton" type="button" onclick="submitFrm();" value="Submit Data">
</div>
</form>
