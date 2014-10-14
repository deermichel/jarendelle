
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

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

public class MasterEvaluator {
	
	/**
	 * The very main evaluator that runs a given code on a screen.
	 * @param code Code.
	 * @param screen Screen.
	 * @throws Exception 
	 */
	public static void evaluate(String code, CodeScreen screen) throws Exception {
		
		// remove comments
		code = MasterEvaluator.removeComments(code);
		
		// remove spaces for faster performance
		code = MasterEvaluator.removeSpaces(code);
		
		// setting up the spaces
		SortedMap<String, String> spaces = 
				new TreeMap<String, String>(new Comparator<String>() {
					public int compare(String s1, String s2) {
						int lengthComparison = s2.length() - s1.length();
						if (lengthComparison != 0) {
							return lengthComparison;
						}
						return s1.compareTo(s2);
					}
		});
		
		// setting up an Arendelle instance
		Arendelle arendelle = new Arendelle(code);
		
		
		//////////////////
		/// EVALUATING ///
		//////////////////
		
		// running
		Kernel.eval(arendelle, screen, spaces);
		
	}
	
	public static String removeComments(String code) {
		
		String codeWithoutComments = "";
		char command = 0;
		
		for (int i = 0; i < code.length(); i++) {
			
			command = code.charAt(i);
			
			if (command == '/') {
				
				command = code.charAt(i + 1);
				
				switch (command) {
				
				case '/':
					
					while (code.charAt(i) != '\n' && i < code.length() - 1) i++;
					
					break;
					
				case '*':
					
					while (!(code.charAt(i) == '*'  && code.charAt(i + 1) == '/') && i < code.length() - 2) i++;
					i++;
					
					break;
					
				default:
					
					codeWithoutComments += '/';
					
					break;
				
				}
				
			} else {
				
				codeWithoutComments += command;
				
			}
			
		}
		
		return codeWithoutComments;
	}
	
	public static String removeSpaces(String code) throws Exception {

		String codeWithoutSpaces = "";
		
		for (int i = 0; i < code.length(); i++) {
			
			// exclude window titles
			if (code.charAt(i) == '\'') {
				do {
					codeWithoutSpaces += code.charAt(i);
					i++;
					if (i > code.length() - 1) throw new Exception("Syntax error, insert ''' to complete statement.");
				} while (!(code.charAt(i) == '\'' && code.charAt(i - 1) != '\\'));
			}
			
			// exclude strings
			if (code.charAt(i) == '\"') {
				do {
					codeWithoutSpaces += code.charAt(i);
					i++;
					if (i > code.length() - 1) throw new Exception("Syntax error, insert '\"' to complete statement.");
				} while (!(code.charAt(i) == '\"'));
			}
			
			if (code.charAt(i) != ' ') codeWithoutSpaces += code.charAt(i);
			
		}
		
		return codeWithoutSpaces;
	}
	
}
