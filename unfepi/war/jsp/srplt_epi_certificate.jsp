<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
    <tr>
        <td>ID<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.PROGRAM_ID.FILTER_NAME()%>"></c:set>
        <input type="text" id="childidname" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> 
        </td>
        <td>Inquiry cell number<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.ORIGINATOR.FILTER_NAME()%>"></c:set>
        <input type="text" id="originator" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> 
        </td>
        <td>Reply cell number<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.RECIPIENT.FILTER_NAME()%>"></c:set>
        <input type="text" id="recipient" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> 
        </td>
        <td>Received date<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_FROM.FILTER_NAME()%>"></c:set>
        <input id="datefrom" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/>
	    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_TO.FILTER_NAME()%>"></c:set>
	    <input id="dateto" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/>
		<a class="clearDate" onclick="clearDate();">X</a>
		<script>
		function clearDate(){	
			$('input[id^="date"]').val("");
		}
		
		$(function() {
			$('input[id^="date"]').datepicker({
		    	duration: '',
		        constrainInput: false,
		        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
		     });
		});
		</script>
		</td>
		<td>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.ROLE_NAME.FILTER_NAME()%>"></c:set>
		<input type="hidden" name="${nextSearchFieldNameValue}" id="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/>
		
		<a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
		<td><%-- <a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_CHILD_RESPONSES_CSV%>');" class="linkiconM iconcsv"></a> --%></td>
   </tr>
</table>
</form>