<%@ include file="/WEB-INF/template/include.jsp"%>

        <link rel="stylesheet" href="css/cssmodal/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/cssmodal/bootstrap-theme.min.css" type="text/css">
        <!-- <link rel="stylesheet" href="css/cssmodal/jquery-ui.css" type="text/css"> -->
        <link rel="stylesheet" href="css/cssmodal/zTreeStyle/zTreeStyle.css" type="text/css">
        <link rel="stylesheet" href="css/cssmodal/mystyle.css" type="text/css">

        <script type="text/javascript"> function getLocations() { return ${model.locationNodes}; } </script>
        <script type="text/javascript"> function getLocationTypes() { return ${model.locationTypeNodes}; } </script>
        <script type="text/javascript"> function getLocationAttributeTypes() { return ${model.locationAttributeTypeNodes}; } </script>
        <script src="js/jsmodal/jquery/jquery.min.js" type="text/javascript"></script>
        <script src="js/jsmodal/jquery/jquery.csv.min.js" type="text/javascript"></script>
        <script src="js/jsmodal/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jsmodal/ztree/jquery.ztree.core.js"></script>
        <script type="text/javascript" src="js/jsmodal/ztree/jquery.ztree.excheck.js"></script>
        <script type="text/javascript" src="js/jsmodal/ztree/jquery.ztree.exedit.js"></script>

        <!-- <script src='js/jsmodal/csv_to_html_table.js'></script> -->
        <script>
	        $(function() {
	            $('#i_file').change( function(event) {
	                var tmppath = URL.createObjectURL(event.target.files[0]);
	                CsvToHtmlTable.init({
	                	//csv_path: document.getElementById("myFile").value,
	                    //csv_path: 'data/mycsv.csv', 
	                    csv_path: tmppath, 
	                    element: 'table-container-csv', 
	                    allow_download: true,
	                    csv_options: {separator: ',', delimiter: '"'},
	                    datatables_options: {"paging": false}
	                });
	                $("#myH").html("<strong>CSV to HTML Table</strong>");
	            });
	        });
        </script>

        <table border=0 height=660px align=left >
            <tr>
                <td align="left" valign=top style="border-right: #999999 1px dashed">
					 <a class="anchorCustom" id="collapseAllBtn" href="#" title="Collapse All" onclick="return false;">Collapse All</a>
                    &nbsp;
                    <a class="anchorCustom" id="expandAllBtn" href="#" title="Expand All" onclick="return false;">Expand All</a>
                    <ul id="treeDemo" class="ztree myTreeClass"></ul>
                </td>
            </tr>
        </table>

        <div id="myDiv"></div>

        <div class="container">
            <div id="errors"></div> 
            <!-- Trigger the modal with a button -->
            <!-- <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Open Modal</button>-->
            <div id="dvTable"></div> 
	        
	        <!-- <input type="file" id="i_file" value="" accept=".csv" multiple /> --> 
	        
	        <!-- <div class="container-fluid"> -->
	            <div id="myH"></div><br>
	            <div id="table-container-csv"></div>
	        <!-- </div> -->
			<input name='saveBtn' id='saveBtn' type='button' style='visibility: hidden' value='Save' title='Save' />
        </div>

        <!-- Modal Add New Location-->
        <div class="modal fade" id="myModal" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Add New Location</h4>
                    </div>
                    <form id="nodeForm" name="nodeForm">
                    	<div class="modal-body">
                             <!-- <div class="form-group">
                                <label for="addId" class="form-control-label">Id:</label>
                                <input type="number" class="form-control" id="addId" name="addId" value="" >
                            </div> -->
                            <div class="form-group">
                                <label for="addName" class="form-control-label">Name:</label>
                                <input type="text" class="form-control" id="addName" name="addName" value="">
                            </div>
                            <div class="form-group">
                                <label for="addShortName" class="form-control-label">Short Name:</label>
                                <input type="text" class="form-control" id="addShortName" name="addShortName" value="">
                            </div>
                            <div class="form-group">
                                <label for="addFullName" class="form-control-label">Full Name:</label>
                                <input type="text" class="form-control" id="addFullName" name="addFullName" value="">
                            </div>
                            <div class="form-group">
                                <label for="addParent" class="form-control-label">Parent Id:</label>
                                <input type="number" class="form-control" id="addParent" name="addParent" value="" readonly>
                            </div>
                            
                            <div class="form-group">
                                <label for="addDescription" class="form-control-label">Description:</label>
                                <input type="text" class="form-control" id="addDescription" name="addDescription" value="">
                            </div>
                            <div class="form-group">
                                <label for="addOtherIdentifier" class="form-control-label">Other Identifier:</label>
                                <input type="number" class="form-control" id="addOtherIdentifier" name="addOtherIdentifier" value="">
                            </div>
                            <div class="form-group">
                                <label for="addType" class="form-control-label">Type:</label>
                                <select id="addType" name="addType" class="form-control myclass" >
                                    <option value= ""></option>
                               </select>
                            </div>
	                    </div>
	                    <div class="modal-footer">
	                        <input type="button" data-toggle="modal" data-target="#myModalLocationType" data-dismiss="modal" value="Add New Location Type" class="btn btn-default">
	                        <input name="addAllNodes" id="addAllNodes" type="submit" value="Add Location" class="btn btn-default" data-dismiss="modal">
	                    </div>
                	</form>
                </div>
            </div>
        </div>

        <!-- Modal Add New Location Type-->
        <div class="modal fade" id="myModalLocationType" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Add New Location Type</h4>
                    </div>
                    <form id="locationTypeForm" name="locationTypeForm">
                    	<div class="modal-body">
                            <!-- <div class="form-group">
                                <label for="addlocationTypeId" class="form-control-label">Location Type Id:</label>
                                <input type="number" class="form-control" id="addlocationTypeId" name="addlocationTypeId" value="">
                            </div> -->
                            <div class="form-group">
                                <label for="addlocationTypeName" class="form-control-label">Location Type Name:</label>
                                <input type="text" class="form-control" id="addlocationTypeName" name="addlocationTypeName" value="">
                            </div>
                            <div class="form-group">
                                <label for="addlocationTypeLevel" class="form-control-label">Location Type Level:</label>
                                <input type="number" class="form-control" id="addlocationTypeLevel" name="addlocationTypeLevel" value="">
                            </div>
	                    </div>
	                    <div class="modal-footer">
	                        <input type="button" data-toggle="modal" data-target="#myModal" data-dismiss="modal" value="Back" class="btn btn-default" >
	                        <input name="addAlllocationTypes" id="addAlllocationTypes" type="submit" value="Add Location Types" class="btn btn-default" data-dismiss="modal">
	                    </div>
                	</form>
                </div>
            </div>
        </div>

        <!-- Modal Add New Location Attribute Types-->
        <div class="modal fade" id="myModalLocationAttributeType" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Add New Location Attribute Type</h4>
                    </div>
                    <form id="locationAttributeTypeForm" name="locationAttributeTypeForm">
                    	<div class="modal-body">
                            <!-- <div class="form-group">
                                <label for="addlocationAttributeTypeId" class="form-control-label">Location Attribute Type Id:</label>
                                <input type="number" class="form-control" id="addlocationAttributeTypeId" name="addlocationAttributeTypeId" value="">
                            </div> -->
                            <div class="form-group">
                                <label for="addlocationAttributeTypeName" class="form-control-label">Location Attribute Type Name:</label>
                                <input type="text" class="form-control" id="addlocationAttributeTypeName" name="addlocationAttributeTypeName" value="">
                            </div>
                            <div class="form-group">
                                <label for="addlocationAttributeTypeDescription" class="form-control-label">Description:</label>
                                <input type="text" class="form-control" id="addlocationAttributeTypeDescription" name="addlocationAttributeTypeDescription" value="">
                            </div>
                            <div class="form-group">
                                <label for="addlocationAttributeTypeCategory" class="form-control-label">Category:</label>
                                <input type="text" class="form-control" id="addlocationAttributeTypeCategory" name="addlocationAttributeTypeCategory" value="">
                            </div>
	                    </div>
	                    <div class="modal-footer">
	                        <input type="button" data-toggle="modal" data-target="#myModalLocationAttribute" data-dismiss="modal" value="Back" class="btn btn-default" >
	                        <input name="addAlllocationAttributeTypes" id="addAlllocationAttributeTypes" type="submit" value="Add Location Attribute Type" class="btn btn-default" data-dismiss="modal">
	                    </div>
                	</form>
                </div>
            </div>
        </div>

        <!-- Modal Add New Location Attribute-->
        <div class="modal fade" id="myModalLocationAttribute" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Add New Location Attribute</h4>
                    </div>
                    <div id="form_sample"></div>
                    <!-- <form id="locatioAttributeTypeForm" name="locationAttributeTypeForm">
                    	<div class="modal-body">
                            <div class="form-group">
                                <label for="addlocationAttributeTypeId" class="form-control-label">Location Attribute Type Id:</label>
                                <input type="number" class="form-control" id="addlocationAttributeTypeId" name="addlocationAttributeTypeId" value="">
                            </div>
                            <div class="form-group">
                                <label for="addlocationAttributeTypeDescription" class="form-control-label">Location Attribute Type Name:</label>
                                <input type="text" class="form-control" id="addlocationAttributeTypeDescription" name="addlocationAttributeTypeDescription" value="">
                            </div>
	                    </div>
	                    <div class="modal-footer">
	                        <input type="button" data-toggle="modal" data-target="#myModal" data-dismiss="modal" value="Back" class="btn btn-default" >
	                        <input name="addAlllocationAttributeTypes" id="addAlllocationAttributeTypes" type="submit" value="Add Location Types" class="btn btn-default" data-dismiss="modal">
	                    </div>
                	</form> -->
                </div>
            </div>
        </div>
	    <script type="text/javascript" src="js/jsmodal/form.js"></script>
        <script type="text/javascript" src="js/jsmodal/myscript.js"></script>


