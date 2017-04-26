class movingDrawable extends drawable{
	
	constructor(shape,x1,y1,x2,y2,color,speed,radius){
		super(shape,speed,color,radius);
		this.x = x1;
		this.y = y1;
		this.goalX = x2;
		this.goalY = y2;
		this.update = this.getMover(x1,y1,x2,y2,speed);
		this.update(this,10);
		
	}
	
	getMover(x1,y1,x2,y2,speed){
		let xDelt = x2 - x1;
		let yDelt = y2 - y1;
		let mag = Math.sqrt(xDelt * xDelt + yDelt * yDelt);
		let dX = xDelt / mag;
		let dY = yDelt / mag;
		if(x1 == x2 && y1 == y2){
			return function(){
				return true;
			}
		}
		let move = function(drawable,delta){
			drawable.x += dX * drawable.speed * delta;
			drawable.y += dY * drawable.speed * delta;
		}
		if(dX < 0){
			if(dY < 0){
				return function(drawable,delta){
					move(drawable,delta);
					return (drawable.x < drawable.goalX || drawable.y < drawable.goalY)
				}
			}
			else{
				return function(drawable,delta){
					move(drawable,delta);
					return (drawable.x < drawable.goalX || drawable.y > drawable.goalY)
				}
			}
		}
		else{
			if(dY < 0){
				return function(drawable,delta){
					move(drawable,delta);
					return (drawable.x > drawable.goalX || drawable.y < drawable.goalY)
				}
			}
			else{
				return function(drawable,delta){
					move(drawable,delta);
					return (drawable.x > drawable.goalX || drawable.y > drawable.goalY)
				}
			}
		}
	}
	
}