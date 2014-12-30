<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
    <tr>
        <td>Role name<br>
   		<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.ROLE_NAME.FILTER_NAME()%>"></c:set>
        <input type="text" id="roleName" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
        <td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
    </tr>
</table>
</form>