<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<div class="dvwform">
<table >
	<thead>
        <tr>
        	<th>---</th>
            <th>Recipient Number</th>
            <th>Due DateTime</th>
            <th>Sent DateTime</th>
            <th>Reminder Day Num</th> 
            <th>Sms Status</th>
            <th>Child Id</th>
            <!-- <th>Reference Number</th> -->
            <th>Vaccination Record Number</th>
            <th>Failure Cause</th>
            <th>Reminder Type</th>
<%
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){%>
            <th>Reminder Id</th>
            <th>Reference Num</th>
<%}%>
        </tr>
    </thead>
    <tbody class="rows">
   <c:forEach items="${model.datalist}" var="sms">
   <tr>
   	 		<td><input type="text" readonly="readonly" value="+" title="${sms.rsmsRecordNum}r" class="expandDataButton" onclick="expandRecord(this);"/></td>
            <td><c:out value="${sms.recipient}"></c:out></td>
            <td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${sms.dueDate}" /></td>
            <td><fmt:formatDate pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>" value="${sms.sentDate}"/></td>
            <td>${sms.dayNumber}</td>
            <td><c:out value="${sms.reminderStatus}"/></td>
   			<td><a onclick="viewChildDetails(this.text)" class="anchorCustom">${sms.vaccination.child.idMapper.identifiers[0].identifier}</a></td>
            <%-- <td><c:out value="${sms.referenceNumber}"></c:out></td> --%>
            <td>${sms.vaccinationRecordNum}</td>
            <td>${sms.smsCancelReason}</td>
            <td>${sms.reminder.reminderType}</td>
<%
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){%>
            <td>${sms.rsmsRecordNum}</td>
            <td>${sms.referenceNumber}</td>
<%}%>
     </tr>
     <tr id="${sms.rsmsRecordNum}r" style="display: none;" >
       	<td colspan="80" class="dvwinner"><div>${sms.text}</div></td>
     </tr>
  </c:forEach>
   	<% ServiceContext sc = (ServiceContext)request.getAttribute("sc");
 	//must close session here; or do it in finally of respective controller else can create memory leak
 	sc.closeSession();%>
  </tbody>
</table>
</div>


