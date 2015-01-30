<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<div class="dvwform">
<table>
	<thead>
       <tr>
       		<th></th>
       		<th>Child Id</th>
       		<th>Vaccine</th>
       		<th>Lottery Approved</th>
            <th>Lottery Date</th>
            <th>Lottery Won</th>
            <th>Transaction Status</th>
            <th>Amount</th>
            <th>Verification Code</th>
            <th>Sms Date</th>
            <th>Sms Status</th>
            <th>Consumption Date</th>
            <th>Transaction Date</th>
            <th>Center ID</th>
            <th>Data Entry Source</th>
            <!-- <th>Vaccinator</th> -->
            <!-- <th>Store</th>
            <th>Storekeeper ID</th> -->
        </tr>
    </thead>
   <tbody class="rows">
   <c:forEach items="${model.datalist}" var="lott">
   		<tr>
	   		<td style="width: 40px !important;"><a id="${lott.lottery.childLotteryId}anc" class="linkiconS iconexpand" title="Expand and show details" onclick="expandD('${lott.lottery.class.name}', ${lott.lottery.childLotteryId});" value="+"></a>
			<a class="linkiconS iconnote" title="Add note" onclick="openNoteWindow('${lott.lottery.class.name}', ${lott.lottery.childLotteryId})"></a></td>
   			<td><a onclick="viewChildDetails(this.text);" class="anchorCustom">${lott.lottery.vaccination.child.idMapper.identifiers[0].identifier}</a></td>
   	 		<td class="left">${lott.lottery.vaccination.vaccine.name}</td>
            <td>${lott.lottery.vaccination.hasApprovedLottery}</td>
            <td><fmt:formatDate value="${lott.lottery.lotteryDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td>${lott.lottery.hasWonLottery}</td>
            <td class="lowercase">${lott.lottery.codeStatus}</td>
            <td>${lott.lottery.amount}</td>
            <td>${lott.lottery.code}</td>
            <td><fmt:formatDate value="${lott.reminder[0].dueDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
            <td class="lowercase">${lott.reminder[0].reminderStatus}</td>
            <td><fmt:formatDate value="${lott.lottery.consumptionDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${lott.lottery.transactionDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td>${vacc.vaccinationCenter.idMapper.identifiers[0].identifier}</td>
            <td class="lowercase">${lott.encounter[0].dataEntrySource} (${lott.encounter[0].encounterType})</td>
            <%-- <td>${vacc.vaccinator.idMapper.identifiers[0].identifier}</td> --%>
            <%-- <td>${lott.lottery.storekeeper.storeName}</td>
            <td>${lott.lottery.storekeeper.idMapper.identifiers[0].identifier}</td> --%>
        </tr>
        <tr id="${lott.lottery.childLotteryId}r" style="display: none;" >
       	<td colspan="80" class="dvwinner">
       	<div>
			<table id="${lott.lottery.childLotteryId}tbl">
			</table>
		</div>
		</td>
     </tr>
  </c:forEach>
  </tbody>
</table>
</div>
   	<%
   		ServiceContext sc = (ServiceContext) request.getAttribute("sc");
   		//must close session here; or do it in finally of respective controller else can create memory leak
   		sc.closeSession();
   	%>


