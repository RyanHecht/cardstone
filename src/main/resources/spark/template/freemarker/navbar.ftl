  <#assign nav>
  <nav class="navbar navbar-default">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/menu">Cardstone</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li><a href="/games">Past Games</a></li>
            <li><a href="/lobbies">Lobbies</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Decks <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="/decks">View Saved</a></li>
                <li><a href="/deckDraw.html">Create New</a></li>
              </ul>
            </li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li><p class="navbar-text" id="loggedin"></p></li>
            <li><a href="/login" id="logout">Logout</a></li>
          </ul>
        </div>
      </div>
    </nav>
  </#assign>

<script src="js/jquery-2.1.1.js"></script>
<script src="js/jquery-cookie.js"></script>  
<script type="text/javascript">
	$(document).ready(() => {
		$("#loggedin").text("Logged in as " + $.cookie("username"));
		$("#logout").on("click", function() {
			console.log("nadsfadsfs");
			$.removeCookie("id", {path: '/'});
			$.removeCookie("username", {path: '/'});
     	});
     });
</script>