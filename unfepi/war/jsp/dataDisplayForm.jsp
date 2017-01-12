<%@ include file="/WEB-INF/template/include.jsp"%>

<c:if test="${dataForm.isInsertable}">
	<a href="add${fn:toLowerCase(dataForm.formName)}.htm" class="easyui-linkbutton" iconCls="icon-add" 
	   style="margin-top: -30px; float: right;">New ${dataForm.formTitle}</a>
</c:if>

<script type="text/javascript">
	function goForSearch(containerForm) {
		containerForm.action = "?action=search";
		containerForm.submit();
	}
	function exportdata(containerForm, exportType) {
		containerForm.action = "exportdata?extype=" + exportType;
		containerForm.submit();
	}

	function expandRecord(obj) {
		var trId = obj.title;
		// var divId=obj.title+"dv";
		act = obj.value;
		if (act == undefined || act == "+") {
			document.getElementById(trId).style.display = "table-row";
			//document.getElementById(divId).style.display="table";
			obj.value = "-";
		} else {
			document.getElementById(trId).style.display = "none";
			//document.getElementById(divId).style.display="none";
			obj.value = "+";
		}
	}
</script>

<%
	if (formReq.getServiceType().equals(ServiceType.DATA_VIEW)) {
%>
	<jsp:include page="/portlets/dvf_${fn:toLowerCase(dataForm.formName)}.jsp" />
<%
	} else if (formReq.getServiceType().equals(ServiceType.DATA_SEARCH)) {
%>
	<%@ include file="dataPager.jsp"%>
	<jsp:include page="srplt_${fn:toLowerCase(dataForm.formName)}.jsp" />
	<jsp:include page="/portlets/dsvf_${fn:toLowerCase(dataForm.formName)}.jsp" />
<%
	}
%>
<c:choose>
	<c:when test="${model.datalist != null && empty model.datalist}">
		<div class="emptydataset">No Records Found</div>
	</c:when>
</c:choose>
