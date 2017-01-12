<%@ include file="/WEB-INF/template/include.jsp"%>
<style>
<!--
#report{
	text-align: center;
 	border-collapse: collapse;
}
#report th{
}
.dvwform td{
	border: thin solid #c3beb3;
}
.td_color{
	background-color:#fdf4e7;
}
.dvwform .th_color{
	background-color:#d9d7d5;
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
				<th colspan="3" class="th_color">Round</th>
<%-- 				<th colspan="${fn:length(model.vaccines) + 2}"></th> --%>
				<th class="th_color" rowspan="2">site</th>
				<c:forEach items="${model.vaccines}" var="vname">
					<th rowspan="2" style="background-color: #e5e4e3">${vname['name']}</th>
				</c:forEach>
				<th class="th_color" rowspan="2">total</th>
			</tr>
			<tr>
				<th class="th_color">day</th>
				<th class="th_color">date</th>
				<th class="th_color" style="width:1cm;">roundId</th>
				
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${model.report}" var="report">
				<tr>
					<td class="td_color">${report['day']}</td>
					<td class="td_color">${report['date']}</td>
					<td class="td_color">${report['roundId']}</td>
					<td class="td_color" style="width: auto;">${report['site']}</td>
					<c:forEach items="${model.vaccines}" var="vname">
						<td style="width:1cm;" class="sum">${report[vname['name']]}</td>
					</c:forEach>
					<td class="td_color">${report['total']}</td>
				</tr>
			</c:forEach>		
			
			<tr style="height: 0.6cm;">
			<td colspan="4" style="background-color: #e5e4e3">total</td>
			<c:forEach items="${model.vaccines}" var="vtname">
					<td style="background-color: #d9d7d5">${model[vtname['name']]}</td>
			</c:forEach>
			<td style="background-color: #d9d7d5"></td>
			</tr>
			
		</tbody>
	</table>
</div>