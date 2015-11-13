<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">

 	$( document ).ready(function(){
		document.getElementById("tt1Date").style.display = "none";
		document.getElementById("tt2Date").style.display = "none";
		document.getElementById("tt3Date").style.display = "none";
		document.getElementById("tt4Date").style.display = "none";
		document.getElementById("tt5Date").style.display = "none";
	});
/*
	function ttVaccine() {
		//alert(document.getElementById("isVaccinated").value);
		if(document.getElementById("isVaccinated").value == "Yes"){
			document.getElementById("ttInfo").style.display = "block";
			document.getElementById("tt1").style.display = "block";
			document.getElementById("tt2").style.display = "block";
			document.getElementById("tt3").style.display = "block";
			document.getElementById("tt4").style.display = "block";
			document.getElementById("tt5").style.display = "block";
		} else {
			document.getElementById("ttInfo").style.display = "none";
			document.getElementById("tt1").style.display = "none";
			document.getElementById("tt2").style.display = "none";
			document.getElementById("tt3").style.display = "none";
			document.getElementById("tt4").style.display = "none";
			document.getElementById("tt5").style.display = "none";
		}
	}*/
	
	function vaccineStatus(object){
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; //January is 0!
		var yyyy = today.getFullYear();

		if(dd<10) {
		    dd='0'+dd
		} 

		if(mm<10) {
		    mm='0'+mm
		} 

		today = dd +'-'+ mm +'-'+ yyyy;
		var id = object.id.substring(0,3);
		var value = object.options[object.selectedIndex].value;
		//alert(value);
		//alert(today);
		if(value == "VACCINATED"){
			var dateId = id + "Date";
			document.getElementById(dateId).classList.remove("calendarbox");
			document.getElementById(dateId).classList.remove("hasDatepicker");
			document.getElementById(dateId).value = today;
			document.getElementById(dateId).readOnly = "true";
		}
		
		if(value != "RETRO (date missing)"){
			document.getElementById( id + "Date").style.display = "block";
		}
		
	}

</script>

		<!-- <tr>
			<td id="vaccinated">Kya pehlay koi TT kay teekay lagay hain?</td>
			<td><select id="isVaccinated" onChange="ttVaccine();">
				<option></option>
				<option value="Yes">Yes</option>
				<option value="No">No</option>
			</select></td>
		</tr>

		<tr id="ttInfo">
			<td>Pehlay lagwaey gaey TT k teekay</td>
			<td><select multiple>
				<option value="TT1">TT1</option>
				<option value="TT2">TT2</option>
				<option value="TT3">TT3</option>
				<option value="TT4">TT4</option>
				<option value="TT5">TT5</option>
			</select></td>
		</tr> -->

		<tr id="tt1">
			<td>TT1 ki tareekh</td>
			<td><select id="tt1Status" name="tt1Status" onChange="vaccineStatus(this);">
					<option></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO(date missing)">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select></td>
			<td><input id="tt1Date" name="tt1Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr id="tt2">
			<td>TT2 ki tareekh</td>
			<td><select id="tt2Status" name="tt2Status" onChange="vaccineStatus(this);">
					<option></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO (date missing)">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select></td>
			<td><input id="tt2Date" name="tt2Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr id="tt3">
			<td>TT3 ki tareekh</td>
			<td><select id="tt3Status" name="tt3Status" onChange="vaccineStatus(this);">
					<option></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO (date missing)">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select></td>
			<td><input id="tt3Date" name="tt3Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr id="tt4">
			<td>TT4 ki tareekh</td>
			<td><select id="tt4Status" name="tt4Status" onChange="vaccineStatus(this);">
					<option></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO (date missing)">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select></td>
			<td><input id="tt4Date" name="tt4Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr id="tt5">
			<td>TT5 ki tareekh</td>
			<td><select id="tt5Status" name="tt5Status" onChange="vaccineStatus(this);">
					<option></option>
					<option value="RETRO">RETRO</option>
					<option value="RETRO (date missing)">RETRO (date missing)</option>
					<option value="VACCINATED">VACCINATED</option>
					<option value="SCHEDULED">SCHEDULED</option>
			</select></td>
			<td><input id="tt5Date" name="tt5Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<%-- <tr>
			<td>Aaj lagnay waala teeka</td>
			<td><spring:bind path="command.${commandAdditionalPathStr}shortName">
			<select id="shortName" name="shortName">
				<option></option>
				<option value="TT1">TT1</option>
				<option value="TT2">TT2</option>
				<option value="TT3">TT3</option>
				<option value="TT4">TT4</option>
				<option value="TT5">TT5</option>
			</select></spring:bind></td>
		</tr>

		<tr>
			<td>Aaj ki taareekh</td>
			<td><input id="currDate" name="${commandAdditionalPathStr}currDate" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr>
			<td>Agla teeka</td>
			<td><select>
				<option></option>
				<option value="TT1">TT1</option>
				<option value="TT2">TT2</option>
				<option value="TT3">TT3</option>
				<option value="TT4">TT4</option>
				<option value="TT5">TT5</option>
			</select></td>
		</tr>

		<tr>
			<td>Aglay teekay ki tareekh</td>
			<td><input id="dueDate" name="${commandAdditionalPathStr}dueDate" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr> --%>