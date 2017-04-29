<#assign lobby_table>
<table class="table table-hover">
  <thead>
    <th>Lobby Name</th>
    <th>Host</th>
    <th>Private?</th>
    <th>Full?</th>
  </thead>
  <tbody id="lobby-table">
  </tbody>
</table>

<!-- Modal -->
<div id="hostLobby" class="modal fade" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Create a lobby</h4>
      </div>
      <div class="modal-body">
             <div class="form-group row">
               <label class="col-sm-2 col-form-label">Name</label>
               <div class="col-sm-8">
               <input type="text" class="form-control" id="lname">
               </div>
             </div>
		    <div class="form-group row">
		      <label class="col-sm-2">Private?</label>
		      <div class="col-sm-10">
		        <div class="form-check">
		            <input class="form-check-input" type="checkbox" id="lprivate"> 
		        </div>
		      </div>
		    </div>
             <div class="form-group row">
               <label class="col-sm-2 col-form-label">Password</label>
               <div class="col-sm-8">
               <input type="password" class="form-control" id="lpw">
               </div>
             </div>
		    <div class="form-group row">
		      <div class="offset-sm-2 col-sm-10">
		        <input class="btn btn-primary" type="submit" id="createlobby" value="Host">
		      </div>
			</div>
      </div>
    </div>
  </div>
</div> 


<div id="passwordModal" class="modal fade" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="pwTitle">Do you want to?</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class='col-sm-10'>
            <div class='form-group'>
            <div class='input-group'>
            <input type="password" class="form-control" id="pw">
               <span class="input-group-btn">
                  <input class="btn btn-primary" type="submit" id="pwSubmit">
               </span>
            </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div> 

<div id="messageModal" class="modal fade" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title" id="messageheader"></h4>
      </div>
      <div class="modal-body">
        <p id="message"> </p>
      </div>
    </div>
  </div>
</div> 
</#assign>