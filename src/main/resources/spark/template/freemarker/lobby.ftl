<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/bootstrapalt.css">
  </head>
  <#include "lobby_display.ftl">
  <body>
     <div class="container-fluid">
	   <div class="row">
	     ${lobby_display}
	   </div>
	   <div class="row">
	     <div class="col-md-2" id="lobbybuttons">
	       <#if isHost>
	         <button type="button" class="btn btn-default disabled">Play</button>
	       </#if>
	       <button type="button" class="btn btn-default" id="leave">Leave</button>
	     </div>
	   </div>
	</div>
     <script src="js/jquery-2.1.1.js"></script>
     <script src="js/bootstrapalt.min.js"></script>
     <script src="js/lobbySocket.js"></script>
     <script src="js/lobbydisplay.js"></script>
  </body>
</html>