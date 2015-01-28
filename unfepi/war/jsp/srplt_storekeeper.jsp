<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.model.Model.Gender"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
    <tr>
    	<td>Storekeeper ID<br>
    	<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.PROGRAM_ID.FILTER_NAME()%>"></c:set>
        <input type="text" id="storekeeperid" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
        <td>Part of Name<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.NAME_PART.FILTER_NAME()%>"></c:set>
        <input type="text" id="storekeepername" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
        <td>Store Name<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.STORE_NAME.FILTER_NAME()%>"></c:set>
        <input type="text" id="storename" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/> </td>
        <td></td>
        <td>Gender<br>
        <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.GENDER.FILTER_NAME()%>"></c:set>
        <input type="hidden" id="genderVal"  value="${model[nextSearchFieldNameValue]}"/> 
        <select id="gender" name="${nextSearchFieldNameValue}">
           	<option></option>
           	<c:forEach  items="<%= Gender.values() %>" var="gen">
        	<option>${gen}</option>
           	</c:forEach>               
        </select>
        <script><!--
            sel = document.getElementById("gender");
            val=document.getElementById("genderVal").value;
            makeTextSelectedInDD(sel,val);
         //-->
         </script>
         </td>
         <td>Closest Vaccination Center<br>
         <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.VACCINATION_CENTER.FILTER_NAME()%>"></c:set>
		 <input type="hidden" id="vaccinationCenterIdinh" value="${model[nextSearchFieldNameValue]}"/>
	     <select id="vaccinationCenterddp" name="${nextSearchFieldNameValue}" >
	       	<option></option>
	       	<c:forEach items="${model.vaccinationCenters}" var="vcenter"> 
	       	<option value="${vcenter.mappedId}">${vcenter.idMapper.identifiers[0].identifier}:${vcenter.name}</option>
	       	</c:forEach> 
	     </select>
	     <script><!--
	        sel = document.getElementById("vaccinationCenterddp");
	        val=document.getElementById("vaccinationCenterIdinh").value;
	        makeValueSelectedInDD(sel,val);
	      //-->
	     </script>    
		</td>
        <td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
    </tr>
</table>
</form>