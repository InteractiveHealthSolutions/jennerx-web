<%@page import="org.ird.unfepi.model.User.UserStatus"%>
<c:if test="${fn:toLowerCase(command.username) != 'administrator'}">
<tr>
           <td>User Status : </td>
           <td>
               <spring:bind path="command.status">
               <select name="status" id="status" bind-value="${status.value}">
               <c:forEach items="<%=UserStatus.values() %>" var="ustatus">
               <option >${ustatus}</option>
               </c:forEach>
               </select>
               <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
			</spring:bind>
          </td>
       </tr>
	<c:if test="${fn:length(notAllowedRoles) > 0}">		
     <tr>
        <td colspan="2">
         <span class="error">You donot have privileges to assign following roles while creating any user <br>(role have more privileges than your role).
		 </span>
		 <br>
            <div style="color: blue;font-style: italic">
         	<c:forEach items="${notAllowedRoles}" var="rol">
			-- ${rol.rolename}<br>
			</c:forEach>
         </div>
         </td>
     </tr> 
	</c:if>
     <tr>
         <td>User Role: <span class="mendatory-field">*</span></td>
		 <td>
		 <input type="hidden" value="${userRole.rolename}" id="userroleVal"/>
		 <select id="userrole" name="userrole">
		 <option></option>
		 <c:forEach items="${allowedRoles}" var="role">
		 <option>${role.rolename}</option>
		 </c:forEach>
		 </select>
		 <script>
		 	<!--
               sel = document.getElementById("userrole");
               val=document.getElementById("userroleVal").value;
	           
               makeTextSelectedInDD(sel,val);
           	//-->
        </script>
		</td>
     </tr>
</c:if>