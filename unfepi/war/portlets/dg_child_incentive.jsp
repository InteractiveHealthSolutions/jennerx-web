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
            <th>Incentive Scheme</th>
            <th>Incentive Date</th>
            <th>Status</th>
            <th>Amount</th>
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
	   		<td style="width: 40px !important;"><a id="${lott.incentive.childIncentiveId}anc" class="linkiconS iconexpand" title="Expand and show details" onclick="expandD('ChildIncentive.class', ${lott.incentive.childIncentiveId});" value="+"></a>
			<a class="linkiconS iconnote" title="Add note" onclick="openNoteWindow('ChildIncentive.class', ${lott.incentive.childIncentiveId})"></a></td>
   			<td><a onclick="viewChildDetails(this.text);" class="anchorCustom">${lott.incentive.vaccination.child.idMapper.identifiers[0].identifier}</a></td>
   	 		<td class="left">${lott.incentive.vaccination.vaccine.name}</td>
   	 		<td class="left">${lott.incentive.arm.armName}</td>
            <td><fmt:formatDate value="${lott.incentive.incentiveDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td class="lowercase">${lott.incentive.incentiveStatus}</td>
            <td>${lott.incentive.amount}</td>
            <td><fmt:formatDate value="${lott.reminder[0].dueDate}" pattern="<%=WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA%>"/></td>
            <td class="lowercase">${lott.reminder[0].reminderStatus}</td>
            <td><fmt:formatDate value="${lott.incentive.consumptionDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${lott.incentive.transactionDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td>${lott.incentive.vaccination.vaccinationCenter.idMapper.identifiers[0].identifier}</td>
            <td class="lowercase">${lott.encounter[0].dataEntrySource} (${lott.encounter[0].encounterType})</td>
            <%-- <td>${vacc.vaccinator.idMapper.identifiers[0].identifier}</td> --%>
            <%-- <td>${lott.incentive.storekeeper.storeName}</td>
            <td>${lott.incentive.storekeeper.idMapper.identifiers[0].identifier}</td> --%>
        </tr>
        <tr id="${lott.incentive.childIncentiveId}r" style="display: none;" >
       	<td colspan="80" class="dvwinner">
       	<div>
			<table id="${lott.incentive.childIncentiveId}tbl">
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


