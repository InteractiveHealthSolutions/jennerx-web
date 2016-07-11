<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<tr>
        <td><spring:message code="label.village"/><span class="mendatory-field">*</span></td>
        <td><spring:bind path="command.${commandAdditionalPathStr}address1">
            <input type="text" id="address1" name="${commandAdditionalPathStr}address1" maxlength="255" value="<c:out value="${status.value}"/>"/>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</td>
	</tr>
	
</tr>
