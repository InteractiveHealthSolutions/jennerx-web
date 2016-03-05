<%@page import="org.ird.unfepi.model.Model.TimeIntervalUnit"%>
<%@page import="org.ird.unfepi.model.Vaccine"%>

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
function subfrm() {
	
	/* var vaccname=document.getElementById("vaccineName").value;
	var vaccineFullName=document.getElementById("vaccineFullName").value;
	var vaccineshortName=document.getElementById("vaccineshortName").value;
	var gapfrom=document.getElementById("gapFromPreviousMilestone").value;
	var unitgapfrom=document.getElementById("prevGapUnit").value;
	var gapto=document.getElementById("gapFromNextMilestone").value;
	var unitgapto=document.getElementById("nextGapUnit").value;
	
	clearVaccinationMessagesDiv();
	
	if( vaccname!=null && vaccname!='' &&
		gapfrom!=null && gapfrom!='' &&
		gapto!=null && gapto!=''){
		
		if(confirm("Are you sure you want to add vaccine with name '"+vaccname+"' ?")){
			var description=document.getElementById("description").value;
			DWRVaccineService.addVaccine(vaccname,vaccineFullName,vaccineshortName,description,gapfrom,unitgapfrom,gapto,unitgapto,success);
		}else{
			return;
		}
	}else{
		alert('Mendatory fields are missing for vaccine');
	} */
	  
	submitThisForm();
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
//-->
</script>
<form id="frm" name = "frm" method="post">
<span id="pageHeadingTextDiv" style="color: #FF7A7A">Vaccine <span style="font-size: medium;">(New)</span></span>
<table class = "mobileForms" style="outline-color : #FF7A7A">
		
		<%@ include file="plt_global_errors.jsp" %>
		
	<tr>
        <td>Vaccine Name : <span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.name">
        <input type="text" id="vname" name="name" maxlength="15" value="<c:out value="${status.value}"/>"/><br/>
		<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
        </td>
    </tr>
	<%@ include file="paletteVaccine.jsp" %>
	<tr>
        <td></td>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">
        </td>
    </tr>
</table>
</form>