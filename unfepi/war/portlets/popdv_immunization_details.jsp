<%@page import="java.util.Locale"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<c:catch var="catchException">
	<c:out value="${lmessage}"></c:out>

	<div class="dvwform">
		<table>
		<c:forEach items="${model.columnNames}" var="col">
		<c:if test="${fn:containsIgnoreCase(col, 'VaccineId') || fn:containsIgnoreCase(col, 'village')}">
		<tr style="height:0.1cm;"><td style="background-color: #e59308;" colspan="2"></td></tr>
        </c:if>
		<tr>
		<td><b>${col}</b></td><td>${model.data[col]}</td>
		</tr>
		</c:forEach>
		</table>
	</div>
</c:catch>

<c:if test="${catchException!=null}">
	<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException} </span>
</c:if>