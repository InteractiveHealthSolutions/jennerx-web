<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<div class="dvwform">
<table >
<tr>
<td colspan="2"><input class="easyui-searchbox" data-options="prompt:'Enter Child ID',searcher:doSearch" value="${param.childId}"/>
<span class="error-message">${model.errorMessage}</span>
	<script>
		function doSearch(value){
			window.location="childDashboard.htm?childId="+value;
		}
	</script>
</td>
<td colspan="2"><a href="addchild.htm" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="float: right;">Enrollment</a>
</td>
</tr>
</table>
</div>
<c:if test="${not empty model.datalist}">
<div class="dvwform">
<table>
<tr>
<td colspan="100"><a href="followupVaccination.htm?child_id=${model.datalist.child.mappedId}" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="float: right;"> Follow up </a></td>
</tr>
<tr>
    <td colspan="3" class="headerrow">
<%
boolean editchilddata=UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_CHILDREN_DATA, request);
if(UserSessionUtils.getActiveUser(request).isDefaultAdministrator()){%>
(${model.datalist.child.mappedId})
<%}%>
    <a onclick="viewChildDetails(this.text);" class="anchorCustom headingS">${model.datalist.child.idMapper.identifiers[0].identifier}</a> : ${model.datalist.child.firstName} ${model.datalist.child.lastName} (${model.datalist.child.gender})
    <%if(editchilddata){ %>
    <a href="editchild.htm?programId=${model.datalist.child.idMapper.identifiers[0].identifier}" class="linkiconS iconedit" style="background-color: white;"></a>
    <%} %>
    </td>
</tr>
<tr>
<td colspan="2">Father Name : ${model.datalist.child.fatherFirstName} ${model.datalist.child.fatherLastName}</td>
<td>Address : 
<c:forEach items="${model.datalist.address}" var="add">
${add.addHouseNumber} ${add.addStreet} ${add.addSector} ${add.addArea} ${add.addDistrict} ${add.addColony} ${add.addtown} UC:${add.addUc}
<br>
</c:forEach></td></tr>
<tr><td colspan="2">Birthdate : <fmt:formatDate value="${model.datalist.child.birthdate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/> <c:if test="${model.datalist.child.estimatedBirthdate}">(approx)</c:if></td>
<td>Contacts :<br>
<c:forEach items="${model.datalist.contacts}" var="cont">
${fn:substring(cont.numberType,0,3)}- ${cont.number}<br>
</c:forEach>
</td>
</tr>
</table>
<table class="right">
<tr>
    <td colspan="20" class="headerrow-light left">Vaccination History</td>
</tr>
        <tr>
       		<th class="center">Vaccine</th>
       		<th class="center">Vaccination Date</th>
            <th class="center">Vaccination Duedate</th>
            <th class="center">Center ID</th>
            <th class="center">Vaccinator</th>
            <th class="center">Vaccination Record Num</th>
        </tr>
        <c:forEach items="${model.datalist.vaccinations}" var="vacc">
        	<c:set var="enrvaccclass" value=""></c:set>
   			<c:if test="${vacc.vaccine.vaccineId == model.datalist.child.enrollmentVaccineId}">
   			<c:set var="enrvaccclass" value="datahighlight"></c:set></c:if>
   		<tr class="enrvaccclass">
   	 		<td class="${enrvaccclass} left">${vacc.vaccine.name}</td>
            <td class="${enrvaccclass}"><fmt:formatDate value="${vacc.vaccinationDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td class="${enrvaccclass}"><fmt:formatDate value="${vacc.vaccinationDuedate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td class="${enrvaccclass}">${vacc.vaccinationCenter.idMapper.identifiers[0].identifier}</td>
            <td class="${enrvaccclass}">${vacc.vaccinator.idMapper.identifiers[0].identifier}</td>
            <td class="${enrvaccclass}">${vacc.vaccinationRecordNum}</td>
        </tr>
  </c:forEach>
</table>
<c:if test="${not empty model.datalist.childLotteries}">
<table class="right">
<tr>
    <td colspan="20" class="headerrow-light left">Lottery Data</td>
</tr>
        <tr>
       		<th class="center">Vaccine</th>
       		<th class="center">Arm</th>
       		<th class="center">Lottery Participation</th>
            <th class="center">Lottery Generated Date</th>
            <th class="center">Lottery Status</th>
            <th class="center">Amount</th>
         <!--    <th class="center">Verification Code</th> -->
            <!-- <th class="center">Transaction Status</th> -->
        <!--     <th class="center">Consumption Date</th>
            <th class="center">Transaction Date</th> -->
            <th class="center">Center ID</th>
            <!-- <th class="center">Vaccinator</th> -->
       <!--      <th class="center">Store</th>
            <th class="center">Storekeeper ID</th> -->
        </tr>
        <c:forEach items="${model.datalist.childLotteries}" var="lott">
   		<tr>	
   	 		<td class="left">${lott.vaccination.vaccine.name}</td>
   	 		<td>${lott.arm.armName}</td>
            <td>${lott.vaccination.hasApprovedLottery}</td>
            <td><fmt:formatDate value="${lott.incentiveDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td>${lott.hasWonIncentive}</td>
            <td>${lott.amount}</td>
            <%-- <td>${lott.code}</td> --%>
            <%-- <td class="lowercase">${lott.codeStatus}</td> --%>
     <%--        <td><fmt:formatDate value="${lott.consumptionDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td>
            <td><fmt:formatDate value="${lott.transactionDate}" pattern="<%=WebGlobals.GLOBAL_DATE_FORMAT_JAVA%>"/></td> --%>
            <td>${vacc.vaccinationCenter.idMapper.identifiers[0].identifier}</td>
            <%-- <td>${vacc.vaccinator.idMapper.identifiers[0].identifier}</td> --%>
         <%--    <td>${lott.storekeeper.storeName}</td>
            <td>${lott.storekeeper.idMapper.identifiers[0].identifier}</td> --%>
        </tr>
  </c:forEach>
</table>
</c:if>
<table class="left">
<tr>
    <td colspan="20" class="headerrow-light left">SMS History</td>
</tr>
        <tr>
       		<th class="center">Vaccine</th>
       		<th class="center">Sms Reminder</th>
       		<th class="center">SMS 1 Status</th>
            <th class="center">SMS 1 Date</th>
            <th class="center">SMS 2 Status</th>
            <th class="center">SMS 2 Date</th>
            <th class="center">SMS 3 Status</th>
            <th class="center">SMS 3 Date</th>
            <!-- <th class="center">Lottery SMS Status</th>
            <th class="center">Lottery SMS Date</th> -->
            
        </tr>
        <c:forEach items="${model.datalist.childReminders}" var="rem" >
   	 	<tr>
		<c:forEach items="${rem}" var="remr" varStatus="st">
			<td <c:if test="${st.index!=0}">class="right lowercase"</c:if>><c:out value="${remr}"></c:out></td>
        </c:forEach>
        </tr>
  		</c:forEach>
</table>
</div>
</c:if>


