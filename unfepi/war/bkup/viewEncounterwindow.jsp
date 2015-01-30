<%@ include file="/WEB-INF/template/headercompact.jsp"%>

<c:catch var="catchException">
<c:out value="${lmessage}"></c:out>
	<table  class="datalistreporting">
          
      <tbody >
	<tr>
      <th colspan="2" style="font-size: 15px;font-weight: bolder;background-color: #dddddd;text-align: left;">Encounter Details</th>
	</tr>
	 <tr>
	        <th>Encounter number</th><td><c:out value="${model.encounter.id.encounterId}" /></td>
	 </tr>  
	 <tr>          
	        <th>Person1 ID</th><td><c:out value="${model.encounter.p1.programId}"></c:out></td>
	 </tr>
     <tr>
            <th>Person2 ID</th> <td><c:out value="${model.encounter.p2.programId}"></c:out></td>
     </tr>
     <tr>
            <th>Encounter type</th> <td><c:out value="${model.encounter.encounterType}"></c:out></td>
     </tr>
     <tr>
            <th>Data entry Source</th> <td><c:out value="${model.encounter.dataEntrySource}"></c:out></td>
     </tr>
     <tr>
            <th>Entered date</th> <td><c:out value="${model.encounter.dateEncounterEntered}"></c:out></td>
     </tr>
     <tr>            
	        <th>Start date</th><td><c:out value="${model.encounter.dateEncounterStart}"></c:out></td>
	 </tr>
     <tr>
            <th>End date</th> <td><c:out value="${model.encounter.dateEncounterEnd}"></c:out></td>
     </tr>
     <tr>
            <th>User</th> <td><c:out value="${model.encounter.createdByUser.username}"></c:out></td>
     </tr>
      <tr>
            <th>Details</th> <td><c:out value="${model.encounter.detail}"></c:out></td>
     </tr>
    <tr>
        <td colspan="2" ><span class="separator-heading">Encounter Results</span></td>
    </tr>
    <c:forEach items="${model.encounterresults}" var="encres" >
   	<tr><th>${encres[0]}</th><td>${encres[1]}</td></tr>
  	</c:forEach>
</tbody>
</table>
</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>
<%@ include file="/WEB-INF/template/footer.jsp"%>