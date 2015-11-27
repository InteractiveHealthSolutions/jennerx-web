<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.context.Context"%>
<%@page import="org.ird.unfepi.context.LoggedInUser"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

	<div class="datagridepi">
	<table >
	<thead>
        <tr>
            <th>Vaccinator Id</th>
            <th>Amount Due</th>
            <th>Amount Paid</th>
            <th>Transaction Date/ Incentivization Date</th>
            <th>Transaction Status</th>
            <th>Paid Date</th>
            <th>Paid By</th>
        </tr>
    </thead>
   <tbody class="rows">
   <c:forEach items="${model.transactions}" var="trans">
  	 <tr>
			<td><c:out value="${trans.vaccinator.idMapper.identifiers[0].identifier}"></c:out></td>
			<td><c:out value="${trans.amountDue}"></c:out></td>
			<td><c:out value="${trans.amountPaid}"></c:out></td>
			<td><c:out value="${trans.createdDate}"></c:out></td>
			<td><c:out value="${trans.transactionStatus}"></c:out></td>
			<td><c:out value="${trans.paidDate}"></c:out></td>
			<td><c:out value="${trans.paidByUserId.username}"></c:out></td>
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>


