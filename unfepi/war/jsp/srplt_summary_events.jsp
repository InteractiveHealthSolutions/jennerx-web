<%@ include file="/WEB-INF/template/include.jsp"%>
<%@page import="org.ird.unfepi.GlobalParams.SearchFilter"%>
<%@page import="org.ird.unfepi.constants.SystemPermissions"%>

<script type="text/javascript">
$(function(){
	
	if($("#healthprogramsearchoption").val().length == 0 ){
		$("#sitesearchoption").empty();
		$("#sitesearchoption, #sitesearchoptionname").hide();
	}else{
		ShowSiteOptions();
	}
	
	$("#healthprogramsearchoption").change(function(){
		
		if($("#healthprogramsearchoption").val().length == 0 ){
			$("#sitesearchoption").empty();
			$("#sitesearchoption, #sitesearchoptionname").hide();
		}else{
			ShowSiteOptions();
		}
	});
	
	function ShowSiteOptions(){
		$("#sitesearchoption, #sitesearchoptionname").show();
		var someText = ${model.sitesJ};
		$("#sitesearchoption").empty();
		$("#sitesearchoption").append("<option value='' ></option>");
		$.each(someText, function (index, item) {
			selectoption = '${model.siteMappedId}';			
			if(selectoption == item.mappedId){
				$("#sitesearchoption").append("<option value='"+ item.mappedId +"' selected >"+item.fullName+"</option>");
			}else{
				$("#sitesearchoption").append("<option value='"+ item.mappedId +"' >"+item.fullName+"</option>");
			}
		});
	}
});
</script>


<form class="searchpalette" id="searchfrm" name="searchfrm" method="post">
	<table>
	<tr>
	
	<td>Health Program<br>
	    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.HEALTH_PROGRAM.FILTER_NAME()%>"></c:set>
		<select id="healthprogramsearchoption" name="${nextSearchFieldNameValue}" bind-value="${model[nextSearchFieldNameValue]}">
		   	<option value=""></option>
			<c:forEach items="${model.healthprograms}" var="hpg">
				<option value="${hpg.programId}">${hpg.name}</option>
			</c:forEach>
		</select>
	</td>
	
		<td id="sitesearchoptionname">Site<br>
	    <c:set var="nextSearchFieldNameValue" value="<%=SearchFilter.SITE.FILTER_NAME()%>"></c:set>
		<select id="sitesearchoption" name="${nextSearchFieldNameValue}" bind-value="${model[nextSearchFieldNameValue]}">
<!-- 		   	<option></option> -->
<%-- 			<c:forEach items="${model.sites}" var="site"> --%>
<%-- 				<option value="${site.mappedId}">${site.name}</option> --%>
<%-- 			</c:forEach> --%>
		</select>
	</td>	
	
	<td><a onclick="goForSearch(document.searchfrm);" class="searchButton"></a></td>
<%-- 	<td><a onclick="exportdata(document.searchfrm, '<%=SystemPermissions.DOWNLOAD_SUMMARY_VACCINATIONS_CSV%>');" class="linkiconM iconcsv"></a></td> --%>
	</tr>
	</table>
</form>