<%@ include file="/WEB-INF/template/include.jsp"%>

<div class="dvwform">
<table>
<thead>
<tr>
	<th>Id</th>
	<th>Name</th>
	<th>Unit per pack</th>
	<th>Quantity</th>
	<th></th>
</tr>
</thead>
<tbody>
<c:forEach items="${model.itemStocks}" var="item" varStatus="varststus">
<tr>
<td>${item.itemRecordNum}</td>
<td>${item.name}</td>
<td>${item.unit_per_pack}</td>
<td>${item.quantity}</td>
<td><a href="edititem.htm?iid=${item.itemRecordNum}" class="icon"><img alt="edit" src ="images/edit-icon.png" class="icon"></a></td>
</tr>
</c:forEach>
</tbody>
</table>
</div>