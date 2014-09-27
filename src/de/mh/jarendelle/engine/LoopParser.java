
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

public class LoopParser {

	/** This is the LoopParser kernel, where it parses and runs a loop.
	 * @param arendelle a given anrendelle instance
	 * @throws Exception 
	 */
	public static void parse(Arendelle arendelle, CodeScreen screen) throws Exception {
		
		String mathExpr = "";
		for (int i = arendelle.i + 1; arendelle.code.charAt(i) != ','; i++) {
			mathExpr += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		int loopTimes = new Expression(mathExpr).eval().intValue();
		
		String loopCode = "";
		for (int i = arendelle.i + 2; i != arendelle.code.lastIndexOf(']'); i++) {
			loopCode += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		arendelle.i++;
		
		Arendelle loopArendelle = new Arendelle(loopCode);
		CodeScreen loopScreen = screen;
		for (int i = 0; i < loopTimes; i++) Kernel.eval(loopArendelle, loopScreen);
		screen = loopScreen;
		
	}
	
}
