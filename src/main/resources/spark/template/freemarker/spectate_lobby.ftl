<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="css/bootstrapalt.css">
    <link rel="stylesheet" href="css/main.css">
  </head>
<#include "navbar.ftl">
  <body>
  	${nav}
     <div class="container-fluid fullCont">
	   <div class="row fullRow">
	   <div class="col-md-4 tallCol">
		  <div class="panel panel-default tallPanel" id="blah">
		    <div class="panel-heading">
		      <h3 class="panel-title">${p1}</h3>
		    </div>
		    <div class="panel-body">
				<div class="radio">
		  			<label><input type="radio" id="hostRadio" name="spectateRadio" value=${id1} checked>Spectate </label>
				</div>
		    </div>
		  </div>
		</div>
		<div class="col-md-4 tallCol">
			<h4>Versus</h4>
		</div>
		<div class="col-md-4 tallCol pull-right">
		  <div class="panel panel-default tallPanel">
		    <div class="panel-heading">
		      <h3 class="panel-title" id="oppname">${p2}</h3>
		    </div>
		    <div class="panel-body">
			    <div class="radio">
		  			<label><input type="radio" id="otherRadio" name="spectateRadio" value=${id2}>Spectate </label>
				</div>
				<p id="message"><#if msg??> ${msg} </#if> </p>
		    </div>
		  </div>
		</div>
	   <div class="col-md-2 centerText">
	       <button type="button" class="btn btn-default" id="leave">Leave</button>
	   </div>
	</div>

     <script src="js/jquery-2.1.1.js"></script>
     <script src="js/bootstrapalt.min.js"></script>
     <script src="js/jquery-cookie.js"></script>
     <script src="js/spectateLobbySocket.js"></script>
     <script src="js/spectateLobby.js"></script>
  </body>
</html>
