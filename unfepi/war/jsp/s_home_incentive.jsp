<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp" %>

<div class="dvwform centerdivh" style="width: 600px">
<div class="formheadingWRule">CCT for Caregivers</div>
<table>
			<tr>
<!-- 			<td colspan="16"><span class="formheadingS">Project Summary</span> -->
			<%-- <a class="linkiconS iconcsv" href="exportdata?extype=<%=SystemPermissions.DOWNLOAD_SUMMARY_PROJECT_CSV%>"></a> --%></td>
			</tr>
				<tr>
				<th>Vaccine Name</th>
				<th>Conditional Cash Transfer (PKR)</th>
				
			</tr>
			<tr>
				<td>BCG/OPV0</td>
				<td>200</td>
			</tr>
			<tr>
				<td>Penta1/OPV1/PCV1</td>
				<td>200</td>
			</tr>
			<tr>
				<td>Penta2/OPV2/PCV2</td>
				<td>200</td>
			</tr>
			<tr>
				<td>Penta3/OPV3/PCV3</td>
				<td>200</td>
			</tr>
			<tr>
				<td>Measles1</td>
				<td>200</td>
			</tr>
			<tr>
				<td>Measles2</td>
				<td>200</td>
			</tr>
		</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>