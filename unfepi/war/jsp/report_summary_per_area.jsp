<table class="filter">
	<tr>
		<td class="search_filter">Area</td>
		<td class="search_filter">Health Program</td>
		<td class="search_filter">Round</td>
	</tr>
	<tr>
		<td><select id="search_ar_spa" name="search_area" bind-value="">
				<option></option>
				<c:forEach items="${model.locationList}" var="ar">
					<option id="ar_spa_${ar.locationId}" value="${ar.locationId}">${ar.name}</option>
				</c:forEach>
		</select></td>
		<td><select id="search_hp_spa" name="search_healthprogram"
			bind-value="">
				<option></option>
				<c:forEach items="${model.healthProgramList}" var="hp">
					<option id="hp_spa_${hp.programId}" value="${hp.programId}" >${hp.name}</option>
				</c:forEach>
		</select></td>
		<td><select id="search_rd_spa" name="search_round" bind-value="">
				<option></option>
				<c:forEach items="${model.roundList}" var="rd">
					<option id="rd_spa_${rd.roundId}" value="${rd.roundId}" disabled>${rd.name}</option>
				</c:forEach>
		</select></td>
		<td>
			<button onclick="summaryPerAreaReport();">search</button>
		</td>
	</tr>
</table>
<br>
<h1 id="calendername2" style="float: right; padding-right: 100px; color: #ED8117"></h1>
<table id="summaryperareatable" class="easyui-datagrid"></table>
<br>
<br>
<table id="summaryPerAreaMinMaxAgetable" class="easyui-datagrid"></table>
<script type="text/javascript">
$( document ).ready(function() {
	
	$('#search_ar_spa').change(function(){
		
		$("#search_hp_spa, #search_rd_spa").val("");
		$("#search_hp_spa option, #search_rd_spa option").prop('disabled', true);
		
		if($("#search_ar_spa").val().length != 0 ){
			$.get( "search/healthprograms/"+$('#search_ar_spa').val()+".htm" , function( data ) {
				var healthprogram = $.parseJSON(data);
				$.each(healthprogram, function(index, value){
					$("#search_hp_spa option").each(function () {
						if (value == this.id.replace(/\D/g,"")){
							$('#hp_spa_'+value).prop('disabled', false);
						 }
					});
				});
			});
		}
	});
	
	$('#search_hp_spa').change(function(){
		
		$("#search_rd_spa").val("");
		$("#search_rd_spa option").prop('disabled', true);
		
		if($("#search_hp_spa").val().length != 0 ){
			$.get( "search/rounds/"+$('#search_hp_spa').val()+".htm" , function( data ) {
				var round = $.parseJSON(data);
				$.each(round, function(index, value){
					$("#search_rd_spa option").each(function () {
						if (value == this.id.replace(/\D/g,"")){
							$('#rd_spa_'+value).prop('disabled', false);
						 }
					});
				});
			});
		}
	});

});
	
	
function summaryPerAreaReport(){
	var healthProgramId = $("#search_hp_spa").val();
	var roundId = $("#search_rd_spa").val();
	var locationId = $("#search_ar_spa").val();
	var parameters = "?program="+healthProgramId+"&round="+roundId+"&area="+locationId;
// 	console.log(parameters);
	$.get( "summaryPerArea.htm"+parameters , function( data ) {
// 		console.log(data);
		var reportdata = $.parseJSON(data);	
		$("#calendername2").text(reportdata.calender);
		var columns = reportdata.columns;
		var tablewidth = reportdata.tablewidth;
 		$('#summaryperareatable').datagrid({
 			width: tablewidth,
 			singleSelect:true,
//  			fit: true,
 			fitColumns: false,
 			nowrap: true, 
 			autoRowHeight: false,
 			showHeader: true,
 			pagination: true,
 			pageSize: '10',
 			url: "summaryPerArea.htm"+parameters ,
 			columns : columns
		});
 		
		$('#summaryPerAreaMinMaxAgetable').datagrid({
			width: 205,
 			singleSelect:true,
 			nowrap: true, 
 			autoRowHeight: false,
 			showHeader: true,
//  			pagination: true,
 			pageSize: '10',
 			url: "summaryPerAreaMinMaxAge.htm"+parameters ,
 			columns : [[
						{field : 'shortName', title : 'vaccine',width : 100}, 
						{field : 'minAge', title : 'minAge', width : 50}, 
						{field : 'maxAge', title : 'maxAge', width : 50},
// 						{field : '', title : 'result', width : 75}
 			          ]]
		}); 		
//  		$('#summaryperareatable').datagrid('loadData', reportdata);
	});

}
</script>