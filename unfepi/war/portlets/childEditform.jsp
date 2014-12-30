<%@page import="org.ird.unfepi.model.Child.STATUS"%>
<%@page import="org.ird.unfepi.model.Child"%>
<%@page import="org.ird.unfepi.model.Model.Gender"%>
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

window.onload = onloadSettingOfControls;

function onloadSettingOfControls() {
}

function subfrm(){
	var st = document.getElementById("status").value;
	if(st == '<%=STATUS.TERMINATED%>'){
		if(trim(document.getElementById("terminationReason").value) == ''){
			alert('Termination reason not specified');
			return;
		}
		if(trim(document.getElementById("terminationDate").value) == ''){
			alert('Termination date not specified');
			return;
		}
	}
	  
	submitThisForm();
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
//-->
</script>

<% boolean enableBirthdateEdit = true;%>

<form method="post" id="frm" name="frm" >
<span id="pageHeadingTextDiv" style="color: #41DAF5">Enrollment <br><span style="font-size: medium;">(Edit-Biographic info) </span> </span>
<table class = "mobileForms" style="outline-color : #41DAF5">
	<tr><td colspan="2">
	<span class="mobileFormsInfoDiv">
	Note: <br>
	~ If birthdate is changed, make sure PENDING vaccines specifically Penta1 and Measles1 are following schedule<br>
	~ Changing child`s status to <%=STATUS.TERMINATED%> would lead to Cancellation of all reminders and ID could never be reactivated</span>
	</td></tr>
			
		<%@ include file="plt_global_errors.jsp" %>
		
	<tr>
    	<td>Assigned Id (Program Id) : </td>
        <td>
           	<span class="hltext1"><c:out value="${command.child.idMapper.identifiers[0].identifier}"/></span>
        </td>
    </tr>
   	<tr>
		<td>Date Enrolled<span class="mendatory-field">*</span></td>
        <td>
        
        <spring:bind path="command.child.dateEnrolled">
       <input id="dateEnrolled" name="child.dateEnrolled" readonly="readonly" value="<fmt:formatDate value="${command.child.dateEnrolled}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>"  class="calendarbox"/>
	   <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
    	</td>
	</tr>
	<c:set var="commandAdditionalPathStr" value="child."></c:set>
	<%@ include file="plt_child_biographic_info.jsp" %>
	
	<tr>
		<td>Child`s Status :<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.child.status" >
			<input type="hidden" value="${status.value}" id="statusVal"/>
			<select id="status" name="child.status" onchange="childStatusChanged();">
			<option <c:if test="${fn:containsIgnoreCase(command.child.status,'TERMINATED')}">disabled="disabled"</c:if>><%=STATUS.FOLLOW_UP%></option>
			<option><%=STATUS.TERMINATED%></option>
			<option disabled="disabled"><%=STATUS.COMPLETED%></option>
			<option disabled="disabled"><%=STATUS.UNENROLLED%></option>
			<option disabled="disabled"><%=STATUS.UNRESPONSIVE%></option>
			</select>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script><!--
              sel = document.getElementById("status");
              val=document.getElementById("statusVal").value;
              makeTextSelectedInDD(sel,val);
              
              function childStatusChanged() {
            	  var st = document.getElementById("status").value;
            	  if(st == '<%=STATUS.TERMINATED%>'){
            		  document.getElementById("terminationdiv").style.display = 'inline-block';
            	  }
            	  else{
            		  document.getElementById("terminationdiv").style.display = 'none';
            	  }
              }
              //-->
             </script>
            
            <div id="terminationdiv" style="border: 2px outset;display: inline-block; box-shadow: #6A91B3 5px 5px 5px;<c:if test="${fn:containsIgnoreCase(command.child.status,'FOLLOW_UP')}">display:none</c:if>">
			<spring:bind path="command.child.terminationReason">
				<textarea id="terminationReason" name="child.terminationReason" rows="3" cols="16">${status.value}</textarea>Reason
				<br><span class="error-message"><c:out value="${status.errorMessage}" /></span>
			</spring:bind>
			<spring:bind path="command.child.terminationDate">
				<input id="terminationDate" name="child.terminationDate" value="${status.value}" class="calendarbox"/>Date
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
		<td>Additional Note</td>
		<td><spring:bind path="command.child.description">
			<textarea name="child.description" maxlength="255"></textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
    <tr>
        <td></td>
        <td>
    <c:choose>
	<c:when test="${not fn:containsIgnoreCase(command.child.status,'UNENROLLED')}">
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
    </c:when>
	<c:otherwise><div class="mobileFormsInfoDiv">UNENROLLED child`s data cannot be edited</div></c:otherwise>
    </c:choose>
        </td>
    </tr>
</table>
</form>
