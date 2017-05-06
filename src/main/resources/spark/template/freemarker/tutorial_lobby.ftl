<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="stylesheet" href="css/bootstrapalt.css">
    <link rel="stylesheet" href="css/main.css">
  </head>
  <#include "tutorial_lobby_display.ftl">
  <#include "navbar.ftl">
    <#include "message_modal.ftl">
  <body>
     ${nav}
     <div class="container-fluid fullCont">
	   <div class="row fullRow">
	     ${lobby_display}
	   </div>
	   <div class="col-md-2 centerText" id="lobbybuttons">
	       <button type="button" class="btn btn-default disabled" id="play">Play</button>
	       <button type="button" class="btn btn-default disabled" id="leave">Leave</button>
	     </div>
	</div>
	${modal}
     <script src="js/bootstrapalt.min.js"></script>
     <script src="js/lobbySocket.js"></script>
     <script src="js/tutorial.js"></script>
     <script src="js/tutorialLobbyDisplay.js"></script>
  </body>
</html>