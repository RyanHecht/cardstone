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
	 <h3> Join a game... </h3>
	   <div class="row">
	     <div class="col-sm-8" id="lobbytable">
	       ${lobby_table}
	     </div>
	   </div>
	   <button class="btn btn-default" type="submit" id="hostbutton">...or create one</button>
       <br>
       <a href="/login">Logout</a>
	</div>
     <script type="text/javascript">
       <#if error?? && errorHeader??>
          let error = "${error}";
		  let errorHeader = "${errorHeader}";
	   </#if>
	 </script>
     <script src="js/bootstrapalt.min.js"></script>
     <script src="js/lobbiesdisplay.js"></script>
  </body>
</html>
