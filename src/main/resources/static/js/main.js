let textbox;
let submitBtn;

$(document).ready(() => {
	uploadBtn = document.getElementById("upload");
	uploadBtn.addEventListener("click", onUpload, false);
});

function onSubmit(event) {
	const currPuppy = textbox.val();
	console.log(currPuppy);
	if (currPuppy != "") {
		puppyList.append("<li>" + currPuppy + "</li>");
	}
	
	textbox.val("");
	
	console.log($("#user").val());
	const postParams = {username: $("#user").val(), added: currPuppy};
	$.post("add_puppy", postParams, responseJSON => {
	});
};

function processFile(e) {
	let file = e.target.result, results;
	if (file && file.length) {
		results = file.split(/[\n,]/);
		console.log(results);
		
		const postParams = JSON.stringify(results);
		console.log(postParams);
		$.post("/deck_upload", postParams, responseJSON => {
			const responseObject = JSON.parse(responseJSON);
			console.log(responseObject);
		});
	}
}

function onUpload(event) {
	const input = $("#files").get(0);
	
	const reader = new FileReader();
	if (input.files.length) {
		let textFile = input.files[0];
		reader.readAsText(textFile);
		$(reader).on('load', processFile);
	} else {
		alert("Houston, we have a prollem");
	}
};
