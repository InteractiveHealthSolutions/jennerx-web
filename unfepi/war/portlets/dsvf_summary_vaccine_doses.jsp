<%@ include file="/WEB-INF/template/include.jsp"%>
<style>
#report{
	text-align: center;
	border-spacing: 0px;
  	border-collapse: collapse;
 	vertical-align: text-bottom;
}
.dvwform td{
	border: thin solid #c3beb3;
}
.dvwform .th_0{
	background-color:#d9d7d5;
}
.dvwform .th_1{
	background-color:#e5e4e3;
}

.dvwform .td_0{
	background-color:#fcefdc;
}
.td_total{
	background-color:#d9d7d5;
}
.td_count{
	background-color:#e5e4e3;
}
.dvwform tr:HOVER >td{
 background-color: #f8f0bc;
}
</style>

<div class="dvwform">
	<table id="report">
		<thead>
			<tr>
				<th colspan="4" class="th_0" style="background-color:#d9d7d5;">Round</th>
				<th colspan="${model.doses}" class="th_1">0-11 M</th>
				<th colspan="${model.doses}" class="th_1">12-23 M</th>
				<th colspan="${model.doses}" class="th_1">24-59 M</th>
				<th rowspan="2" class="th_0" style="background-color:#d9d7d5;">total</th>
			</tr>
			<tr>
				<th class="th_0" style="background-color:#d9d7d5;">day</th>
				<th class="th_0" style="background-color:#d9d7d5;">date</th>
				<th class="th_0" style="background-color:#d9d7d5;">roundId</th>
				<th class="th_0" style="background-color:#d9d7d5;">Site</th>
				<c:forEach var="vac" items="${model.vaccineL}" varStatus="s" >
					<th class="th_1"> dose ${s.index+1}</th>
				</c:forEach>
				<c:forEach var="vac" items="${model.vaccineL}" varStatus="s" >
					<th class="th_1"> dose ${s.index+1}</th>
				</c:forEach>
				<c:forEach var="vac" items="${model.vaccineL}" varStatus="s" >
					<th class="th_1"> dose ${s.index+1}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${model.report}" var="report">
				<tr>
					<td class="td_0">${report['day']}</td>
					<td class="td_0">${report['date']}</td>
					<td class="td_0">${report['roundId']}</td>
					<td class="td_0">${report['site']}</td>
					
					<c:forEach var="vac" items="${model.vaccineL}" varStatus="s" >
						<c:set var="nextdose" value="${vac.name}_0_11" />
						<td class="td_1">${report[nextdose]}</td>
					</c:forEach>
					<c:forEach var="vac" items="${model.vaccineL}" varStatus="s" >
						<c:set var="nextdose" value="${vac.name}_12_23" />
						<td class="td_1">${report[nextdose]}</td>
					</c:forEach>
					<c:forEach var="vac" items="${model.vaccineL}" varStatus="s" >
						<c:set var="nextdose" value="${vac.name}_24_59" />
						<td class="td_1">${report[nextdose]}</td>
					</c:forEach>
					
					<td class="td_0">${report['count']}</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="4" class="td_total">subtotal</td>
				
				<c:forEach var="vac" items="${model.vaccineL}" varStatus="s" >
					<c:set var="nextdose" value="${vac.name}_0_11" />
					<td class="td_count">${model.recordsum[0][nextdose]}</td>
				</c:forEach>
				<c:forEach var="vac" items="${model.vaccineL}" varStatus="s" >
					<c:set var="nextdose" value="${vac.name}_12_23" />
					<td class="td_count">${model.recordsum[0][nextdose]}</td>
				</c:forEach>
				<c:forEach var="vac" items="${model.vaccineL}" varStatus="s" >
					<c:set var="nextdose" value="${vac.name}_24_59" />
					<td class="td_count">${model.recordsum[0][nextdose]}</td>
				</c:forEach>
				<td class="td_total"></td>
			</tr>
			<tr>
			<td colspan="4" class="td_total">total</td>
			<td colspan="${model.doses}" class="td_count">${model.recordsum[0]['total_0_11']}</td>
			<td colspan="${model.doses}" class="td_count">${model.recordsum[0]['total_12_23']}</td>
			<td colspan="${model.doses}" class="td_count">${model.recordsum[0]['total_24_59']}</td>
			<td class="td_total"></td>
			</tr>
		</tbody>
	</table>
</div>