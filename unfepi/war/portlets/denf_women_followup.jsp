<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page import="org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.model.WomenVaccination"%>

<script type="text/javascript">
function marriage() {
	if(document.getElementById("maritalStatus").value == "Married" || document.getElementById("maritalStatus").value == "Widowed"){
		document.getElementById("husbandName").style.display = "block";
	} else {
		document.getElementById("husbandName").style.display = "none";
	}
}

function subfrm(){
	document.getElementById("frm").submit();
}
</script>


<c:set var="womensessvar" value="womenfollowup"></c:set>

<%-- <c:set var="womensessvar" value="vcv"></c:set> --%>
<form method="post" id="frm" name="frm" >
<table class="denform-h">
	<tr>
        <td colspan="2" class="headerrow">Vaccination Details</td>
    </tr>
     <tr>
		<td>Vaccinator ID <span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.vaccinatorId">
            <select id="vaccinatorId" name="vaccinatorId" bind-value="${status.value}">
                <option></option>
                <c:forEach items="${vaccinators}" var="vaccinator"> 
                <option value="${vaccinator.mappedId}">${vaccinator.idMapper.identifiers[0].identifier} : ${vaccinator.firstName}</option>
            	</c:forEach> 
            </select>
            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
		</td>
	</tr>
	<tr>
		<td>Vaccination Center <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.vaccinationCenterId">
            <select id="vaccinationCenterId" name="vaccinationCenterId" bind-value="${status.value}" onchange="centerChanged();">
               	<option></option>
            	<c:forEach items="${vaccinationCenters}" var="vcenter"> 
            	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier} : ${vcenter.name}</option>
            	</c:forEach> 
            </select>
            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
		</td>
	</tr>
  
	<tr>
			<td colspan="2" class="headerrow">Basic Info</td>
	</tr>
	<tr>
		<td>
			<%@ include file="dg_women_biographic_info.jsp" %>
		</td>
	</tr>
	<tr>
		<td>
		<c:set var="commandAdditionalPathStr" value="address."></c:set>
		<%@ include file="plt_address.jsp" %>
		</td>
	</tr>
    <tr>	
        <td colspan="2" class="headerrow">Vaccine Information</td>
    </tr>
    <tr>
    <td>
    <c:set var="commandAdditionalPathStr" value=""></c:set>
        <%@ include file="plt_vaccine_women_tt.jsp" %>
    </td>
   </tr>
	<tr>
        <td colspan="2" class="headerrow">Programme Details</td>
    </tr>
	<tr>
		<td>SMS reminder ke liye Mobile Number</td>
		<td><spring:bind path="command.contactPrimary">
			<input type="number" id="contactPrimary" name="contactPrimary" maxlength="15" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Raabtay ke liye koi aur number</td>
		<td><spring:bind path="command.contactSecondary">
			<input type="number" id="contactSecondary" name="contactSecondary" maxlength="15" value="${status.value}" class="numbersOnly" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
        <td></td>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
        </td>
    </tr>
</table>
</form>
