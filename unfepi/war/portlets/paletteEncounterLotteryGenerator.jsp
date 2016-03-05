<table class="previousDataDisplay">
<tr>
    <td colspan="2" class="separator-heading">LOTTERY GENERATOR DETAILS <a id="lottgendetAnch" onclick="lotteryGenDetails(this);">hide</a>
<script type="text/javascript">
<!--
function lotteryGenDetails(obj) {
	if(obj.innerHTML == 'hide'){
		document.getElementById("lottgendetTr").style.display="none";
		obj.innerHTML = 'show';
	}
	else{
		document.getElementById("lottgendetTr").style.display="table-row";
		obj.innerHTML = 'hide';
	}
}
//-->
</script>
	</td>
</tr>
<tr id="lottgendetTr">
<td colspan="2">
    <c:set var="encounterlottgen" value="encounter${PageChildId}"></c:set>
    <c:set var="encounterreslottgen" value="encounterres${PageChildId}"></c:set>
   <table class="previousDataDisplay">
	<tr>
		<td>Vaccinator</td>
		<td>${sessionScope[encounterlottgen].p2.programId}</td>
	</tr>
	<c:forEach items="${sessionScope[encounterreslottgen]}" var="encrres"> 
      <tr>
		<td>${encrres.id.element}</td>
		<td>${encrres.value}</td>
	  </tr>
    </c:forEach> 
	</table>
</td>
</tr>
</table>