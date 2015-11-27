<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<div class="dvwform">
<table >
	<thead>
        <tr>
			<th>---</th>
			<th>Encounter</th>
			<th>Person1 ID</th>
			<th>Person2 ID</th>
			<th>Encounter Type</th>
			<th>Data Entry Source</th>
			<th>Entered Date</th>
			<th>Start Date</th>
			<th>End Date</th>
			<th>User</th>
			<th>Details</th>
		</tr>
    </thead>
    <tbody>
   <c:forEach items="${model.datalist}" var="enc">
   	 <tr>
   	 <td><a  title="encid=${enc.id.encounterId}&p1=${enc.p1.identifiers[0].identifier}&p2=${enc.p2.identifiers[0].identifier}"  onclick="viewEncounterDetails(this.title);" class="linkiconS iconviewdetails"></a></td>
			<td><c:out value="${enc.id.encounterId}"></c:out></td>
			<td><c:out value="${enc.p1.identifiers[0].identifier}"></c:out></td>
            <td><c:out value="${enc.p2.identifiers[0].identifier}"></c:out></td>
            <td class="lowercase"><c:out value="${enc.encounterType}"></c:out></td>
            <td class="lowercase"><c:out value="${enc.dataEntrySource}"/></td>
            <td><fmt:formatDate value="${enc.dateEncounterEntered}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
			<td><fmt:formatDate value="${enc.dateEncounterStart}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
			<td><fmt:formatDate value="${enc.dateEncounterEnd}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
            <td><c:out value="${enc.createdByUser.username}"/></td>
            <td><c:out value="${enc.detail}"/></td>
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>


