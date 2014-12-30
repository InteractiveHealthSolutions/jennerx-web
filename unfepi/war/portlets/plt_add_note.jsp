<%@page import="org.ird.unfepi.model.CommunicationNote.CommunicationEventType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<div class="headerwindow"></div>
<script type="text/javascript">
<!--
var winObj;
var proId;
var proCls;


function openWindow(probeClass, probeId, windowObj) {
	winObj = windowObj;
	proId = probeId;
	proCls = probeClass;
	$('#probeId').val(probeId);
	$('#probeClass').val(probeClass);
	
	$(windowObj).window('open');
}

jQuery('#target').click(createcalendar);

var createcalendar = function() {
	
};

function submit() {

	showMsg("Please wait while submitting data....");
	try{
	receiver = document.getElementById("receiver").value;
	eventDate = document.getElementById("eventDate").value;
	subject = document.getElementById("subject").value;
	source = document.getElementById("source").value;
	problemGroup = document.getElementById("problemGroup").value;
	problem = document.getElementById("problem").value;
	solutionGroup = document.getElementById("solutionGroup").value;
	solution = document.getElementById("solution").value;
	probeId = document.getElementById("probeId").value;
	probeClass = document.getElementById("probeClass").value;
	communicationEventType = document.getElementById("communicationEventType").value;
	description = document.getElementById("description").value;

	if (receiver != '' && eventDate != '' && source != '' && communicationEventType != '') {
		if (confirm("Are you sure to submit data ?")) {
			document.getElementById("changebtn").style.disabled = true;
			DWRCommunicationService.addNote(receiver, source, subject, problem, problemGroup, 
					solution, solutionGroup, convertToDate(eventDate), probeId, probeClass, 
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
		document.getElementById("receiver").value = "";
		document.getElementById("eventDate").value = "";
		document.getElementById("subject").value = "";
		document.getElementById("source").value = "";
		document.getElementById("problemGroup").value = "";
		document.getElementById("problem").value = "";
		document.getElementById("solutionGroup").value = "";
		document.getElementById("solution").value = "";
		document.getElementById("probeId").value = "";
		document.getElementById("probeClass").value = "";
		document.getElementById("communicationEventType").value = "";
		document.getElementById("description").value = "";
		
		alert(msg);
		
		$(winObj).window('close');
	}
	else{
		showMsg(msg);
	}
	document.getElementById("changebtn").style.disabled = false;
	
	expandD(proCls, proId);
};
function showMsg(msg) {
	document.getElementById("messageDiv").innerHTML = "<p><span style=\"color:green\">"
			+ msg + "</span></p>";
}
//-->
</script>
<div id="messageDiv"></div>
<table class="denform-h">
<tr>
   <td colspan="2" class="headerrow">Add Note</td>
</tr>
  <tr>
    <td>Phone operator <span class="mendatory-field">*</span></td>
    <td><input id="receiver" name="receiver" type="text" maxlength="255"/></td>
  </tr>
  <tr>
    <td>Datetime <span class="mendatory-field">*</span></td>
    <td><input id="eventDate" name="eventDate" maxDate="+0d" class="calendarbox"/></td>
  </tr>
  <tr>
    <td>Who called <span class="mendatory-field">*</span></td>
    <td><select id="source" name="source">
			<option></option>
			<option>Caregiver</option>
			<option>Vaccinator</option>
			<option>Storekeeper</option>
			<option>Other</option>
		</select>
	</td>
  </tr>
  <tr>
    <td>Call about whom</td>
    <td><input id="subject" name="subject" type="text" maxlength="255"/></td>
  </tr>
  <tr>
    <td>Reason for call (code)</td>
    <td><input id="problemGroup" name="problemGroup" type="text" maxlength="255"/></td>
  </tr>
  <tr>
    <td>Reason for call</td>
    <td><textarea id="problem" name="problem" maxlength="255"></textarea></td>
  </tr>
  <tr>
    <td>What was told (code)</td>
    <td><input id="solutionGroup" name="solutionGroup" type="text" maxlength="255"/></td>
  </tr>
  <tr>
    <td>What was told</td>
    <td><textarea id="solution" name="solution" maxlength="255"></textarea></td>
  </tr>
  <tr>
    <td></td>
    <td><input id="probeId" name="probeId" type="hidden" maxlength="255"/></td>
  </tr>
  <tr>
    <td></td>
    <td><input id="probeClass" name="probeClass" type="hidden" maxlength="255"/></td>
  </tr>
	<tr>
		<td>Action type <span class="mendatory-field">*</span></td>
		<td> 
			<select id="communicationEventType"	name="communicationEventType" class="capitalize">
				<c:forEach items="<%=CommunicationEventType.values()%>" var="vr">
					<option value="${vr}">${fn:toLowerCase(vr)}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
    <td>Additional note</td>
    <td><textarea id="description" name="description" maxlength="255"></textarea></td>
  </tr>
  <tr>
    <td></td>
    <td><input id="changebtn" type="button" value="Change" onclick="submit();"/></td>
  </tr>
</table>
