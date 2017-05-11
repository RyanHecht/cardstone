package cardgamelibrary;

import java.io.Serializable;

import cards.AirElement;
import cards.BalanceElement;
import cards.EarthElement;
import cards.FireElement;
import cards.WaterElement;
import devotions.AirDevotion;
import devotions.BalanceDevotion;
import devotions.Devotion;
import devotions.EarthDevotion;
import devotions.FireDevotion;
import devotions.NoDevotion;
import devotions.WaterDevotion;
import game.Player;



public enum DevotionType implements Serializable{
	FIRE,WATER,EARTH,AIR,BALANCE,NO_DEVOTION;

	public static Devotion getDevotion(Player p, ElementType src) {
		if(src.equals(ElementType.AIR)){
			return new AirDevotion(p);
		}
		else if(src.equals(ElementType.WATER)){
			return new WaterDevotion(p);
		}
		else if(src.equals(ElementType.EARTH)){
			return new EarthDevotion(p);
		}
		else if(src.equals(ElementType.FIRE)){
			return new FireDevotion(p);
		}
		else if(src.equals(ElementType.BALANCE)){
			return new BalanceDevotion(p);
		}
		else{
			GlobalLogger.problem("Invalid element card " + src + p);
			return new NoDevotion(p);
		}
	}
}

