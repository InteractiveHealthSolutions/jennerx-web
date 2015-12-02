<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.context.Context"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<script type="text/javascript">
<!--
function showChangeDiv(title){
	if(document.getElementById("changeTrDiv"+title).style.display=='none'){
		document.getElementById("changeTrDiv"+title).style.display="inline-block";
		document.getElementById("paymentDate"+title).value='';
		document.getElementById("addonNote"+title).value='';
		document.getElementById("changeBtn"+title).value='cancel';
	}
	else {
		document.getElementById("changeTrDiv"+title).style.display="none";
		document.getElementById("paymentDate"+title).value='';
		document.getElementById("addonNote"+title).value='';
		document.getElementById("changeBtn"+title).value='change';

	}
}
function processChange(title){
	var paymentDate=Date.parseExact(document.getElementById("paymentDate"+title).value,globalDOf);
	if(paymentDate == null){
		paymentDate=Date.parseExact(document.getElementById("paymentDate"+title).value,globalDTf);
	}
	if(paymentDate == null){
		alert('Specify the date of payment');
		return;
	}
	
	var addnote = document.getElementById("addonNote"+title).value;
	DWRVaccinatorService.markPaid(title,addnote,paymentDate,{
		async: false,
		callback: function (res) {
			if((res.toLowerCase().replace(/ /g,"")).startsWith("done:")){
				document.getElementById("changeBtn"+title).style.display='none';
				document.getElementById("statusTextDiv"+title).innerHTML=
					"<span style=\"text-decoration: line-through\">"+document.getElementById("statusTextDiv"+title).innerHTML+"</span><br>PAID "+document.getElementById("paymentDate"+title).value;
				showChangeDiv(title);
			}
		}});
}
//-->
</script>
<div class="dvwform">
<table>
	<thead>
	<tr><td colspan="40">
<div style="float: right;">
<%
boolean isAuthIncentivize = UserSessionUtils.hasActiveUserPermission(SystemPermissions.DO_VACCINATOR_INCENTIVIZATION,request);
if(isAuthIncentivize){
%>
<a class="icon" onclick="showIncentDiv();">INCENTIVIZE  <img alt="Incentivize" src="images/givemoney.png" class="icon-big"></a>
<div id="incentivediv" style="display: none;outline: 1px blue ridge ;">
select date<input id="incentivizationDateUpper" name="incentivizationDateUpper" value="" maxdate="-2d" class="calendarbox" style="width: 90px" readonly="readonly"/>
<input type="button" value="OK" style="width: 50px;" onclick="doIncentivize();">
</div>
</div>
<script type="text/javascript">
<!--
$( document ).ready(function() {
    $('#incentivizationDateUpper').datepicker({
    	duration: '',
        constrainInput: false,
        maxDate: '-2d',
        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
     });
});

function showIncentDiv(){
	if(document.getElementById("incentivediv").style.display=='none'){
		document.getElementById("incentivediv").style.display="inline-block";
	}
	else {
		document.getElementById("incentivediv").style.display="none";
	}
}
function doIncentivize(){
	var selectedincentivizationDateUpper=Date.parseExact(document.getElementById("incentivizationDateUpper").value,globalDOf);
	if(selectedincentivizationDateUpper == null){
		selectedincentivizationDateUpper=Date.parseExact(document.getElementById("incentivizationDateUpper").value,globalDTf);
	}
	if(selectedincentivizationDateUpper == null){
		alert('Specify the last/upper date of incentivization first (last date to consider till transactions have been done)');
		return;
	}
	
	DWRVaccinatorService.doIncentivizeVaccinators(selectedincentivizationDateUpper,{
		async: false,
		callback: function (res2) {
			if(res2){
				alert(res2);
			}
	}});
	
}
//-->
</script>
<%
}
%>
    </td>
    </tr>
   <!--   <tr>
            <th>Vaccinator Id</th>
            <th>Incentive Date</th>
            <th>Transactions Done</th>
            <th>Amount Due (Vaccinator)</th>
            <th>Transaction Status</th>
            
        </tr>
     -->
    
        <tr>
            <th>Vaccinator Id</th>
            <th>Incentivization Event Date</th>
            <th>Incentivization Date Range (From-To)</th>
            <th>Transactions Done</th>
            <th>Amount Due</th>
            <th>Commission</th>
            <th>Amount Due (Vaccinator)</th>
            <th>Transaction Status</th>
            <!-- <th>Description</th> -->
        </tr>
    </thead>
   <tbody class="rows">
   <c:forEach items="${model.datalist}" var="map">
  	 <tr>
			<td><c:out value="${map.parti.vaccinator.idMapper.identifiers[0].identifier}"></c:out></td>
			<td><fmt:formatDate value="${map.parti.vaccinatorIncentiveEvent.dateOfEvent}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
			<td><fmt:formatDate value="${map.parti.vaccinatorIncentiveEvent.dataRangeDateLower}" pattern="dd-MM-yyyy HH:mm:ss"/>
			--<fmt:formatDate value="${map.parti.vaccinatorIncentiveEvent.dataRangeDateUpper}" pattern="dd-MM-yyyy HH:mm:ss"/>
			<td>${map.CHILD_LOTTERY_WINNINGS}${map.TOTAL_TRANSACTIONS}</td>
			<td>${map.CHILD_LOTTERY_WON_AMOUNT}${map.TOTAL_AMOUNT_DUE}</td>
			<td><c:out value="${map.parti.vaccinatorIncentiveParams.commission}"></c:out></td>
			<td><c:out value="${map.transac.amountDue}"></c:out></td>
			<td>
			<div style="width: 160px">
			<div id="statusTextDiv${map.transac.vaccinatorIncentiveTransactionId}" style="display: inline;">
			<c:out value="${map.transac.transactionStatus}"/> <fmt:formatDate value="${map.transac.paidDate}" pattern="dd-MM-yyyy"/>
			</div>
<%
if(UserSessionUtils.hasActiveUserPermission(SystemPermissions.UPDATE_FINANCIAL_TRANSACTION,request)){
%>
			<c:if test="${fn:toLowerCase(map.transac.transactionStatus) == 'due'}">
				<input id="changeBtn${map.transac.vaccinatorIncentiveTransactionId}" title="${map.transac.vaccinatorIncentiveTransactionId}" type="button" class="anchorButton" value="change" onclick="showChangeDiv(this.title);">
				<div id="changeTrDiv${map.transac.vaccinatorIncentiveTransactionId}" style="display: none;font-style: italic;font-size:smaller;color: maroon;text-align: right;border: 1px #69E6E7 ridge;">
					Paid Date: <input id="paymentDate${map.transac.vaccinatorIncentiveTransactionId}" value="" class="calendarbox" style="width: 90px" readonly="readonly"/>
					AddonNote:<textarea id="addonNote${map.transac.vaccinatorIncentiveTransactionId}" style="width: 90px" maxlength="150"></textarea>
					<input type="button" title="${map.transac.vaccinatorIncentiveTransactionId}" value="OK" style="width: 50px;" onclick="processChange(this.title);">
				</div>
			</c:if>
<%
}
%>
			<div>
			</td>
			<%-- <td><div style="width: 160px; overflow: scroll;"><c:out value="${map.parti.description}"></c:out></div></td> --%>
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>
<script type="text/javascript">
<!--
$(function() {
    $("[id*="+'paymentDate'+"]").datepicker({
    	duration: '',
        constrainInput: false,
        maxDate: '-3d',
        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
     });
});
//-->
</script>

