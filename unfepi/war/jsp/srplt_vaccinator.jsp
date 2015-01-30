<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.model.Model.Gender"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
    <tr>
	    <td>Vaccinator ID<br>
	    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.PROGRAM_ID.FILTER_NAME()%>"></c:set>
	    <input type="text" id="vaccinatorid" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
	    <td>Vaccination Center<br>
	    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.VACCINATION_CENTER.FILTER_NAME()%>"></c:set>
		<select id="vaccinationCenterddp" name="${nextSearchFieldNameValue}" bind-value="${model[nextSearchFieldNameValue]}">
		   	<option></option>
		  	<c:forEach items="${model.vaccinationCenters}" var="vcenter"> 
		   	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier}:${vcenter.name}</option>
		   	</c:forEach> 
		</select>
		</td>
	    <td>Part of Name<br>
	    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.NAME_PART.FILTER_NAME()%>"></c:set>
	    <input type="text" id="vaccinatorname" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
	    <td>Gender<br>
	    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.GENDER.FILTER_NAME()%>"></c:set>
	    <select id="gender" name="${nextSearchFieldNameValue}" bind-value="${model[nextSearchFieldNameValue]}">
	     	<option></option>
	      	<c:forEach  items="<%= Gender.values() %>" var="gen">
	    	<option>${gen}</option>
	    	</c:forEach>               
	    </select>
	    </td>
	    <td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
		<td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_VACCINATOR_CSV%>');" class="linkiconM iconcsv"></a></td>
    </tr>
</table>
</form>