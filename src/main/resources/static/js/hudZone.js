class hudZone extends drawableZone{

    constructor(div,health,res){
        super();
        this.div = div;
        this.health = health;
        this.res = res;
        this.prepTip();
    }
    
    draw(){
        console.log("started draw" + this.div);
        let $this = this;
        let totalHeight = 100;
        this.div.children(".healthBox").text($this.health);
        this.div.children(".healthLine").css({
			'height':function(index, value){
                if($this.health > MAX_HEALTH){
                    return totalHeight;
                }
				return (totalHeight * $this.health / MAX_HEALTH); 
		}});
        this.div.children(".resBox").text($this.res);
        console.log("completed draw" + this);
        $("#optionsPopperGear").click(popOptionsMenu);
    }
    
    forceRedraw(){
        this.draw();
    }
    
    prepTip(){
        let $this = this;
        this.div.qtip({
				content: {
					text: $this.div.next('div') 
				},
                prerender: true,
				style: {
					classes: 'qtip-bootstrap',
				},
				position: { 
					viewport: $(window),
					adjust: {
						method: 'flipinvert shift'
					}
					
				}
			});
    }

}