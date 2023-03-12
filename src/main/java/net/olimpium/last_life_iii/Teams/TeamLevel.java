package net.olimpium.last_life_iii.Teams;

import java.util.ArrayList;

public class TeamLevel {


	private int level;
	public TeamLevel(int level){
		this.level = level;
	}

	/**
	 * Gets the stats of the level
	 * @return Returns an ArrayList where the first element equals the EnderChest size (ammount of rows) and the second element the amount of trinkets you can hold
	 */
	public ArrayList<Integer> getStats(){
		return getStats(level);
	}
	public ArrayList<Integer> getStats(int level){
		System.out.println("LEVEL: " + level);
		// first element = EnderChest size | second element = trinket size;
		ArrayList<Integer> perks = new ArrayList<>();
		switch (level){
			case 1:
				perks.add(1);
				perks.add(1);
				break;
			case 2:
				perks.add(2);
				perks.add(1);
				break;
			case 3, 4:
				perks.add(2);
				perks.add(2);
				break;
			case 5, 6:
				perks.add(3);
				perks.add(3);
				break;
			case 7, 8:
				perks.add(4);
				perks.add(3);
				break;
			case 9:
				perks.add(4);
				perks.add(4);
				break;
			case 10:
				perks.add(5);
				perks.add(5);
				break;
		}

		return perks;
	}


}
