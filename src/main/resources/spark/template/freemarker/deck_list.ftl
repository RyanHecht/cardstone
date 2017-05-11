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
    <tr>
    <td style="vertical-align:middle">${deck}</td>
    <td><button class="btn btn-default btn-sm" onclick="viewDeck("${deck}")">Edit</button></td>
    <td><button class="btn btn-default btn-sm" onclick="deleteDeck($(this),"${deck?replace(' ', '_')}")">Delete</button></td>
    <tr>
  </#list>
</table>
</#assign>
