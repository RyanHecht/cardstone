<#assign deck_list>
<ul id="deck_list" class="list-unstyled">
<#list decks as deck>
  <#assign link = "/deck?name=" + deck?replace(" ", "_")>
  <li><a href=${link} class="btn btn-default btn-lg btn-block">${deck}</a></li>
</#list>
</ul>
</#assign>
