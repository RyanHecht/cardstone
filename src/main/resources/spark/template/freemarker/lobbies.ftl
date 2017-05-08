<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
   
    <link rel="stylesheet" href="css/bootstrapalt.css">
     <link rel="stylesheet" href="css/main.css">
  </head>
  <#include "lobbies_table.ftl">
  <#include "navbar.ftl">
  <body>
     ${nav}
     <div class="container">
	   <div class="row">
	     <div class="center-block regDisplay" id="lobbytable">
	       <br>
	       <h3> Join a game... </h3>
	       ${lobby_table}
	       <button class="btn btn-default" type="submit" id="hostbutton">...or create one</button>
	     </div>
	   </div>
	</div>
     <script type="text/javascript">
       let error;
       let errorHeader;
       <#if error?? && errorHeader??>
          error = "${error}";
		  errorHeader = "${errorHeader}";
	   </#if>
	 </script>
     <script src="js/bootstrapalt.min.js"></script>
     <script src="js/tutorial.js"></script>
     <script src="js/lobbiesdisplay.js"></script>
  </body>
</html>
