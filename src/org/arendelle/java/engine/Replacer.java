package org.arendelle.java.engine;

import java.util.SortedMap;

public class Replacer {

	/** Replaces all variables in the given expression with their values.
	 * @param expression Expression.
	 * @param screen Screen.
	 * @param spaces Spaces.
	 * @return The final expression.
	 */
	public static String replace(String expression, CodeScreen screen, SortedMap<String, String> spaces) {

		expression = Sources.replace(expression, screen);
		expression = Spaces.replace(expression, screen, spaces);
		
		return expression;
	}
	
	/** Replaces all random numbers in the given expression with their values.
	 * @param expression Expression.
	 * @param screen Screen.
	 * @return The final expression.
	 */
	public static String replaceRND(String expression, CodeScreen screen) {
		
		expression = expression.replaceAll("#rnd", Replacer.RNDGenerator(screen));
		
		return expression;
	}
	
	/** Generates a random number in the format of "0.XXXXX"
	 * @param screen Screen.
	 * @return The generated number.
	 */
	public static String RNDGenerator(CodeScreen screen) {
		
		String number = "0.";
		
		for (int i = 0; i < 5; i++) number += String.valueOf(screen.rand.nextInt(10));
		
		return number;
	}
	
}
