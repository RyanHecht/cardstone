<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="stylesheet" href="css/bootstrapalt.css">
    <link rel="stylesheet" href="css/main.css">
  </head>
  <body>
     <div class="container">
       <div class="row">
         <div class="col-sm-8">
           <ol id="event_list">
			<#list events as event>
			  <li>${event}</li>
			</#list>
		   </ol>
         </div>
       </div>
     </div>
  </body>
</html>