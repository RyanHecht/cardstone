<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="stylesheet" href="css/bootstrapalt.css">
    <link rel="stylesheet" href="css/main.css">
  </head>
  <#include "game_table.ftl">
  <#include "navbar.ftl">
  <#include "message_modal.ftl">
  <body>
     ${nav}
     <div class="container">
     <br>
     <br>
	 <h3> ${username}'s Games</h3> 
	   <div class="row">
	     <div class="col-sm-8" id="games">
	       ${game_table}
	     </div>
	   </div>
	</div>
	${modal}
     <script src="js/bootstrapalt.min.js"></script>
     <script src="js/tutorial.js"></script>
     <script src="js/gamedisplay.js"></script>
  </body>
</html>