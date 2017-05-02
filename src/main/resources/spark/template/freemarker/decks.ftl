<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/bootstrapalt.css">
  </head>
  <#include "deck_list.ftl">
  <#include "navbar.ftl">
  <body>
     ${nav}
     <div class="container">
	   <div class="row">
	   <br>
	   <br>
	   <h3> ${username}'s Decks</h3> 
	     <div class="col-sm-8" id="userlist">
	       ${deck_list}
	     </div>
	   </div>
	   <a href="/deckDraw.html">Create new deck</a>
       <a href="/login">Logout</a>
	</div>
     <script src="js/bootstrapalt.min.js"></script>
  </body>
</html>
