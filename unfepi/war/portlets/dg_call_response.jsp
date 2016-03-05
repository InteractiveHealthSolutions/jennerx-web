<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<div class="dvwform">
<table >
	<thead>
        <tr>
        	<!-- <th></th> -->
        	<th></th>
            <th>ID</th>
            <th>Caller</th>
            <th>Call Date</th>
            <th>Call Type</th>
            <th>Project Phone</th>
        </tr>
    </thead>
   <tbody>
   <c:forEach items="${model.datalist}" var="resp">
   	 <tr>
  	 		<%-- <td><input type="text" readonly="readonly" value="+" title="${resp.responseId}r" class="expandDataButton" onclick="expandRecord(this);"/></td> --%>
            <td style="width: 60px !important;"><a id="${resp.responseId}anc" class="linkiconS iconexpand" onclick="expandD('${resp.class.name}', ${resp.responseId});" value="+"></a>
			<a class="linkiconS iconnote" onclick="openNoteWindow('${resp.class.name}', ${resp.responseId})"></a>
   			<a class="linkiconS iconreply" onclick="openReplyWindow('${resp.mappedId}', '${resp.class.name}', ${resp.responseId}, '${resp.originator}')"></a>
			</td>
            <td><c:out value="${resp.idMapper.identifiers[0].identifier}"></c:out></td>
            <td><c:out value="${resp.originator}"></c:out></td>
            <td><fmt:formatDate value="${resp.responseDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
            <td><c:out value="${resp.responseType}"></c:out></td>
            <td><c:out value="${resp.recipient}"></c:out></td>
     </tr>
     <tr id="${resp.responseId}r" style="display: none;" >
       	<td colspan="80" class="dvwinner">
       	<div>
			<table id="${resp.responseId}tbl">
			</table>
		</div>
		</td>
     </tr>
  </c:forEach>
   	<%
   		ServiceContext sc = (ServiceContext) request.getAttribute("sc");
   		//must close session here; or do it in finally of respective controller else can create memory leak
   		sc.closeSession();
   	%>
  </tbody>
</table>
</div>

