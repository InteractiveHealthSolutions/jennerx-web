<%@ include file="/WEB-INF/template/include.jsp"%>

<div class="dvwform">
<table >
	<thead>
        <tr>
            <th>---</th>
            <th>Name</th>
            <th>Type</th>
            <th>Size(KB)</th>
            <th>Date Created</th>
            <th>Creator</th>
        </tr>
    </thead>
   <tbody class="rows">
   <c:forEach items="${model.datalist}" var="dolb">
   	 <tr>
   	 		<td><a href="downloadReport?record=${dolb.downloadableId}" class="linkiconS iconcsv"></a></td>
            <td><c:out value="${dolb.downloadableName}"></c:out></td>
            <td><c:out value="${dolb.downloadableType}"></c:out></td>
            <td><c:out value="${dolb.sizeBytes/(1000)}"></c:out></td>
            <td><fmt:formatDate value="${dolb.createdDate}" pattern="dd-MM-yyyy hh:mm:ss"/></td>
            <td></td>
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>


