<%@ include file="/WEB-INF/template/include.jsp"%>
<style>
<!--
#report{
	text-align: center;
 	border-collapse: collapse;
}
#report th{
/* 	padding: 4px 2px 4px 2px; */
}
.dvwform td{
	border: thin solid #c3beb3;
}
.td_0{
	background-color:#fcefdc;
}
.td_2{
/* 	background-color:#f8fce6; */
}
.td_1{
/*  	background-color:#f8fce6; */
}
.td_3{
	background-color:#fcefdc;
}
.td_count{
	background-color: #e5e4e3 ;
}
.td_total{
	padding: 4px 2px 4px 2px;
	background-color: #e5e4e3;
	font-weight: bold;
}
.dvwform .th_0{
	background-color:#d9d7d5;
}
.dvwform .th_1{
	background-color:#e5e4e3;
}
.dvwform tr:HOVER >td{
 background-color: #f8f0bc;
}

-->
</style>

<div class="dvwform">
<table id="report">
<thead>
<tr>
	
	<th colspan="3" class="th_0">Round</th>
	<th rowspan="2" class="th_0">Site</th>
	<th colspan="3" class="th_1">Enrollment</th>
	<th colspan="3" class="th_1">FollowUp</th>
	<th rowspan="2" class="th_0">Total</th>
</tr>
<tr>
	<th class="th_0">day</th>
	<th class="th_0">date</th>
	<th class="th_0">id</th>
	<th class="th_1">0-11 M</th>
	<th class="th_1">12-23 M</th>
	<th class="th_1">24-59 M</th>
	<th class="th_1">0-11 M</th>
	<th class="th_1">12-23 M</th>
	<th class="th_1">24-59 M</th>
</tr>

</thead>
<tbody>
	<c:forEach items="${model.report}" var="report">
				<tr>
					
					<td class="td_0">${report['day']}</td>
					<td class="td_0">${report['dateEncounterEntered']}</td>
					<td class="td_0">${report['roundId']}</td>
					<td class="td_0">${report['siteName']}</td>
					<td class="td_1">${report['ENROLLMENT_0_11']}</td>
					<td class="td_1">${report['ENROLLMENT_12_23']}</td>
					<td class="td_1">${report['ENROLLMENT_24_59']}</td>
					<td class="td_2">${report['FOLLOWUP_0_11']}</td>
					<td class="td_2">${report['FOLLOWUP_12_23']}</td>
					<td class="td_2">${report['FOLLOWUP_24_59']}</td>
					<td class="td_3">${report['count']}</td>
				</tr>
	</c:forEach>
	
	<tr>
	<td colspan="4" class="td_total">subtotal</td>
	<td class="td_count">${model.recordsum[0].ENROLLMENT_0_11}</td>
	<td class="td_count">${model.recordsum[0].ENROLLMENT_12_23}</td>
	<td class="td_count">${model.recordsum[0].ENROLLMENT_24_59}</td>
	<td class="td_count">${model.recordsum[0].FOLLOWUP_0_11}</td>
	<td class="td_count">${model.recordsum[0].FOLLOWUP_12_23}</td>
	<td class="td_count">${model.recordsum[0].FOLLOWUP_24_59}</td>
	<td class="td_count"></td>
	</tr>
	
	<tr>
	<td colspan="4" class="td_total"> total</td>
	<td colspan="3" class="td_count">${model.recordsum[0].ENROLLMENT}</td>
	<td colspan="3" class="td_count">${model.recordsum[0].FOLLOWUP}</td>
	<td class="td_count"></td>
	</tr>
	
</tbody>
</table>
</div> 
