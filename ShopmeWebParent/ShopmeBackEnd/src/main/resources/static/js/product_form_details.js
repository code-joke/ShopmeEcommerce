$(document).ready(function() {
	$("a[name='linkRemoveDetail']").each(function(index){
		$(this).click(function(){
			removeDetailSectionByIndex(index);
		});
	});
});

function addNextDetailSection() {
	allDivDetails = $("[id^='divDetail']");
	divDetailsCount = allDivDetails.length;
	
	htmlDetailSection = 
		`<div class="form-inline" id="divDetail${divDetailsCount}">
			<input type="hidden" name="detailIDs" value="0"/>
			<label class="m-3">Name:</label>
			<input type="text" class="form-control w-25" name="detailNames" maxlength="255" placeholder="Enter name"/>
			<label class="m-3">Value:</label>
			<input type="text" class="form-control w-25" name="detailValues" maxlength="255" placeholder="Enter value"/>
		</div>`;
		
	$("#divProductDetails").append(htmlDetailSection);
		
	
	previousDivDetailSection = allDivDetails.last();
	previousDivDetailID = previousDivDetailSection.attr("id");
	
	htmlLinkRemove = `<a class="btn fas fa-times-circle fa-2x icon-gray"
							href="javascript:removeDetailSectionByID('${previousDivDetailID}')"
							title="Delete this value"></a>`;
	
	previousDivDetailSection.append(htmlLinkRemove);
	
	$("input[name='detailNames']").last().focus();
	
}

function removeDetailSectionByID(id) {
	$("#" + id).remove();
}


function removeDetailSectionByIndex(index) {
	$("#divDetail" + index).remove();
}