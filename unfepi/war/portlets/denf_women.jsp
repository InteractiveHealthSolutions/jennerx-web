<%@page import="org.directwebremoting.json.types.JsonArray"%>
<%@page import="java.util.List"%>
<%@page import="org.ird.unfepi.web.controller.AddWomenController"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="org.ird.unfepi.constants.FormType"%>
<%@page import="org.ird.unfepi.utils.validation.REG_EX"%>
<%@page import="org.ird.unfepi.model.Model.ContactTeleLineType"%>

<%@page import="org.ird.unfepi.model.Women"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">

function calendarShow() {
	
}
function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
</script>

<form method="post" id="frm" name="frm">
	<table class="denform-h">

		<tr>
    		<td>Enrollment ki Tareekh <span class="mendatory-field">*</span></td>
       	     <td>
       		 <spring:bind path="command.centerVisit.visitDate">
   	    	 <!-- MUST be named as centerVisitDate: used in plt_vaccine_schedule for autopopulating date incase of status VACCINATED -->
      		 	 <input id="centerVisitDate" name="centerVisit.visitDate" value="${status.value}" maxDate="+0d" class="calendarbox" onclosehandler="centerVisitDateChanged"/>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			</td>
		</tr>
		
		<tr>
    	<td>Project ID <span class="mendatory-field">*</span></td>
        <td>
        	<spring:bind path="command.projectId">
				<input type="text" id="programId" name="projectId" maxlength="14" value="<c:out value="${status.value}"/>" class="numbersOnly" />
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
        </td>
    	</tr>
		
		<tr>
			<td colspan="2" class="headerrow">Basic Info</td>
		</tr>
		<c:set var="commandAdditionalPathStr" value="women."></c:set>
		<%@ include file="plt_women_biographic_info.jsp" %>
		
		<tr>
			<td colspan="2" class="headerrow">Address</td>
		</tr>
		<c:set var="commandAdditionalPathStr" value="address."></c:set>
		<%@ include file="plt_address.jsp" %>

		<tr>
			<td colspan="2" class="headerrow">Vaccine Information</td>
		</tr>
		<c:set var="commandAdditionalPathStr" value="vaccine."></c:set>
		<%@ include file="plt_vaccine_women_tt.jsp" %>

		<tr>
		<td>Raabtay ke liye Mobile Number</td>
		<td><spring:bind path="command.centerVisit.contactPrimary">
			<input type="text" id="contactPrimary" name="centerVisit.contactPrimary" maxlength="15" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Raabtay ke liye koi aur number</td>
		<td><spring:bind path="command.centerVisit.contactSecondary">
			<input type="text" id="contactSecondary" name="centerVisit.contactSecondary" maxlength="15" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
		
		<tr>
			<td></td>
			<td><input type="button" id="submitBtn" value="Submit Data"
				onclick="submitThisForm();"></td>
		</tr>
	</table>
</form>
