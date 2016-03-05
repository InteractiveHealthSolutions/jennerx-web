
<%@page import="org.ird.unfepi.GlobalParams.DailySummaryVaccineGivenTableVariables"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.model.Vaccination"%>

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

function calculateVisits() {
	var bcgv = document.getElementById("totalBcg").value;
	var p1v = document.getElementById("totalPenta1").value;
	var p2v = document.getElementById("totalPenta2").value;
	var p3v = document.getElementById("totalPenta3").value;
	var m1v = document.getElementById("totalMeasles1").value;
	var m2v = document.getElementById("totalMeasles2").value;
	
	var reg = /^[0-9]+/;
	if(bcgv!='' && reg.test(bcgv)!=true){
		alert('Specify a valid, numeric and non-whitespace input in BCG');
		return;
	}
	if(p1v!='' && reg.test(p1v)!=true){
		alert('Specify a valid, numeric and non-whitespace input in Penta1');
		return;
	}
	if(p2v!='' && reg.test(p2v)!=true){
		alert('Specify a valid, numeric and non-whitespace input in Penta2');
		return;
	}
	if(p3v!='' && reg.test(p3v)!=true){
		alert('Specify a valid, numeric and non-whitespace input in Penta3');
		return;
	}
	if(m1v!='' && reg.test(m1v)!=true){
		alert('Specify a valid, numeric and non-whitespace input in Measles1');
		return;
	}
	if(m2v!='' && reg.test(m2v)!=true){
		alert('Specify a valid, numeric and non-whitespace input in Measles2');
		return;
	}

	var bcgVisits = bcgv==''?0:parseInt(bcgv);
	var penta1Visits = p1v==''?0:parseInt(p1v);
	var penta2Visits = p2v==''?0:parseInt(p2v);
	var penta3Visits = p3v==''?0:parseInt(p3v);
	var measles1Visits = m1v==''?0:parseInt(m1v);
	var measles2Visits = m2v==''?0:parseInt(m2v);

	var totalVisits = bcgVisits + penta1Visits + penta2Visits + penta3Visits + measles1Visits + measles2Visits;
	document.getElementById("calculatedTotalVaccineGiven").value = totalVisits;
}

function subfrm()
{
	var summaryDateval = document.getElementById("summaryDate").value;
	if(summaryDateval==''){
		alert('Plz specify a summary date first');
		return;
	}
	var cenid = document.getElementById("vaccinationCenterId").value;
	if(cenid == ''){
		alert('Plz specify a vaccination center first');
		return;
	}
	
	var vctorid = document.getElementById("vaccinatorId").value;
	if(vctorid == ''){
		alert('Plz specify a vaccinator first');
		return;
	}
	
	var sumdt=Date.parseExact(summaryDateval,globalDOf);

	if(sumdt == null){
		sumdt=Date.parseExact(document.getElementById("summaryDate").value,globalDTf);
	}
	
	var dsId0000 = document.getElementById("dsId0000").value;
	
	DWRDailySummaryService.isAnyOtherDailySummaryExists(dsId0000,cenid,sumdt,{
		async: false,
		callback: function (res) {
			if(!res){
				submitThisForm();
			}
			else{
				alert('A daily summary for given vaccination center ('+getTextSelectedInDD(document.getElementById('vaccinationCenterId'))+') on '+sumdt+' have already been submitted');
			}
		}});
}

function submitThisForm() {
	//document.getElementById("submitBtn").disabled=true;
	document.getElementById("frm").submit();
}
//-->
</script>
<form method="post" id="frm" name="frm" >
<span id="pageHeadingTextDiv" style="color: #D7DF01">Daily Summary <span style="font-size: medium;">(Edit)</span></span>

