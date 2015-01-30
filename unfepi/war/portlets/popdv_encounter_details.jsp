<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<%@ include file="/WEB-INF/template/include.jsp"%>

<c:catch var="catchException">
<c:out value="${lmessage}"></c:out>
<div class="datalistform">
<table>
	 <tr>
	        <td>Encounter number</td><td><c:out value="${model.encounter.id.encounterId}" /></td>
	 </tr>  
	 <tr>          
	        <td>Person1 ID</td><td><c:out value="${model.encounter.p1.identifiers[0].identifier}"></c:out></td>
	 </tr>
     <tr>
            <td>Person2 ID</td> <td><c:out value="${model.encounter.p2.identifiers[0].identifier}"></c:out></td>
     </tr>
     <tr>
            <td>Encounter type</td> <td><c:out value="${model.encounter.encounterType}"></c:out></td>
     </tr>
     <tr>
            <td>Data entry Source</td> <td><c:out value="${model.encounter.dataEntrySource}"></c:out></td>
     </tr>
     <tr>
            <td>Entered date</td> <td><c:out value="${model.encounter.dateEncounterEntered}"></c:out></td>
     </tr>
     <tr>            
	        <td>Start date</td><td><c:out value="${model.encounter.dateEncounterStart}"></c:out></td>
	 </tr>
     <tr>
            <td>End date</td> <td><c:out value="${model.encounter.dateEncounterEnd}"></c:out></td>
     </tr>
     <tr>
            <td>User</td> <td><c:out value="${model.encounter.createdByUser.username}"></c:out></td>
     </tr>
      <tr>
            <td>Details</td> <td><c:out value="${model.encounter.detail}"></c:out></td>
     </tr>
     <tr>
        <td colspan="2" class="headerrow">Encounter Results</td>
    </tr>
    <c:forEach items="${model.encounterresults}" var="encres" >
    <c:choose>
    <c:when test="${not empty encres[3]}">
    <c:if test="${not empty encres[2] && groupname!= encres[2]}">
    <c:set value="${encres[2]}" var="groupname"></c:set>
    <tr><td colspan="10" class="headerrow-compact left">${encres[2]}</td></tr>
    </c:if>
   	<tr><td class="lowercase">${encres[3]}</td><td class="lowercase">${encres[1]}</td>
<%
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){%>
   	<td class="lowercase">${encres[0]}</td><td class="lowercase">${encres[2]}</td>
<%}%>
   	</tr>
    </c:when>
    <c:otherwise>
  	<tr><td class="lowercase">${encres[0]}</td><td class="lowercase">${encres[1]}</td>
<%
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){%>
  	<td>${encres[3]}</td><td>${encres[2]}</td>
<%}%>
  	</tr>
    </c:otherwise>
    </c:choose>
  	</c:forEach>
</table>
</div>
</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>
