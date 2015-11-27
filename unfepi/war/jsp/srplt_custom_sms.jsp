<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.model.Model.SmsStatus"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
    <tr>
        <td>Recipient ID<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.PROGRAM_ID.FILTER_NAME()%>"></c:set>
        <input type="text" id="childId" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/></td>
        <td>Cell number<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.CELL_NUMBER.FILTER_NAME()%>"></c:set>
        <input type="text" id="cellNum" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/></td>
        <td>Sms status<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.SMS_STATUS.FILTER_NAME()%>"></c:set>
        <input type="hidden" id="smsstatusVal" value="${model[nextSearchFieldNameValue]}"/> 
        <select id="smsstatus" name="${nextSearchFieldNameValue}">
        	<option></option>
            <c:forEach  items="<%=SmsStatus.values()%>" var="smsst">
            <option>${smsst}</option>
            </c:forEach>               
        </select>
        <script><!--
             sel = document.getElementById("smsstatus");
             val=document.getElementById("smsstatusVal").value;
             makeTextSelectedInDD(sel,val);
         //-->
         </script>
        </td>
        <td>Due date<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_FROM.FILTER_NAME()%>"></c:set>
        <input id="dueDatefrom" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/> 
		<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_TO.FILTER_NAME()%>"></c:set>
		<input id="dueDateto" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/>
		<a class="clearDate" onclick="clearDueDate();">X</a>
		<script>
		function clearDueDate(){	
			$('input[id^="dueDate"]').val("");
		}
		
		$(function() {
			$('input[id^="dueDate"]').datepicker({
		    	duration: '',
		        constrainInput: false,
		        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
		     });
		});
		</script>
		</td>
        <td>Sent date<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE2_FROM.FILTER_NAME()%>"></c:set>
        <input id="sentDatefrom" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/>
		<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE2_TO.FILTER_NAME()%>"></c:set>
		<input id="sentDateto" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox"  readonly="readonly"/>
		<a class="clearDate" onclick="clearSentDate();">X</a>
		<script>
		function clearSentDate(){	
			$('input[id^="sentDate"]').val("");
		}
		
		$(function() {
			$('input[id^="sentDate"]').datepicker({
		    	duration: '',
		        constrainInput: false,
		        dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
		     });
		});
		</script>
		</td>
		<td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
		<td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_CUSTOM_SMS_CSV%>');" class="linkiconM iconcsv"></a></td>
    </tr>           
</table>
</form>