<span class="warning-message">${errorMessage}</span>
<span class="error-message">${errorMessagev}</span>
<table class = "mobileForms" style="outline-color : #D7DF01">
		
		<%@ include file="plt_global_errors.jsp" %>
		
	<tr>
		<td>Summary Date<span class="mendatory-field">*</span></td>
        <td>
        <input type="hidden" id="dsId0000" value="${command.dailySummary.serialNumber}">
        <spring:bind path="command.dailySummary.summaryDate">
        <input id="summaryDate" name="dailySummary.summaryDate" value="<fmt:formatDate value="${command.dailySummary.summaryDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>" class="calendarbox"/>
		<script>
			$(function() {
			    $('#summaryDate').datepicker({
			    	duration: '',
			        constrainInput: false,
			        maxDate: '+0d',
			        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
			     });
			});
		</script>
       	<br><span class="error-message"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
    	</td>
	</tr>
	<tr>
		<td>Vaccination Center<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.dailySummary.vaccinationCenterId">
			<input type="hidden" id="vaccinationCenterIdinh" name="vaccinationCenterIdinh" value="${status.value}"/>
            <select id="vaccinationCenterId" name="dailySummary.vaccinationCenterId">
               	<option></option>
            	<c:forEach items="${vaccinationCenters}" var="vcenter"> 
            	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier}:${vcenter.name}</option>
            	</c:forEach> 
            </select>
            <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
            <script><!--
               sel = document.getElementById("vaccinationCenterId");
               val=document.getElementById("vaccinationCenterIdinh").value;
               makeTextSelectedInDD(sel,val);
            //-->
            </script>    
		</td>
	</tr>
	<tr>
		<td>Vaccinator<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.dailySummary.vaccinatorId">
			<input type="hidden" id="vaccinatorIdinh" name="vaccinatorIdinh" value="${status.value}" /> 
            <select id="vaccinatorId" name="dailySummary.vaccinatorId">
                <option></option>
                <c:forEach items="${vaccinators}" var="vaccinator"> 
                <option value="${vaccinator.mappedId}">${vaccinator.idMapper.identifiers[0].identifier}:${vaccinator.firstName}</option>
            	</c:forEach> 
            </select>
            <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
            <script><!--
               sel = document.getElementById("vaccinatorId");
               val=document.getElementById("vaccinatorIdinh").value;
               makeTextSelectedInDD(sel,val);
            //-->
            </script>
		</td>
	</tr>
	<c:forEach items="${command.dsvgList}" var="vgl" varStatus="vglstatus">
	<tr>
	<c:choose>
		<c:when test="${fn:containsIgnoreCase(vgl.vaccineName,'bcg')}">
		<td>Total BCG:<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[${vglstatus.index}].quantityGiven">
				<input type="text" id="totalBcg" name="${status.expression}" onchange="calculateVisits();"  value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</c:when>
		<c:when test="${fn:containsIgnoreCase(vgl.vaccineName,'penta1')}">
		<td>Total Penta 1:<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[${vglstatus.index}].quantityGiven">
				<input type="text" id="totalPenta1" name="${status.expression}" onchange="calculateVisits();"  value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</c:when>
		<c:when test="${fn:containsIgnoreCase(vgl.vaccineName,'penta2')}">
		<td>Total Penta 2:<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[${vglstatus.index}].quantityGiven">
				<input type="text" id="totalPenta2" name="${status.expression}" onchange="calculateVisits();"  value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</c:when>
		<c:when test="${fn:containsIgnoreCase(vgl.vaccineName,'penta3')}">
		<td>Total Penta3:<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[${vglstatus.index}].quantityGiven">
				<input type="text" id="totalPenta3" name="${status.expression}" onchange="calculateVisits();"  value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</c:when>
		<c:when test="${fn:containsIgnoreCase(vgl.vaccineName,'measles1')}">
		<td>Total Measles 1:<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[${vglstatus.index}].quantityGiven">
				<input type="text" id="totalMeasles1" name="${status.expression}" onchange="calculateVisits();"  value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</c:when>
		<c:when test="${fn:containsIgnoreCase(vgl.vaccineName,'measles2')}">
		<td>Total Measles 2:<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[${vglstatus.index}].quantityGiven">
				<input type="text" id="totalMeasles2" name="${status.expression}" onchange="calculateVisits();"  value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</c:when>
		<c:when test="${fn:containsIgnoreCase(vgl.vaccineName,'total_vaccinated')}">
		<td>Total number of vaccines given (excluding OPV and TT)</td>
		<td>
			<spring:bind path="command.dsvgList[${vglstatus.index}].quantityGiven">
				<input type="text"  id="calculatedTotalVaccineGiven" name="${status.expression}" value="${status.value}" readonly="readonly" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</c:when>
		<c:otherwise>
		<td>Total ${vgl.vaccineName}:<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[${vglstatus.index}].quantityGiven">
				<input type="text" name="${status.expression}" value="<c:out value="${status.value}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
		</c:otherwise>
	</c:choose>
	</tr>
	</c:forEach>
<%-- <tr>
		<td>Additional Note</td>
		<td><spring:bind path="command.description">
			<textarea name="description"></textarea>
			<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
</tr> --%>
    <tr>
        <td>
        <input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();" align="right" style="width: 300px;">
        </td>
        <td></td>
</tr>
</table>
</form>