<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/bootstrapalt.css">
  </head>
  <#include "lobby_display.ftl">
  <#include "navbar.ftl">
  <body>
     ${nav}
     <div class="container-fluid fullCont">
	   <div class="row fullRow">
	     ${lobby_display}
	   </div>
	   <div class="col-md-2 centerText" id="lobbybuttons">
	       <#if isHost>
	         <button type="button" class="btn btn-default disabled" id="play">Play</button>
	       </#if>
	       <button type="button" class="btn btn-default" id="leave">Leave</button>
	     </div>
	</div>
	
	 <script type="text/javascript">
		let isHost = ${isHost?string("true", "false")};
	 </script>
     <script src="js/bootstrapalt.min.js"></script>
     <script src="js/lobbySocket.js"></script>
     <script src="js/lobbydisplay.js"></script>
  </body>
</html>