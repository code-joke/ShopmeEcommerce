dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function() {
	
	$("#shortDescription").richText();
	$("#fullDescription").richText();
	
	//dropDownBrandChange
	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});
	
	getCategoriesForNewForm();
});

function getCategoriesForNewForm() {
	catIdField = $("#categoryId");
	editMode = false;
	
	if(catIdField.length) {
		editMode = true;
	}
	
	if(!editMode) getCategories();
}

function getCategories() {
	brandId = dropdownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categories";
	
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category){
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});
}
		
function checkUnique(form) {
	productId = $("#id").val();
	productName = $("#name").val();
	productAlias = $("#alias").val();
	productCode = $("#code").val();
	
	csrfValue = $("input[name='_csrf']").val();
	
	params = {id: productId, name: productName, alias: productAlias, code: productCode, _csrf: csrfValue};
	
	$.post(checkUniqueUrl, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "DuplicateName") {
			showWarningModal("Tên sản phẩm đã tồn tại: " + productName);	
		} else if (response == "DuplicateAlias") {
			showWarningModal("Đường dẫn đã tồn tại: " + productAlias);
		} else if (response == "DuplicateCode") {
			showWarningModal("Mã sản phẩm / SKU đã tồn tại: " + productCode);
		} else {
			showErrorModal("Máy chủ hiện không phản hồi");
		}
		
	}).fail(function() {
		showErrorModal("Không thể kết nối đến máy chủ");
	});
	
	return false;	
}
		