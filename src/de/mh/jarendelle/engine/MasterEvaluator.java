
//
//  JArendelle - Java Portation of the Arendelle Language
//  Copyright (c) 2014 mh
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

package de.mh.jarendelle.engine;

public class MasterEvaluator {
	
	/**
	 * The very main evaluator that runs a given code on the screen.
	 * @param code Code.
	 * @param screen Screen.
	 * @throws Exception 
	 */
	public static void evaluate(String code, CodeScreen screen) throws Exception {
		
		//
		// remove comments
		//
		code = MasterEvaluator.removeComments(code);
		
		//
		// remove spaces for faster performance
		//
		code = MasterEvaluator.removeSpaces(code);
		
		//
		// replace upper-case letters with the lower-case ones
		//
		code = code.toLowerCase();
		
		//
		// setting up the Arendelle
		//
		Arendelle arendelle = new Arendelle(code);
		
		
		//////////////////
		/// EVALUATING ///
		//////////////////
		
		// TODO: Set IDE title
		
		//
		// running
		//
		Kernel.eval(arendelle, screen);
		
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
	
	public static String removeSpaces(String code) {

		String codeWithoutSpaces = "";
		
		for (char c : code.toCharArray()) {
			if (c != ' ') codeWithoutSpaces += c;
		}
		
		return codeWithoutSpaces;
	}
	
}
