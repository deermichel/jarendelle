package org.arendelle.java.engine;

import java.util.SortedMap;

public class Spaces {

	/** Replaces all spaces (variables) in the given expression with their values.
	 * @param expression Expression.
	 * @return The final expression.
	 */
	public static String replace(String expression, CodeScreen screen, SortedMap<String, String> spaces) {
		
		for (String name : spaces.keySet()) {
			expression = expression.replaceAll('@' + name, spaces.get(name));
		}
		
		return expression;
	}
	
	/** This is the Spaces kernel, where it parses and edit spaces.
	 * @param arendelle a given Arendelle instance
	 * @param screen Screen.
	 * @param spaces Spaces.
	 * @throws Exception 
	 */
	public static void parse(Arendelle arendelle, CodeScreen screen, SortedMap<String, String> spaces) throws Exception {
		
		String name = "";
		for (int i = arendelle.i + 1; !(arendelle.code.charAt(i) == ',' || arendelle.code.charAt(i) == ')'); i++) {
			name += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		String expression = "";
		if (arendelle.code.charAt(arendelle.i + 1) == ',') {
			for (int i = arendelle.i + 2; arendelle.code.charAt(i) != ')'; i++) {
				expression += arendelle.code.charAt(i);
				arendelle.i = i;
			}
		}
		
		expression = expression.replaceAll("#rnd", Sources.RNDGenerator(screen));
		
		if (expression == "") {
			
			//spaces.put(name, TODO: User input));
			
		} else {
			
			switch(expression.charAt(0)) {
			
			case '"':
				//spaces.put(name, User input));
				break;
				
			case '+':
			case '-':
			case '*':
			case '/':
			case '×':
			case '÷':
				spaces.put(name, String.valueOf(new Expression(Replacer.replace(spaces.get(name) + expression.charAt(0) + expression.substring(1), screen, spaces)).eval().intValue()));
				break;
				
			default:
				spaces.put(name, String.valueOf(new Expression(Replacer.replace(expression, screen, spaces)).eval().intValue()));
				break;
				
			}
			
		}
		
		arendelle.i++;
		
	}
	
}
