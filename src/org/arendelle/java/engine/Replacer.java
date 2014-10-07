package org.arendelle.java.engine;

import java.util.SortedMap;

public class Replacer {

	/** Replaces all variables in the given expression with their values.
	 * @param expression Expression.
	 * @return The final expression.
	 */
	public static String replace(String expression, CodeScreen screen, SortedMap<String, String> spaces) {

		expression = Sources.replace(expression, screen);
		expression = Spaces.replace(expression, screen, spaces);
		
		return expression;
	}
	
}
