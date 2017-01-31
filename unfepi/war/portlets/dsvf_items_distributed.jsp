<%@ include file="/WEB-INF/template/include.jsp"%>

<div class="dvwform">
<table>
<thead>
<tr>
	<th>Child</th>
	<th>Date</th>
	<th>Item</th>
	<th>Item Count</th>
</tr>
</thead>
<tbody>
<c:forEach items="${model.itemdistributed}" var="item" varStatus="varststus">
<tr>
<%-- <td><a onClick="viewImmunizationDetails(this.text);" class="anchorCustom">${item['identifier']}</a></td> --%>
<td>${item['identifier']}</td>
<td>${item['distributedDate']}</td>
<td>${item['items']}</td>
<td>${item['count']}</td>
</tr>
</c:forEach>
</tbody>
</table>
</div>