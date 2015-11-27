<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp" %>

<div class="dvwform">
<div class="formheadingWRule">Location Tree</div>
<table>
<tr><td><a onclick="$('#cc').tree('collapseAll')" class="anchorCustom">collapse</a>  
<a onclick="$('#cc').tree('expandAll')" class="anchorCustom">expand</a></td></tr>
<tr>
<td colspan="2">
<ul id="cc" class="easyui-tree" style="text-align: left;"></ul>
<script type="text/javascript">
$( document ).ready(function() {
$('#cc').tree({
    loader: treeDataLoaderLocations
    });
    
});

function treeDataLoaderLocations(parentId){
	//alert(JSON.stringify(parentId));
	DWREntityService.getLocationHierarchy({"parentId": (isNaN(parentId)?"":parentId)}, 
			{callback: function(result) {
				$('#cc').tree('loadData' ,result);
				$('#cc').tree('collapseAll');
			}, async: false, timeout: 5000});
}
</script>
</td>
</tr>
</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>