jQuery.fn.outerHTML = function() {
  return jQuery('<div />').append(this.eq(0).clone()).html();
}; 

function valOf(id) {
	return $(id).val();
}

$('input[type="submit"]').click(function() {
	 const id = $(this).attr('id');

	 const username = valOf("#username");
	 const pw = valOf("#password");
	 if (username != "" && pw != "") {
		 const postParams = {username: username, password: pw};
		 $.post(id, postParams, responseJSON => {
			const respObj = JSON.parse(responseJSON);
			console.log(respObj);
			 
			if (respObj.auth) {
				window.location.replace("/menu");
			} else {
				$("#error").text(respObj.error);
			}
		 });
	 }
 });