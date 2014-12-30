<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<tr>
        <td>House number</td>
        <td><spring:bind path="command.${commandAdditionalPathStr}addHouseNumber">
            <input type="text" id="addHouseNumber" name="${commandAdditionalPathStr}addHouseNumber" maxlength="30" value="<c:out value="${status.value}"/>"/>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
        <td>Street</td>
        <td>
            <spring:bind path="command.${commandAdditionalPathStr}addStreet">
            <input type="text" id="addStreet" name="${commandAdditionalPathStr}addStreet" maxlength="30" value="<c:out value="${status.value}"/>"/>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</td>
    </tr>
    <tr>
         <td>Sector</td>
         <td>
             <spring:bind path="command.${commandAdditionalPathStr}addSector">
             <input type="text" id="addSector" name="${commandAdditionalPathStr}addSector" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <span class="error-message"><c:out value="${status.errorMessage}"/></span>
		 </spring:bind>
    </tr>
    <tr>
        <td>Colony <span class="mendatory-field"></span></td>
        <td>
            <spring:bind path="command.${commandAdditionalPathStr}addColony">
            <input type="text" id="addColony" name="${commandAdditionalPathStr}addColony" maxlength="30" value="<c:out value="${status.value}"/>"/>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
        <td>Koi qareebi nishaani </td>
        <td>
            <spring:bind path="command.${commandAdditionalPathStr}addLandmark">
            <input type="text" id="addLandmark" name="${commandAdditionalPathStr}addLandmark" maxlength="50" value="<c:out value="${status.value}"/>"/>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td>City <span class="mendatory-field">*</span></td>
		<td><spring:bind path="command.${commandAdditionalPathStr}cityId">
			<select id="cityId" name="${commandAdditionalPathStr}cityId" onchange="loadTown();" bind-value="${status.value}">
				<option></option>
				<c:forEach items="${cities}" var="ci">
					<option value="${ci.cityId}">${ci.cityId}:${ci.cityName}</option>
				</c:forEach>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script><!--
              function loadTown() {
				DWREntityService.getLocationList(["town","na",'DONOT KNOW'],$('#cityId').val(),{
						async: false,
						callback: function (resl) {
							$('#townlist').empty().append('<option></option>');
							for ( var i = 0; i < resl.length; i++) {
								$('#townlist').append('<option value="'+resl[i].locationId+'">'+resl[i].name+'</option>');
							}
							
							var v = document.getElementById("cityId").value;
		  					if(v == '<%=WebGlobals.OTHER_OPTION_ID_IN_DB%>'){
		  						document.getElementById("cityName").disabled = false;
		  					}
		  					else{
		  						document.getElementById("cityName").value = '';
		  						document.getElementById("cityName").disabled = true;
		  					}
		  					
							$('#townlist').val($('#townlist').attr('bind-value'));
							
							if($('#townlist').val() != ''){
								loadUC();
							}
					}});
			  }
			
			$( document ).ready(function() {
				if($('#cityId').val() != ''){
					loadTown();
				}
			});

              //-->
             </script> 
		</td>
	</tr>
	<tr>
		<td>City, if Other <span class="mendatory-field">*</span></td>
		<td>
			<spring:bind path="command.${commandAdditionalPathStr}cityName">
			<input type="text" id="cityName" name="${commandAdditionalPathStr}cityName" maxlength="30" value="${status.value}" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</td>
	</tr>
	<tr>
        <td>Town <span class="mendatory-field">*</span></td>
        <td>
            <spring:bind path="command.${commandAdditionalPathStr}addtown">
            <select id="townlist" name="${commandAdditionalPathStr}addtown" onchange="loadUC();" bind-value="${status.value}">
                <option></option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script><!-- 
              function loadUC() {
  				DWREntityService.getLocationList(["uc","na",'DONOT KNOW'],$('#townlist').val(),{
  						async: false,
  						callback: function (resl) {
  							$('#uclist').empty().append('<option></option>');
  							for ( var i = 0; i < resl.length; i++) {
  								$('#uclist').append('<option value="'+resl[i].locationId+'">'+resl[i].fullname+'</option>');
  							}
  							$('#uclist').val($('#uclist').attr('bind-value'));
  				}});
              }
          //--></script> 
		</td>
	</tr>
	<tr>
		<td>Union Council<span class="mendatory-field">*</span></td>
        <td>
            <spring:bind path="command.${commandAdditionalPathStr}addUc">
            <select id="uclist" name="${commandAdditionalPathStr}addUc" bind-value="${status.value}">
                <option></option>
			</select>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</td>
	</tr>
