<!DOCTYPE html>
<#include "boardDraw.html">
<script type="text/javascript">
  let isReplay = ${isReplay?string("true", "false")};
  <#if isReplay>
    let gameId = ${gameId};
  </#if>
</script>
</body>
</html>