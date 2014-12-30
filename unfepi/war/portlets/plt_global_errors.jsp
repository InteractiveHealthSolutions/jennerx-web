<c:if test="${not empty command}">
<div style="text-align: center;border : solid thin orange"><spring:bind path="command">
    <c:forEach items="${status.errorMessages}" var="error">
    <span class="error-message" style="width: auto;">~ <c:out value="${error}"/></span>
    </c:forEach></spring:bind>
</div>
</c:if>