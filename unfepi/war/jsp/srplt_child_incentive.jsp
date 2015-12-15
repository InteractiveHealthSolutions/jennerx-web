<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
 	<tr>
    <td>Child ID<br>
	    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.PROGRAM_ID.FILTER_NAME()%>"></c:set>
	    <input type="text" id="childid" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
    <td>Lottery date<br>
    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_FROM.FILTER_NAME()%>"></c:set>
    <input id="lotteryDatefrom" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/> 
	<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_TO.FILTER_NAME()%>"></c:set>
	<input id="lotteryDateto" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/>
	<a class="clearDate" onclick="clearLotteryDate();">X</a>
	<script>
	function clearLotteryDate(){	
		$('input[id^="lotteryDate"]').val("");
	}
	
	$(function() {
		$('input[id^="lotteryDate"]').datepicker({
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
    	<option>10</option>
    	<option>20</option>
    	<option>30</option> 
    	<option>50</option>
    	<option>100</option>
    	<option>150</option>
    	<option>200</option>          
    </select>to
    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.AMOUNT1_UPPER.FILTER_NAME()%>"></c:set>
    <input type="hidden" id="amounttransferredrangeUVal" value="${model[nextSearchFieldNameValue]}"/> 
    <select id="amounttransferredrangeU" name="${nextSearchFieldNameValue}" >
    	<option></option>
    	<option>20</option>
    	<option>30</option>
    	<option>50</option> 
    	<option>70</option>
    	<option>100</option>
    	<option>150</option>
    	<option>250</option> 
    	<option>500</option>  
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
    <td>
    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.TYPE.FILTER_NAME()%>"></c:set>
	<input type="hidden" name="${nextSearchFieldNameValue}" id="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/>
    
    <a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
    <td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_CHILD_LOTTERY_CSV%>');" class="linkiconM iconcsv"></a></td>
    
	</tr>
</table>
</form>