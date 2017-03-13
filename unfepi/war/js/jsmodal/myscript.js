var headerColsForLocAttrs = ["parent", "attribute_name", "value", "type_value"];
var requiredColsForLocAttrs = ["parent", "attribute_name", "value", "type_value"];
var csvDataForLocAttrs = [];
var unknownColsForLocAttrs = [];

var headerCols = ["name", "short_name", "full_name", "parent", "identifier", "latitude", "longitude", "type", "description"];//, "children_population", "women_population"];//
var requiredCols = ["name", "parent", "type"];
var unknownCols = [];
var requiredColsData = [];
var csvData = [];
var zNodes = getLocations();
var locationTypeNodes = getLocationTypes();
var locationAttributeDataNodes = getLocationAttributes();
var locationAttributeNodes = getLocationAttributeTypes();
var parentName;
var locationAttributeId = [];
var locationAttributeName = [];
var locationAttributeDisplayName = [];
var locationAttributeDescription = [];
var locationAttributeCategory = [];
var globalRowId;
var globalCellId;
var nodesToBeSaved = [];
var errorStr = "";
var mySelectData = [];
if(jQuery.isEmptyObject(zNodes)) { zNodes = []; errorStr += "Location Database is EMPTY.<br/>"; }
if(jQuery.isEmptyObject(locationTypeNodes)) { locationTypeNodes = []; errorStr += "Location Type Database is EMPTY.<br/>"; }
if(jQuery.isEmptyObject(locationAttributeDataNodes)) { locationAttributeDataNodes = []; }
if(jQuery.isEmptyObject(locationAttributeNodes)) { locationAttributeNodes = []; }
if(!jQuery.isEmptyObject(locationAttributeNodes)) { 
	for(var i = 0; i<locationAttributeNodes.length; i++) {
		locationAttributeId.push(locationAttributeNodes[i].id);
		locationAttributeName.push(locationAttributeNodes[i].name);
		locationAttributeDisplayName.push(locationAttributeNodes[i].displayName);
		locationAttributeDescription.push(locationAttributeNodes[i].description);
		locationAttributeCategory.push(locationAttributeNodes[i].category);
	}
}
var disabledNodes = [66, 77 ,88 ,99];
$(document).ready(function(){
	if(errorStr !== "") {
//		$("#alertModalBody").html(errorStr);
//		$("#alertModal").modal('show');
		alertDIV("warning", "ERROR: ", errorStr);
	}
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
    $("#selectAll").bind("click", selectAll);
  $("#saveBtn").bind("click", saveTableRow);

    if(!jQuery.isEmptyObject(locationTypeNodes)) { 
    	for (var i = 0; i < locationTypeNodes.length; i++) {
    		var itemval= '<option value="' + locationTypeNodes[i].id +'">' + locationTypeNodes[i].name + '</option>';
    		$("#addType").append(itemval);
        }
	}
	$("#expandAllBtn").bind("click", {type:"expandAll"}, expandNode);
	$("#collapseAllBtn").bind("click", {type:"collapseAll"}, expandNode);

	$('#fileUpload').change( function(event) {
    	unknownCols = [];
    	requiredColsData = [];
    	csvData = [];
    	var fileUpload = document.getElementById("fileUpload");
    	var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt)");
    	if (regex.test(fileUpload.value.toLowerCase())) {
//    		alert(fileUpload.value.toLowerCase());
    		if (typeof (FileReader) != "undefined") {
    			var reader = new FileReader();
    			reader.onload = function (e) {
    				var rowArr = e.target.result.split("\n");
    				for(var i=0; i<rowArr.length-1; i++) {
    					csvData.push(rowArr[i].split(","));
    				}
    				csvData = ValidateTable();
    				if(csvData === null) {}
    				else {
    					$.ajax({
    						url: "/unfepi/data/location/newCsv",
        					type: "POST",
        					data : { csvData: JSON.stringify(csvData) },
        					success : function(result) {
        						var data = JSON.parse(result);
        						var zNodesData = eval(data.locationNodes[0]);
        					    var locationTypeNodesData = eval(data.locationTypeNodes[0]);
        						var locationAttributeNodesData = eval(data.locationAttributeTypeNodes[0]);
        						var locationAttributeDataNodesData = eval(data.locationAttributeNodes[0]);
//        						$("#alertModalBody").html(data.returnStr.toString());
//        						$("#alertModal").modal('show');
        						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
        						if(zNodesData != null && locationTypeNodesData != null && locationAttributeDataNodesData != null && locationAttributeNodesData != null) {
        							zNodes = zNodesData;
            						locationTypeNodes = locationTypeNodesData;
            						locationAttributeNodes = locationAttributeNodesData;
            						locationAttributeDataNodes = locationAttributeDataNodesData;
            						$.fn.zTree.init($("#treeDemo"), setting, zNodes);
            					    $("#selectAll").bind("click", selectAll);
            					    $("#saveBtn").bind("click", saveTableRow);
            						for (var i = 0; i < locationTypeNodes.length; i++) {
            							var itemval= '<option value="' + locationTypeNodes[i].id +'">' + locationTypeNodes[i].name + '</option>';
            							$("#addType").append(itemval);
            					    }
            						$("#expandAllBtn").bind("click", {type:"expandAll"}, expandNode);
            						$("#collapseAllBtn").bind("click", {type:"collapseAll"}, expandNode);
        						}
        					}, error: function(jqXHR, textStatus, errorThrown) { 
        						alertDIV("warning", "ERROR: ", "EXCEPTION OCCURED");
        						/*alert("EXCEPTION OCCURED");*/ 
    						}
    					});
    				}
    			} 
    			reader.readAsText(fileUpload.files[0]);
    		} else { alertDIV("warning", "ERROR: ", "This browser does not support HTML5.");/*alert("This browser does not support HTML5.");*/ }
    	} else { /*alert("Please upload a valid CSV file.");*/ alertDIV("warning", "ERROR: ", "Please upload a valid CSV file."); fileUpload.value = null; }
	});
	
	$('#fileUploadLocAttrs').change( function(event) {
    	unknownColsForLocAttrs = [];
    	requiredColsDataForLocAttrs = [];
		csvDataForLocAttrs = [];
		var fileUpload = document.getElementById("fileUploadLocAttrs");
    	var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt)");
    	if (regex.test(fileUpload.value.toLowerCase())) {
//    		alert(fileUpload.value.toLowerCase());
    		if (typeof (FileReader) != "undefined") {
    			var reader = new FileReader();
    			reader.onload = function (e) {
    				var rowArr = e.target.result.split("\n");
    				for(var i=0; i<rowArr.length-1; i++) {
    					csvDataForLocAttrs.push(rowArr[i].split(","));
    				}
    				csvDataForLocAttrs = ValidateTableForLocAttrs();
    				if(csvDataForLocAttrs === null) {}
    				else {
    					$.ajax({
    						url: "/unfepi/data/location/newCsvForLocAttrs",
        					type: "POST",
        					data : { csvData: JSON.stringify(csvDataForLocAttrs) },
        					success : function(result) {
        						var data = JSON.parse(result);
//        						$("#alertModalBody").html(data.returnStr.toString());
//        						$("#alertModal").modal('show');
        						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
        					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "EXCEPTION OCCURED"); /*alert("EXCEPTION OCCURED");*/ }
    					});
    				}
    			} 
    			reader.readAsText(fileUpload.files[0]);
    		} else { alertDIV("warning", "ERROR: ", "This browser does not support HTML5."); /*alert("This browser does not support HTML5.");*/ }
    	} else { alertDIV("warning", "ERROR: ", "Please upload a valid CSV file."); /*alert("Please upload a valid CSV file.");*/ fileUpload.value = null; }
	});


	$("#addAllNodes").bind("click", function() {
		var pattern = new RegExp("^[a-zA-Z ]*$");
		if(document.getElementById("addName").value !== "" && document.getElementById("addShortName").value !== "" && document.getElementById("addFullName").value !== "" && document.getElementById("addType").value !== "" && document.getElementById("addParent").value !== "0") {
			if(( document.getElementById("addName").value.length >= 3 && document.getElementById("addName").value.length <= 30 && pattern.test(document.getElementById("addName").value )) && ( document.getElementById("addShortName").value.length >= 3 && document.getElementById("addShortName").value.length <= 30 && pattern.test(document.getElementById("addShortName").value )) && (document.getElementById("addFullName").value.length >= 3 && document.getElementById("addFullName").value.length <= 50 && pattern.test(document.getElementById("addFullName").value))) {
		        $('#myModal').modal('toggle');
				var dataNode = { name: document.getElementById("addName").value, shortName: document.getElementById("addShortName").value, fullName: document.getElementById("addFullName").value, pId: document.getElementById("addParent").value, description: document.getElementById("addDescription").value, otherIdentifier: document.getElementById("addOtherIdentifier").value, type: document.getElementById("addType").value, latitude: document.getElementById("addLatitude").value, longitude: document.getElementById("addLongitude").value };
				$.ajax({
					url: "/unfepi/data/location/newlocation",
					type: "POST",
					data : dataNode,//$("#nodeForm").serialize(),
					success : function(result) {
						var data = JSON.parse(result);
						var id = eval(data.id[0]);
						if(data.status.toString() === "info") { }
						else { addAll(id); }
//						$("#alertModalBody").html(data.returnStr.toString());
//						$("#alertModal").modal('show');
//						alert("New Location Added.");
						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
						document.getElementById("errors").innerHTML = "";
				        document.getElementById("addName").value = "";
				        document.getElementById("addShortName").value = "";
				        document.getElementById("addFullName").value = "";
				        document.getElementById("addParent").value = "";
				        document.getElementById("addDescription").value = "";
				        document.getElementById("addOtherIdentifier").value = "";
				        document.getElementById("addType").value = "";
				        document.getElementById("addLatitude").value = "";
				        document.getElementById("addLongitude").value = "";
					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE CREATING NEW LOCATION"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION"); $("#alertModal").modal('show');*/ }
				});
			}
			else {//VALIDATION
				var errorStr = "";
				if(!(document.getElementById("addName").value.length >= 3)) { errorStr += "Location Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addName").value.length <= 30)) { errorStr += "Location Name must have atmost 30 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("addName").value))) { errorStr += "Location Name must have alphabets only.<br/>"; }
				if(!(document.getElementById("addShortName").value.length >= 3)) { errorStr += "Location Short Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addShortName").value.length <= 30)) { errorStr += "Location Short Name must have atmost 30 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("addShortName").value))) { errorStr += "Location Short Name must have alphabets only.<br/>"; }
				if(!(document.getElementById("addFullName").value.length >= 3)) { errorStr += "Location Full Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addFullName").value.length <= 30)) { errorStr += "Location Full Name must have atmost 30 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("addFullName").value))) { errorStr += "Location Full Name must have alphabets only.<br/>"; }
//			    $("#alertModalBody").html(errorStr); 
//			    $("#alertModal").modal('show');
				alertDIV1("warning", "ERROR: ", errorStr, "_addLocation");
			}
		}
		else {//VALIDATION-CHECK-NULL
			var errorStr = "";
			if(document.getElementById("addName").value === "") { errorStr += "Location Name is required.<br/>"; }
			if(document.getElementById("addShortName").value === "") { errorStr += "Location Short Name is required.<br/>"; }
			if(document.getElementById("addFullName").value === "") { errorStr += "Location Full Name is required.<br/>"; }
			if(document.getElementById("addParent").value === "0") { errorStr += "Location Parent is required.<br/>"; }
			if(document.getElementById("addType").value === "") { errorStr += "Location Type is required."; }	
//		    $("#alertModalBody").html(errorStr); 
//		    $("#alertModal").modal('show');
			alertDIV1("warning", "ERROR: ", errorStr, "_addLocation");
		 }
	});	
	$("#addAlllocationTypes").bind("click", function() {
		var pattern = new RegExp("^[0-9]*$");
		if(document.getElementById("addlocationTypeName").value !== "" ) {
			if(document.getElementById("addlocationTypeName").value.length >= 3 && document.getElementById("addlocationTypeName").value.length <= 50 && pattern.test(document.getElementById("addlocationTypeLevel").value )) {
		        $('#myModalLocationType').modal('toggle');
				var dataNode = { name: document.getElementById("addlocationTypeName").value, level: document.getElementById("addlocationTypeLevel").value, description: document.getElementById("addlocationTypeDescription").value };
				$.ajax({
					url: "/unfepi/data/location/newlocationType",
					type: "POST",
					data : dataNode,//$("#nodeForm").serialize(),
					success : function(result) {
						var data = JSON.parse(result);
						var id = eval(data.id[0]);
						var name = document.getElementById("addlocationTypeName").value;
						var level = document.getElementById("addlocationTypeLevel").value;
						var description = document.getElementById("addlocationTypeDescription").value;
						var node = { id: id, name: name, level: level, description: description };
						var itemval= '<option value="' + node.id +'">' + node.name + '</option>';
						if(data.status.toString() === "info") { }
						else { $("#addType").append(itemval); locationTypeNodes.push(node); }
//						$("#alertModalBody").html(data.returnStr.toString());
//						$("#alertModal").modal('show');
						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
//						alert("New Location Type Added.");
						document.getElementById("addlocationTypeName").value = "";
						document.getElementById("addlocationTypeLevel").value = "";
						document.getElementById("addlocationTypeDescription").value = "";
					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE CREATING NEW LOCATION TYPE"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION TYPE"); $("#alertModal").modal('show');*/ }
				});
			}
			else {//VALIDATION
				var errorStr = "";
		        if(!(document.getElementById("addlocationTypeName").value.length >= 3)) { errorStr += "Location Type Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addlocationTypeName").value.length <= 50)) { errorStr += "Location Type Name must have atmost 50 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("addlocationTypeLevel").value ))) { errorStr += "Location Type Level must have numbers only.<br/>"; }
//			    if(!(pattern.test(document.getElementById("addlocationTypeName").value))) { errorStr += "Name must have alphabets only."; }
//			    $("#alertModalBody").html(errorStr); 
//			    $("#alertModal").modal('show');
			    alertDIV1("warning", "ERROR: ", errorStr, "_addLocationType");
			}
		}
		else {//VALIDATION
			var errorStr= "";
			if(document.getElementById("addlocationTypeName").value === "") { errorStr += "Location Type Name is required."; }
//			    $("#alertModalBody").html(errorStr); 
//			    $("#alertModal").modal('show');
			    alertDIV1("warning", "ERROR: ", errorStr, "_addLocationType");
		 }
	});	
	$("#updateAlllocationTypes").bind("click", function() {
		var pattern = new RegExp("^[0-9]*$");
		var id = document.getElementById("updatelocationTypeName").value;
		var name = document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text;
		var level = document.getElementById("updatelocationTypeLevel").value;
		var description = document.getElementById("updatelocationTypeDescription").value;
		if(document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text !== "" ) {
			if(document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text.length >= 3 && document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text.length <= 50 && pattern.test(document.getElementById("updatelocationTypeLevel").value )) {
		        $('#myModalLocationTypeUpdate').modal('toggle');
				$.ajax({
					url: "/unfepi/data/location/updatelocationType",
					type: "POST",
					data : { id: id, name: name, level: level, description: description },
//					data : $("#locationTypeFormUpdate").serialize(),
					success : function(result) {
						var data = JSON.parse(result);
						var node = { id: id, name: name, level: level, description: description };
						for (var i = 0; i < locationTypeNodes.length; i++) {
							var id_ = null;
							var name_ = null;
							var level_ = null;
							var description_ = null;
							if(Array.isArray(locationTypeNodes[i].id[0])) { id_ = locationTypeNodes[i].id[0]; } else { id_ = locationTypeNodes[i].id; }
							if(Array.isArray(locationTypeNodes[i].name[0])) { name_ = locationTypeNodes[i].name[0]; } else { name_ = locationTypeNodes[i].name; }
							if(Array.isArray(locationTypeNodes[i].level[0])) { level_ = locationTypeNodes[i].level[0]; } else { level_ = locationTypeNodes[i].level; }
							if(Array.isArray(locationTypeNodes[i].description[0])) { description_ = locationTypeNodes[i].description[0]; } else { description_ = locationTypeNodes[i].description; }

							if(id_.toString() === node.id) {
								locationTypeNodes[i].id[0] = node.id;
								locationTypeNodes[i].name[0] = node.name;
								locationTypeNodes[i].level[0] = node.level;
								locationTypeNodes[i].description[0] = node.description;
							}
						}
//						$("#alertModalBody").html("Location Type Updated.");
//						$("#alertModal").modal('show');
						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
//						alert("Location Type Updated.");
						document.getElementById("updatelocationTypeName").value = id;
						document.getElementById("updatelocationTypeLevel").value = level;
						document.getElementById("updatelocationTypeDescription").value = description;
					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE UPDATING NEW LOCATION TYPE"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION TYPE"); $("#alertModal").modal('show');*/ }
				});
			}
			else {//VALIDATION
				var errorStr = "";
//		        if(!(document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text >= 3)) { errorStr += "Location Type Name must have atleast 3 characters.<br/>"; }
//			    if(!(document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text <= 50)) { errorStr += "Location Type Name must have atmost 50 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("updatelocationTypeLevel").value ))) { errorStr += "Location Type Level must have numbers only.<br/>"; }
//			    if(!(pattern.test(document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text))) { errorStr += "Name must have alphabets only."; }
//			    $("#alertModalBody").html(errorStr); 
//			    $("#alertModal").modal('show');
			    alertDIV1("warning", "ERROR: ", errorStr, "_updateLocationType");
			}
		}
		else {//VALIDATION
			var errorStr= "";
			if(document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text === "") { errorStr += "Location Type Name is required."; }
//			    $("#alertModalBody").html(errorStr); 
//			    $("#alertModal").modal('show');
			    alertDIV1("warning", "ERROR: ", errorStr, "_updateLocationType");
		 }
	});	
	$("#addAlllocationAttributeTypes").bind("click", function() {
		if(document.getElementById("addlocationAttributeTypeName").value !== "" ) {
			var dataNode = { name: document.getElementById("addlocationAttributeTypeName").value, displayName: document.getElementById("addlocationAttributeTypeDisplayName").value, description: document.getElementById("addlocationAttributeTypeDescription").value, category: document.getElementById("addlocationAttributeTypeCategory").value };
	        $('#myModalLocationAttributeType').modal('toggle');
			$.ajax({
				url: "/unfepi/data/location/newlocationattributetype",
				type: "POST",
				data : dataNode,//$("#locationAttributeTypeForm").serialize(),
				success : function(result) {
					var data = JSON.parse(result);
					var id = eval(data.id[0]);
					var name = document.getElementById("addlocationAttributeTypeName").value;
					var displayName = document.getElementById("addlocationAttributeTypeDisplayName").value;
					var description = document.getElementById("addlocationAttributeTypeDescription").value;
					var category = document.getElementById("addlocationAttributeTypeCategory").value;
					var node = { id: id, name: name, displayName: displayName, description: description, category: category };
					if(data.status.toString() === "info") { }
					else { 
						locationAttributeNodes.push(node);
						locationAttributeId.push(id);
						locationAttributeName.push(name);
						locationAttributeDisplayName.push(displayName);
						locationAttributeDescription.push(description);
						locationAttributeCategory.push(category);
						addToModal(id);
					}
//					$("#alertModalBody").html(data.returnStr.toString());
//					$("#alertModal").modal('show');
					alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString()); 
//					alert("New Attribute Type Created.");
				}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE CREATING NEW LOCATION ATTRIBUTE TYPE"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION ATTRIBUTE TYPE"); $("#alertModal").modal('show');*/ }
			});

		}
		else {// VALIDATION
			var errorStr = "";
			if(document.getElementById("addlocationAttributeTypeName").value === "" ) { errorStr += "Location Attribute Type Name is required."; }
//			$("#alertModalBody").html(errorStr); 
//		    $("#alertModal").modal('show');
			alertDIV1("warning", "ERROR: ", errorStr, "_addLocationAttributeType");
		}
	});	
	$("#updateAlllocationAttributeTypes").bind("click", function() {
		if(document.getElementById("updatelocationAttributeTypeName").value !== "" ) {
			var id = document.getElementById("updatelocationAttributeTypeName").value;
			var name = document.getElementById("updatelocationAttributeTypeName").options[document.getElementById("updatelocationAttributeTypeName").selectedIndex].text;
			var displayName = document.getElementById("updatelocationAttributeTypeDisplayName").value;
			var description = document.getElementById("updatelocationAttributeTypeDescription").value;
			var category = document.getElementById("updatelocationAttributeTypeCategory").value;
	        $('#myModalLocationAttributeTypeUpdate').modal('toggle');
			$.ajax({
				url: "/unfepi/data/location/updatelocationattributetype",
				type: "POST",
				data : { id: id, name: name, displayName: displayName, description: description, category: category },
//				data : $("#locationAttributeTypeFormUpdate").serialize(),
				success : function(result) {
					var data = JSON.parse(result);
					var node = { id: id, name: name, displayName: displayName, description: description, category: category };
					locationAttributeNodes.push(node);
					locationAttributeId.push(id);
					locationAttributeName.push(name);
					locationAttributeDisplayName.push(displayName);
					locationAttributeDescription.push(description);
					locationAttributeCategory.push(category);

//					$("#alertModalBody").html("Location Attribute Type Updated.");
//					$("#alertModal").modal('show');
					alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString()); 
//					alert("Location Attribute Type Updated.");
				}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE UPDATING NEW LOCATION ATTRIBUTE TYPE"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION ATTRIBUTE TYPE"); $("#alertModal").modal('show');*/ }
			});
		}
		else {// VALIDATION
			var errorStr = "";
			if(document.getElementById("updatelocationAttributeTypeName").value === "" ) { errorStr += "Location Attribute Type Name is required."; }
//			$("#alertModalBody").html(errorStr); 
//		    $("#alertModal").modal('show');
			alertDIV1("warning", "ERROR: ", errorStr, "_updateLocationAttributeType");
		}
	});	

	$("#addAlllocationAttributes").bind("click", function() {
		if(document.getElementById("locationParent").value !== "" ) {
			var boundArr = [];
			var errBoundArr = [];
			var errorStr= "";
			var parent = document.getElementById("locationParent").value;
//			var i=0;
			for (var i = 0; i < locationAttributeId.length; i++) {
				var myBound = null;
				var radioGroup = document.getElementsByName("bound_" + locationAttributeId[i]);
				for (var j = 0; j < radioGroup.length; j++) {
					if(radioGroup[j].checked) {
						myBound = radioGroup[j];
						if(myBound.value === "None") { 
							var node = {id: myBound.id, locationAttributeTypeValue: document.getElementById("locationAttribute_"+locationAttributeId[i][0]).value, value1: null, value2: null, type: myBound.value, locationAttributeTypeId: locationAttributeId[i][0], locationAttributeTypeName: locationAttributeDisplayName[i][0] }; 
							if(node.locationAttributeTypeValue !== "") {
								if(node.value1 !== "" && node.value2 !== "") { boundArr.push(node); }
								else { if(node.value1 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + node.type + "' attribute is required.<br/>"; } }
							}
//							else { errorStr += node.locationAttributeTypeName + " is required.<br/>";  }
						}
						else if(myBound.value === "Time_Bound") {
							if(document.getElementById("locationAttribute_"+locationAttributeId[i][0]).value !== "") {
								var node = {id: myBound.id, locationAttributeTypeValue: document.getElementById("locationAttribute_"+locationAttributeId[i][0]).value, value1: document.getElementById(myBound.id+"_BOUND_From").value, value2: document.getElementById(myBound.id+"_BOUND_To").value, type: myBound.value, locationAttributeTypeId: locationAttributeId[i][0], locationAttributeTypeName: locationAttributeDisplayName[i][0] }; 
								var type = node.type;
								while(type.includes("_")) { type = type.replace("_", " "); }
								if(node.locationAttributeTypeValue !== "") {
									if(node.value1 !== "" && node.value2 !== "") { boundArr.push(node); }
									else {
										if(node.value1 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + type + " From' attribute is required.<br/>"; }
										if(node.value2 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + type + " To' attribute is required.<br/>"; }
									}
								} else { errorStr += node.locationAttributeTypeName + " is required.<br/>"; }
							} else { errorStr += locationAttributeName[i][0] + " is required.<br/>"; }
						}
						else {
							if(document.getElementById("locationAttribute_"+locationAttributeId[i][0]).value !== "") {
							var node = {id: myBound.id, locationAttributeTypeValue: document.getElementById("locationAttribute_"+locationAttributeId[i][0]).value, value1: document.getElementById(myBound.id+"_BOUND").value, value2: null, type: myBound.value, locationAttributeTypeId: locationAttributeId[i][0], locationAttributeTypeName: locationAttributeDisplayName[i][0] }; 
								if(node.locationAttributeTypeValue !== "") {
									if(node.value1 !== "" && node.value2 !== "") { boundArr.push(node); }
									else { if(node.value1 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + node.type + "' attribute is required.<br/>"; } }
								} else { errorStr += node.locationAttributeTypeName + " is required.<br/>"; }
							} else { errorStr += locationAttributeName[i][0] + " is required.<br/>"; }
						}
					}
				}
			}
			
			if(errorStr !== "") {
//				$("#alertModalBody").html(errorStr);
//				$("#alertModal").modal('show');
				alertDIV1("warning", "ERROR: ", errorStr, "_addLocationAttribute");
			}
			else {
		        $('#myModalLocationAttribute').modal('toggle');
				$.ajax({
					url: "/unfepi/data/location/newlocationattribute",
					type: "POST",
					data : { parent: parent, array: JSON.stringify(boundArr) },
//					data : $("#locatioAttributeForm").serialize(),
					success : function(result) {
						var data = JSON.parse(result);
						var idArr = eval(data.id[0]);
						if(idArr.length === boundArr.length) {
							for (var i = 0; i < boundArr.length; i++) {
								var myLocAttrNode = { id: eval([idArr[i]]), typeId: eval([boundArr[i].locationAttributeTypeId]), locationId: eval([parent]), value: eval([boundArr[i].locationAttributeTypeValue]), typeName: eval([boundArr[i].type]), typeValue1: eval([boundArr[i].value1]), typeValue2: eval([boundArr[i].value2]) };
								locationAttributeDataNodes.push(myLocAttrNode);
							}
						}
						$("#alertModalBody").html(data.returnStr.toString());
//						$("#alertModal").modal('show');
						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
//						alert("New Attribute Created.");

						for (var i = 0; i < boundArr.length; i++) {
							var node = boundArr[i];
							if(node.type === "None") { 
								if(node.value1 !== "" && node.value2 !== "") {
									document.getElementById("locationAttribute_"+node.locationAttributeTypeId).value = "";
									document.getElementById(node.id+"_DIV").innerHTML = "";
									var radioGroup = document.getElementsByName("bound_" + node.locationAttributeTypeId);
									for (var j = 0; j < radioGroup.length; j++) { if(radioGroup[j].value === "None") { radioGroup[j].checked = true; } }
								}
							}
							else if(node.type === "Time_Bound") {
								if(node.value1 !== "" && node.value2 !== "") {
									document.getElementById("locationAttribute_"+node.locationAttributeTypeId).value = "";
									document.getElementById(node.id+"_DIV").innerHTML = "";
									var radioGroup = document.getElementsByName("bound_" + node.locationAttributeTypeId);
									for (var j = 0; j < radioGroup.length; j++) { if(radioGroup[j].value === "None") { radioGroup[j].checked = true; } }
								}
							}
							else { 
								if(node.value1 !== "" && node.value2 !== "") {
									document.getElementById("locationAttribute_"+node.locationAttributeTypeId).value = "";
									document.getElementById(node.id+"_DIV").innerHTML = "";
									var radioGroup = document.getElementsByName("bound_" + node.locationAttributeTypeId);
									for (var j = 0; j < radioGroup.length; j++) { if(radioGroup[j].value === "None") { radioGroup[j].checked = true; } }
								}
							}
						}
						for(var i=0; i<locationAttributeId.length; i++) { document.getElementById("locationAttribute_"+locationAttributeId[i]).value = ""; }
					}
				});
			}
		}
		else {// VALIDATION
			var errorStr = "";
			if(document.getElementById("locationParent").value === "" ) { errorStr += "Parent is required."; }
//		    $("#alertModalBody").html(errorStr); 
//		    $("#alertModal").modal('show');
			alertDIV1("warning", "ERROR: ", errorStr, "_addLocationAttribute");
		}
	});	
});
var count = 0;
var log, className = "dark";
var setting = {
    view: {
        addHoverDom: addHoverDom,
        removeHoverDom: removeHoverDom,
        selectedMulti: false
    },
    edit: {
        enable: true,
        editNameSelectAll: true,
        showRemoveBtn: false,
        showRenameBtn: false
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        beforeDrag: beforeDrag,
        beforeEditName: beforeEditName,
        beforeRemove: beforeRemove,
        beforeRename: beforeRename,
        onRemove: onRemove,
        onRename: onRename,
        onClick: onClick,
        beforeCollapse: beforeCollapse,
		beforeExpand: beforeExpand,
		onCollapse: onCollapse,
		onExpand: onExpand
    }
};
function beforeCollapse(treeId, treeNode) {
	className = (className === "dark" ? "":"dark");
	showLog("[ "+getTime()+" beforeCollapse ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name );
	return (treeNode.collapse !== false);
}
function onCollapse(event, treeId, treeNode) {
	showLog("[ "+getTime()+" onCollapse ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name);
}		
function beforeExpand(treeId, treeNode) {
	className = (className === "dark" ? "":"dark");
	showLog("[ "+getTime()+" beforeExpand ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name );
	return (treeNode.expand !== false);
}
function onExpand(event, treeId, treeNode) {
	showLog("[ "+getTime()+" onExpand ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name);
}
function showLog(str) {
	if (!log) log = $("#log");
	log.append("<li class='"+className+"'>"+str+"</li>");
	if(log.children("li").length > 8) {
		log.get(0).removeChild(log.children("li")[0]);
	}
}
function getTime() {
	var now= new Date(),
	h=now.getHours(),
	m=now.getMinutes(),
	s=now.getSeconds(),
	ms=now.getMilliseconds();
	return (h+":"+m+":"+s+ " " +ms);
}
function expandNode(e) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
	type = e.data.type,
	nodes = zTree.getSelectedNodes();
	if (type == "expandAll") {
		zTree.expandAll(true);
	} else if (type == "collapseAll") {
		zTree.expandAll(false);
	}
}
function beforeDrag(treeId, treeNodes) {
    return false;
}
function beforeEditName(treeId, treeNode) {
    className = (className === "dark" ? "":"dark");
    showLog("[ "+getTime()+" beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    setTimeout(function() {
    if (confirm("Start node '" + treeNode.name + "' editorial status?")) {
        setTimeout(function() {
            zTree.editName(treeNode);
        }, 0);
    }
    }, 0);
    return false;
}
function beforeRemove(treeId, treeNode) {
    className = (className === "dark" ? "":"dark");
    showLog("[ "+getTime()+" beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    return confirm("Confirm delete node '" + treeNode.name + "' it?");
}
function onRemove(e, treeId, treeNode) {
    showLog("[ "+getTime()+" onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
}
function beforeRename(treeId, treeNode, newName, isCancel) {
    className = (className === "dark" ? "":"dark");
    showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
    if (newName.length === 0) {
        setTimeout(function() {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.cancelEditName();
            alert("Node name can not be empty.");
        }, 0);
        return false;
    }
    return true;
}
function onRename(e, treeId, treeNode, isCancel) {
    showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
}
function showLog(str) {
    if (!log) log = $("#log");
    log.append("<li class='"+className+"'>"+str+"</li>");
    if(log.children("li").length > 8) { log.get(0).removeChild(log.children("li")[0]); }
}
function getTime() {
    var now= new Date(),
    h=now.getHours(),
    m=now.getMinutes(),
    s=now.getSeconds(),
    ms=now.getMilliseconds();
    return (h+":"+m+":"+s+ " " +ms);
}
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    
    if (treeNode.editNameFlag || $("#addNodeBtn_"+treeNode.tId).length>0) return;
    if (treeNode.editNameFlag || $("#addStatBtn_"+treeNode.tId).length>0) return;
    var addStr1 = "<button type='button' id='addNodeBtn_" + treeNode.tId + "' style='background: transparent; background-color: Transparent; background-repeat:no-repeat; border: none; cursor:pointer; overflow: hidden; outline:none;' title='Add New Location' data-toggle='modal' data-target='#myModal' ><img src='images/plus.png' width='15px' height='15px'></button>";
    var addStr2 = "<button type='button' id='addStatBtn_" + treeNode.tId + "' style='background: transparent; background-color: Transparent; background-repeat:no-repeat; border: none; cursor:pointer; overflow: hidden; outline:none;' title='Add New Lcation Attribute' data-toggle='modal' data-target='#myModalLocationAttribute' ><img src='images/plus.png' width='15px' height='15px'></button>";
    
    document.getElementById("nextBtnLocation").setAttribute("type", "button");
    document.getElementById("nextBtnLocationType").setAttribute("type", "button");
    document.getElementById("nextBtnLocationAttributeType").setAttribute("type", "button");
    document.getElementById("nextBtnLocationAttribute").setAttribute("type", "button");
    
	document.getElementById("nextBtnLocation").style.visibility = "visible";
	document.getElementById("nextBtnLocationType").style.visibility = "visible";
	document.getElementById("nextBtnLocationAttributeType").style.visibility = "visible";
	document.getElementById("nextBtnLocationAttribute").style.visibility = "visible";

	
	var tId = null; var id = null; var name = null;
	if(Array.isArray(treeNode.tId)) { tId = treeNode.tId[0]; } else { tId = treeNode.tId; }
	if(Array.isArray(treeNode.name)) { name = treeNode.name[0]; } else { name = treeNode.name; }
	if(Array.isArray(treeNode.tId)) { typeName = treeNode.typeName[0]; } else { typeName = treeNode.typeName; }
	
	
	document.getElementById(tId + "_a").title = name + "-[" + typeName + "]";
    sObj.after(addStr1 + " " + addStr2);
};
function removeHoverDom(treeId, treeNode) {
    $("#addNodeBtn_"+treeNode.tId).unbind().remove();
    $("#addStatBtn_"+treeNode.tId).unbind().remove();
};
function selectAll() {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
}
function onClick(e, treeId, treeNode) {
	if(treeNode != null) {
		var parentDiv = document.getElementById("addParentLocationListDiv");
		parentDiv.innerHTML = ""; 
		var label = document.createElement("LABEL");
		label.setAttribute("for", "addParent");
		label.setAttribute("class", "form-control-label");
		label.innerHTML = "Parent Id:"
		parentDiv.appendChild(label);
		var parentInput = document.createElement('input');
		parentInput.setAttribute("type", "text");  
		parentInput.setAttribute("name", "addParent");
		parentInput.setAttribute("id", "addParent");
		parentInput.setAttribute("readonly", "readonly");
		parentInput.setAttribute("class", "form-control");
		parentInput.setAttribute("value", "");
		parentInput.setAttribute("title", "Choose Parent Location");
		parentInput.setAttribute("placeholder", "Parent Location");
		parentDiv.appendChild(parentInput);

		var parentDiv = document.getElementById("addParentLocationAttributeListDiv");
		parentDiv.innerHTML = ""; 
		var label = document.createElement("LABEL");
		label.setAttribute("for", "addParent");
		label.setAttribute("class", "form-control-label");
		label.innerHTML = "Parent Id:"
		parentDiv.appendChild(label);
		var parentInput = document.createElement('input');
		parentInput.setAttribute("type", "text");  
		parentInput.setAttribute("name", "locationParent");
		parentInput.setAttribute("id", "locationParent");
		parentInput.setAttribute("readonly", "readonly");
		parentInput.setAttribute("class", "form-control");
		parentInput.setAttribute("value", "");
		parentInput.setAttribute("title", "Choose Parent Location");
		parentInput.setAttribute("placeholder", "Parent Location");
		parentDiv.appendChild(parentInput);

		document.getElementById("locationParent").value = treeNode.id;
		document.getElementById("addParent").value = treeNode.id;
		parentName = treeNode.name;
	}
    if(jQuery.isEmptyObject(treeNode.children) !== true) GenerateTable(treeNode.children, treeNode.name);
    else document.getElementById("dvTable").innerHTML = "<h2>Child Locations of '" + treeNode.name + "'</h2><br/><br/><p>&emsp;No Child Locations associated with '"+treeNode.name+"'.</p>";
}
function addAll(id) {
	var name = document.getElementById("addName").value;
    var shortName = document.getElementById("addShortName").value;
    var fullName = document.getElementById("addFullName").value;
    var parent = document.getElementById("addParent").value;
    var description = document.getElementById("addDescription").value;
    var otherIdentifier = document.getElementById("addOtherIdentifier").value;
    var type = document.getElementById("addType").value;
    var typeName = document.getElementById("addType").options[document.getElementById("addType").selectedIndex].text;
    var latitude = document.getElementById("addLatitude").value;
    var longitude = document.getElementById("addLongitude").value;    
    var node;
	var treeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	var treeNode = treeObj.getNodeByParam('id', parent);
    var parentName = null;
    if(treeNode === null) { parentName = ""; }
    else { if(Array.isArray(treeNode.pName)) { parentName = treeNode.pName[0]; } else { parentName = treeNode.pName; } }
    if(id != "" && name != "" && type != "") {
        node = { id: id, name: name, shortName: shortName, fullName: fullName, pId: parent, pName: parentName, description: description, otherIdentifier: otherIdentifier, type: type, typeName: typeName, latitude: latitude, longitude: longitude, open:true};
        document.getElementById("errors").innerHTML = "";
        document.getElementById("addName").value = "";
        document.getElementById("addShortName").value = "";
        document.getElementById("addFullName").value = "";
        document.getElementById("addParent").value = "";
        document.getElementById("addDescription").value = "";
        document.getElementById("addOtherIdentifier").value = "";
        document.getElementById("addType").value = "";
        document.getElementById("addLatitude").value = "";
        document.getElementById("addLongitude").value = "";
//    	console.log(node);
//    	console.log(zNodes);
    	zNodes.push(node);
//    	console.log(zNodes);
	    addToTree(node);
	    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
//	    var x = document.getElementById("addParent");
//	    var option = document.createElement("addParent");
//	    option.value = parent;
//	    option.text = parentName;
//	    x.add(option);
    }
}
function GenerateTable(myNodes, parentName) {
	var headerArray = ["", "Name", "Short Name", "Full Name", "Other Identifier", "Location Type", "Description"];
    var editArray = [];
    var idArray = [];
    var nameArray = [];
    var shortNameArray = [];
    var fullNameArray = [];
    var pIdArray = [];
    var pNameArray = [];
    var descriptionArray = [];
    var otherIdentifierArray = [];
    var typeArray = [];
    var typeNameArray = [];
    
    var i=0;
    while(myNodes[i] !== undefined) {
    	editArray.push("<a name='editBtn' id='editBtn' class='icon'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>");
        idArray.push(myNodes[i].id);
        nameArray.push(myNodes[i].name);
        shortNameArray.push(myNodes[i].shortName);
        fullNameArray.push(myNodes[i].fullName);
        pIdArray.push(myNodes[i].pId);
        pNameArray.push(parentName);
        descriptionArray.push(myNodes[i].description);
        otherIdentifierArray.push(myNodes[i].otherIdentifier);
        typeArray.push(myNodes[i].type);
        typeNameArray.push(myNodes[i].typeName);
        i++;
    }
    var arr = [editArray.length, idArray.length, nameArray.length, shortNameArray.length, fullNameArray.length, pIdArray.length, pNameArray.length, descriptionArray.length, otherIdentifierArray.length, typeArray.length, typeNameArray.length];
    arr = arr.sort();
    arr = arr.reverse();
    var rowsAfterHeader = arr[0];
    var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "datatable";
//    table.width = '80%';
    var row = table.insertRow(-1);
    row.id = "row_h";
    for (var i = 0; i < headerArray.length; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = headerArray[i];
        row.appendChild(headerCell);
    }
    var count=0;
    for (var i = 0; i < rowsAfterHeader; i++) {
    	row = table.insertRow(-1);
        row.id = "row"+i;

        var cell = row.insertCell(-1);
        var id = "row"+i+"_"+count;
        cell.innerHTML = "<a name='editBtn' id='editBtn' class='icon' title='Update "+nameArray[i]+"' onclick='editTableRow(\""+id+"\");'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>";
        cell.id= "row"+i+"_"+count; count++;

        var cell = row.insertCell(-1);
        if(nameArray[i] !== undefined) { cell.innerHTML = "<a id='row"+i+"_link' name='row"+i+"_link' style='cursor:pointer' title='View Details of "+nameArray[i]+"' onclick='viewLocationDetails("+JSON.stringify(idArray[i][0])+");'>" + nameArray[i] + "</a> "; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(shortNameArray[i] !== undefined) { cell.innerHTML = shortNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(fullNameArray[i] !== undefined) { cell.innerHTML = fullNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(otherIdentifierArray[i] !== undefined) { cell.innerHTML = otherIdentifierArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(typeNameArray[i] !== undefined) { cell.innerHTML = typeNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(descriptionArray[i] !== undefined) { cell.innerHTML = descriptionArray[i]; }
        else { cell.innerHTML = ""; }

    }
    document.getElementById("dvTable").innerHTML = "<h2>Child Locations of '" + parentName + "'</h2>";
    document.getElementById("dvTable").appendChild(table);
}
function addToTree(node) {
	if(document.getElementById("datatable") == null) {}
	else {
		var i = document.getElementById("datatable").rows.length;
		var table = document.getElementById("datatable");
		var row = table.insertRow(-1);
	    row.id = "row"+i;
        var id = "row"+i+"_"+count;
		var edit = "<a name='editBtn' id='editBtn' class='icon' title='Update "+node.name+"' onclick='editTableRow(\""+id+"\");'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>";

		var cell = row.insertCell(-1);
	    cell.id= "row"+i+"_"+count; count++;
	    cell.innerHTML = edit;

	    var html = "<a id='row"+i+"_link' name='row"+i+"_link' style='cursor:pointer' title='Update "+node.name+"'onclick='viewLocationDetails("+JSON.stringify(node.id)+");'>" + node.name + "</a>"; 

	    var cell = row.insertCell(-1);
	    if(node.name !== undefined) { cell.innerHTML = html; }
	    else { cell.innerHTML = ""; }

	    var cell = row.insertCell(-1);
	    if(node.shortName !== undefined) { cell.innerHTML = node.shortName; }
	    else { cell.innerHTML = ""; }

	    var cell = row.insertCell(-1);
	    if(node.fullName !== undefined) { cell.innerHTML = node.fullName; }
	    else { cell.innerHTML = ""; }

	    var cell = row.insertCell(-1);
	    if(node.otherIdentifier !== undefined) { cell.innerHTML = node.otherIdentifier; }
	    else { cell.innerHTML = ""; }
	    
	    var cell = row.insertCell(-1);
	    if(node.typeName !== undefined) { cell.innerHTML = node.typeName; }
	    else { cell.innerHTML = ""; }

	    var cell = row.insertCell(-1);
	    if(node.description !== undefined) { cell.innerHTML = node.description; }
	    else { cell.innerHTML = ""; }
	}
}
function addToModal(id) {
	var bodyDiv = document.getElementById("locationAttributeModal");
	
	var div = document.createElement("div");
    div.setAttribute("class", "form-group");

    var descriptionLabel = document.createElement('label');
    descriptionLabel.innerHTML = document.getElementById("addlocationAttributeTypeDisplayName").value + ":";
    descriptionLabel.setAttribute("for", "locationAttribute_"+id);
    descriptionLabel.setAttribute("class", "form-control-label");
    div.appendChild(descriptionLabel);

    var descriptionValue = document.createElement('input');
    descriptionValue.setAttribute("type", "number");  
    descriptionValue.setAttribute("name", "locationAttribute_"+id);
    descriptionValue.setAttribute("id", "locationAttribute_"+id);
    descriptionValue.setAttribute("class", "form-control");
    descriptionValue.setAttribute("value", "");
    div.appendChild(descriptionValue);

    bodyDiv.appendChild(div);
    
    var radioHTML = "";
    radioHTML += '<div id="bound_'+locationAttributeId[i]+'_RADIO" class="form-group">';
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="None" checked>&nbsp;None&nbsp;';//+locationAttributeId[i]+
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Anually">&nbsp;Anually&nbsp;';
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
    radioHTML += '</div>'
    radioHTML += '<div id="bound_'+locationAttributeId[i]+'_DIV"></div>';
    
    bodyDiv.innerHTML += radioHTML;
	
	document.getElementById("addlocationAttributeTypeName").value = "";
	document.getElementById("addlocationAttributeTypeDisplayName").value = "";
	document.getElementById("addlocationAttributeTypeDescription").value = "";
	document.getElementById("addlocationAttributeTypeCategory").value = "";
}
function editTableCell(cell) {
	cell.contentEditable = false;
}
function editTableRow(cellId) {
    var rowId = cellId.substr(0, cellId.indexOf("_"));
    document.getElementById(cellId).innerHTML = "<a name='saveBtn' id='saveBtn' title='Save' onclick='saveTableRow();' class='icon'><img alt='edit' src='images/tick.png' class='icon' width='15px' height='15px' ></a>";
    for (var i=0; i<document.getElementById(rowId).cells.length; i++) {
	  	if(i===0) { }
	  	else if(i===1) { }
	  	else if(i===5) { 
	  		var html = "<select id='mySelect_"+rowId+"' name='mySelect_"+rowId+"' class='form-control myclass' title='Choose Location Type' >";//onchange='mySelectChange(this);'
	  		if(!jQuery.isEmptyObject(locationTypeNodes)) { 
  				for (var j = 0; j < locationTypeNodes.length; j++) { 
  					if(locationTypeNodes[j].name[0] === document.getElementById(rowId).cells[i].innerHTML) { html += '<option value="' + locationTypeNodes[j].id +'" selected>' + locationTypeNodes[j].name + '</option>'; } 
		  			else { html += '<option value="' + locationTypeNodes[j].id +'">' + locationTypeNodes[j].name + '</option>'; } 
	  			} 
			} else {html += '<option value=""></option>'; } html += "</select>";
			document.getElementById(rowId).cells[i].innerHTML = html;
//			console.log(html);
//	  		document.getElementById(rowId).cells[i].contentEditable = true;
//	  		document.getElementById(rowId).cells[i].style.padding = "10px";
	  		document.getElementById(rowId).cells[i].style.backgroundColor = "#f8fbff"; 
	  		document.getElementById(rowId).cells[i].style.border = "2px solid #428bca";
	  	} 
	  	else {
	  		document.getElementById(rowId).cells[i].contentEditable = true;
	  		document.getElementById(rowId).cells[i].style.backgroundColor = "#f8fbff"; 
	  		document.getElementById(rowId).cells[i].style.padding = "10px";
	  		document.getElementById(rowId).cells[i].style.border = "2px solid #428bca";
  		}
	} globalRowId = rowId; globalCellId = cellId;
}
function saveTableRow() {
	var headerArray = ["", "Name", "Short Name", "Full Name", "Other Identifier", "Location Type", "Description"];
	var txt;
    var r = confirm("Would you like to save it?");
    if (r === true) {
    	var name = document.getElementById(globalRowId+"_link").innerHTML;
    	var treeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		var node = treeObj.getNodeByParam('name', name);
		if(node.name[0] === name) { 
			for (var i = 0; i < headerArray.length; i++) {
			  	if(i === 0) { }
			  	else if(i === 1) { }
			  	else if(i === 2) { node.shortName = document.getElementById(globalRowId).cells[i].innerHTML;}
			  	else if(i === 3) { node.fullName = document.getElementById(globalRowId).cells[i].innerHTML;}
			  	else if(i === 4) { node.otherIdentifier = document.getElementById(globalRowId).cells[i].innerHTML;}
			  	else if(i === 5) { node.type = document.getElementById("mySelect_"+globalRowId).value; node.typeName = document.getElementById("mySelect_"+globalRowId).options[document.getElementById("mySelect_"+globalRowId).selectedIndex].text; 			  		document.getElementById(globalRowId).cells[i].innerHTML = document.getElementById("mySelect_"+globalRowId).options[document.getElementById("mySelect_"+globalRowId).selectedIndex].text; }
			  	else if(i === 6) { node.description = document.getElementById(globalRowId).cells[i].innerHTML;}
			  	else { }
				document.getElementById(globalRowId).cells[i].contentEditable = false;
			  	document.getElementById(globalRowId).cells[i].style.backgroundColor = "";
			  	document.getElementById(globalRowId).cells[i].style.border = "";
			  	document.getElementById(globalRowId).cells[i].style.padding = "";			}
		}
		treeObj.updateNode(node);
		document.getElementById(globalRowId).cells[0].innerHTML = "<a name='editBtn' id='editBtn' class='icon'  title='Update "+name+"' onclick='editTableRow(\""+globalCellId+"\");'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>";

		var id = null;
		var name = null;
		var shortName = null;
		var fullName = null;
		var pId = null;
		var pName = null;
		var description = null;
		var otherIdentifier = null;
		var type = null;
		var typeName = null;
		var latitude = null;
		var longitude = null;
		
		if(Array.isArray(node.id)) { id = node.id[0]; } else { id = node.id; }
		if(Array.isArray(node.name)) { name = node.name[0]; } else { name = node.name; }
		if(Array.isArray(node.shortName)) { shortName = node.shortName[0]; } else { shortName = node.shortName; }
		if(Array.isArray(node.fullName)) { fullName = node.fullName[0]; } else { fullName = node.fullName; }
		if(Array.isArray(node.pId)) { pId = node.pId[0]; } else { pId = node.pId; }
		if(Array.isArray(node.pName)) { pName = node.pName[0]; } else { pName = node.pName; }
		if(Array.isArray(node.description)) { description = node.description[0]; } else { description = node.description; }
		if(Array.isArray(node.otherIdentifier)) { otherIdentifier = node.otherIdentifier[0]; } else { otherIdentifier = node.otherIdentifier; }
		if(Array.isArray(node.type)) { type = node.type[0]; } else { type = node.type; }
		if(Array.isArray(node.typeName)) { typeName = node.typeName[0]; } else { typeName = node.typeName; }
		if(Array.isArray(node.latitude)) { latitude = node.latitude[0]; } else { latitude = node.latitude; }
		if(Array.isArray(node.longitude)) { longitude = node.longitude[0]; } else { longitude = node.longitude; }
		$.ajax({
			url: "/unfepi/data/location/newlocationupdate",
			type: "POST",
			data : { id: id, name: name, shortName: shortName, fullName: fullName, pId: pId, pName: pName, description: description, otherIdentifier: otherIdentifier, type: type, typeName: typeName, latitude: latitude, longitude: longitude },
			success : function(result) {
				var data = JSON.parse(result);
//				alert("Location Updated");
//				$("#alertModalBody").html("Location Updated");
//				$("#alertModal").modal('show');
				alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
			}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE UPDATING CHILD LOCATIONS"); /*$("#alertModalBody").html("ERROR OCCURED WHILE UPDATING CHILD LOCATIONS"); $("#alertModal").modal('show');*/ }
		});
    }
    else {
		var node = treeObj.getNodeByParam('id', document.getElementById(globalRowId).cells[1].innerHTML);
		if(node.id == document.getElementById(globalRowId).cells[1].innerHTML) { 
			document.getElementById(globalRowId).cells[2].innerHTML = node.name;
			document.getElementById(globalRowId).cells[3].innerHTML = node.shortName;
			document.getElementById(globalRowId).cells[4].innerHTML = node.fullName;
			document.getElementById(globalRowId).cells[5].innerHTML = node.otherIdentifier;
			document.getElementById(globalRowId).cells[6].innerHTML = node.typeName;
			document.getElementById(globalRowId).cells[7].innerHTML = node.description;
			document.getElementById(globalRowId).cells[2].contentEditable = true;
			document.getElementById(globalRowId).cells[3].contentEditable = true;
			document.getElementById(globalRowId).cells[4].contentEditable = true;
			document.getElementById(globalRowId).cells[5].contentEditable = true;
			document.getElementById(globalRowId).cells[6].contentEditable = true;
			document.getElementById(globalRowId).cells[7].contentEditable = true;
		}
		treeObj.updateNode(node);
    }
}
function removeDuplicates(arr){
    var tmp = [];
    for(var i = 0; i < arr.length; i++){ if(tmp.indexOf(arr[i]) == -1) { tmp.push(arr[i]); } }
    return tmp;
}
function remove(array, value) {
	var newArray = [];
    for(var i=0; i<array.length; i++) {
    	if(array[i]==value) {}
		else {
			newArray.push(array[i]);
        }
	}
    return newArray;
}
function openModals(id) {
	if(id === 'addLocationBtn') { 		
		document.getElementById("nextBtnLocation").setAttribute("type", "hidden"); 
//		document.getElementById("nextBtnLocation").style.visibility = "hidden";
		document.getElementById("addParentLocationListDiv").innerHTML = "";
		var parentDiv = document.getElementById("addParentLocationListDiv");
		parentDiv.innerHTML = ""; 
		var label = document.createElement("LABEL");
		label.setAttribute("for", "addParent");
		label.setAttribute("class", "form-control-label");
		label.innerHTML = "Parent Name: *"
		parentDiv.appendChild(label);
		var parentSelect = document.createElement("SELECT");
		parentSelect.setAttribute("id", "addParent");
		parentSelect.setAttribute("name", "addParent");
		parentSelect.setAttribute("class", "form-control myclass");
		parentSelect.setAttribute("title", "Choose Parent Location");

		parentDiv.appendChild(parentSelect);
	    var parentOption = document.createElement("option");
	    parentOption.setAttribute("value", "0");
	    document.getElementById("addParent").appendChild(parentOption);
	    if(!jQuery.isEmptyObject(zNodes)) {
	    	var idArray = [];
	    	var nameArray = [];
	    	var tempArray = [];
	    	for (var i = 0; i < zNodes.length; i++) {
	    		console.log(zNodes[i]);
	    		idArray.push(zNodes[i].id);
	    		nameArray.push(zNodes[i].name);
	    		tempArray.push(zNodes[i].name);
	    	} tempArray.sort();
	    	
	    	for (var i = 0; i < tempArray.length; i++) {
	    		var name = tempArray[i];
	    		var id = idArray[nameArray.indexOf(tempArray[i])];
	    		var parentOption = document.createElement("option");
			    parentOption.setAttribute("value", id);
			    var parentOptionText = document.createTextNode(name);
			    parentOption.appendChild(parentOptionText);
			    document.getElementById("addParent").appendChild(parentOption);
			}
	    }
	}
	else if(id === 'addLocationAttributeBtn') { 
		document.getElementById("nextBtnLocationAttribute").setAttribute("type", "hidden"); 
//		document.getElementById("nextBtnLocationAttribute").style.visibility = "hidden";
		document.getElementById("addParentLocationAttributeListDiv").innerHTML = "";
		var parentDiv = document.getElementById("addParentLocationAttributeListDiv");
		parentDiv.innerHTML = ""; 
		var label = document.createElement("LABEL");
		label.setAttribute("for", "addParent");
		label.setAttribute("class", "form-control-label");
		label.innerHTML = "Parent Name: *"
		parentDiv.appendChild(label);
		var parentSelect = document.createElement("SELECT");
		parentSelect.setAttribute("id", "locationParent");
		parentSelect.setAttribute("name", "locationParent");
		parentSelect.setAttribute("class", "form-control myclass");
		parentSelect.setAttribute("onchange", "locationAttributeParentChange()");
		parentSelect.setAttribute("title", "Choose Parent Location");		
		parentDiv.appendChild(parentSelect);
		var parentOption = document.createElement("option");
	    parentOption.setAttribute("value", "0");
	    document.getElementById("locationParent").appendChild(parentOption);
	    if(!jQuery.isEmptyObject(zNodes)) { 
	    	var idArray = [];
	    	var nameArray = [];
	    	var tempArray = [];
	    	for (var i = 0; i < zNodes.length; i++) {
	    		idArray.push(zNodes[i].id[0]);
	    		nameArray.push(zNodes[i].name[0]);
	    		tempArray.push(zNodes[i].name[0]);
	    	} tempArray.sort();
	    	for (var i = 0; i < tempArray.length; i++) {
	    		var name = tempArray[i];
	    		var id = idArray[nameArray.indexOf(tempArray[i])];
	    		var parentOption = document.createElement("option");
			    parentOption.setAttribute("value", id);
			    var parentOptionText = document.createTextNode(name);
			    parentOption.appendChild(parentOptionText);
			    document.getElementById("locationParent").appendChild(parentOption);
	    	}
	    }
	}
	else if(id === 'addLocationTypeBtn') { /*document.getElementById("nextBtnLocationType").style.visibility = "hidden";*/ document.getElementById("nextBtnLocationType").setAttribute("type", "hidden"); }
	else if(id === 'addLocationAttributeTypeBtn') { /*document.getElementById("nextBtnLocationAttributeType").style.visibility = "hidden";*/ document.getElementById("nextBtnLocationAttributeType").setAttribute("type", "hidden"); }
//	else if(id === 'viewLocationBtn') { }
//	else if(id === 'viewLocationAttributeBtn') { }
	else if(id === 'viewLocationTypeBtn') { viewLocationTypeDetails(); }
	else if(id === 'viewLocationAttributeTypeBtn') { viewLocationAttributeTypeDetails(); }
//	else if(id === 'updateLocationBtn') { }
//	else if(id === 'updateLocationAttributeBtn') { }
	else if(id === 'updateLocationTypeBtn') { 
		if(!jQuery.isEmptyObject(locationTypeNodes)) { 
			var nameHtml = "<select id='updatelocationTypeName' name='updatelocationTypeName' class='form-control myclass' onchange='updateLocationTypeChange()' title='Choose Location Type' >";
			for (var i = 0; i < locationTypeNodes.length; i++) {
				nameHtml += '<option value="' + locationTypeNodes[i].id +'">' + locationTypeNodes[i].name + '</option>'; 
				if(locationTypeNodes[i].id[0] === 1) {
//					if(Array.isArray(locationTypeNodes[i].name)) { document.getElementById("updatelocationTypeName").value = locationTypeNodes[i].name[0]; document.getElementById("updatelocationTypeName").innerHTML = locationTypeNodes[i].name[0]; } else { document.getElementById("updatelocationTypeName").value = locationTypeNodes[i].name; document.getElementById("updatelocationTypeName").innerHTML = locationTypeNodes[i].name; }
					if(Array.isArray(locationTypeNodes[i].level)) { document.getElementById("updatelocationTypeLevel").value = locationTypeNodes[i].level[0]; document.getElementById("updatelocationTypeLevel").innerHTML = locationTypeNodes[i].level[0]; } else { document.getElementById("updatelocationTypeLevel").value = locationTypeNodes[i].level; document.getElementById("updatelocationTypeLevel").innerHTML = locationTypeNodes[i].level; }
					if(Array.isArray(locationTypeNodes[i].description)) { document.getElementById("updatelocationTypeDescription").value = locationTypeNodes[i].description[0]; document.getElementById("updatelocationTypeDescription").innerHTML = locationTypeNodes[i].description[0]; } else { document.getElementById("updatelocationTypeDescription").value = locationTypeNodes[i].description; document.getElementById("updatelocationTypeDescription").innerHTML = locationTypeNodes[i].description; }
				}
			} nameHtml += "</select>";
			document.getElementById("updatelocationTypeDiv").innerHTML = nameHtml;
		}
	}
	else if(id === 'updateLocationAttributeTypeBtn') { 
		if(!jQuery.isEmptyObject(locationAttributeNodes)) { 
			var nameHtml = "<select id='updatelocationAttributeTypeName' name='updatelocationAttributeTypeName' class='form-control myclass' onchange='updateLocationAttributeTypeChange()' title='Choose Location Attribute Type' >";
			for (var i = 0; i < locationAttributeNodes.length; i++) {
				nameHtml += '<option value="' + locationAttributeNodes[i].id +'">' + locationAttributeNodes[i].name + '</option>'; 
				if(locationAttributeNodes[i].id[0] === 1) {
//					if(Array.isArray(locationAttributeNodes[i].name)) { document.getElementById("updatelocationAttributeTypeName").value = locationAttributeNodes[i].name[0]; document.getElementById("updatelocationAttributeTypeName").innerHTML = locationAttributeNodes[i].name[0]; } else { document.getElementById("updatelocationAttributeTypeName").value = locationAttributeNodes[i].name; document.getElementById("updatelocationAttributeTypeName").innerHTML = locationAttributeNodes[i].name; }
					if(Array.isArray(locationAttributeNodes[i].displayName)) { document.getElementById("updatelocationAttributeTypeDisplayName").value = locationAttributeNodes[i].displayName[0]; document.getElementById("updatelocationAttributeTypeDisplayName").innerHTML = locationAttributeNodes[i].displayName[0]; } else { document.getElementById("updatelocationAttributeTypeDisplayName").value = locationAttributeNodes[i].displayName; document.getElementById("updatelocationAttributeTypeDisplayName").innerHTML = locationAttributeNodes[i].displayName; }
					if(Array.isArray(locationAttributeNodes[i].description)) { document.getElementById("updatelocationAttributeTypeDescription").value = locationAttributeNodes[i].description[0]; document.getElementById("updatelocationAttributeTypeDescription").innerHTML = locationAttributeNodes[i].description[0]; } else { document.getElementById("updatelocationAttributeTypeDescription").value = locationAttributeNodes[i].description; document.getElementById("updatelocationAttributeTypeDescription").innerHTML = locationAttributeNodes[i].description; }
					if(Array.isArray(locationAttributeNodes[i].category)) { document.getElementById("updatelocationAttributeTypeCategory").value = locationAttributeNodes[i].category[0]; document.getElementById("updatelocationAttributeTypeCategory").innerHTML = locationAttributeNodes[i].category[0]; } else { document.getElementById("updatelocationAttributeTypeCategory").value = locationAttributeNodes[i].category; document.getElementById("updatelocationAttributeTypeCategory").innerHTML = locationAttributeNodes[i].category; }
				}
			} nameHtml += "</select>";
			document.getElementById("updatelocationAttributeTypeDiv").innerHTML = nameHtml;
		}
	}
	else { console.log(id); }
}
function ValidateUnknownCols(csvData) {
	for(var i=0; i<csvData[0].length; i++) {
		if(headerCols.includes(csvData[0][i].trim().toLowerCase())) { }
		else if(i=== csvData[0].length-1) {
			if(headerCols.includes(csvData[0][i].trim().toLowerCase())) { }
			else { unknownCols.push(csvData[0][i].trim().toUpperCase()); }
		}
		else { unknownCols.push(csvData[0][i].trim().toUpperCase()); }
	}
	var str = "";
	for (var i = 0; i < unknownCols.length; i++) {
		str += unknownCols[i];
		if(i !== unknownCols.length-1) { str += "<br/>"; }
	}
	if(unknownCols.length>0) { return (unknownCols.length + " UNKNOWN COLUMNS FOUND, I.E:-<br/>" + str); }	
	else { return ""; }
}
function ValidateRequiredNull(csvData) {
	var str = "";
	for(var i=0; i<csvData[0].length; i++) {
		if((requiredCols.includes(csvData[0][i].trim().toLowerCase()))  && ((csvData[0][i].trim().toLowerCase()) != "parent")) {//
			for(var j=1; j<csvData.length; j++) {
				if(csvData[j][i] === "") {
					//alert("EMPTY CELLS FOUND!");
					csvData[j][i] = "NULL";
					str += "EMPTY CELLS FOUND AT '" + csvData[0][i] + "' COLUMN";
				}
			}
		}
	}
	return str;
}
function ValidateNameParent(csvData) {
	var nameArr = [];
	var parentArr = [];
	for(var i=0; i<csvData[0].length; i++) {
		if((requiredCols.includes(csvData[0][i])) && ((csvData[0][i]) === "name")) {
//			nameArr.push(csvData[0][i]);
			for(var j=1; j<csvData.length; j++) { nameArr.push(csvData[j][i]); }
		}
		else if((requiredCols.includes(csvData[0][i])) && ((csvData[0][i]) === "parent")) {
//			parentArr.push(csvData[0][i]);
			for(var j=1; j<csvData.length; j++) { parentArr.push(csvData[j][i]); }
		}
	}
	requiredColsData.push(nameArr);
	requiredColsData.push(parentArr);
	var str = "";
	if (typeof parentArr[0] === 'undefined') { return ""; }
	else {
		str += "NO LOCATION INFO FOUND FOR:-<br/>";
		for(var i=1; i<parentArr.length; i++) {
			if(nameArr.includes(parentArr[i])) { }
			else if(parentArr[i] === "NULL") { }
			else {  
				str += parentArr[i]; 
				if(i!= parentArr.length-1) { str += "<br/>"; } 
			}
		}
		return "";
	}
}
function ValidateTable() {
	var x = ValidateUnknownCols(csvData);
	var y = ValidateRequiredNull(csvData);
	var z = ValidateNameParent(csvData);
	if(x === "" && y === "" && z === "") { return csvData; }
	else {
		var str = "";
		str += "" + x + "<br/>";
		str += "<br/>" + y + "<br/>";
		str += "<br/>" + z + "";
//		$("#alertModalBody").html(str);
//		$("#alertModal").modal('show');
		alertDIV("warning", "ERROR: ", str);
		return null;
	}
}
function ValidateTableForLocAttrs() {
	var x = ValidateUnknownColsForLocAttrs(csvDataForLocAttrs);
	var y = ValidateRequiredNullForLocAttrs(csvDataForLocAttrs);
//	var z = ValidateNameParent(csvData);
//	if(x === "" && y === "" && z === "") { return csvData; }
	if(x === "" && y === "" ) { return csvDataForLocAttrs; }
	else {
		var str = "";
		str += "" + x + "<br/>";
		str += "<br/>" + y + "<br/>";
//		str += "<br/>" + z + "";
//		$("#alertModalBody").html(str);
//		$("#alertModal").modal('show');
		alertDIV("warning", "ERROR: ", str);
		return null;
	}
}
function ValidateUnknownColsForLocAttrs(csvDataForLocAttrs) {
	for(var i=0; i<csvDataForLocAttrs[0].length; i++) {
		if(headerColsForLocAttrs.includes(csvDataForLocAttrs[0][i].trim().toLowerCase())) { }
		else if(i=== csvDataForLocAttrs[0].length-1) {
			if(headerColsForLocAttrs.includes(csvDataForLocAttrs[0][i].trim().toLowerCase())) { }
			else { unknownColsForLocAttrs.push(csvDataForLocAttrs[0][i].trim().toUpperCase()); }
		}
		else { unknownColsForLocAttrs.push(csvDataForLocAttrs[0][i].trim().toUpperCase()); }
	}
	var str = "";
	for (var i = 0; i < unknownColsForLocAttrs.length; i++) {
		str += unknownColsForLocAttrs[i];
		if(i !== unknownColsForLocAttrs.length-1) { str += "<br/>"; }
	}
	if(unknownColsForLocAttrs.length>0) { return (unknownColsForLocAttrs.length + " UNKNOWN COLUMNS FOUND, I.E:-<br/>" + str); }	
	else { return ""; }
}
function ValidateRequiredNullForLocAttrs(csvDataForLocAttrs) {
	var str = "";
	for(var i=0; i<csvDataForLocAttrs[0].length; i++) {
		if((requiredColsForLocAttrs.includes(csvDataForLocAttrs[0][i].trim().toLowerCase()))) {//
			for(var j=1; j<csvDataForLocAttrs.length; j++) {
				if(csvDataForLocAttrs[j][i] === "") {
					//alert("EMPTY CELLS FOUND!");
					csvDataForLocAttrs[j][i] = "NULL";
					str += "EMPTY CELLS FOUND AT '" + csvDataForLocAttrs[0][i] + "' COLUMN";
				}
			}
		}
	}
	return str;
}
function openNav() {
	if(document.getElementById("mySidenav").style.width === "") { 
		document.getElementById("mySidenav").style.width = "81%"; 
		document.getElementById("mySidenav").style.visibility = "visible"; 
	}
    else if(document.getElementById("mySidenav").style.width === "81%") { 
		document.getElementById("mySidenav").style.width = ""; 
    	document.getElementById("mySidenav").style.visibility = "hidden"; 
   	}
}
function downloadCSV() {
	var myNodes = zNodes;
	var headerArray = ["name", "short_name", "full_name", "parent", "identifier", "latitude", "longitude", "type", "description"];
    var nameArray = [];
    var shortNameArray = [];
    var fullNameArray = [];
    var pIdArray = [];
    var pNameArray = [];
    var descriptionArray = [];
    var otherIdentifierArray = [];
    var typeArray = [];
    var typeNameArray = [];
    var latitudeArray = [];
    var longitudeArray = [];
    
    var i=0;
    while(myNodes[i] !== undefined) {
        nameArray.push(myNodes[i].name);
        shortNameArray.push(myNodes[i].shortName);
        fullNameArray.push(myNodes[i].fullName);
        pIdArray.push(myNodes[i].pId);
        pNameArray.push(myNodes[i].pName);
        descriptionArray.push(myNodes[i].description);
        otherIdentifierArray.push(myNodes[i].otherIdentifier);
        typeArray.push(myNodes[i].type);
        typeNameArray.push(myNodes[i].typeName);
        latitudeArray.push(myNodes[i].latitude);
        longitudeArray.push(myNodes[i].longitude);
        i++;
    }
    var arr = [nameArray.length, shortNameArray.length, fullNameArray.length, pIdArray.length, pNameArray.length, descriptionArray.length, otherIdentifierArray.length, typeArray.length, typeNameArray.length, latitudeArray.length, longitudeArray.length];
    arr = arr.sort();
    arr = arr.reverse();
    var rowsAfterHeader = arr[0];
    var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "zTreeTable";
//	    table.width = '100%';
    var row = table.insertRow(-1);
    for (var i = 0; i < headerArray.length; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = headerArray[i].toUpperCase();
        row.appendChild(headerCell);
    }
    var count=0;
    for (var i = 0; i < rowsAfterHeader; i++) {;
        row = table.insertRow(-1);
        row.id = "row"+i;

        var cell = row.insertCell(-1);
        if(nameArray[i] !== undefined) { cell.innerHTML = nameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(shortNameArray[i] !== undefined) { cell.innerHTML = shortNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(fullNameArray[i] !== undefined) { cell.innerHTML = fullNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(otherIdentifierArray[i] !== undefined) { cell.innerHTML = pNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(otherIdentifierArray[i] !== undefined) { cell.innerHTML = otherIdentifierArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(latitudeArray[i] !== undefined) { cell.innerHTML = latitudeArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(longitudeArray[i] !== undefined) { cell.innerHTML = longitudeArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(typeNameArray[i] !== undefined) { cell.innerHTML = typeNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(descriptionArray[i] !== undefined) { cell.innerHTML = descriptionArray[i]; }
        else { cell.innerHTML = ""; }
    }
    document.getElementById("zTreeTableDiv").innerHTML = "";
    document.getElementById("zTreeTableDiv").appendChild(table);
    document.getElementById("zTreeTableDiv").style.visibility = "hidden";
}
function downloadCSVTemplate() { 
	var headerArray = ["name", "short_name", "full_name", "parent", "identifier", "latitude", "longitude", "type", "description"];
	var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "zTreeTable";
//	table.width = '80%';
    var row = table.insertRow(-1);
    for (var i = 0; i < headerArray.length; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = headerArray[i].toUpperCase();
        row.appendChild(headerCell);
    }	
    var rowsAfterHeader = 5;
    for (var i = 0; i < rowsAfterHeader; i++) {;
	    row = table.insertRow(-1);
	    row.id = "row"+i;

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "[Location " + (i+1) + " Name]";

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

	    var cell = row.insertCell(-1);
	    if(i===0) { cell.innerHTML = ""; }
	    else { cell.innerHTML = "[Location " + i + " Name]"; }

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

	    var cell = row.insertCell(-1);
	    if(i===0) { cell.innerHTML = "COUNTRY"; }
	    else if(i===1) { cell.innerHTML = "CITY"; }
	    else if(i===2) { cell.innerHTML = "DISTRICT"; }
	    else if(i===3) { cell.innerHTML = "TOWN"; }
	    else if(i===4) { cell.innerHTML = "UC"; }
	    
	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

    }
    document.getElementById("zTreeTableDiv").innerHTML = "";
    document.getElementById("zTreeTableDiv").appendChild(table);
    document.getElementById("zTreeTableDiv").style.visibility = "hidden";
}
function downloadCSVTemplateForLocAttrs() { 
	var headerArray = ["parent", "attribute_name", "value", "type_value"];
	var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "zTreeTable";
//	table.width = '80%';
    var row = table.insertRow(-1);
    for (var i = 0; i < headerArray.length; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = headerArray[i].toUpperCase();
        row.appendChild(headerCell);
    }	
    var rowsAfterHeader = 5;
    for (var i = 0; i < rowsAfterHeader; i++) {;
	    row = table.insertRow(-1);
	    row.id = "row"+i;

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "[Location Attribute " + (i+1) + "]";

	    var cell = row.insertCell(-1);
	    if(i===0) { cell.innerHTML = "[Attribute " + (i+1) +" Name]"; }
	    else if(i===1) { cell.innerHTML = "[Attribute "+ (i+1) + " Name]_ANUALLY"; }
	    else if(i===2) { cell.innerHTML = "[Attribute "+ (i+1) + " Name]_MONTHLY"; }
	    else if(i===3) { cell.innerHTML = "[Attribute "+ (i+1) + " Name]_DAILY"; }
	    else if(i===4) { cell.innerHTML = "[Attribute "+ (i+1) + " Name]_TIME_BOUND"; }

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "[Location Attribute Name " + (i+1) + " Value]";

	    var cell = row.insertCell(-1);
	    if(i===0) { cell.innerHTML = ""; }
	    else if(i===1) { cell.innerHTML = "2017"; }
	    else if(i===2) { cell.innerHTML = "2017-03"; }
	    else if(i===3) { cell.innerHTML = "2017-02-03"; }
	    else if(i===4) { cell.innerHTML = "2017-02-10:2017-02-11"; }
    }
    document.getElementById("zTreeTableDiv").innerHTML = "";
    document.getElementById("zTreeTableDiv").appendChild(table);
    document.getElementById("zTreeTableDiv").style.visibility = "hidden";
}
function compareArr(array1, array2) {
	var count = 0; 
	if(array1.length !== array2.length) {return false;}
	else {
		if(array1.length === array2.length) {
			for(var i=0; i<array1.length; i++) {
				if(array1[i] === array2[i]) {
					count ++;
				}
				else { }
			}
		}
		if(count === array1.length-1 && count === array2.length-1) { return true; }
		else { return false;}
	}
}
function myFunctionChecks(myBound) {
	var div = document.getElementById(myBound.id+"_DIV");
	div.innerHTML = "";
	
	var value = myBound.value;
	while (value.includes("_")) { value = value.replace("_", " "); }

    if(myBound.value === "None") { div.innerHTML = ""; }
    else if(myBound.value === "Anually") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "number");
	    boundValue.setAttribute("name", myBound.id+"_BOUND");
	    boundValue.setAttribute("id", myBound.id+"_BOUND");
	    boundValue.setAttribute("min", "1990");
	    boundValue.setAttribute("max", "2020");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Year, 1990 to 2020");
	    boundValue.setAttribute("placeholder", "Year");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
	else if(myBound.value === "Monthly") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "month");
	    boundValue.setAttribute("name", myBound.id+"_BOUND");
	    boundValue.setAttribute("id", myBound.id+"_BOUND");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Month");
	    boundValue.setAttribute("placeholder", "Month");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
	else if(myBound.value === "Daily") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", myBound.id+"_BOUND");
	    boundValue.setAttribute("id", myBound.id+"_BOUND");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Day");
	    boundValue.setAttribute("placeholder", "Day");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
    else if(myBound.value === "Time_Bound") {
    	var div1 = document.createElement('div');
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + " From:";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND_From");
    	boundLabel.setAttribute("class", "form-control-label");
        div1.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", myBound.id+"_BOUND_From");
	    boundValue.setAttribute("id", myBound.id+"_BOUND_From");
	    boundValue.setAttribute("onchange", "setDateOfTo(this)");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Date From");
	    boundValue.setAttribute("placeholder", "Date From");
	    div1.appendChild(boundValue);	
	    div1.setAttribute("class", "form-group");
	    div.appendChild(div1);

    	var div1 = document.createElement('div');
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + " To:";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND_To");
    	boundLabel.setAttribute("class", "form-control-label");
        div1.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", myBound.id+"_BOUND_To");
	    boundValue.setAttribute("id", myBound.id+"_BOUND_To");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Date To");
	    boundValue.setAttribute("placeholder", "Date To");
	    div1.appendChild(boundValue);	
	    div1.setAttribute("class", "form-group");
	    div.appendChild(div1);
    }
    else { div.innerHTML = ""; }
}
function myFunctionSetChecks(id, valuee, val1, val2) {
	var div = document.getElementById(id+"_DIV");
	div.innerHTML = "";
	
	var value = valuee;
	while (value.includes("_")) { value = value.replace("_", " "); }

    if(valuee === "None") { div.innerHTML = ""; }
    else if(valuee === "Anually") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "number");
	    boundValue.setAttribute("name", id+"_BOUND");
	    boundValue.setAttribute("id", id+"_BOUND");
	    boundValue.setAttribute("min", "1990");
	    boundValue.setAttribute("max", "2020");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val1);
	    boundValue.setAttribute("title", "Enter Year, 1990 to 2020");
	    boundValue.setAttribute("placeholder", "Year");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
	else if(valuee === "Monthly") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "month");
	    boundValue.setAttribute("name", id+"_BOUND");
	    boundValue.setAttribute("id", id+"_BOUND");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val1);
	    boundValue.setAttribute("title", "Enter Month");
	    boundValue.setAttribute("placeholder", "Month");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
	else if(valuee === "Daily") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", id+"_BOUND");
	    boundValue.setAttribute("id", id+"_BOUND");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val1);
	    boundValue.setAttribute("title", "Enter Day");
	    boundValue.setAttribute("placeholder", "Day");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
    else if(valuee === "Time_Bound") {
    	var div1 = document.createElement('div');
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + " From:";
    	boundLabel.setAttribute("for", id+"_BOUND_From");
    	boundLabel.setAttribute("class", "form-control-label");
        div1.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", id+"_BOUND_From");
	    boundValue.setAttribute("id", id+"_BOUND_From");
	    boundValue.setAttribute("onchange", "setDateOfTo(this)");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val1);
	    boundValue.setAttribute("title", "Enter Date From");
	    boundValue.setAttribute("placeholder", "Date From");
	    div1.appendChild(boundValue);	
	    div1.setAttribute("class", "form-group");
	    div.appendChild(div1);

    	var div1 = document.createElement('div');
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + " To:";
    	boundLabel.setAttribute("for", id+"_BOUND_To");
    	boundLabel.setAttribute("class", "form-control-label");
        div1.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", id+"_BOUND_To");
	    boundValue.setAttribute("id", id+"_BOUND_To");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val2);
	    boundValue.setAttribute("title", "Enter Date To");
	    boundValue.setAttribute("placeholder", "Date To");
	    div1.appendChild(boundValue);	
	    div1.setAttribute("class", "form-group");
	    div.appendChild(div1);
    }
    else { div.innerHTML = ""; }
}
function setDateOfTo(fromDateObj) {
	var id = fromDateObj.id;
	var value = fromDateObj.value;
	var newId = fromDateObj.id.replace("_From", "_To");
	document.getElementById(newId).setAttribute("min", document.getElementById(id).value);
}
function alertDIV(messageStatus, messageHeading, messageText) { 
	document.getElementById("myAlertDIV").innerHTML = "";
	document.getElementById("myAlertDIV").innerHTML = "<div class='alert alert-"+messageStatus+" alert-dismissable' style='margin-bottom: 0px; padding: 5px;'><a href='#' class='close' data-dismiss='alert' aria-label='close' style=' right: 0; top: 0;' title='Close' >&times;</a><strong>"+messageHeading+"</strong> "+messageText+"</div>"; 
//    $("#myAlertDIV").alert();
//    $("#myAlertDIV").fadeTo(10000, 10000).slideUp(2000, function(){ $("#myAlertDIV").slideUp(5000); });   
//	document.getElementById("myAlertDIV").innerHTML = "";
}
function alertDIV1(messageStatus, messageHeading, messageText, id) { 
	document.getElementById("myAlertDIV1"+id).innerHTML = "";
	document.getElementById("myAlertDIV1"+id).innerHTML = "<div class='alert alert-"+messageStatus+" alert-dismissable' style='margin-bottom: 0px; padding: 5px;'><a href='#' class='close' data-dismiss='alert' aria-label='close' style=' right: 0; top: 0;' title='Close' >&times;</a><strong>"+messageHeading+"</strong><br/>"+messageText+"</div>"; 
//    $("#myAlertDIV1"+id).alert();
//    $("#myAlertDIV1"+id).fadeTo(10000, 10000).slideUp(2000, function(){ $("#myAlertDIV1"+id).slideUp(5000); });   
//	document.getElementById("myAlertDIV1"+id).innerHTML = "";
}
function viewLocationDetails(nodeName) {
	var treeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	var node = treeObj.getNodeByParam('id', nodeName);
	var headerArray = ["KEY", "VALUE"];
    var table = document.createElement("TABLE");
		
    var headerArray1 = ["Attribute Id", "Attribute Type Id", "Location Attribute Value"];
    var headerArray2 = ["Attribute Id", "Attribute Type Id", "Type Name", "Location Attribute Value", "Type Value From", "Type Value To"];
    var idLocation = null;
    var nameLocation = null;
    var shortNameLocation = null;
    var fullNameLocation = null;
    var pIdLocation = null;
    var pNameLocation = null;
    var typeLocation = null;
    var typeNameLocation = null;
    var descriptionLocation = null;
    var otherIdentifierLocation = null;
    var latitudeLocation = null;
    var longitudeLocation = null;
    if(Array.isArray(node.id)) { idLocation = node.id[0]; } else { idLocation = node.id; }
    if(Array.isArray(node.name)) { nameLocation = node.name[0]; } else { nameLocation = node.name; }
    if(Array.isArray(node.shortName)) { shortNameLocation = node.shortName[0]; } else { shortNameLocation = node.shortName; }
    if(Array.isArray(node.fullName)) { fullNameLocation = node.fullName[0]; } else { fullNameLocation = node.fullName; }
    if(Array.isArray(node.pId)) { pIdLocation = node.pId[0]; } else { pIdLocation = node.pId; }
    if(Array.isArray(node.pName)) { pNameLocation = node.pName[0]; } else { pNameLocation = node.pName; }
    if(Array.isArray(node.type)) { typeLocation = node.type[0]; } else { typeLocation = node.type; }
    if(Array.isArray(node.typeName)) { typeNameLocation = node.typeName[0]; } else { typeNameLocation = node.typeName; }
    if(Array.isArray(node.description)) { descriptionLocation = node.description[0]; } else { descriptionLocation = node.description; }
    if(Array.isArray(node.otherIdentifier)) { otherIdentifierLocation = node.otherIdentifier[0]; } else { otherIdentifierLocation = node.otherIdentifier; }
    if(Array.isArray(node.latitude)) { latitudeLocation = node.latitude[0]; } else { latitudeLocation = node.latitude; }
    if(Array.isArray(node.longitude)) { longitudeLocation = node.longitude[0]; } else { longitudeLocation = node.longitude; }

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Name: "; 
    var value = row.insertCell(1); value.innerHTML = nameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Short Name: "; 
    var value = row.insertCell(1); value.innerHTML = shortNameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Full Name: "; 
    var value = row.insertCell(1); value.innerHTML = fullNameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Parent Id: "; 
    var value = row.insertCell(1); value.innerHTML = pIdLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Parent Name: "; 
    var value = row.insertCell(1); value.innerHTML = pNameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Type: "; 
    var value = row.insertCell(1); value.innerHTML = typeLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Type Name: "; 
    var value = row.insertCell(1); value.innerHTML = typeNameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Description: "; 
    var value = row.insertCell(1); value.innerHTML = descriptionLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Identifier: "; 
    var value = row.insertCell(1); value.innerHTML = otherIdentifierLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Latitude: "; 
    var value = row.insertCell(1); value.innerHTML = latitudeLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Longitude: "; 
    var value = row.insertCell(1); value.innerHTML = longitudeLocation;

    if(!jQuery.isEmptyObject(locationAttributeDataNodes)) { 
        var table2 = document.createElement("TABLE");
        var row = table2.insertRow(-1);
        for (var i = 0; i < headerArray1.length; i++) {
        	if(i===0) {}
        	else if(i===1) {}
        	else {
            	var headerCell = document.createElement("TH");
                headerCell.innerHTML = headerArray1[i];
                row.appendChild(headerCell);
        	}
        }
    	for (var i = 0; i < locationAttributeDataNodes.length; i++) {
    		var locationIdForLocationAttribute = null;
    		var idForLocationAttribute = null;
    		var typeIdForLocationAttribute = null; 
    		var valueForLocationAttribute = null;
    		var typeNameForLocationAttribute = null;
    		var typeValue1ForLocationAttribute = null;
    		var typeValue2ForLocationAttribute = null;
    		
    		if(Array.isArray(locationAttributeDataNodes[i].locationId)) { locationIdForLocationAttribute = locationAttributeDataNodes[i].locationId[0]; } else { locationIdForLocationAttribute = locationAttributeDataNodes[i].locationId; }
    		if(Array.isArray(locationAttributeDataNodes[i].id)) { idForLocationAttribute = locationAttributeDataNodes[i].id[0]; } else { idForLocationAttribute = locationAttributeDataNodes[i].id; }
    		if(Array.isArray(locationAttributeDataNodes[i].typeId)) { typeIdForLocationAttribute = locationAttributeDataNodes[i].typeId[0]; } else { typeIdForLocationAttribute = locationAttributeDataNodes[i].typeId; }
    		if(Array.isArray(locationAttributeDataNodes[i].value)) { valueForLocationAttribute = locationAttributeDataNodes[i].value[0]; } else { valueForLocationAttribute = locationAttributeDataNodes[i].value; }
    		if(Array.isArray(locationAttributeDataNodes[i].typeName)) { typeNameForLocationAttribute = locationAttributeDataNodes[i].typeName[0]; } else { typeNameForLocationAttribute = locationAttributeDataNodes[i].typeName; }
    		if(Array.isArray(locationAttributeDataNodes[i].typeValue1)) { typeValue1ForLocationAttribute = locationAttributeDataNodes[i].typeValue1[0]; } else { typeValue1ForLocationAttribute = locationAttributeDataNodes[i].typeValue1; }
    		if(Array.isArray(locationAttributeDataNodes[i].typeValue2)) { typeValue2ForLocationAttribute = locationAttributeDataNodes[i].typeValue2[0]; } else { typeValue2ForLocationAttribute = locationAttributeDataNodes[i].typeValue2; }

    		if(idLocation === locationIdForLocationAttribute) {
    			if("None" === typeNameForLocationAttribute ) {
    				row = table2.insertRow(-1);
//    				var value = row.insertCell(-1); value.innerHTML = idForLocationAttribute;
//    				var value = row.insertCell(-1); value.innerHTML = typeIdForLocationAttribute;
    				var value = row.insertCell(-1); value.innerHTML = valueForLocationAttribute; 
    			}
    		}
    	}
        var table3 = document.createElement("TABLE");
        var row = table3.insertRow(-1);
        for (var i = 0; i < headerArray2.length; i++) {
        	if(i===0) {}
        	else if(i===1) {}
        	else {
	            var headerCell = document.createElement("TH");
	            headerCell.innerHTML = headerArray2[i];
	            row.appendChild(headerCell);
            }
        }
		for (var i = 0; i < locationAttributeDataNodes.length; i++) {
    		var locationIdForLocationAttribute = null;
    		var idForLocationAttribute = null;
    		var typeIdForLocationAttribute = null; 
    		var valueForLocationAttribute = null;
    		var typeNameForLocationAttribute = null;
    		var typeValue1ForLocationAttribute = null;
    		var typeValue2ForLocationAttribute = null;
    		
    		if(Array.isArray(locationAttributeDataNodes[i].locationId)) { locationIdForLocationAttribute = locationAttributeDataNodes[i].locationId[0]; } else { locationIdForLocationAttribute = locationAttributeDataNodes[i].locationId; }
    		if(Array.isArray(locationAttributeDataNodes[i].id)) { idForLocationAttribute = locationAttributeDataNodes[i].id[0]; } else { idForLocationAttribute = locationAttributeDataNodes[i].id; }
    		if(Array.isArray(locationAttributeDataNodes[i].typeId)) { typeIdForLocationAttribute = locationAttributeDataNodes[i].typeId[0]; } else { typeIdForLocationAttribute = locationAttributeDataNodes[i].typeId; }
    		if(Array.isArray(locationAttributeDataNodes[i].value)) { valueForLocationAttribute = locationAttributeDataNodes[i].value[0]; } else { valueForLocationAttribute = locationAttributeDataNodes[i].value; }
    		if(Array.isArray(locationAttributeDataNodes[i].typeName)) { typeNameForLocationAttribute = locationAttributeDataNodes[i].typeName[0]; } else { typeNameForLocationAttribute = locationAttributeDataNodes[i].typeName; }
    		if(Array.isArray(locationAttributeDataNodes[i].typeValue1)) { typeValue1ForLocationAttribute = locationAttributeDataNodes[i].typeValue1[0]; } else { typeValue1ForLocationAttribute = locationAttributeDataNodes[i].typeValue1; }
    		if(Array.isArray(locationAttributeDataNodes[i].typeValue2)) { typeValue2ForLocationAttribute = locationAttributeDataNodes[i].typeValue2[0]; } else { typeValue2ForLocationAttribute = locationAttributeDataNodes[i].typeValue2; }

    		if(idLocation === locationIdForLocationAttribute) {
    			if("None" === typeNameForLocationAttribute ) {}
    			else {
    				row = table3.insertRow(-1);
//    				var value = row.insertCell(-1); value.innerHTML = idForLocationAttribute;
//    				var value = row.insertCell(-1); value.innerHTML = typeIdForLocationAttribute;
    				var value = row.insertCell(-1); value.innerHTML = typeNameForLocationAttribute;
    				var value = row.insertCell(-1); value.innerHTML = valueForLocationAttribute;
    				var value = row.insertCell(-1); value.innerHTML = typeValue1ForLocationAttribute;
    				var value = row.insertCell(-1); value.innerHTML = typeValue2ForLocationAttribute;
    			}
    		}
    	}    	
    }
	var html = ""; 
	html += "<h4 style='text-align: center;'>LOCATION DETAIL</h4><table id='locationTable' name='locationTable' style=' width: 100%;'>" + table.innerHTML + "</table>"
	if("<tbody><tr><th>Location Attribute Value</th></tr></tbody>" === table2.innerHTML) {}
    else { html += "<h4 style='text-align: center;'>LOCATION ATTRIBUTE DETAILS</h4><table id='locationAttributeTable' name='locationAttributeTable' style=' width: 100%;'>" + table2.innerHTML + "</table>" }
    if("<tbody><tr><th>Type Name</th><th>Location Attribute Value</th><th>Type Value From</th><th>Type Value To</th></tr></tbody>" === table3.innerHTML) {}
    else { html += "<h4 style='text-align: center;'>LOCATION ATTRIBUTE DETAILS-TIME BOUND</h4><table id='locationAttributeTimeTable' name='locationAttributeTimeTable' style=' width: 100%;'>" + table3.innerHTML + "</table>" }
	
//	document.getElementById("nodeDetails").innerHTML = "";
//	document.getElementById("nodeDetails").appendChild(table);

    var myWindow = window.open("", "", "width=500,height=500");
    myWindow.document.write("<html><head><title>LOCATION DETAILS OF '" + node.name[0].toUpperCase() + "'</title><style>table, th, td { border: 1px solid silver; border-collapse: collapse; }</style></head><body>" + html + "</body></html>");
//    myWindow.document.write("<html><head><title>LOCATION DETAILS OF '" + node.name[0].toUpperCase() + "'</title><style>table, th, td { border: 1px solid silver; border-collapse: collapse; }</style></head><body><h4 style='text-align: center;'>LOCATION DETAIL</h4><table style=' width: 100%;'>" + table2.innerHTML + "</table></body></html>");
}
function viewLocationTypeDetails() {
	var headerArray = ["Name", "Level", "Description"];
	var table = document.createElement("TABLE");
    
    if(!jQuery.isEmptyObject(locationTypeNodes)) { 
        var row = table.insertRow(-1);
        for (var i = 0; i < headerArray.length; i++) {
            var headerCell = document.createElement("TH");
            headerCell.innerHTML = headerArray[i];
            row.appendChild(headerCell);
        }
		for (var i = 0; i < locationTypeNodes.length; i++) {
			var id = null;
			var name = null;
			var level = null;
			var description = null;
			if(Array.isArray(locationTypeNodes[i].id)) { id = locationTypeNodes[i].id[0]; } else { id = locationTypeNodes[i].id; }
			if(Array.isArray(locationTypeNodes[i].name)) { name = locationTypeNodes[i].name[0]; } else { name = locationTypeNodes[i].name; }
			if(Array.isArray(locationTypeNodes[i].level)) { level = locationTypeNodes[i].level[0]; } else { level = locationTypeNodes[i].level; }
			if(Array.isArray(locationTypeNodes[i].description)) { description = locationTypeNodes[i].description[0]; } else { description = locationTypeNodes[i].description; }

	    	row = table.insertRow(-1);
		    var value = row.insertCell(-1); value.innerHTML = name;
			if(level === "") { var value = row.insertCell(-1); value.innerHTML = "0" ; }
			else { var value = row.insertCell(-1); value.innerHTML = level; }
		    var value = row.insertCell(-1); value.innerHTML = description;
		}
	}
//	document.getElementById("nodeDetails").innerHTML = "";
//	document.getElementById("nodeDetails").appendChild(table);

    var myWindow = window.open("", "", "width=500,height=500");
    myWindow.document.write("<html><head><title>LOCATION TYPE DETAILS</title><style>table, th, td { border: 1px solid silver; border-collapse: collapse; }</style></head><body><h4 style='text-align: center;'>LOCATION TYPE DETAILS</h4><table style=' width: 100%;'>" + table.innerHTML + "</table></body></html>");
}
function viewLocationAttributeTypeDetails() {
	var headerArray = ["Name", "Display Name", "Description", "Category"];
    var table = document.createElement("TABLE");
    
    if(!jQuery.isEmptyObject(locationAttributeNodes)) { 
    	var row = table.insertRow(-1);
        for (var i = 0; i < headerArray.length; i++) {
            var headerCell = document.createElement("TH");
            headerCell.innerHTML = headerArray[i];
            row.appendChild(headerCell);
        }
        for (var i = 0; i < locationAttributeNodes.length; i++) {
			var id = null;
			var name = null;
			var displayName = null;
			var description = null;
			var category = null;
			if(Array.isArray(locationAttributeNodes[i].id)) { id = locationAttributeNodes[i].id[0]; } else { id = locationAttributeNodes[i].id; }
			if(Array.isArray(locationAttributeNodes[i].name)) { name = locationAttributeNodes[i].name[0]; } else { name = locationAttributeNodes[i].name; }
			if(Array.isArray(locationAttributeNodes[i].displayName)) { displayName = locationAttributeNodes[i].displayName[0]; } else { displayName = locationAttributeNodes[i].displayName; }
			if(Array.isArray(locationAttributeNodes[i].description)) { description = locationAttributeNodes[i].description[0]; } else { description = locationAttributeNodes[i].description; }
			if(Array.isArray(locationAttributeNodes[i].category)) { category = locationAttributeNodes[i].category[0]; } else { category = locationAttributeNodes[i].category; }

			row = table.insertRow(-1);
		    var value = row.insertCell(-1); value.innerHTML = name;
		    var value = row.insertCell(-1); value.innerHTML = displayName;
		    var value = row.insertCell(-1); value.innerHTML = description;
		    var value = row.insertCell(-1); value.innerHTML = category;
		}
	}
//	document.getElementById("nodeDetails").innerHTML = "";
//	document.getElementById("nodeDetails").appendChild(table);

    var myWindow = window.open("", "", "width=500,height=500");
    myWindow.document.write("<html><head><title>LOCATION ATTRIBUTE TYPE DETAILS</title><style>table, th, td { border: 1px solid silver; border-collapse: collapse; }</style></head><body><h4 style='text-align: center;'>LOCATION ATTRIBUTE TYPE DETAILS</h4><table style=' width: 100%;'>" + table.innerHTML + "</table></body></html>");
}
function mySelectChange(mySelect) {
	var selectIndex = document.getElementById(mySelect.id).selectedIndex;
	var selectOption = document.getElementById(mySelect.id).options;
	var index = selectOption[selectIndex].index;
	var value = selectOption[selectIndex].value;
	var text = selectOption[selectIndex].text;
	var rowId = mySelect.id.replace("mySelect_", "");
//	var selectDataNode = {rowId: rowId, index: index, text: text, value: value};
//	mySelectData.push(selectDataNode);

//	var i=0;
	for (var i = 0; i < mySelectData.length; i++) {
		if(mySelectData[i].rowId === rowId) {
			mySelectData[i].index = index;
			mySelectData[i].text = text;
			mySelectData[i].value = value;
		}
	}
	console.log("mySelectChange");
	console.log(mySelectData);
}
function updateLocationTypeChange() {
	var id = null;
	if(!jQuery.isEmptyObject(locationTypeNodes)) { 
		for (var i = 0; i < locationTypeNodes.length; i++) {
			if(Array.isArray(locationTypeNodes[i].id)) { id = locationTypeNodes[i].id[0]; } else { id = locationTypeNodes[i].id; }
			if(id === document.getElementById("updatelocationTypeName").value) {
//				if(Array.isArray(locationTypeNodes[i].name)) { document.getElementById("updatelocationTypeName").value = locationTypeNodes[i].name[0]; } else { document.getElementById("updatelocationTypeName").value = locationTypeNodes[i].name; }
				if(Array.isArray(locationTypeNodes[i].level)) { document.getElementById("updatelocationTypeLevel").value = locationTypeNodes[i].level[0]; } else { document.getElementById("updatelocationTypeLevel").value = locationTypeNodes[i].level; }
				if(Array.isArray(locationTypeNodes[i].description)) { document.getElementById("updatelocationTypeDescription").value = locationTypeNodes[i].description[0]; } else { document.getElementById("updatelocationTypeDescription").value = locationTypeNodes[i].description; }
			}
			else if(id.toString() === document.getElementById("updatelocationTypeName").value) {
//				if(Array.isArray(locationTypeNodes[i].name)) { document.getElementById("updatelocationTypeName").value = locationTypeNodes[i].name[0]; } else { document.getElementById("updatelocationTypeName").value = locationTypeNodes[i].name; }
				if(Array.isArray(locationTypeNodes[i].level)) { document.getElementById("updatelocationTypeLevel").value = locationTypeNodes[i].level[0]; } else { document.getElementById("updatelocationTypeLevel").value = locationTypeNodes[i].level; }
				if(Array.isArray(locationTypeNodes[i].description)) { document.getElementById("updatelocationTypeDescription").value = locationTypeNodes[i].description[0]; } else { document.getElementById("updatelocationTypeDescription").value = locationTypeNodes[i].description; }
			}
		}
	}
}
function updateLocationAttributeTypeChange() {
	var id = null;
	if(!jQuery.isEmptyObject(locationAttributeNodes)) { 
		for (var i = 0; i < locationAttributeNodes.length; i++) {
			if(Array.isArray(locationAttributeNodes[i].id)) { id = locationAttributeNodes[i].id[0]; } else { id = locationAttributeNodes[i].id; }
			if(id === document.getElementById("updatelocationAttributeTypeName").value) {
//				if(Array.isArray(locationAttributeNodes[i].name)) { document.getElementById("updatelocationAttributeTypeName").value = locationAttributeNodes[i].name[0]; } else { document.getElementById("updatelocationAttributeTypeName").value = locationAttributeNodes[i].name; }
				if(Array.isArray(locationAttributeNodes[i].displayName)) { document.getElementById("updatelocationAttributeTypeDisplayName").value = locationAttributeNodes[i].displayName[0]; } else { document.getElementById("updatelocationAttributeTypeDisplayName").value = locationAttributeNodes[i].displayName; }
				if(Array.isArray(locationAttributeNodes[i].description)) { document.getElementById("updatelocationAttributeTypeDescription").value = locationAttributeNodes[i].description[0]; } else { document.getElementById("updatelocationAttributeTypeDescription").value = locationAttributeNodes[i].description; }
				if(Array.isArray(locationAttributeNodes[i].category)) { document.getElementById("updatelocationAttributeTypeCategory").value = locationAttributeNodes[i].category[0]; } else { document.getElementById("updatelocationAttributeTypeCategory").value = locationAttributeNodes[i].category; }
			}
			else if(id.toString() === document.getElementById("updatelocationAttributeTypeName").value) {
//				if(Array.isArray(locationAttributeNodes[i].name)) { document.getElementById("updatelocationAttributeTypeName").value = locationAttributeNodes[i].name[0]; } else { document.getElementById("updatelocationAttributeTypeName").value = locationAttributeNodes[i].name; }
				if(Array.isArray(locationAttributeNodes[i].displayName)) { document.getElementById("updatelocationAttributeTypeDisplayName").value = locationAttributeNodes[i].displayName[0]; } else { document.getElementById("updatelocationAttributeTypeDisplayName").value = locationAttributeNodes[i].displayName; }
				if(Array.isArray(locationAttributeNodes[i].description)) { document.getElementById("updatelocationAttributeTypeDescription").value = locationAttributeNodes[i].description[0]; } else { document.getElementById("updatelocationAttributeTypeDescription").value = locationAttributeNodes[i].description; }
				if(Array.isArray(locationAttributeNodes[i].category)) { document.getElementById("updatelocationAttributeTypeCategory").value = locationAttributeNodes[i].category[0]; } else { document.getElementById("updatelocationAttributeTypeCategory").value = locationAttributeNodes[i].category; }
			}
		}
	}
}
function locationAttributeParentChange() {
	var id = null;
	var locationId = null;
	var typeId = null;
	var typeName = null;
	var typeValue1 = null;
	var typeValue2 = null;
	var value = null;
	var locationAttributeNodesForParent = [];
//	var i=0;
	for (var i = 0; i < locationAttributeDataNodes.length; i++) {
		if(locationAttributeDataNodes[i].locationId[0].toString() === document.getElementById("locationParent").value) {
			locationAttributeNodesForParent.push(locationAttributeDataNodes[i]);
		}
	}
//	console.log(locationAttributeNodesForParent[0]);
	for (var i = 0; i < locationAttributeNodesForParent.length; i++) {
		if(Array.isArray(locationAttributeNodesForParent[i].id)) { id = locationAttributeNodesForParent[i].id[0]; } else { id = locationAttributeNodesForParent[i].id; }
		if(Array.isArray(locationAttributeNodesForParent[i].locationId)) { locationId = locationAttributeNodesForParent[i].locationId[0]; } else { locationId = locationAttributeNodesForParent[i].locationId; }
		if(Array.isArray(locationAttributeNodesForParent[i].typeId)) { typeId = locationAttributeNodesForParent[i].typeId[0]; } else { typeId = locationAttributeNodesForParent[i].typeId; }
		if(Array.isArray(locationAttributeNodesForParent[i].typeName)) { typeName = locationAttributeNodesForParent[i].typeName[0]; } else { typeName = locationAttributeNodesForParent[i].typeName; }
		if(Array.isArray(locationAttributeNodesForParent[i].typeValue1)) { typeValue1 = locationAttributeNodesForParent[i].typeValue1[0]; } else { typeValue1 = locationAttributeNodesForParent[i].typeValue1; }
		if(Array.isArray(locationAttributeNodesForParent[i].typeValue2)) { typeValue2 = locationAttributeNodesForParent[i].typeValue2[0]; } else { typeValue2 = locationAttributeNodesForParent[i].typeValue2; }
		if(Array.isArray(locationAttributeNodesForParent[i].value)) { value = locationAttributeNodesForParent[i].value[0]; } else { value = locationAttributeNodesForParent[i].value; }

		
		document.getElementById("locationAttribute_"+typeId).value = value;
		if(typeName === "None") { }
		else {
			var radioHTML = "";
		    if(typeName === "None") { 
			    radioHTML += '<div id="bound_'+typeId+'_RADIO" class="form-group">';
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None" checked>&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually" >&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
			    radioHTML += '</div>';
			    radioHTML += '<div id="bound_'+typeId+'_DIV"></div>';
		    }
		    else if(typeName === "Anually") { 
			    radioHTML += '<div id="bound_'+typeId+'_RADIO" class="form-group">';
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None" >&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually" checked>&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
			    radioHTML += '</div>';
			    radioHTML += '<div id="bound_'+typeId+'_DIV"></div>';
		    }
		    else if(typeName === "Monthly") { 
			    radioHTML += '<div id="bound_'+typeId+'_RADIO" class="form-group">';
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None" >&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually" >&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly" checked>&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
			    radioHTML += '</div>';
			    radioHTML += '<div id="bound_'+typeId+'_DIV"></div>';
		    }
		    else if(typeName === "Daily") { 
			    radioHTML += '<div id="bound_'+typeId+'_RADIO" class="form-group">';
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None" >&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually" >&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly" >&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily" checked>&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound" >&nbsp;Time Bound&nbsp;';  
			    radioHTML += '</div>';
			    radioHTML += '<div id="bound_'+typeId+'_DIV"></div>';
		    }
		    else if(typeName === "Time_Bound") { 
			    radioHTML += '<div id="bound_'+typeId+'_RADIO" class="form-group">';
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None" >&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually" >&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly" >&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily" >&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound" checked>&nbsp;Time Bound&nbsp;';  
			    radioHTML += '</div>';
			    radioHTML += '<div id="bound_'+typeId+'_DIV"></div>';
		    }
		    var divId = "bound_"+typeId; var divVal = typeName; var val1 = typeValue1; var val2 = typeValue2;
			document.getElementById("bound_"+typeId+"_RADIO").innerHTML = radioHTML;
		    myFunctionSetChecks(divId, divVal, val1, val2);
		}
	
	}
}