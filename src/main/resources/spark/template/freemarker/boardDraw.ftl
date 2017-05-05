<!DOCTYPE html>
<#include "boardDraw.html">
<script type="text/javascript">
  let isReplay = ${isReplay?string("true", "false")};
  <#if isReplay>
    let gameId = ${gameId};
  </#if>
  console.log("yoyoyoy i set game id and isReplay");
</script>
		<script src="js/jquery-2.1.1.js"></script>
		<script src="js/jquery-cookie.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
        <script src="js/MouseManagerSystem.js"></script>
		<script src="js/animation.js"></script>
        <script src="js/cardDrawnAnimation.js"></script>
		<script src="js/animationsMaker.js"></script>
        <script src="js/Enums.js"></script>
        <script src="js/TurnTimer.js"></script>
		<script src="js/tether.min.js"></script>
		<script src="js/bootstrap.js"></script>
		<script src="js/cardCacher.js"></script>
        
		<script src="js/drawable.js"></script>

		<script src="js/radialAnimation.js"></script>
		<script src="js/movingDrawable.js"></script>
		<script src="js/linearAnimation.js"></script>
		<script src="js/manaPool.js"></script>
		<script src="js/cost.js"></script>
		<script src="js/card.js"></script>

		<script src="js/drawableZone.js"></script>
        <script src="js/hudZone.js"></script>
		<script src="js/healthResZone.js"></script>
		<script src="js/chooseZone.js"></script>
		<script src="js/cardCollection.js"></script>
		<script src="js/board.js"></script>
		<script src="js/server.js"></script>
		<script src="js/cardDrawing.js"></script>
		<script src="js/qtip.js"></script>
</body>
</html>