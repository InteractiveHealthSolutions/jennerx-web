
<%@page import="org.ird.unfepi.DataForm"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.GlobalParams.ServiceType"%>
<%@page import="org.ird.unfepi.service.exception.UserServiceException" %>
<%@page import="org.ird.unfepi.utils.UserSessionUtils" %>
<%@page import="org.ird.unfepi.constants.SystemPermissions" %>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
<!--

$( document ).ready(function() {
	$('select[bind-value]').each(function() {
		$(this).val($(this).attr('bind-value'));
	});

	$('select[bind-text] option:contains("' + $(this).attr('bind-text') + '")').attr('selected', 'selected');
	 
	/* $('select[bind-text] option').each(function() {
		$(this).val($(this).attr('bind-text'));
	}); */

	$('.calendarbox').each(function() {
		if($(this).attr('uneditable') == 'uneditable'){
			$(this).attr('readonly', 'readonly');
		}
		else {
			$(this).datepicker({
			  	duration: '',
			    constrainInput: false,
			    maxDate: $(this).attr('maxDate'),
			    minDate: $(this).attr('minDate'),
			    dateFormat: '<%=WebGlobals.GLOBAL_DATE_FORMAT_JS%>',
			    onClose: window[$(this).attr('onclosehandler')],
			    onSelect: window[$(this).attr('onselecthandler')]
			});
			if($(this).attr('datenote') == 'true'){
				$(this).after('<span class="datenote">(dd-mm-yyyy)</span>');
			}
		}
	});
	
	$(".numbersOnly").forceNumeric();
});

jQuery.fn.forceNumeric = function () {
    return this.each(function () {
        $(this).keypress(function (e) {
            if (/\D/g.test(this.value))
            {
                // Filter non-digits from input value.
                this.value = this.value.replace(/\D/g, '');
            }
            
             var key = e.which || e.keyCode;

             /* if (!e.shiftKey && !e.altKey && !e.ctrlKey &&
            // numbers   
                key >= 48 && key <= 57 ||
            // Numeric keypad
                key >= 96 && key <= 105 ||
            // comma, period and minus, . on keypad
               key == 190 || key == 188 || key == 109 || key == 110 ||
            // Backspace and Tab and Enter
               key == 8 || key == 9 || key == 13 ||
            // Home and End
               key == 35 || key == 36 ||
            // left and right arrows
               key == 37 || key == 39 ||
            // Del and Ins
               key == 46 || key == 45)
                return true; */
                
                if(key == 8 || key == 9 || key == 13 || 
                	key == 35 || key == 36 || 
                	key == 37 || key == 39 || 
                	key == 46 || key == 45 ||
                	/^\d+$/.test(String.fromCharCode(key))){
                	return true;
                }

            return false;
        });
    });
};
//-->
</script>
<c:set var="dataForm" value="${model.dataFormObject}"></c:set>
<c:if test="${empty dataForm}">
<c:set var="dataForm" value="${dataFormObject}"></c:set>
</c:if>
<c:choose>
<c:when test="${not empty dataForm}">
<%
boolean perm=false;
DataForm formReq = null;
try{
	formReq = (DataForm)pageContext.getAttribute("dataForm");
	perm=UserSessionUtils.hasActiveUserPermission(formReq.getPermission(),request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}
if(perm){ 
%>

<span class="formheadingWRule">${dataForm.formTitle}</span>

<% if(formReq.getServiceType().equals(ServiceType.DATA_ENTRY)){ %>
<%@ include file="dataEntryForm.jsp" %>
<%}
else if(formReq.getServiceType().equals(ServiceType.DATA_EDIT)){%>
<%@ include file="dataEditForm.jsp" %>
<%}
else {
%>
<%@ include file="dataDisplayForm.jsp" %>
<%}%>

<%
}else{
%>
<%@ include file="pageDeniedAccess.jsp" %>
<%
}
%>
</c:when>
<c:otherwise><%@ include file="pageError.jsp" %></c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/template/footer.jsp" %>