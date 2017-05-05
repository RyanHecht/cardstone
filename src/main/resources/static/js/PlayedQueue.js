class PlayedQueue{
    
    constructor(){
        this.pastEvents = [];
        this.div = $("#cardQueue");
    }
    
    add(card){
        if(this.pastEvents.length >= PQ_SIZE){
            this.pastEvents.pop();
        }
        this.pastEvents.unshift(card);
    }
    
    redraw(){
        this.div.empty();
        for(let x = 0; x < this.pastEvents.length; x++){
            this.div.append("<div class='queueBox hasTooltip'></div>");
            let subBox = this.div.children().last();
            console.log(subBox);
            console.log(this.div);
            subBox.css("background-image","url("+this.pastEvents[x].imagePath+")");
            this.div.append("<div class='bigCardBox'></div>");
            let bigBox = this.div.children().last(); 
            bigBox.hide();
            
            this.pastEvents[x].drawBig(bigBox);
            bigBox.removeClass("hasTooltip");
        }
        this.prepTips(this.div);
    }
    
    prepTips(){
		let maxHeight = $(document).height();
		let maxWidth = $(document).width();
		let height;
		if(maxHeight > 400){
			height = 350;
		}
		else{
			height = maxHeight * .8;
			$(".bigCardBox").css({
				fontSize : "10px",
				lineHeight : "90%"
			});
		}
		let width = height * WIDTH_RATIO;
		while(width > maxWidth){
			height -= 10;
			width = height * WIDTH_RATIO;
		}
		this.div.children('.hasTooltip').each(function() {
            console.log("tipping");
            console.log(this);
            console.log($(this).next('div'));
			$(this).qtip({
				content: {
					text: $(this).next('div') 
				},
				style: {
					height: height,
					width: width,
					classes: 'qtip-bootstrap',
				},
				position: { 
					viewport: $(window),
					adjust: {
						method: 'flipinvert shift'
					}
					
				}
			});
		});
	}
    
}