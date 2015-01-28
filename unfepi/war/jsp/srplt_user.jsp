<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.model.User.UserStatus"%>

<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" >
<table>
	<tr>
		<td>Login Id<br>
		<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.USERNAME.FILTER_NAME()%>"></c:set>
		<input type="text" id="userId" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}" />
		</td>
		<td>Name<br>
		<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.NAME_PART.FILTER_NAME()%>"></c:set>
		<input type="text" id="partOfName" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/>
		</td>
		<td>Program Id<br>
		<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.PROGRAM_ID.FILTER_NAME()%>"></c:set>
		<input type="text" id="programId" name="${nextSearchFieldNameValue}" value="${model[nextSearchFieldNameValue]}"/>
		</td>
		<td>Status<br>
		<c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.USER_STATUS.FILTER_NAME()%>"></c:set>
		<input type="hidden" id="userstatusVal" value="${model[nextSearchFieldNameValue]}"/>
		<select id="userstatus"	name="${nextSearchFieldNameValue}">
			<option></option>
			<c:forEach items="<%=UserStatus.values()%>" var="sts">
			<option>${sts}</option>
			</c:forEach>
		</select>
		<script>
			sel = document.getElementById("userstatus");
			val = document.getElementById("userstatusVal").value;
			makeValueSelectedInDD(sel,val);
		</script>
		</td>
		<td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
		<td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_USER_CSV%>');" class="linkiconM iconcsv"></a></td>
	</tr>
</table>
</form>
