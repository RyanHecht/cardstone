<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="stylesheet" href="css/bootstrapalt.css">
    <link rel="stylesheet" href="css/main.css">
  </head>
  <#include "deck_list.ftl">
  <#include "navbar.ftl">
  <#include "message_modal.ftl">
  <body>
     ${nav}
     <div class="container">
	   <div class="row">
	   	<div class="center-block regDisplay">
		   <br>
		   <h3> ${username}'s Decks</h3> 
		   ${deck_list}
		   <a id="create_deck" href="/deckDraw.html">Create new deck</a>
	   	</div>
	 </div>
	 ${modal}
	 <script>
	   function deleteDeck(button, ofName) {
	     button.parent().parent().remove();
	     const postParams = {deck: ofName, userId: $.cookie("id")};
	     $.post("delete_deck", postParams, responseJSON => {
	     });
	   };
	   
	   function viewDeck(ofName) {
	     window.location.replace("/deck?name=" + ofName.replace(' ', '_'));
	   };
 	 </script>
     <script src="js/bootstrapalt.min.js"></script>
     <script src="js/tutorial.js"></script>
     <script src="js/decks.js"></script>
  </body>
</html>
