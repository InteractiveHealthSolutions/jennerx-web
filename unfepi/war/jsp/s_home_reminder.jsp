<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp" %>

<div class="dvwform centerdivh" style="width: 600px">
<div class="formheadingWRule">Reminders Schedule</div>
<table>
			<tr>
<!-- 			<td colspan="16"><span class="formheadingS">Project Summary</span> -->
			<%-- <a class="linkiconS iconcsv" href="exportdata?extype=<%=SystemPermissions.DOWNLOAD_SUMMARY_PROJECT_CSV%>"></a> --%></td>
			</tr>
				<tr>
				<th>Sms Reminder</th>
				<th>Schedule Send Day</th>
				
			</tr>
			<tr>
				<td>SMS 1</td>
				<td>-1</td>
			</tr>
			<tr>
				<td>SMS 2</td>
				<td>0</td>
			</tr>
			<tr>
				<td>SMS 3</td>
				<td>6</td>
			</tr>
		</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>