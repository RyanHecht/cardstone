<#assign game_table>
<table class="table table-hover">
  <thead>
    <th>Winner</th>
    <th>Opponent</th>
    <th># of Moves</th>
   <#-- <th>Date</th> -->
  </thead>
  <tbody>
    <#list games as game>
      <#assign link = "/replay?id=" + game.id>
      <tr class="clickable-row" data-href=${link}>
        <td> ${game.id} </td>
        <td> ${game.winner} </td>
        <td> ${game.moves} </td>
      </tr>
    </#list>
  </tbody>
</table>
</#assign>
