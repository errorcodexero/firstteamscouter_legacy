package com.wilsonvillerobotics.firstteamscouter.utilities;

import java.util.Hashtable;
import java.util.Set;

public class FTSUtilities {

	private static Boolean DEBUG = true;
	private static int testTeamNums[] = {1425, 1520, 2929, 1114, 500, 600, 700, 800, 900, 1000};
	private static Hashtable<Integer, String> testTeamData = new Hashtable<Integer, String>(){
		{ 
			put(1425, "Error Code Xero");
			put(1520, "Flaming Chickens");
			put(2929, "JagBots");
			put(1114, "Derp");
			put(500, "Spots");
			put(600, "Frozen Poultry");
			put(700, "Jumping Frogs");
			put(800, "Droids");
			put(900, "Sad Pandas");
			put(1000, "Grommets");
		}
	};
	
	public static void printToConsole(String message) {
		if(message.isEmpty() || !DEBUG) return;
		int len = 50 - message.length();
		String spacer = "";
		for(int i = 0; i < len/2;i++) {
			spacer += " ";
		}
		System.out.println("");
		System.out.println("**************************************************");
		System.out.println(spacer + message);
		System.out.println("**************************************************");
		System.out.println("");
	}
	
	public static Set<Integer> getTestTeamNumbers() {
		Set<Integer> teamNums = testTeamData.keySet();
		return teamNums;
	}
	
	public static String getTeamName(int tNum) {
		return testTeamData.get(tNum);
	}
}
