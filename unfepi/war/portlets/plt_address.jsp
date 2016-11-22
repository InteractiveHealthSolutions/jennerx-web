<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<tr>
		<td>Location<span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.${commandAdditionalPathStr}address2">
			     <select id="locationId"  name="${commandAdditionalPathStr}address2" bind-value="${status.value}" onchange="" class="requiredField">
	               	<option id=""></option>
	            	<c:forEach items="${locations}" var="loc"> 
	            	<option id="loc${loc.locationId}" value="${loc.fullName}">${loc.fullName}</option>
	            	</c:forEach> 
	            </select>
	            <span class="error-message"><c:out	value="${status.errorMessage}" /></span> 
            </spring:bind>
			<script type="text/javascript">
			</script>
		</td>
	</tr>

<tr>
        <td><spring:message code="label.village"/><span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}address1">
            <input type="text" id="address1" name="${commandAdditionalPathStr}address1" maxlength="255" 
            	   value="<c:out value="${status.value}"/>" onkeypress="return isChar(event);" />
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</td>
	</tr>
	
</tr>
