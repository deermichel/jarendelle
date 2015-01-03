
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

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class StoredSpaces {

	/** replaces all stored spaces (stored variables) in the given expression with their values
	 * @param expression
	 * @return The final expression
	 */
	public static String replace(String expression, CodeScreen screen) {
		
		// copy whole code without stored spaces
		String expressionWithoutStoredSpaces = "";
		for (int i = 0; i < expression.length(); i++) {
			
			if (expression.charAt(i) == '$') {
				
				i++;
				
				// get name
				String name = "";
				while(!(expression.substring(i, i + 1).matches("[^A-Za-z0-9.]"))) {
					name += expression.charAt(i);
					i++;
					if (i >= expression.length()) break;
				}
				
				i--;
				
				// get path and read stored space
				String storedSpacePath = screen.mainPath + "/" + name.replace('.', '/') + ".space";
				try {
					expressionWithoutStoredSpaces += new String(Files.readAllBytes(Paths.get(storedSpacePath)), StandardCharsets.UTF_8);
					// expressionWithoutStoredSpaces += Files.read(new File(storedSpacePath));
				} catch (Exception e) {
					Reporter.report("No stored space as '$" + name + "' found.", -1);
					expressionWithoutStoredSpaces += "0";
				}
				
			} else {
				expressionWithoutStoredSpaces += expression.charAt(i);
			}
			
		}
		expression = expressionWithoutStoredSpaces;
		
		return expression;
	}
	
	/** StoredSpaces kernel which parses and edit stored spaces
	 * @param arendelle a given Arendelle instance
	 * @param screen
	 * @param spaces
	 */
	public static void parse(Arendelle arendelle, CodeScreen screen, HashMap<String, String> spaces) {
		
		// get name
		String name = "";
		for (int i = arendelle.i + 2; !(arendelle.code.charAt(i) == ',' || arendelle.code.charAt(i) == ')' || arendelle.code.charAt(i) == '['); i++) {
			name += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		// get index for array
		String index = "";
		int nestedGrammars = 0;
		if (arendelle.code.charAt(arendelle.i + 1) == '[') {
			for (int i = arendelle.i + 2; !(arendelle.code.charAt(i) == ']' && nestedGrammars == 0); i++) {
				index += arendelle.code.charAt(i);
				arendelle.i = i;
				
				if (arendelle.code.charAt(i) == '[') {
					nestedGrammars++;
				} else if (arendelle.code.charAt(i) == ']') {
					nestedGrammars--;
				}
			}
			index = String.valueOf(new Expression(Replacer.replace(index, screen, spaces)).eval().intValue());
			arendelle.i++;
		} else {
			index = "0";
		}
		
		// get mathematical expression for condition
		String expression = "";
		if (arendelle.code.charAt(arendelle.i + 1) == ',') {
			for (int i = arendelle.i + 2; !(arendelle.code.charAt(i) == ')' && nestedGrammars == 0); i++) {
				expression += arendelle.code.charAt(i);
				arendelle.i = i;
				
				if (arendelle.code.charAt(i) == '[' || arendelle.code.charAt(i) == '{' || arendelle.code.charAt(i) == '(') {
					nestedGrammars++;
				} else if (arendelle.code.charAt(i) == ']' || arendelle.code.charAt(i) == '}' || arendelle.code.charAt(i) == ')') {
					nestedGrammars--;
				}
				
			}
		}
		
		arendelle.i++;
		
		// get path
		String storedSpacePath = screen.mainPath + "/" + name.replace('.', '/') + ".space";
		String storedSpaceValue = "";
		
		// determine action
		if (expression == "") {
			
			// get user input
			if (!screen.interactiveMode) {
				Reporter.report("Not running in Interactive Mode!", arendelle.line);
				return;
			}
			String message = (index == "0") ? "Sign stored space '$" + name + "' with a number:" : "Sign stored space '$" + name + "' at index " + index + " with a number:";
			String value = JOptionPane.showInputDialog(message);
			storedSpaceValue = String.valueOf(new Expression(Replacer.replace(value, screen, spaces)).eval().intValue());
			
		} else if (expression.equals("done")) {
			
			// delete stored space
			new File(storedSpacePath).delete();
			return;
			
		} else {
			
			switch(expression.charAt(0)) {
			
			case '"':

				// get user input by message
				if (!screen.interactiveMode) {
					Reporter.report("Not running in Interactive Mode!", arendelle.line);
					return;
				}
				String value = JOptionPane.showInputDialog(expression.substring(1, expression.length() - 1));
				storedSpaceValue = String.valueOf(new Expression(Replacer.replace(value, screen, spaces)).eval().intValue());
				
				break;
				
			case '+':
			case '-':
			case '*':
			case '/':
				// edit stored space
				try {
					storedSpaceValue = String.valueOf(new Expression(Replacer.replace(Arrays.getArray(new String(Files.readAllBytes(Paths.get(storedSpacePath)), StandardCharsets.UTF_8)).get(index) + expression.charAt(0) + expression.substring(1), screen, spaces)).eval().intValue());
					// storedSpaceValue = String.valueOf(new Expression(Replacer.replace(Files.read(new File(storedSpacePath)) + expression.charAt(0) + expression.substring(1), screen, spaces)).eval().intValue());
				} catch (Exception e) {
					Reporter.report(e.toString(), arendelle.line);
				}
				break;
				
			default:
				// create stored space
				storedSpaceValue = String.valueOf(new Expression(Replacer.replace(expression, screen, spaces)).eval().intValue());
				break;
				
			}
			
		}
		
		// save stored space or update existing
		try {
			HashMap<String, String> array = new HashMap<String, String>();
			if (new File(storedSpacePath).exists()) array = Arrays.getArray(new String(Files.readAllBytes(Paths.get(storedSpacePath)), StandardCharsets.UTF_8));
			for (int i = 0; i < Integer.valueOf(index); i++) if (!array.containsKey(String.valueOf(i))) array.put(String.valueOf(i), "0");
			array.put(index, storedSpaceValue);
			
			PrintWriter writer;
			writer = new PrintWriter(storedSpacePath);
			writer.print(Arrays.getRawSpace(array));
			writer.close();
			// Files.write(new File(storedSpacePath), Arrays.getRawSpace(array));
		} catch (Exception e) {
			Reporter.report(e.toString(), arendelle.line);
		}
		
	}
	
}
