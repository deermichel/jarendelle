
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
import java.util.SortedMap;

import javax.swing.JOptionPane;

public class StoredSpaces {

	/** Replaces all stored spaces (variables) in the given expression with their values.
	 * @param expression Expression.
	 * @return The final expression.
	 */
	public static String replace(String expression, CodeScreen screen) {
		
		String expressionWithoutStoredSpaces = "";
		for (int i = 0; i < expression.length(); i++) {
			
			if (expression.charAt(i) == '$') {
				
				i++;
				
				String name = "";
				while(!(expression.substring(i, i + 1).matches("[^A-Za-z0-9]"))) {
					name += expression.charAt(i);
					i++;
					if (i >= expression.length()) break;
				}
				
				i--;
				
				String storedSpacePath = screen.mainPath + "/" + name.replace('.', '/') + ".space";
				try {
					expressionWithoutStoredSpaces += new String(Files.readAllBytes(Paths.get(storedSpacePath)), StandardCharsets.UTF_8);
				} catch (Exception e) {
					Reporter.report(e.toString(), -1);
					expressionWithoutStoredSpaces += "0";
				}
				
			} else {
				expressionWithoutStoredSpaces += expression.charAt(i);
			}
			
		}
		expression = expressionWithoutStoredSpaces;
		
		return expression;
	}
	
	/** This is the StoredSpaces kernel, where it parses and edit stored spaces.
	 * @param arendelle a given Arendelle instance
	 * @param screen Screen.
	 * @param spaces Spaces.
	 */
	public static void parse(Arendelle arendelle, CodeScreen screen, SortedMap<String, String> spaces) {
		
		String name = "";
		for (int i = arendelle.i + 2; !(arendelle.code.charAt(i) == ',' || arendelle.code.charAt(i) == ')'); i++) {
			name += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		String expression = "";
		int nestedGrammars = 0;
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
		
		String storedSpacePath = screen.mainPath + "/" + name.replace('.', '/') + ".space";
		String storedSpaceValue = "";
		
		if (expression == "") {
			
			if (!screen.interactiveMode) {
				Reporter.report("Not running in Interactive Mode!", arendelle.line);
				return;
			}
			String value = JOptionPane.showInputDialog("Sign stored space '@" + name + "' with a number:");
			storedSpaceValue = String.valueOf(new Expression(Replacer.replace(value, screen, spaces)).eval().intValue());
			
		} else if (expression.equals("done")) {
			
			new File(storedSpacePath).delete();
			return;
			
		} else {
			
			switch(expression.charAt(0)) {
			
			case '"':

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
			case '�':
			case '�':
				try {
					storedSpaceValue = String.valueOf(new Expression(Replacer.replace(new String(Files.readAllBytes(Paths.get(storedSpacePath)), StandardCharsets.UTF_8) + expression.charAt(0) + expression.substring(1), screen, spaces)).eval().intValue());
				} catch (Exception e) {
					Reporter.report(e.toString(), arendelle.line);
				}
				break;
				
			default:
				storedSpaceValue = String.valueOf(new Expression(Replacer.replace(expression, screen, spaces)).eval().intValue());
				break;
				
			}
			
		}
		
		try {
			PrintWriter writer;
			writer = new PrintWriter(storedSpacePath);
			writer.print(storedSpaceValue);
			writer.close();
		} catch (Exception e) {
			Reporter.report(e.toString(), arendelle.line);
		}
		
	}
	
}