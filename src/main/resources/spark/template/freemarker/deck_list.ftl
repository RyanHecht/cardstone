<#assign deck_list>

<table class="table table-hover">
  <thead>
    <th>Deck Name</th>
    <th>Edit</th>
    <th>Delete</th>
  </thead>
  <#if decks?size == 0>
  	<tr> <td> <h5> You have no decks </h4> </td> <td> </td> <td> </td> </tr>
  </#if>
  <#list decks as deck>
    <#assign editLink =  "/deck?name=" + deck?replace(" ", "_")>
    <tr>
    <td style="vertical-align:middle">${deck}</td>
    <td><a href=${editLink} class="btn btn-default btn-sm">Edit</a></td>
    <td><button class="btn btn-default btn-sm" onclick="deleteDeck($(this),'${deck}')">Delete</button></td>
    <tr>
  </#list>
</table>
</#assign>
