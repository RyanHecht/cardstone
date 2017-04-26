<#assign deck_list>
<ul id="deck_list">
<#list decks as deck>
  <#assign link = "/deck?name=" + deck>
  <li><a href=${link} class="btn btn-default btn-lg btn-block">${deck}</a></li>
</#list>
</ul>
</#assign>
