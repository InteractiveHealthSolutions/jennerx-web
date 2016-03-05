
<%@page import="org.ird.unfepi.GlobalParams.DailySummaryVaccineGivenTableVariables"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.model.Vaccination"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
<!--
function subfrm()
{
	var summaryDateval = document.getElementById("summaryDate").value;
	if(summaryDateval==''){
		alert('Plz specify a summary date first');
		return;
	}
	var vctorid = document.getElementById("vaccinatorId").value;
	if(vctorid == ''){
		alert('Plz specify a vaccinator first');
		return;
	}
	
	var cenid = document.getElementById("vaccinationCenterId").value;
	if(cenid == ''){
		alert('Plz specify a vaccination center first');
		return;
	}
	
	var sumdt=Date.parseExact(summaryDateval,globalDOf);

	if(sumdt == null){
		sumdt=Date.parseExact(document.getElementById("summaryDate").value,globalDTf);
	}
	DWRDailySummaryService.isDailySummaryExists(cenid,sumdt,{
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
<table class="denform-h">
	<tr>
		<td>Summary Date<span class="mendatory-field">*</span></td>
        <td>
        <spring:bind path="command.dailySummary.summaryDate">
        <input id="summaryDate" name="dailySummary.summaryDate" value="<fmt:formatDate value="${command.dailySummary.summaryDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/>" class="calendarbox" readonly="readonly"/>
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
	<tr>
		<td>Total BCG:<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[0]">
				<input type="hidden" id="dsvgList[0].vaccineId" name="dsvgList[0].vaccineId" value="1" />
				<input type="hidden" id="dsvgList[0].vaccineExists" name="dsvgList[0].vaccineExists" value="true" />
				<input type="hidden" id="dsvgList[0].vaccineName" name="dsvgList[0].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_BCG.getREPRESENTATION() %>" />
				<input type="text" id="totalBcg" name="dsvgList[0].quantityGiven" onchange="calculateVisits();"  value="<c:out value="${command.dsvgList[0].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total Penta 1<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[1]">
				<input type="hidden" id="dsvgList[1].vaccineId" name="dsvgList[1].vaccineId" value="2" />
				<input type="hidden" id="dsvgList[1].vaccineExists" name="dsvgList[1].vaccineExists" value="true" />
				<input type="hidden" id="dsvgList[1].vaccineName" name="dsvgList[1].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_PENTA1.getREPRESENTATION() %>" />
				<input type="text" id="totalPenta1" name="dsvgList[1].quantityGiven" onchange="calculateVisits();"  value="<c:out value="${command.dsvgList[1].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total Penta 2<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[2]">
				<input type="hidden" id="dsvgList[2].vaccineId" name="dsvgList[2].vaccineId" value="3" />
				<input type="hidden" id="dsvgList[2].vaccineExists" name="dsvgList[2].vaccineExists" value="true" />
				<input type="hidden" id="dsvgList[2].vaccineName" name="dsvgList[2].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_PENTA2.getREPRESENTATION() %>" />
				<input type="text" id="totalPenta2" name="dsvgList[2].quantityGiven" onchange="calculateVisits();"  value="<c:out value="${command.dsvgList[2].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total Penta 3<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[3]">
				<input type="hidden" id="dsvgList[3].vaccineId" name="dsvgList[3].vaccineId" value="4" />
				<input type="hidden" id="dsvgList[3].vaccineExists" name="dsvgList[3].vaccineExists" value="true" />
				<input type="hidden" id="dsvgList[3].vaccineName" name="dsvgList[3].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_PENTA3.getREPRESENTATION() %>" />
				<input type="text" id="totalPenta3" name="dsvgList[3].quantityGiven" onchange="calculateVisits();"  value="<c:out value="${command.dsvgList[3].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total Mealses 1<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[4]">
				<input type="hidden" id="dsvgList[4].vaccineId" name="dsvgList[4].vaccineId" value="5" />
				<input type="hidden" id="dsvgList[4].vaccineExists" name="dsvgList[4].vaccineExists" value="true" />
				<input type="hidden" id="dsvgList[4].vaccineName" name="dsvgList[4].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_MEASLES1.getREPRESENTATION() %>" />
				<input type="text" id="totalMeasles1" name="dsvgList[4].quantityGiven" onchange="calculateVisits();"  value="<c:out value="${command.dsvgList[4].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total Measles 2<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[5]">
				<input type="hidden" id="dsvgList[5].vaccineId" name="dsvgList[5].vaccineId" value="6" />
				<input type="hidden" id="dsvgList[5].vaccineExists" name="dsvgList[5].vaccineExists" value="true" />
				<input type="hidden" id="dsvgList[5].vaccineName" name="dsvgList[5].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_MEASLES2.getREPRESENTATION() %>" />
				<input type="text" id="totalMeasles2" name="dsvgList[5].quantityGiven" onchange="calculateVisits();"  value="<c:out value="${command.dsvgList[5].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total OPV 0<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[6]">
				<input type="hidden" id="dsvgList[6].vaccineExists" name="dsvgList[6].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[6].vaccineName" name="dsvgList[6].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_OPV0.getREPRESENTATION() %>" />
				<input type="text" id="dsvgList[6].quantityGiven" name="dsvgList[6].quantityGiven" value="<c:out value="${command.dsvgList[6].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total OPV 1<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[7]">
				<input type="hidden" id="dsvgList[7].vaccineExists" name="dsvgList[7].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[7].vaccineName" name="dsvgList[7].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_OPV1.getREPRESENTATION() %>" />
				<input type="text" id="dsvgList[7].quantityGiven" name="dsvgList[7].quantityGiven" value="<c:out value="${command.dsvgList[7].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total OPV 2<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[8]">
				<input type="hidden" id="dsvgList[8].vaccineExists" name="dsvgList[8].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[8].vaccineName" name="dsvgList[8].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_OPV2.getREPRESENTATION() %>" />
				<input type="text" id="dsvgList[8].quantityGiven" name="dsvgList[8].quantityGiven" value="<c:out value="${command.dsvgList[8].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total OPV 3<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[9]">
				<input type="hidden" id="dsvgList[9].vaccineExists" name="dsvgList[9].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[9].vaccineName" name="dsvgList[9].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_OPV3.getREPRESENTATION() %>" />
				<input type="text" id="dsvgList[9].quantityGiven" name="dsvgList[9].quantityGiven" value="<c:out value="${command.dsvgList[9].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total TT 1<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[10]">
				<input type="hidden" id="dsvgList[10].vaccineExists" name="dsvgList[10].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[10].vaccineName" name="dsvgList[10].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT1.getREPRESENTATION() %>" />
				<input type="text" id="dsvgList[10].quantityGiven" name="dsvgList[10].quantityGiven" value="<c:out value="${command.dsvgList[10].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total TT 2<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[11]">
				<input type="hidden" id="dsvgList[11].vaccineExists" name="dsvgList[11].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[11].vaccineName" name="dsvgList[11].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT2.getREPRESENTATION() %>" />
				<input type="text" id="dsvgList[11].quantityGiven" name="dsvgList[11].quantityGiven" value="<c:out value="${command.dsvgList[11].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total TT 3<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[12]">
				<input type="hidden" id="dsvgList[12].vaccineExists" name="dsvgList[12].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[12].vaccineName" name="dsvgList[12].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT3.getREPRESENTATION() %>" />
				<input type="text" id="dsvgList[12].quantityGiven" name="dsvgList[12].quantityGiven" value="<c:out value="${command.dsvgList[12].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total TT 4<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[13]">
				<input type="hidden" id="dsvgList[13].vaccineExists" name="dsvgList[13].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[13].vaccineName" name="dsvgList[13].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT4.getREPRESENTATION() %>" />
				<input type="text" id="dsvgList[13].quantityGiven" name="dsvgList[13].quantityGiven" value="<c:out value="${command.dsvgList[13].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total TT 5<span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.dsvgList[14]">
				<input type="hidden" id="dsvgList[14].vaccineExists" name="dsvgList[14].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[14].vaccineName" name="dsvgList[14].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_TT5.getREPRESENTATION() %>" />
				<input type="text" id="dsvgList[14].quantityGiven" name="dsvgList[14].quantityGiven" value="<c:out value="${command.dsvgList[14].quantityGiven}"/>" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>Total number of vaccines given (excluding OPV and TT)</td>
		<td>
			<spring:bind path="command.dsvgList[15]">
				<input type="hidden" id="dsvgList[15].vaccineExists" name="dsvgList[15].vaccineExists" value="false" />
				<input type="hidden" id="dsvgList[15].vaccineName" name="dsvgList[15].vaccineName" value="<%=DailySummaryVaccineGivenTableVariables.TOTAL_VACCINE_GIVEN.getREPRESENTATION() %>" />
				<input type="text" id="calculatedTotalVaccineGiven" name="dsvgList[15].quantityGiven" value="<c:out value="${command.dsvgList[15].quantityGiven}"/>" readonly="readonly" />
				<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
<script type="text/javascript">
<!--
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
//-->
</script>
		</td>
	</tr>
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