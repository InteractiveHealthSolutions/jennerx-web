<table class="filter">
	<tr>
		<td class="search_filter">Health Program</td>
		<td class="search_filter">Round</td>
	</tr>
	<tr>
		<td><select id="search_hp_spr" name="search_healthprogram"
			bind-value="">
				<option></option>
				<c:forEach items="${model.healthProgramList}" var="hp">
					<option id="hp_spr_${hp.programId}" value="${hp.programId}">${hp.name}</option>
				</c:forEach>
		</select></td>
		<td><select id="search_rd_spr" name="search_round" bind-value="">
				<option></option>
				<c:forEach items="${model.roundList}" var="rd">
					<option id="rd_spr_${rd.roundId}" value="${rd.roundId}" disabled>${rd.name}</option>
				</c:forEach>
		</select></td>
		<td>
			<button onclick="summaryPerRoundReport();">search</button>
		</td>
	</tr>
</table>
<br>
<h1 id="calendername3" style="float: right; padding-right: 100px; color: #ED8117"></h1>
<table id="summaryperroundtable" class="easyui-datagrid"></table>
<script type="text/javascript" >

$(function(){

	$('#search_hp_spr').change(function(){
		
		$("#search_rd_spr").val("");
		$("#search_rd_spr option").prop('disabled', true);
		
		if($("#search_hp_spr").val().length != 0 ){
			$.get( "search/rounds/"+$('#search_hp_spr').val()+".htm" , function( data ) {
				var round = $.parseJSON(data);
				$.each(round, function(index, value){
					$("#search_rd_spr option").each(function () {
						if (value == this.id.replace(/\D/g,"")){
							$('#rd_spr_'+value).prop('disabled', false);
						 }
					});
				});
			});
		}
	});
});

function summaryPerRoundReport(){
	var healthProgramId = $("#search_hp_spr").val();
	var roundId = $("#search_rd_spr").val();
	
	var parameters = "?program="+healthProgramId+"&round="+roundId;
// 	console.log(parameters);
	$.get( "summaryPerRound.htm"+parameters , function( data ) {
		console.log(data);
		var reportdata = $.parseJSON(data);	
		$("#calendername3").text(reportdata.calender);
		var columns = reportdata.columns;
		var tablewidth = reportdata.tablewidth;
 		$('#summaryperroundtable').datagrid({
 			width: tablewidth,
 			singleSelect:true,
//  			fit: true,
//  			fitColumns: false,
//  			nowrap: true, 
//  			autoRowHeight: false,
 			showHeader: true,
 			pagination: true,
//  			pageSize: '10',
 			url: "summaryPerRound.htm"+parameters ,
 			columns : columns
		});
	});

}

</script>