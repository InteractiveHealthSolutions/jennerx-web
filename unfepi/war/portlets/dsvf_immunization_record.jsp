<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<div class="dvwform">
<div class="error">Data updated at <fmt:formatDate value="${model.dateDmpUpdated}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/>
<%
boolean isAuth = UserSessionUtils.hasActiveUserPermission(SystemPermissions.DO_FORCE_UPDATE_IMMUNIZATION_RECORDS,request);
if(isAuth){
%>
 - <div style="width: 120px; display: inline-block;"><a href="forceUpdateImmunizationRecords" class="linkiconM iconrefresh leftalign"></a> Force update</div>
<%} %>
<div style="float: right;">${model.dataLocationMessage}</div>
</div>

<table >
	<thead>
        <tr>
			<th>Child ID</th>
			<!-- <th>Child Name</th>
			<th>Father Name</th> -->
			<!-- <th>Gender</th> -->
			<th>Birthdate</th>
			<!-- <th>Enrollment Date</th> -->
			<th>EPI Number</th>
			<th>Enrollment Vaccine</th>
			<!-- <th>Enrollment Center</th> -->
<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.COLUMNS.FILTER_NAME()%>"></c:set>
<c:forEach items="${model[nextSearchFieldNameValue]}" var="coln">
			<th class="datahighlight"><%=((String)pageContext.getAttribute("coln")).replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2")%></th>
</c:forEach>
			<th>BCG Date</th>
			<th>DTC-Hib-HepB-1 Date</th>
			<th>DTC-Hib-HepB-2 Date</th>
			<th>DTC-Hib-HepB-3 Date</th>
			<th>Rougeole1 Date</th>
			<th>Rougeole2 Date</th>
			<th>VPO-0 Date</th>
			<th>VPO-1 Date</th>
			<th>VPO-2 Date</th>
			<th>VPO-3 Date</th>
			<th>PCV-1 Date</th>
			<th>PCV-2 Date</th>
			<th>PCV-3 Date</th>
		</tr>
    </thead>
    <tbody>
   <c:forEach items="${model.datalist}" var="map">
   	 <tr>
   	 <td><a onClick="viewImmunizationDetails(this.text);" class="anchorCustom">${map.ChildId}</a></td>
			<%-- <td>${map.ChildFirstName} ${map.ChildLastName}</td>
			<td>${map.FatherFirstName} ${map.FatherLastName}</td> --%>
            <%-- <td>${map.Gender}</td> --%>
            <td><fmt:formatDate value="${map.Birthdate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
			<%-- <td><fmt:formatDate value="${map.DateEnrolled}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td> --%>
            <td><c:out value="${map.EnrollmentEPINumber}"></c:out></td>
            <td><c:out value="${map.EnrollmentVaccine}"/></td>
            <%-- <td><c:out value="${map.EnrollmentCenter}"/></td> --%>
<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.COLUMNS.FILTER_NAME()%>"></c:set>
<c:forEach items="${model[nextSearchFieldNameValue]}" var="coln">
			<td class="datahighlight">${map[coln]}</td>
</c:forEach>

            <td><fmt:formatDate value="${map.BCGVaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.PENTA1VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.PENTA2VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.PENTA3VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.MEASLES1VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.MEASLES2VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.OPV0VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.OPV1VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.OPV2VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.OPV3VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.PCV1VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.PCV2VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${map.PCV3VaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>


