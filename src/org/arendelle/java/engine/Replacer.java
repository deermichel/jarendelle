
//
//  JArendelle - Java Portation of the Arendelle Language
//  Copyright (c) 2014 Micha Hanselmann <h@arendelle.org>
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

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

		expression = Replacer.replaceFunctions(expression, screen, spaces);
		expression = Sources.replace(expression, screen);
		expression = Spaces.replace(expression, screen, spaces);
		expression = StoredSpaces.replace(expression, screen);
		expression = Keys.replace(expression, screen);
		
		expression = Replacer.catchErrors(expression);
		
		return expression;
	}
	
	/** Replaces all functions in the given expression with their return values.
	 * @param expression Expression.
	 * @param screen Screen.
	 * @param spaces Spaces.
	 * @return The final expression.
	 */
	public static String replaceFunctions(String expression, CodeScreen screen, SortedMap<String, String> spaces) {

		String expressionWithoutFunctions = "";
		for (int i = 0; i < expression.length(); i++) {
			
			if (expression.charAt(i) == '!') {
				
				String funcExpression = "";
				int nestedGrammars = 0;
				
				while (!(expression.charAt(i) == ')' && nestedGrammars == 1)) {
					funcExpression += expression.charAt(i);
					
					if (expression.charAt(i) == '[' || expression.charAt(i) == '{' || expression.charAt(i) == '(') {
						nestedGrammars++;
					} else if (expression.charAt(i) == ']' || expression.charAt(i) == '}' || expression.charAt(i) == ')') {
						nestedGrammars--;
					}

					i++;
					if (i >= expression.length()) break;
				}
				funcExpression += expression.charAt(i);
				
				Arendelle tempArendelle = new Arendelle(funcExpression);
				expressionWithoutFunctions += FunctionParser.parse(tempArendelle, screen, spaces);
				
			} else {
				expressionWithoutFunctions += expression.charAt(i);
			}
			
		}
		
		return expressionWithoutFunctions;
	}
	
	/** Catches all errors in the given expression.
	 * @param expression Expression.
	 * @return The final expression.
	 */
	public static String catchErrors(String expression) {
		
		String expressionWithoutErrors = "";
		for (int i = 0; i < expression.length(); i++) {
			
			if (expression.charAt(i) == '@') {

				i++;

				String name = "";
				switch (expression.charAt(i - 1)) {
				
				case '@':
					
					while(!(expression.substring(i, i + 1).matches("[^A-Za-z0-9]"))) {
						name += expression.charAt(i);
						i++;
						if (i >= expression.length()) break;
					}

					Reporter.report("Unsigned space '@" + name + "' found.", -1);
					
					break;
					
				case '!':
					
					while(expression.charAt(i) != '(') {
						name += expression.charAt(i);
						i++;
						if (i >= expression.length()) break;
					}

					Reporter.report("Undefined function: '" + name + "'", -1);
					
					break;
				
				}
				
				i--;
				expressionWithoutErrors += "0";
				
			} else {
				expressionWithoutErrors += expression.charAt(i);
			}
			
		}
		
		return expressionWithoutErrors;
	}
	
}
