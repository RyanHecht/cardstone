<#assign content>
<#include "message_modal.ftl">
<div class="container">
  <h3> Welcome to Cardstone, ${username} </h3> 
  <div class="row">
    <div class="col-sm-8">
	    <a href="/decks" id="decks" class="btn btn-default btn-lg btn-block shadowedButton">Decks</a>
	    <br><br>
        <a href="/games" id="games" class="btn btn-default btn-lg btn-block shadowedButton">Games</a>
	    <br><br>
        <a href="/lobbies" id="lobbies" class="btn btn-default btn-lg btn-block shadowedButton">Lobbies</a>
        <br><br>
    </div>
  </div>
</div>

${modal}
</#assign>
