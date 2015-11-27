<%@page import="org.ird.unfepi.constants.WebGlobals"%>
<style>
.divtd {
width: 130px;
display: inline-block !important;
vertical-align: text-top;
}
.divtd span{
width: auto !important;
}
.divtd select {
width: auto !important;
max-width: 150px !important;
}
.inputsmall {
width: 50px !important;
}
</style>
<br>
        <div class="divtd">House <spring:bind path="command.${commandAdditionalPathStr}addHouseNumber"></div>
        <div class="divtd">
            <input class="inputsmall" type="text" name="addHouseNumber" maxlength="30" value="<c:out value="${status.value}"/>"/>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</div>
<br>
        <div class="divtd">Street</div>
        <div class="divtd">
            <spring:bind path="command.${commandAdditionalPathStr}addStreet">
            <input class="inputsmall" type="text" name="${commandAdditionalPathStr}addStreet" maxlength="30" value="<c:out value="${status.value}"/>"/>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</div>
<br>
         <div class="divtd">Sector</div>
         <div class="divtd">
             <spring:bind path="command.${commandAdditionalPathStr}addSector">
             <input class="inputsmall" type="text" name="${commandAdditionalPathStr}addSector" maxlength="30" value="<c:out value="${status.value}"/>"/>
             <span class="error-message"><c:out value="${status.errorMessage}"/></span>
		 </spring:bind>
		 </div>
<br>
        <div class="divtd">Colony <span class="mendatory-field"></span></div>
        <div class="divtd">
            <spring:bind path="command.${commandAdditionalPathStr}addColony">
            <input class="inputsmall" type="text" name="${commandAdditionalPathStr}addColony" maxlength="30" value="<c:out value="${status.value}"/>"/>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</div>
<br>
        <div class="divtd">Landmark </div>
        <div class="divtd">
            <spring:bind path="command.${commandAdditionalPathStr}addLandmark">
            <input type="text" name="${commandAdditionalPathStr}addLandmark" maxlength="50" value="<c:out value="${status.value}"/>"/>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</div>
<br>
		<div class="divtd">City <span class="mendatory-field">*</span></div>
		<div class="divtd"><spring:bind path="command.${commandAdditionalPathStr}cityId">
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
		</div>
<br>
		<div class="divtd">City, if Other <span class="mendatory-field">*</span></div>
		<div class="divtd">
			<spring:bind path="command.${commandAdditionalPathStr}cityName">
			<input type="text" id="cityName" name="${commandAdditionalPathStr}cityName" maxlength="30" value="<c:out value="${status.value}"/>" />
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
		</div>
<br>
        <div class="divtd">Town <span class="mendatory-field">*</span></div>
        <div class="divtd">
            <spring:bind path="command.${commandAdditionalPathStr}addtown">
            <select id="townlist" name="${commandAdditionalPathStr}addtown" onchange="loadUC();" bind-value="${status.value}">
                <option></option>
			</select>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			<script>
			<!--
			
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
		</div>
<br>
		<div class="divtd">Union Council<span class="mendatory-field">*</span></div>
        <div class="divtd">
            <spring:bind path="command.${commandAdditionalPathStr}addUc">
            <select id="uclist" name="${commandAdditionalPathStr}addUc" bind-value="${status.value}">
                <option></option>
			</select>
            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
			</spring:bind>
		</div>

	