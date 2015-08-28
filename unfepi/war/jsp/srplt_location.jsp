<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
    <tr>
 		<td>Part of Name<br>
     	<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.NAME_PART.FILTER_NAME()%>"></c:set>
        <input type="text" id="namelike" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
        <td>Type<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.TYPE.FILTER_NAME()%>"></c:set>
        <input type="hidden" id="typeVal" value="${model[nextSearchFieldNameValue]}"/> 
        <select id="type" name="${nextSearchFieldNameValue}">
           	<option></option>
           	<c:forEach  items="${model.locationTypes}" var="ty">
       		<option value="${ty.locationTypeId}">${ty.typeName}</option>
          	</c:forEach>               
        </select>
        <script><!--
            sel = document.getElementById("type");
            val=document.getElementById("typeVal").value;
			makeValueSelectedInDD(sel,val);
        //-->
        </script>
        </td>
        <td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
        <td><a href="s_location_tree.htm">view tree</a></td>
   </tr>
</table>
</form>