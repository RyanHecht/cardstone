<#assign lobby_display>
<div class="col-md-4 tallCol">
  <div class="panel panel-default tallPanel" id="blah">
    <div class="panel-heading">
      <h3 class="panel-title">You</h3>
    </div>
    <div class="panel-body">
		<select class="form-control" id="deckselect">
		  <option selected>Pick a deck</option>
		  <option>Tutorial Deck</option>
		</select>
		<p id="usermessage"> </p>
    </div>
  </div>
</div>
<div class="col-md-4 tallCol">
	<h4>Versus</h4>
</div>
<div class="col-md-4 tallCol pull-right">
  <div class="panel panel-default tallPanel">
    <div class="panel-heading">
      <h3 class="panel-title" id="oppname">Tutorial Opponent</h3>
    </div>
    <div class="panel-body">
	    <p id="oppmessage">Tutorial Opponent is ready </p>
    </div>
  </div>
</div>
</#assign>