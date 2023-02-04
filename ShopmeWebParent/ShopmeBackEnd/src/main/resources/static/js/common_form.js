$(document).ready(function() {
	$("#buttonCancel").on("click", function() {
		window.location = moduleURL;
	});
	
	$("#fileImage").change(function(){
		if(!validateFileUpload(this)) {
			return;
		}
		
		showImageThumbnail(this);
	});
	
});

function validateFileUpload(fileInput) {
	fileSize = fileInput.files[0].size;
	fileName = fileInput.files[0].name;
	if(fileSize > 1048576) {
		fileInput.setCustomValidity("You must choose an image less than 1MB !");
		fileInput.reportValidity();
		
		return false;
	} else if(fileName.length > 255) {
		fileInput.setCustomValidity("File name must less than 255 characters !");
		fileInput.reportValidity();
		
		return false;
	} else {
		fileInput.setCustomValidity("");
		
		return true;
	}
}
		
function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	
	reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}
	
function showErrorModal(message) {
	showModalDialog("Error", message)
}

function showWarningModal(message) {
	showModalDialog("Warning", message)
}
	