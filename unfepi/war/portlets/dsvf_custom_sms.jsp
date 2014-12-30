<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.context.ServiceContext"%>

<div class="dvwform">
<table >
	<thead>
        <tr>
        	<th></th>
            <th>Cell Number</th>
            <th>Due DateTime</th>
            <th>Sent DateTime</th>
            <th>Sms Status</th>
            <th>Recipient ID</th>
            <th>Failure Cause</th>
            <th>Creator</th>
            <th>Description</th>
        </tr>
    </thead>
   <tbody>
   <c:forEach items="${model.datalist}" var="sms">
   	 <tr>
   	 		<td><a value="+" title="${sms.usmsRecordNum}r" class="linkiconS iconexpand" onclick="expandRecord(this);"></a></td>
            <td><c:out value="${sms.recipient}"></c:out></td>
            <td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${sms.dueDate}" /></td>
            <td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${sms.sentDate}"/></td>
            <td><c:out value="${sms.smsStatus}"/></td>
   			<td>${sms.idMapper.identifiers[0].identifier}</td>
            <%-- <td><c:out value="${sms.referenceNumber}"></c:out></td> --%>
            <td>${sms.smsCancelReason}</td>
            <td>${sms.createdByUserId.username}</td>
            <td>${sms.description}</td>
     </tr>
     <tr id="${sms.usmsRecordNum}r" style="display: none;" >
       	<td colspan="80" class="dvwinner"><div>${sms.text}</div></td>
     </tr>
  </c:forEach>
   	<% ServiceContext sc = (ServiceContext)request.getAttribute("sc");
 	//must close session here; or do it in finally of respective controller else can create memory leak
 	sc.closeSession();%>
  </tbody>
</table>
</div>


