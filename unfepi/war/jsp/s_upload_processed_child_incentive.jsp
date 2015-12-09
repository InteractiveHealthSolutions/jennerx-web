<%@page import="org.ird.unfepi.constants.SystemPermissions"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp" %>

<script type="text/javascript">
function exportdata(containerForm, exportType){
	containerForm.action = "exportdata?extype="+exportType;
	containerForm.submit();
}
</script>
<div class="dvwform centerdivh" style="width: 600px">
<div class="formheadingWRule">Upload Processed Child Incentives</div>
<form class="searchpalette" id="searchfrm" name="searchfrm" method="post" enctype="multipart/form-data">
<h4>${message}</h4>
<table>
	<tr>
    <td>Upload Processed Incentive CSV
</td>
    <td>
    <input type="file" name="incentiveFile" /> <input type="submit" value='Upload' 
		onclick="exportdata(document.searchfrm, '<%=SystemPermissions.UPLOAD_CHILD_PROCESSED_INCENTIVE_CSV%>');"/>
	</td>
    <td>
	</tr>
		</table>
</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>