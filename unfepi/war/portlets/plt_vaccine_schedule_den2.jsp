<%@page import="org.ird.unfepi.web.utils.VaccineSchedule.VaccineScheduleKey"%>
<%@include file="/WEB-INF/template/include.jsp"%>

<div id="tab3" class="tab-section" style="text-align: center;">

<fieldset>
    <legend> Vaccines history </legend>
    <table class="vaccine_history denform-h" style="table-layout: fixed;">
	</table>
</fieldset>

<br><br>

<fieldset>
    <legend> Current vaccines </legend>
    <table class="current_vaccine denform-h" style="table-layout: fixed;">
	</table>
</fieldset>

<br><br><hr style="width: 50%;"><br>
<input type="button" id="RealContraindication" value="Real Contraindication" onclick="Contraindication();">

<input type="button" id="submitBtn" value="Submit Data" onclick="subfrm();">


</div>

<script type="text/javascript">
			
		var vaccineScheduleList;

		function sendVaccinationHistory() {
			jsonArray = [];

			$("input[name ='retro_vaccine_in']:checked").each(function(index, element) {
				
				var id = (element.id).match(/\d+/g);
				jsonObject = {};
				jsonObject["vaccineId"] = id.toString();
				jsonObject["vaccineName"] = $('#retro_vaccine' + id).val();
				
				if($('#retro_date' + id).val().length > 0){
					jsonObject["vaccinationDate"] = $('#retro_date' + id).val();
				}				
				
				jsonArray.push(jsonObject);
				
			});

			DWRVaccineService.getVaccineSchedule(JSON.stringify(jsonArray), convertToDate($('#birthdate').val()), convertToDate($('#centerVisitDate').val()), '${command.centerVisit.childId}',  $('#vaccinationCenterId').val(), '${command.centerVisit.uuid}', $('#healthProgramId').val(), {callback : function(resultList) {
				console.log(resultList);
				vaccineScheduleList = resultList;
				displaySchedule();
				}
			});
		}
		
		function displaySchedule(){
			$.each(vaccineScheduleList, function(index, element) {	
				var vid = vaccineScheduleList[index].<%=VaccineScheduleKey.vaccine%>.vaccineId;
				var name = vaccineScheduleList[index].<%=VaccineScheduleKey.vaccine%>.name;
				var status = vaccineScheduleList[index].<%=VaccineScheduleKey.status%>;
				var dueDate = vaccineScheduleList[index].<%=VaccineScheduleKey.assigned_duedate%>;

				var dateStr = $.datepicker.formatDate('dd-mm-yy', new Date(dueDate));
				
				console.log(vid + ' ' + name + '  ' + status + '  ' + dueDate);
				
				if(status == "VACCINATED" || status == "RETRO"){
					$(".current_vaccine").append("<tr id='tr"+vid+"'></tr>")
					$("#tr"+vid).append("<td>"+name+"</td><!-- <td>"+status+"</td> <td>"+dateStr+"</td>-->");
					$("#tr"+vid).append("<td><button id='delbtn' onclick='delVaccine("+vid+")'>X</button></td>");
					
					if(status == "RETRO"){
						vaccineScheduleList[index].<%=VaccineScheduleKey.status%> = "VACCINATED";
						vaccineScheduleList[index].<%=VaccineScheduleKey.vaccination_date%> = convertToDate($('#centerVisitDate').val()) ;
						vaccineScheduleList[index].<%=VaccineScheduleKey.center%> = $('#vaccinationCenterId').val()
					}	
				}
				
				if(status == "CURRENT_RETRO" ||  status == "CURRENT_RETRO_DATE_MISSING"){
					$(".vaccine_history").append("<tr id='tr"+vid+"'></tr>")
					$("#tr"+vid).append("<td>"+name+"</td><!-- <td>"+status+"</td><td>"+dateStr+"</td> -->");
					
					vaccineScheduleList[index].<%=VaccineScheduleKey.status%> = status.replace('CURRENT_','');
					vaccineScheduleList[index].<%=VaccineScheduleKey.center%> = $('#vaccinationCenterId').val();
				}
			});
		}
		
		function delVaccine(vid) {
			
			var preReq4 = [];
			
			$.each(vaccineScheduleList, function(index, element) {
				var vaccineId = vaccineScheduleList[index].<%=VaccineScheduleKey.vaccine%>.vaccineId;
				var status = vaccineScheduleList[index].<%=VaccineScheduleKey.status%>;
				if(vaccineId == vid && status != "INVALID_DOSE"){
					preReq4 = element.prerequisiteFor ;
					vaccineScheduleList[index].<%=VaccineScheduleKey.status%> = "NOT_GIVEN";
					vaccineScheduleList[index].<%=VaccineScheduleKey.vaccination_date%> = null;
<%-- 					vaccineScheduleList[index].<%=VaccineScheduleKey.center%> = null; --%>
					
					$("#tr"+ vid).remove();
				}
				
				if(preReq4.length > 0){
					$.each(preReq4, function(i, preReq4VaccineId){
						if((element.vaccine.vaccineId == preReq4VaccineId) && (element.status == 'SCHEDULED')){
							
							element.<%=VaccineScheduleKey.status%> = 'NOT_ALLOWED';
							element.<%=VaccineScheduleKey.prerequisite_passed%> = false ;
							element.<%=VaccineScheduleKey.assigned_duedate%> = null;
							element.<%=VaccineScheduleKey.is_current_suspect%> = false;
							element.<%=VaccineScheduleKey.is_retro_suspect%> = false;
						}
					});
				}
			});			
		}
		
		function Contraindication(){
			if(confirm("do you want to continue ...")){
				$.each(vaccineScheduleList, function(index, element) {
					if(element.<%=VaccineScheduleKey.status%> == "VACCINATED"){
						element.<%=VaccineScheduleKey.status%> = "NOT_VACCINATED";
					}
				});
				subfrm();
			}
		}
		
		function checkboxVac(element){
			var id = (element.id).match(/\d+/g);
			if(element.checked){
				$('#retro_date' + id).prop("disabled", false); 
			} else{
				$('#retro_date' + id).prop("disabled", true);
				$('#retro_date' + id).val("");
			}
		}		
		function checkboxPreAct(element){
			
		}
</script>