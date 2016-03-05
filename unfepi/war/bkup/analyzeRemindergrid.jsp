<%@ include file="/jsp/include.jsp"%>

<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<script type="text/javascript"><!--
function showDiv(obj){
	var trId=obj.title;
	var divId=obj.title+"dv";
	act=obj.value;
	if(act=="+"){
		document.getElementById(trId).style.display="table-row";
		document.getElementById(divId).style.display="table";

		obj.value="-";
	}
	else{
		document.getElementById(trId).style.display="none";
		document.getElementById(divId).style.display="none";
		
		obj.value="+";
	}
}
var win;
function viewVaccDet(obj){
	var pvid=obj.title;

	win=window.open('viewVaccinationwindow.htm?pvId='+pvid,'VaccinationDetails:'+pvid,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}
var win2;
function viewRemDet(obj){
	var rsid=obj.title;

	win2=window.open('viewReminderSmswindow.htm?rsId='+rsid,'ReminderSmsDetails:'+rsid,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win2.focus();
}
//-->
</script>
	<table class="datagridepi" width="100%">
    <thead class="header">
        <tr>
            <th><div >Child Id</div></th>
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <th><div >Child Name</div></th>
<%
}
%>         
			<th>---</th>
	
        </tr>
    </thead>
    <tbody class="rows" style="padding: 0;">
   	<c:forEach items="${model.record}" var="rec">
   	 <tr>
            <td><div ><a title="${rec.child.childId}"  onclick="viewChildDetails(this.title);" >${rec.child.childId}</a></div></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <td>
            <div>
            <c:out value="${rec.child.firstName}"></c:out> 
            <c:out value="${rec.child.middleName}"></c:out> 
            <c:out value="${rec.child.lastName}"></c:out>
            </div>
            </td>
<%
}
%> 
			<td>
			<table border="1" width="100%" class="datagridepi">
			<thead >
            <tr>
            <th>---</th>
            <th><div>---</div></th>
            <th><div>Due Date</div></th>
            <th><div>Vaccine</div></th>
            <th><div>Vaccination Status</div></th>
            <th><div>Is First ?</div></th>
            <th><div>Is Last ?</div></th>
            <th><div>Reminders Pending</div></th>
            <th><div>Any Reminder Late ?</div></th>
            <th><div>Max Days Late ?</div></th>
            <th><div>Reminders count</div></th>
            <th><div>Responses count</div></th>
            </tr>
            </thead>
            <tbody style="padding: 0;" class="alternaterows">

            <c:forEach items="${rec.record}" var="map">
 			<tr>
   	 		<td><input type="text" readonly="readonly" value="+" title="${map.vaccination.vaccinationRecordNum}d${rec.child.childId}p" class="expandDataButton" onclick="showDiv(this);"/></td>
            <td><div><a  title="${map.vaccination.vaccinationRecordNum}"  onclick="viewVaccDet(this);" >Details</a></div></td>           
            <td><div><fmt:formatDate value="${map.vaccination.vaccinationDuedate}" pattern="yyyy-MM-dd"/></div></td>      
            <td><div>${map.vaccination.vaccine.name}</div></td>           
            <td><div>${map.vaccination.vaccinationStatus}</div></td>           
            <td><div>${map.vaccination.isFirstVaccination}</div></td>           
            <td><div>${map.vaccination.isLastVaccination}</div></td>   
            <td><div>${map.remindersPending}</div></td>       
            <td><div>${map.anyReminderLate}</div></td> 
            <td><div>${map.maxDaysLate}</div></td>       
            <td><div>${fn:length(map.reminderSms)}</div></td>
            <td><div>${fn:length(map.response)}</div></td>
			</tr>
 			<tr id="${map.vaccination.vaccinationRecordNum}d${rec.child.childId}p" style="display: none;" >
      		<td></td>
       		<td colspan="11">
            <table border="0" id="${map.vaccination.vaccinationRecordNum}d${rec.child.childId}pdv" class="datagridepi" width="100%">
            <tr><td>
            REMINDER<br>
            <table border="1" width="100%" class="datagridepi">
            <thead style="background-color: silver">
            <tr>
                <th>---</th>
            	<th>Due Time</th>
            	<th>Sent Time</th>
            	<th>Reminder</th>
            	<th>Reminder Status</th>
            	<th>Is Late ?</th>
            	<th>Days Late</th>
            	<th>Hours Late</th>
            	<th>Cell Number</th>
            	<th>Day Number</th>
            </tr>
            </thead>
			<tbody style="padding: 0;" class="rows" > 
            <c:forEach items="${map.reminderSms}" var="remh">
            <tr >
            <td><a  title="${remh.rsmsRecordNum}"  onclick="viewRemDet(this);" >View</a></td>
            <td> <div ><c:out value="${remh.dueDate}"></c:out></div></td>
            <td><div><c:out value="${remh.sentDate}"></c:out></div></td>
            <td><div ><c:out value="${remh.reminder.name}"></c:out></div></td>
            <td><div ><c:out value="${remh.reminderStatus}"></c:out></div></td>
            <td><div ><c:out value="${remh.isSmsLate}"></c:out></div></td>
            <td><div ><c:out value="${remh.dayDifference}"></c:out></div></td>
            <td><div ><c:out value="${remh.hoursDifference}"></c:out></div></td>
            <td><div><c:out value="${remh.cellnumber}"></c:out></div></td>
            <td><div><c:out value="${remh.dayNumber}"></c:out></div></td>
            </tr>
            </c:forEach>
            </tbody>         
            </table>
			<br>
			RESPONSE<br>
			<table border="1" width="100%" class="datagridepi">
            <thead style="background-color: silver">
            <tr>
            	<th>Recieve Time</th>
            	<th>System Recieve Time</th>
            	<th>Cell Number</th>
            	<th>Response Text</th>
            	<th>Type</th>
            	
            </tr>
            </thead>
			<tbody  style="padding: 0;" class="rows">
            <c:forEach items="${map.response}" var="resp">
            <tr>
            <td><div><c:out value="${resp.recieveDate}"></c:out></div></td>
            <td><div><c:out value="${resp.actualSystemDate}"></c:out></div></td>
            <td><div><c:out value=">${resp.cellNo}"></c:out></div></td>
            <td><div><c:out value=">${resp.responseText}"></c:out></div></td>
            <td><div><c:out value="${resp.responseType}"></c:out></div></td> 
            </tr>
           </c:forEach> 
            </tbody>
            </table>
            </td></tr>
            </table>
            </td>
            </tr>
           </c:forEach>
           </tbody>
           </table>
          </td>
        </tr>
      </c:forEach>
    </tbody>
</table>


