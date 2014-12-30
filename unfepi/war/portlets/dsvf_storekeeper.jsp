<%@ include file="/WEB-INF/template/include.jsp"%>

<%@page import="org.ird.unfepi.utils.UserSessionUtils"%>
<%@page import="org.ird.unfepi.context.ServiceContext"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

	<div class="dvwform">
	<table>
	<thead>
        <tr>
<%
boolean editstorkdata=UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_STOREKEEPERS_DATA, request);
boolean nicPerm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_STOREKEEPER_NIC, request);
boolean epWalletPerm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_STOREKEEPER_EP_WALLET_NUMBER, request);

if(editstorkdata){
%>
        	<th></th>
<%
}
%> 
            <th>ID</th>
			<th>Name</th>
			<th>Store</th> 
            <th>Closest Center</th>
            <th>Age</th>
            <th>Gender</th> 
<%
if(nicPerm){
%>           
            <th>NIC</th>
<%
}
if(epWalletPerm){
%>   
            <th>EP Wallet</th>
<%
}
%>
			<th></th>
			<th>Contacts</th>
            <th>Date Registered</th> 
            <!-- <th>Qualification</th> -->
        </tr>
    </thead>
   <tbody>
   <c:forEach items="${model.datalist}" var="map">
   	 <tr>
<%
if(editstorkdata){
%>    	 
			<td><a href="editstorekeeper.htm?programId=${map.storekeeper.idMapper.identifiers[0].identifier}" class="linkiconS iconedit"></a></td>
<%
}
%>
            <td><a onclick="viewStorekeeperDetails(this.text);" class="anchorCustom">${map.storekeeper.idMapper.identifiers[0].identifier}</a></td>
            <td><c:out value="${map.storekeeper.firstName} ${map.storekeeper.lastName}"></c:out></td>
            <td><c:out value="${map.storekeeper.storeName}" /></td>
            <td><c:out value="${map.storekeeper.closestVaccinationCenter.idMapper.identifiers[0].identifier}" /></td>
            <td><fmt:formatNumber value="${map.storekeeper.age/52}" maxFractionDigits="0"></fmt:formatNumber></td>
            <td class="lowercase"><c:out value="${map.storekeeper.gender}"></c:out></td>
<%
if(nicPerm){
%>           
            <td><c:out value="${map.storekeeper.nic}"></c:out></td>
<%
}
if(epWalletPerm){
%>   
            <td><c:out value="${map.storekeeper.epAccountNumber}"></c:out></td>
<%
}
%>
            <td><a href="addcontactNumber.htm?programId=${map.storekeeper.idMapper.identifiers[0].identifier}" class="icon"><img alt="contact" src ="images/contact.png" class="icon"></a></td>
			<td style="padding: 0;margin: 0">
            <div style="width:140px; overflow-: auto;display: inline-block;padding: 0;margin: 0;">
            <c:forEach items="${map.contacts}" var="cont">
            <a href="editcontactNumber.htm?coNum=${cont.contactNumberId}" class="linkiconXS iconedit"></a> ${fn:substring(cont.numberType,0,3)}- ${cont.number}
            <br>
            </c:forEach>
            </div>
            </td>
            <td><fmt:formatDate value="${map.storekeeper.dateRegistered}" pattern="dd-MMM-yyyy" /></td>
            <%-- <td><c:out value="${map.storekeeper.qualification}" /></td> --%>
        </tr>
  </c:forEach>
  </tbody>
</table>
</div>


