        <tr>
          <td>Vaccine Short Name : <span class="mendatory-field">*</span></td>
          <td>
          	<spring:bind path="command.shortName">
          	<input type="text" id="shortName" name="shortName" maxlength="10" value="<c:out value="${status.value}"/>"/><br/>
     	  	<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
          </td>
        </tr>
        <tr>
            <td>Vaccine Full Name: <span class="mendatory-field">*</span></td>
            <td>
            <spring:bind path="command.fullName">
            <input type="text" id="fullName" name="fullName" maxlength="30" value="<c:out value="${status.value}"/>"/><br/>
        	<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
            </td>
        </tr>
        <tr>
            <td>Gap From Previous Milestone: (-1 for not applicable)<span class="mendatory-field">*</span></td>
            <td>
            <spring:bind path="command.gapFromPreviousMilestone">
            <input type="text" maxlength="2" id="gapFromPreviousMilestone" name="gapFromPreviousMilestone" value="<c:out value="${status.value}"/>"/><br/>
        	<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
            </td>
        </tr>
        <tr>
            <td>Unit of Gap</td>
            <td>
            <spring:bind path="command.prevGapUnit">
            <input type="hidden" value="${status.value}" id="pgunitVal"/>
        	<select id="prevGapUnit" name="prevGapUnit">
       		 <c:forEach items="<%=TimeIntervalUnit.values()%>" var="gpv">
        		<option>${gpv}</option>
       		 </c:forEach>
            </select>
            <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script><!--
        	sel = document.getElementById("prevGapUnit");
      	 	val=document.getElementById("pgunitVal").value;
       	 	makeTextSelectedInDD(sel,val);
        	//-->
       		</script> 
        	</td>
       </tr>
       <tr>
	        <td>Gap From Next Milestone: (-1 for not applicable)<span class="mendatory-field">*</span></td>
	        <td>
	        <spring:bind path="command.gapFromNextMilestone">
	        <input type="text" maxlength="2" id="gapFromNextMilestone" name="gapFromNextMilestone" value="<c:out value="${status.value}"/>"/><br/>
	        <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
	        </td>
        </tr>
        <tr>
            <td>Unit of Gap</td>
            <td><spring:bind path="command.nextGapUnit">
            <input type="hidden" value="${status.value}" id="ngunitVal"/>
       		<select id="nextGapUnit" name="nextGapUnit">
       		 <c:forEach items="<%=TimeIntervalUnit.values()%>" var="gnv">
        		<option value="${gnv}">${gnv}</option>
       		 </c:forEach>
            </select><br/>
            <br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script><!--
        	sel = document.getElementById("nextGapUnit");
      	 	val=document.getElementById("ngunitVal").value;
       	 	makeTextSelectedInDD(sel,val);
        	//-->
        	</script>  
        </td>
    </tr>
    <tr>
        <td>Description : </td>
        <td>    
        <spring:bind path="command.description">
        <textarea name="description" maxlength="255" rows="4" cols="25" >${status.value}</textarea>
		<br><span class="error-message"><c:out	value="${status.errorMessage}" /></span>
		</spring:bind>
		</td>
    </tr>