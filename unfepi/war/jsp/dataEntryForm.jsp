<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<%@ include file="/WEB-INF/template/include.jsp"%>


<script type="text/javascript">

</script>

<%@ include file="/portlets/plt_global_errors.jsp"%>

<jsp:include page="/portlets/denf_${fn:toLowerCase(dataForm.formName)}.jsp" />
