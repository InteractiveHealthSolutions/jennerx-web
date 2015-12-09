<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp" %>

<script type="text/javascript">
function exportdata(containerForm, exportType){
	if(document.getElementById("incentiveDatefrom").value == ''
			|| document.getElementById("incentiveDateto").value == ''){
		alert('Date range must be specified');
		return;
	}
	containerForm.action = "exportdata?extype="+exportType;
	containerForm.submit();
}
</script>
<div class="dvwform centerdivh" style="width: 600px">
<div class="formheadingWRule">Export Unconsumed Incentives</div>
<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
	<tr>
    <td>Incentive generated date<br>
    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_FROM.FILTER_NAME()%>"></c:set>
    <input id="incentiveDatefrom" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/> 
	<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_TO.FILTER_NAME()%>"></c:set>
	<input id="incentiveDateto" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/>
	<a class="clearDate" onclick="clearIncentiveDate();">X</a>
	<script>
	function clearIncentiveDate(){	
		$('input[id^="incentiveDate"]').val("");
	}
	
	$(function() {
		$('input[id^="incentiveDate"]').datepicker({
	    	duration: '',
	        constrainInput: false,
	        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
	     });
	});
	</script>
	</td>
    <td>
    <td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_CHILD_UNCONSUMED_INCENTIVE_CSV%>');" class="linkiconM iconcsv"></a></td>
	</tr>
		</table>
</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>