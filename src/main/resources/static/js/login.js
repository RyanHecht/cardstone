jQuery.fn.outerHTML = function() {
  return jQuery('<div />').append(this.eq(0).clone()).html();
}; 

function valOf(id) {
	console.log($(id).val());
	return $(id).val();
}

$('input[type="submit"]').click(function() {
	 console.log("I was clicked");
	 console.log($(this).attr('id'));
	 const id = $(this).attr('id');
	 const formText = "<form action=" + id + " method='POST'>" + 
		"<input type='text' name='username' value=" + valOf("#username") + " />" +
		"<input type='password' name='password' value=" + valOf("#password") + " />" +
		" />";
	
	 const form = $(formText);
	 $('body').append(form);
	 form.submit();
 });