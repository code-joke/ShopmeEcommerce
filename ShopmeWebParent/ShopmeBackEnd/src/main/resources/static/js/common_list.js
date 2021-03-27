function clearFilter() {
	window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName) {
	
	$("#yesButton").attr("href", link.attr("href"));
	$("#confirmText").text("Bạn có chắc muốn xóa " + entityName + " này ?");
	
	$("#confirmModal").modal();	
}
