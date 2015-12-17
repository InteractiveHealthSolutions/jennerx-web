<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<div class="dvwform">
<table >
<tr>
<td colspan="2"><input class="easyui-searchbox" data-options="prompt:'Enter Women ID',searcher:doSearch" value="${param.womenId}"/>
<span class="error-message">${model.errorMessage}</span>
	<script>
		function doSearch(value){
			window.location="womenDashboard.htm?womenId="+value;
		}
	</script>
</td>
<td colspan="2"><a href="addWomen.htm" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="float: right;">Enrollment</a>
</td>
</tr>
</table>
</div>
<c:if test="${not empty model.datalist}">
<div class="dvwform">
<table>
<tr>
<td colspan="100"><a href="womenFollowup.htm?women_id=${model.datalist.women.mappedId}" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="float: right;"> Follow up </a></td>
</tr>
<tr>
    <td colspan="3" class="headerrow">
    </td>
</tr>
<tr>
<td colspan="2">Father Name : ${model.datalist.women.fatherFirstName} ${model.datalist.women.fatherLastName}</td>
<td>Address : 
<c:forEach items="${model.datalist.address}" var="add">
${add.addHouseNumber} ${add.addStreet} ${add.addSector} ${add.addArea} ${add.addDistrict} ${add.addColony} ${add.addtown} UC:${add.addUc}
<br>
</c:forEach></td></tr>
<tr><td colspan="2">Birthdate : <fmt:formatDate value="${model.datalist.women.birthdate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> <c:if test="${model.datalist.women.estimatedBirthdate}">(approx)</c:if></td>
<td>Contacts :<br>
<c:forEach items="${model.datalist.contacts}" var="cont">
${fn:substring(cont.numberType,0,3)}- ${cont.number}<br>
</c:forEach>
</td>
</tr>
</table>
<table class="right">
<tr>
    <td colspan="20" class="headerrow-light left">Vaccination History</td>
</tr>
        <tr>
       		<th class="center">Vaccine</th>
       		<th class="center">Vaccination Date</th>
            <th class="center">Vaccination Duedate</th>
            <th class="center">Center ID</th>
            <th class="center">Vaccinator</th>
            <th class="center">Vaccination Record Num</th>
        </tr>
        <c:forEach items="${model.datalist.vaccinations}" var="vacc">
        	<c:set var="enrvaccclass" value=""></c:set>
   			<c:if test="${vacc.vaccine.vaccineId == model.datalist.women.enrollmentVaccineId}">
   			<c:set var="enrvaccclass" value="datahighlight"></c:set></c:if>
   		<tr class="enrvaccclass">
   	 		<td class="${enrvaccclass} left">${vacc.vaccine.name}</td>
            <td class="${enrvaccclass}"><fmt:formatDate value="${vacc.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td class="${enrvaccclass}"><fmt:formatDate value="${vacc.vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td class="${enrvaccclass}">${vacc.vaccinationCenter.idMapper.identifiers[0].identifier}</td>
            <td class="${enrvaccclass}">${vacc.vaccinator.idMapper.identifiers[0].identifier}</td>
            <td class="${enrvaccclass}">${vacc.vaccinationRecordNum}</td>
        </tr>
  </c:forEach>
</table>

</div>
</c:if>


