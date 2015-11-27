<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.GlobalParams.DownloadableType"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<form id="searchfrm" class="searchpalette" name="searchfrm" method="post" >
    <table>
 	     <tr>
             <td>Downloadable name like<br>
             <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.NAME_PART.FILTER_NAME()%>"></c:set>
             <input type="text" id="downloadablename" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/></td>
         	 <td>Type<br>
             <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.TYPE.FILTER_NAME()%>"></c:set>
             <input type="hidden" id="typeVal" value="${model[nextSearchFieldNameValue]}"/>
             <select id="typedo" name="${nextSearchFieldNameValue}">
             	<option></option>
             	<c:forEach  items="<%= DownloadableType.values() %>" var="typ">
             		<option>${typ}</option>
             	</c:forEach>               
             </select>
            <script><!--
             sel = document.getElementById("typedo");
             val=document.getElementById("typeVal").value;
             makeTextSelectedInDD(sel,val);
         //-->
         </script>
            </td>
         	<td>Created date<br>
         <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_FROM.FILTER_NAME()%>"></c:set>
         <input id="createdDatefrom" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox" readonly="readonly"/>
		 <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.DATE1_TO.FILTER_NAME()%>"></c:set>
		 -<input id="createdDateto" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" class="calendarbox" readonly="readonly"/>
		 <a class="clearDate" onclick="clearcreatedDate();">X</a>
		 <script>
		 function clearcreatedDate(){	
			 $('input[id^="createdDate"]').val("");
		 }
		
		 $(function() {
			 $('input[id^="createdDate"]').datepicker({
		 	    duration: '',
		 	    constrainInput: false,
		 	    dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>'
		     });
		 });
		 </script>
		 </td>
		 <td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
		 <td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_DOWNLOADABLE_REPORT_CSV%>');" class="linkiconM iconcsv"></a></td>
         </tr>
    </table>
    </form>