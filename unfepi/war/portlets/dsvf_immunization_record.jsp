<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
	<script src="immunization.js" type="text/javascript"></script>
	<script type="text/javascript" >
	function callViewImmunizationDetails(childIdentifier){
		viewImmunizationDetails(childIdentifier, $("#calendersearchoption").val() );
	}
	</script>

<div class="dvwform">
	<div class="error">
		Data updated at
		<fmt:formatDate value="${model.dateDmpUpdated}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" />
		<%
			boolean isAuth = UserSessionUtils.hasActiveUserPermission(SystemPermissions.DO_FORCE_UPDATE_IMMUNIZATION_RECORDS,request);
			if (isAuth) {
		%>
		-
		<div style="width: 120px; display: inline-block;">
			<a href="forceUpdateImmunizationRecords" class="linkiconM iconrefresh leftalign"></a> Force update
		</div>
		<%
			}
		%>
		<div style="float: right;">${model.dataLocationMessage}</div>
	</div>

	<table>
		<thead>
			<tr>
				<th>Child ID</th>
				<th>Birthdate</th>
				<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.COLUMNS.FILTER_NAME()%>"></c:set>
				<c:forEach items="${model[nextSearchFieldNameValue]}" var="coln">
					<th class="datahighlight"><%=((String) pageContext.getAttribute("coln")).replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2")%></th>
				</c:forEach>
				<c:forEach items="${model.vaccineNames}" var="vname">
					<th>${vname.name}</th>
				</c:forEach>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${model.datalist}" var="map">
				<tr>
					<td><a id="childIdentifier" onClick="callViewImmunizationDetails(this.text)" class="anchorCustom">${map.identifier}</a></td>

					<td><fmt:formatDate value="${map.birthdate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>" /></td>

					<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.COLUMNS.FILTER_NAME()%>"></c:set>
					<c:forEach items="${model[nextSearchFieldNameValue]}" var="coln">
						<td class="datahighlight">${map[coln]}</td>
					</c:forEach>
						
					<c:forEach items="${model.vaccineNames}" var="vname">
						<c:set var="nextvac" value="${fn:toUpperCase(vname.name)}VaccinationDate"></c:set>						
						<td><fmt:formatDate value="${map[nextvac]}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>" /></td>
					</c:forEach>	
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>


