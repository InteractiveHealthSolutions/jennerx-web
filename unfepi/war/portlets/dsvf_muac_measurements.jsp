<%@ include file="/WEB-INF/template/include.jsp"%>

<div class="dvwform">
<table>
<thead>
<tr>
	<th>Child</th>
	<th>Date</th>
	<th>Color</th>
</tr>
</thead>
<tbody>
<c:forEach items="${model.muacmeasurements}" var="muac" varStatus="varststus">
<tr>
<td><a onClick="viewImmunizationDetails(this.text);" class="anchorCustom">${muac['identifier']}</a></td>
<td>${muac['measureDate']}</td>
<td>${muac['colorrange']}</td>
</tr>
</c:forEach>
</tbody>
</table>
