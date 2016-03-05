<%@page import="org.ird.unfepi.model.CommunicationNote.CommunicationEventType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<div class="headerwindow"></div>
<script type="text/javascript">
<!--
var rwinObj;
var rproId;
var rproCls;
var rrecip;
var rrecipMappedId;

function openRWindow(recipientMappedId, probeClass, probeId, recipient, windowObj) {
	rwinObj = windowObj;
	rproId = probeId;
	rproCls = probeClass;
	rrecip = recipient;
	rrecipMappedId = recipientMappedId;
	$('#r_probeId').val(probeId);
	$('#r_probeClass').val(probeClass);
	$('#r_receiver').val(recipient);

	$(windowObj).window('open');
}

function send() {

	showMsg("Please wait while submitting data....");
	try{
	receiver = document.getElementById("r_receiver").value;
	// eventDate = document.getElementById("r_eventDate").value;
	subject = document.getElementById("r_subject").value;
	// source = document.getElementById("r_source").value;
	problemGroup = document.getElementById("r_problemGroup").value;
	problem = document.getElementById("r_problem").value;
	solutionGroup = document.getElementById("r_solutionGroup").value;
	solution = document.getElementById("r_solution").value;
	probeId = document.getElementById("r_probeId").value;
	probeClass = document.getElementById("r_probeClass").value;
	communicationEventType = document.getElementById("r_communicationEventType").value;
	description = document.getElementById("r_description").value;

	if (receiver != '' && eventDate != '' && source != '' && communicationEventType != '') {
		if (confirm("Are you sure to submit data ?")) {
			document.getElementById("r_changebtn").style.disabled = true;
			DWRCommunicationService.postReply(rrecipMappedId, receiver, subject, problem, problemGroup, 
					solution, solutionGroup, probeId, probeClass, 
					communicationEventType, description, success);
		}
		else {
			return;
		}
	} else {
		alert('Please specify all mandatory fields');
	}
	}
	catch (e) {
		alert("ERROR: "+e);
	}
}

var success = function(msg) {
	if(msg.toLowerCase().indexOf("success") != -1){
		document.getElementById("r_receiver").value = "";
		//document.getElementById("r_eventDate").value = "";
		document.getElementById("r_subject").value = "";
		//document.getElementById("r_source").value = "";
		document.getElementById("r_problemGroup").value = "";
		document.getElementById("r_problem").value = "";
		document.getElementById("r_solutionGroup").value = "";
		document.getElementById("r_solution").value = "";
		document.getElementById("r_probeId").value = "";
		document.getElementById("r_probeClass").value = "";
		document.getElementById("r_communicationEventType").value = "";
		document.getElementById("r_description").value = "";
		
		alert(msg);
		
		$(rwinObj).window('close');
	}
	else{
		showMsg(msg);
	}
	document.getElementById("r_changebtn").style.disabled = false;
	
	expandD(rproCls, rproId);
};
function showMsg(msg) {
	document.getElementById("r_messageDiv").innerHTML = "<p><span style=\"color:green\">"
			+ msg + "</span></p>";
}
//-->
</script>
<div id="r_messageDiv"></div>
<table class="denform-h">
<tr>
   <td colspan="2" class="headerrow">Reply</td>
</tr>
  <tr>
    <td>Mobile number <span class="mendatory-field">*</span></td>
    <td><input id="r_receiver" name="receiver" type="text" maxlength="255"/></td>
  </tr>
  <!-- <tr>
    <td>Datetime <span class="mendatory-field">*</span></td>
    <td><input id="r_eventDate" name="eventDate" maxDate="+0d" class="calendarbox"/></td>
  </tr>
  <tr>
    <td>Who called <span class="mendatory-field">*</span></td>
    <td><select id="r_source" name="source">
			<option></option>
			<option>Caregiver</option>
			<option>Vaccinator</option>
			<option>Storekeeper</option>
			<option>Other</option>
		</select>
	</td>
  </tr> -->
  <tr>
    <td>Sms about whom</td>
    <td><input id="r_subject" name="subject" type="text" maxlength="255"/></td>
  </tr>
  <tr>
    <td>Reason for sms (code)</td>
    <td><input id="r_problemGroup" name="problemGroup" type="text" maxlength="255"/></td>
  </tr>
  <tr>
    <td>Reason for sms</td>
    <td><textarea id="r_problem" name="problem" maxlength="255"></textarea></td>
  </tr>
  <tr>
    <td>Sms content (code)</td>
    <td><input id="r_solutionGroup" name="solutionGroup" type="text" maxlength="255"/></td>
  </tr>
  <tr>
    <td>Sms Text</td>
    <td><textarea id="r_solution" name="solution" maxlength="255"></textarea></td>
  </tr>
  <tr>
    <td></td>
    <td><input id="r_probeId" name="probeId" type="hidden" maxlength="255"/></td>
  </tr>
  <tr>
    <td></td>
    <td><input id="r_probeClass" name="probeClass" type="hidden" maxlength="255"/></td>
  </tr>
	<tr>
		<td>Action type <span class="mendatory-field">*</span></td>
		<td> 
			<select id="r_communicationEventType"	name="communicationEventType" class="capitalize">
				<c:set value="<%=CommunicationEventType.OUTBOUND%>" var="vr"/>
				<option value="${vr}">${fn:toLowerCase(vr)}</option>
			</select>
		</td>
	</tr>
	<tr>
    <td>Additional note</td>
    <td><textarea id="r_description" name="description" maxlength="255"></textarea></td>
  </tr>
  <tr>
    <td></td>
    <td><input id="r_changebtn" type="button" value="Send reply" onclick="send();"/></td>
  </tr>
</table>
