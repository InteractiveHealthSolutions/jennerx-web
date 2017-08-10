<%@page import="org.ird.unfepi.constants.EncounterType"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.model.Encounter.DataEntrySource"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
     <tr>
         <td>Person ID<br>
         <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.PROGRAM_ID.FILTER_NAME()%>"></c:set>
         <input type="text" id="peronid" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
         <td>Data entry source<br>
         <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATA_ENTRY_SOURCE.FILTER_NAME()%>"></c:set>
         <input type="hidden" id="dataentrysourceVal" value="${model[nextSearchFieldNameValue]}"/> 
         <select id="dataentrysource" name="${nextSearchFieldNameValue}">
         	<option></option>
         	<c:forEach  items="<%= DataEntrySource.values() %>" var="dtentsrc">
         		<option>${dtentsrc}</option>
         	</c:forEach>               
         </select>
         <script><!--
             sel = document.getElementById("dataentrysource");
             val=document.getElementById("dataentrysourceVal").value;
             makeTextSelectedInDD(sel,val);
         //-->
         </script>
         </td>
         <td>Encounter type<br>
         <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.ENCOUNTER_TYPE.FILTER_NAME()%>"></c:set>
         <input type="hidden" id="encounterTypeVal" value="${model[nextSearchFieldNameValue]}"/> 
         <select id="encounterType" name="${nextSearchFieldNameValue}">
         	<option></option>
         	<c:forEach  items="<%= EncounterType.values() %>" var="enctyp">
         		<option>${enctyp}</option>
         	</c:forEach>               
         </select>
         <script><!--
             sel = document.getElementById("encounterType");
             val=document.getElementById("encounterTypeVal").value;
             makeTextSelectedInDD(sel,val);
         //-->
         </script>
         </td>
         <td>Entered date<br>
         <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_FROM.FILTER_NAME()%>"></c:set>
         <input id="enteredDatefrom" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox" readonly="readonly"/>
		 <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_TO.FILTER_NAME()%>"></c:set>
		 -<input id="enteredDateto" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox" readonly="readonly"/>
		 <a class="clearDate" onclick="clearEnteredDate();">X</a>
		 <script>
		 function clearEnteredDate(){	
			 $('input[id^="enteredDate"]').val("");
		 }
		
		 $(function() {
			 $('input[id^="enteredDate"]').datepicker({
		 	    duration: '',
		 	    constrainInput: false,
		 	    dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
		     });
		 });
		 </script>
		 </td>
		 <td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
<%-- 		 <td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_ENCOUNTER_CSV%>');" class="linkiconM iconcsv"></a></td> --%>
    </tr>
</table>
</form>