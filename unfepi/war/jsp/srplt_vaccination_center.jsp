<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.model.VaccinationCenter.CenterType"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
    <tr>
        <td>Center ID<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.PROGRAM_ID.FILTER_NAME()%>"></c:set>
        <input type="text" id="centerid" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
     	<td>Part of Name<br>
     	<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.NAME_PART.FILTER_NAME()%>"></c:set>
        <input type="text" id="centername" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
        <td>Type<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.CENTER_TYPE.FILTER_NAME()%>"></c:set>
        <input type="hidden" id="centerTypeVal" value="${model[nextSearchFieldNameValue]}"/> 
        <select id="centerType" name="${nextSearchFieldNameValue}">
           	<option></option>
           	<c:forEach  items="<%= CenterType.values() %>" var="centyp">
       		<option>${centyp}</option>
          	</c:forEach>               
        </select>
        <script><!--
            sel = document.getElementById("centerType");
            val=document.getElementById("centerTypeVal").value;
			makeValueSelectedInDD(sel,val);
        //-->
        </script>
        </td>
        <td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
		<td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_VACCINATION_CENTER_CSV%>');" class="linkiconM iconcsv"></a></td>
   </tr>
</table>
</form>