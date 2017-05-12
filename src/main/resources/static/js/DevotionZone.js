class DevotionZone extends drawableZone{
    constructor(div,devotion){
        super();
        this.div = div;
        this.devotion = devotion;
        this.emblemBox = this.div.children(".devotionEmblem");
        this.labelBox = this.div.children(".devotionLabel");
    }
    
    draw(){
        if(this.devotion.type == "NO_DEVOTION"){
            this.emblemBox.css("background-image","none");
            this.emblemBox.empty();
            this.labelBox.empty();
        }
        else{
            this.emblemBox.css("background-image", "url(images/elements/" + this.devotion.type.toLowerCase() + "Big.jpg)");
            console.log("url(images/elements/" + this.devotion.type.toLowerCase() + "Big.jpg)");
            this.labelBox.text(this.devotion.level);
        }
    }
}