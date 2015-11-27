<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
 	<tr>
    <td>Vaccinator<br>
    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.VACCINATOR.FILTER_NAME()%>"></c:set>
	<input type="hidden" id="vaccinatorIdinh" value="${model[nextSearchFieldNameValue]}"/> 
    <select id="vaccinatorddp" name="${nextSearchFieldNameValue}">
        <option></option>
        <c:forEach items="${model.vaccinators}" var="vaccinator"> 
        <option value="${vaccinator.mappedId}">${vaccinator.idMapper.identifiers[0].identifier}:${vaccinator.firstName}</option>
    	</c:forEach> 
    </select>
    <script><!--
       sel = document.getElementById("vaccinatorddp");
       val=document.getElementById("vaccinatorIdinh").value;
       makeValueSelectedInDD(sel,val);
    //-->
    </script>
	</td>
    <td>Due date<br>
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
    <td>Amount transferred<br>
    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.AMOUNT1_LOWER.FILTER_NAME()%>"></c:set>
    <input type="hidden" id="amounttransferredrangeLVal" value="${model[nextSearchFieldNameValue]}"/> 
    <select id="amounttransferredrangeL" name="${nextSearchFieldNameValue}" >
    	<option></option>
    	<option>0</option>
    	<option>1000</option>
    	<option>2000</option>
    	<option>3000</option> 
    	<option>5000</option>
    	<option>10000</option>
    	<option>15000</option>
    	<option>20000</option>          
    </select>to
    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.AMOUNT1_UPPER.FILTER_NAME()%>"></c:set>
    <input type="hidden" id="amounttransferredrangeUVal" value="${model[nextSearchFieldNameValue]}"/> 
    <select id="amounttransferredrangeU" name="${nextSearchFieldNameValue}" >
    	<option></option>
    	<option>2000</option>
    	<option>3000</option>
    	<option>5000</option> 
    	<option>7000</option>
    	<option>10000</option>
    	<option>15000</option>
    	<option>25000</option> 
    	<option>50000</option>  
    </select>
    <script><!--
        sel = document.getElementById("amounttransferredrangeL");
        val=document.getElementById("amounttransferredrangeLVal").value;
        makeValueSelectedInDD(sel,val);
      	
      	sel = document.getElementById("amounttransferredrangeU");
        val=document.getElementById("amounttransferredrangeUVal").value;
        makeValueSelectedInDD(sel,val);
    //-->
    </script>
    </td>
    <td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
	<td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_STOREKEEPER_INCENTIVE_CSV%>');" class="linkiconM iconcsv"></a></td>
	</tr>
</table>
</form>