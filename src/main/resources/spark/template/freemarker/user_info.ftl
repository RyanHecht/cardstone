<#assign content>
<#-- <#include "list.ftl"> -->
<div class="container">
  <#-- <h3> User ${username} has these puppies... </h3>
  <div class="row">
    ${list}
  </div>
  <div class='row'>
    <input type="hidden" id="user" value="${username}">
    <div class='col-sm-6'>
      <div class='form-group'>
         <div class='input-group'>
            <input type="text" class="form-control" id="puppyname" placeholder="Add a puppy...">
               <span class="input-group-btn">
                  <input class="btn btn-primary" type="submit" id="addpuppy">
               </span>
         </div>
      </div>
    </div>
  </div>
  -->
  <div class='row'>
  <div class='col-sm-6'>
    <input type=file id=files />
    <button id=upload>Upload</button>
  </div>
  </div>
  <a href="/login">Logout</a>
</div>
</#assign>
