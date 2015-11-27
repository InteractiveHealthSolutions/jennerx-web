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
	if(document.getElementById("datePreferenceChanged").value == ''){
		alert('Please specify date of preference changed');
		return;
	}
	
	var remAppr = document.getElementsByName("hasApprovedReminders");
	
	for ( var i = 0; i < remAppr.length; i++) {
		if(remAppr.item(i).checked 
				&& remAppr.item(i).value.toLowerCase().indexOf('true') != -1)
		{
			DWRValidationService.validateMobileNumber(document.getElementById("reminderCellNumber").value, {
				async: false,
				callback: function (res2) {
					if(res2.toLowerCase()!='ok'){
						alert(res2);
						return;
					}
					
					submitThisForm();
			}});
			break;
		}
		else if(remAppr.item(i).checked 
				&& remAppr.item(i).value.toLowerCase().indexOf('false') != -1)
		{
			submitThisForm();
		}
	}
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
//-->
</script>
<form method="post" id="frm" name="frm" >
<span id="pageHeadingTextDiv" style="color: #41DAF5">Enrollment <span style="font-size: medium;">(Edit-Preferences)</span></span><br>
<table class = "mobileForms" style="outline-color : #41DAF5">
		
		<%@ include file="plt_global_errors.jsp" %>
		
	<tr>
    	<td>Assigned Id (Program Id) : </td>
        <td>
           	<span class="hltext1"><c:out value="${programId}"/><input type="hidden" name="programId" value="${programId}"></span>
        </td>
    </tr>
   	<tr>
		<td>Date Preference changed<span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.datePreferenceChanged">
        <input id="datePreferenceChanged" name="datePreferenceChanged" value="${status.value}" class="calendarbox" />
        <br><span class="datenote">(ex: 01-Jan-2000)</span>
        <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
        </spring:bind>
		<script>
			$(function() {
			    $('#datePreferenceChanged').datepicker({
			    	duration: '',
			        constrainInput: false,
			        maxDate: '+0d',
			        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
			     });
			});
		</script>
    	</td>
	</tr>
    <tr>
		<td>Would you like to receive SMS reminders?<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.hasApprovedReminders">
				<input type="radio" name="hasApprovedReminders" <c:if test='${not empty status.value && status.value == true}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING%>"/>Yes<br>
				<input type="radio" name="hasApprovedReminders" <c:if test='${not empty status.value && status.value == false}'>checked = "checked"</c:if> value="<%=WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING%>"/>No
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Number to be used for SMS reminders</td>
		<td>
			<input type="text" id="reminderCellNumber" name="reminderCellNumber" maxlength="15" value="${reminderCellNumber}" />
		</td>
	</tr>
    <tr>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
        </td>
        <td>
        </td>
    </tr>
</table>
</form>
