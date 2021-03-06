<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/css/tether.min.css">
    <link rel="stylesheet" href="css/bootstrapalt.css">
    <link rel="stylesheet" href="css/main.css">

  </head>
  <body>
     <#include "user_info.ftl">
     <#include "navbar.ftl">
     ${nav}
     ${content}
     <script type="text/javascript">
	    $("#greeting").text("Welcome to Cardstone, " + $.cookie("username"));
	</script>
     <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js"></script>
     <script src="js/bootstrapalt.min.js"></script>
     <script src="js/tutorial.js"></script>
     <script src="js/menu.js"></script>
  </body>
</html>
