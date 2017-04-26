<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/bootstrap.css">
  </head>
  <#include "game_table.ftl">
  <body>
     <div class="container">
	 <h3> ${username}'s Games</h3> 
	   <div class="row">
	     <div class="col-sm-8" id="userlist">
	       ${game_table}
	     </div>
	   </div>
       <a href="/login">Logout</a>
	</div>
     <script src="js/jquery-2.1.1.js"></script>
     <script src="js/bootstrap.min.js"></script>
     <script src="js/gamedisplay.js"></script>
  </body>
</html>