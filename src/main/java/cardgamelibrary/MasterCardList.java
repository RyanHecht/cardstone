package cardgamelibrary;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import com.google.common.reflect.ClassPath;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import game.Player;
import game.PlayerType;

/**
 * Singleton class that holds an instance of every card in the game. relevant
 * stackoverflow:
 * http://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
 *
 * @author Raghu
 *
 */
public final class MasterCardList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static MasterCardList	master			= new MasterCardList();
	private LinkedList<Card>			masterList	= new LinkedList<>();
	private Player								fakePlayer	= new Player(0, PlayerType.PLAYER_ONE, -1);
	private static final Gson			GSON				= new Gson();

	/**
	 * Private constructor to defeat instantiation.
	 */
	private MasterCardList() {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		try {
			for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
				if (info.getName().startsWith("cards")) {
					String fullName = info.getName();
					if (!(fullName.equals("cards.FireElement") || fullName.equals("cards.WaterElement")
							|| fullName.equals("cards.EarthElement") || fullName.equals("cards.AirElement")
							|| fullName.equals("cards.BalanceElement"))) {
						Object p = Class.forName(info.getName()).getConstructor(Player.class).newInstance(fakePlayer);
						if (p instanceof Creature) {
							masterList.add((Creature) p);
						} else if (p instanceof AuraCard) {
							masterList.add((AuraCard) p);
						} else if (p instanceof SpellCard) {
							masterList.add((SpellCard) p);
						} else if (p instanceof Element) {
							masterList.add((Element) p);
						}

					}
				}
			}
			// now that we have added everything except our elements we should add
			// those.

			Element fire = (Element) Class.forName("cards.FireElement").getConstructor(Player.class).newInstance(fakePlayer);
			Element water = (Element) Class.forName("cards.WaterElement").getConstructor(Player.class)
					.newInstance(fakePlayer);
			Element earth = (Element) Class.forName("cards.EarthElement").getConstructor(Player.class)
					.newInstance(fakePlayer);
			Element air = (Element) Class.forName("cards.AirElement").getConstructor(Player.class).newInstance(fakePlayer);
			Element balance = (Element) Class.forName("cards.BalanceElement").getConstructor(Player.class)
					.newInstance(fakePlayer);
			masterList.addFirst(balance);
			masterList.addFirst(air);
			masterList.addFirst(earth);
			masterList.addFirst(water);
			masterList.addFirst(fire);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the card list from the class.
	 *
	 * @return a list of all cards in the cards package.
	 */
	public String getAllCards() {
		List<JsonObject> list = new LinkedList<>();

		for (Card c : masterList) {
			list.add(c.jsonifySelf());
		}
		return (GSON.toJson(list));
	}

}
