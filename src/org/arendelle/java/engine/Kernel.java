
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

import java.util.Arrays;
import java.util.HashMap;

/** Arendelles Kernel which evaluates, reads and runs the code. */
public class Kernel {

	/**
	 * eval is the main core of the language where it evaluates the given code.
	 * @param arendelle a given Arendelle instance
	 * @param screen Screen.
	 * @param spaces Spaces.
	 * @throws Exception
	 */
	public static void eval(Arendelle arendelle, CodeScreen screen, HashMap<String, String> spaces) throws Exception {
		
		/*
		 *  So what we do is we read the code char-by-char and run the commands
		 *  Just-In-Time. If we find a grammer we let grammer parsers take care
		 *  of that.
		 */
		
		char command = 0;
		
		while (arendelle.i < arendelle.code.length()) {
			
			// get command and replace upper-case letters with the lower-case one
			command = arendelle.code.toLowerCase().charAt(arendelle.i);
			
			
			////////////////////////
			/// START OF RUNTIME ///
			////////////////////////
			
			switch (command) {
			
			//////////////////////////////////////////////////
			///                  Grammars                  ///
			//////////////////////////////////////////////////
			
			case '[':
				LoopParser.parse(arendelle, screen, spaces);
				break;
				
			/*case '!':
				break;*/
				
			case '(':
				Spaces.parse(arendelle, screen, spaces);
				break;
				
			case '{':
				ConditionParser.parse(arendelle, screen, spaces);
				break;
				
			case '\'':
				String title = "";
				for (int i = arendelle.i + 1; arendelle.code.charAt(i) != '\''; i++) {
					title += arendelle.code.charAt(i);
					arendelle.i = i;
				}
				screen.title = Replacer.replace(title, screen, spaces);
				arendelle.i++;
				break;
			
				
			//////////////////////////////////////////////////
			///                  Commands                  ///
			//////////////////////////////////////////////////
				
			case 'p':
				if (screen.x >= 0 && screen.y >= 0 && screen.x < screen.width && screen.y < screen.height) {
					screen.screen[screen.x][screen.y] = screen.color + 1;
				}
				break;
				
			case 'u':
				screen.y--;
				break;
				
			case 'd':
				screen.y++;
				break;
				
			case 'r':
				screen.x++;
				break;
				
			case 'l':
				screen.x--;
				break;
				
			case 'e':
				LoopParser.breakLoop = true;
				break;
				
			case 'n':
				screen.color = (screen.color + 1) % 4;
				break;
				
			case 'c':
				for (int[] row : screen.screen) Arrays.fill(row, 0);
				break;
				
			case 'w':
				Thread.sleep(1);
				break;
				
			/*case 's':
			    TODO: Implement command
				break;*/
				
			case 'i':
				screen.x = 0;
				screen.y = 0;
				break;
				
				
			/////////////////////////////////////////////////////
			///               Errors and Others               ///
			/////////////////////////////////////////////////////
				
			case ']':
				throw new Exception("Unexpected loop token ']' found.");
				
			case ')':
				throw new Exception("Unexpected variable token ')' found.");
				
			case '}':
				throw new Exception("Unexpected condition token '}' found.");
				
			case '<':
				throw new Exception("Unexpected function header found.");
				
			case '>':
				throw new Exception("Unexpected function header token '>' found.");
				
			case ',':
				throw new Exception("Unexpected grammar divider ',' found.");
				
			/*case '\n':
				TODO: Implement line counter
				break;*/
				
			default:
				if (command != ' ' && command != ';' && command != '\n') throw new Exception("Unknown command: '" + command + "'");
				
			}
			
			//////////////////////
			/// END OF RUNTIME ///
			//////////////////////

			arendelle.i++;
			
		}
		
	}
	
}
