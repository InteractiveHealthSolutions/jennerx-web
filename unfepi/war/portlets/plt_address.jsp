<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<tr>
        <td>Address</td>
        <td><spring:bind path="command.${commandAdditionalPathStr}address1">
            <input type="text" id="address1" name="${commandAdditionalPathStr}address1" maxlength="255" value="<c:out value="${status.value}"/>"/>
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
				<option value="${status.value}"></option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
<script>
  $(document).ready(function() {
      DWREntityService.getLocationList(["city", "other"],null,{
           async: false,
           callback: function (resl) {
                   $('#cityId').empty().append('<option></option>');
                   for ( var i = 0; i < resl.length; i++) {
                           $('#cityId').append('<option value="'+resl[i].locationId+'">'+resl[i].fullname+'</option>');
                   }
                   $('#cityId').val($('#cityId').attr('bind-value'));
      }});
  });
              function loadTown() {
				DWREntityService.getLocationList(["town","na",'DONOT KNOW'],$('#cityId').val(),{
						async: false,
						callback: function (resl) {
							$('#townlist').empty().append('<option></option>');
							for ( var i = 0; i < resl.length; i++) {
								$('#townlist').append('<option value="'+resl[i].name+'">'+resl[i].name+'</option>');
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
  				DWREntityService.getLocationListByParentName(["uc","na",'DONOT KNOW'],$('#townlist').val(),{
  						async: false,
  						callback: function (resl) {
  							$('#uclist').empty().append('<option></option>');
  							for ( var i = 0; i < resl.length; i++) {
  								$('#uclist').append('<option value="'+resl[i].name+'">'+resl[i].fullname+'</option>');
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
