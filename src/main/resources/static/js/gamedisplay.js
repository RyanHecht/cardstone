$(document).ready(() => {
	$(".clickable-row").on('click', function() {
		window.location.replace($(this).data("href"));
	});
});