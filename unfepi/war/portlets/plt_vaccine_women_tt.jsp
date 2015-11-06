<%@page import="org.ird.unfepi.constants.WebGlobals"%>

<script type="text/javascript">

/* 	$( document ).ready(function(){
		document.getElementById("tt").style.display = "none";
	});
 */
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
	}

</script>

<tr>
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
		</tr>

		<tr id="tt1">
			<td>TT1 ki tareekh</td>
			<td><input id="tt1Date" name="${commandAdditionalPathStr}tt1Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr id="tt2">
			<td>TT2 ki tareekh</td>
			<td><input id="tt2Date" name="${commandAdditionalPathStr}tt2Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr id="tt3">
			<td>TT3 ki tareekh</td>
			<td><input id="tt3Date" name="${commandAdditionalPathStr}tt3Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr id="tt4">
			<td>TT4 ki tareekh</td>
			<td><input id="tt4Date" name="${commandAdditionalPathStr}tt4Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr id="tt5">
			<td>TT5 ki tareekh</td>
			<td><input id="tt5Date" name="${commandAdditionalPathStr}tt5Date" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>

		<tr >
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
			<td><input id="tt1Date" name="${commandAdditionalPathStr}tt1Datehdate" maxDate="+0d" value="${status.value}" class="calendarbox"/></td>
		</tr>