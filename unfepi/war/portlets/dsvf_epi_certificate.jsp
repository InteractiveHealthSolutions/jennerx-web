<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<div class="dvwform">
<table >
	<thead>
        <tr>
        	<th>---</th>
            <th>Inquirer Mobile</th>
            <th>Inquiry DateTime</th>
            <th>Inquired ID</th>
            <th>Inquiry Status</th> 
            <th>Problem</th>
            <th>Solution</th>
            <th>Reply Due date</th>
            <th>Reply Sent date</th>
            <th>Reply Status</th>
            <th>Reply Failure Cause</th>
<%
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){%>
            <th>System Logging Date</th>
            <th>System Mobile</th>
            <th>Inquiry Reference</th>
            <th>Reply Reference</th>
            <th>Group ID</th>
            <th>Subject</th>
<%}%>
        </tr>
    </thead>
    <tbody class="rows">
   <c:forEach items="${model.datalist}" var="map">
   <tr>
   	 		<td><a title="${map.inquirysms.responseId}-${map.replysms.usmsRecordNum}rr" class="linkiconS iconexpand" onclick="expandRecord(this);" value="+"></a></td>
            <td><c:out value="${map.inquirysms.originator}"></c:out></td>
            <td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${map.inquirysms.responseDate}" /></td>
   			<td><a onclick="viewChildDetails(this.text)" class="anchorCustom">${map.inquirysms.idMapper.identifiers[0].identifier}</a></td>
            <td class="lowercase">${map.inquirysms.responseStatus}</td>
            <td class="lowercase">${map.inquirynote.problemGroup}</td>
            <td class="lowercase">${map.inquirynote.solutionGroup}</td>
            <td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${map.replysms.dueDate}"/></td>
            <td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${map.replysms.sentDate}"/></td>
            <td class="lowercase"><c:out value="${map.replysms.smsStatus}"/></td>            
            <td>${map.replysms.smsCancelReason}</td>
<%
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){%>
            <td>${map.inquirysms.systemLoggingDate}</td>
            <td>${map.inquirysms.recipient}</td>
            <td>${map.inquirysms.referenceNumber}</td>
            <td>${map.replysms.referenceNumber}</td>
            <td>${map.inquirynote.groupId}</td>
            <td>${map.inquirynote.subject}</td>
<%}%>
     </tr>
     <tr id="${map.inquirysms.responseId}-${map.replysms.usmsRecordNum}rr" style="display: none;" >
       	<td colspan="80" class="dvwinner"><div>INQUIRY: ${map.inquirysms.responseBody}</div><div>REPLY: ${map.replysms.text}</div></td>
     </tr>
  </c:forEach>
  </tbody>
</table>
</div>


