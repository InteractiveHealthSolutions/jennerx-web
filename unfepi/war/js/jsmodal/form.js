var locationAttributeNodes = getLocationAttributeTypes();
var locationAttributeId = [];// = ["1", "2", "3", "4", "5"];
var locationAttributeName = [];// = ["Stat1", "Stat2", "Stat3", "Stat4", "Stat5"];
var locationAttributeDescription = [];
var locationAttributeCategory = [];
for(var i = 0; i<locationAttributeNodes.length; i++) {
	locationAttributeId.push(locationAttributeNodes[i].id);
	locationAttributeName.push(locationAttributeNodes[i].name);
	locationAttributeDescription.push(locationAttributeNodes[i].description);
	locationAttributeCategory.push(locationAttributeNodes[i].category);
}

var formDIV = document.getElementById("form_sample");

var form = document.createElement('form');
form.id = "locatioAttributeForm";
form.name = "locatioAttributeForm";
formDIV.appendChild(form);

var bodyDiv = document.createElement("div");
bodyDiv.id="locationAttributeModal"
bodyDiv.setAttribute("class", "modal-body");
form.appendChild(bodyDiv);

var div = document.createElement("div");
div.setAttribute("class", "form-group");

var descriptionLabel = document.createElement('label');
descriptionLabel.innerHTML = "Parent Node";
descriptionLabel.setAttribute("for", "locationParent");
descriptionLabel.setAttribute("class", "form-control-label");
div.appendChild(descriptionLabel);

var descriptionValue = document.createElement('input');
descriptionValue.setAttribute("type", "text");  
descriptionValue.setAttribute("name", "locationParent");
descriptionValue.setAttribute("id", "locationParent");
descriptionValue.setAttribute("readonly", "readonly");
descriptionValue.setAttribute("class", "form-control");
descriptionValue.setAttribute("value", "");
div.appendChild(descriptionValue);

bodyDiv.appendChild(div);

for(var i = 0; i<locationAttributeId.length; i++) {
	if(locationAttributeName[i] === "" || locationAttributeName[i] === null) {}
	else {
	    var div = document.createElement("div");
	    div.setAttribute("class", "form-group");
	
	    var descriptionLabel = document.createElement('label');
	    descriptionLabel.innerHTML = locationAttributeName[i];
	    descriptionLabel.setAttribute("for", "locationAttribute_"+locationAttributeId[i]);
	    descriptionLabel.setAttribute("class", "form-control-label");
	    div.appendChild(descriptionLabel);
	
	    var descriptionValue = document.createElement('input');
	    descriptionValue.setAttribute("type", "number");  
	    descriptionValue.setAttribute("name", "locationAttribute_"+locationAttributeId[i]);
	    descriptionValue.setAttribute("id", "locationAttribute_"+locationAttributeId[i]);
	    descriptionValue.setAttribute("class", "form-control");
	    descriptionValue.setAttribute("value", "");
	    div.appendChild(descriptionValue);
	
	    bodyDiv.appendChild(div);
	}
}
var footerDiv = document.createElement("div");
footerDiv.setAttribute("class", "modal-footer");
form.appendChild(footerDiv);

var button = document.createElement('input'); 
button.setAttribute("type", "button");
button.setAttribute("data-toggle", "modal");
button.setAttribute("data-target", "#myModalLocationAttributeType");
button.setAttribute("data-dismiss", "modal");
button.setAttribute("value", "Add New Location Attribute Type");
button.setAttribute("class", "btn btn-default");
footerDiv.appendChild(button);

var button = document.createElement('input'); 
button.setAttribute("type", "submit");
button.setAttribute("id", "addAlllocationAttributes");
button.setAttribute("name", "addAlllocationAttributes");
//button.setAttribute("data-dismiss", "modal");
button.setAttribute("value", "Add Location Attribute");
button.setAttribute("class", "btn btn-default");
footerDiv.appendChild(button);