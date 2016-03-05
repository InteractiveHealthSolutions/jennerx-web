<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>


<script type="text/javascript">
<!--

window.onbeforeunload = function (e) {
	  var e = e || window.event;
	  // For IE and Firefox
	  if (e) {
	    e.returnValue = '';
	  }
	  // For Safari
	  return '';
	};
	
//-->
</script>

<%@ include file="/portlets/plt_global_errors.jsp" %>

<jsp:include page="/portlets/denf_${fn:toLowerCase(dataForm.formName)}.jsp"/>
