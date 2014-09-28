
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
	 * @param arendelle a given Arendelle instance
	 * @return the new Arendelle instance
	 * @throws Exception 
	 */
	public static Arendelle parse(Arendelle arendelle) throws Exception {
		
		String mathExpr = "";
		for (int i = arendelle.i + 1; arendelle.code.charAt(i) != ','; i++) {
			mathExpr += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		int loopTimes = new Expression(mathExpr).eval().intValue();
		
		String loopCode = "";
		int nestedLoops = 0;
		for (int i = arendelle.i + 2; !(arendelle.code.charAt(i) == ']' && nestedLoops == 0); i++) {
			
			loopCode += arendelle.code.charAt(i);
			
			switch (arendelle.code.charAt(i)) {
			case '[':
				nestedLoops++;
				break;
			case ']':
				nestedLoops--;
				break;
			}
			
			arendelle.i = i;
		}
		
		arendelle.i++;
		
		Arendelle loopArendelle = new Arendelle(arendelle);
		loopArendelle.code = loopCode;
		for (int i = 0; i < loopTimes; i++) {
			loopArendelle.i = 0;
			loopArendelle = Kernel.eval(loopArendelle);
		}
		
		loopArendelle.code = arendelle.code;
		loopArendelle.i = arendelle.i;
		arendelle = new Arendelle(loopArendelle);
		
		return arendelle;
	}
	
}
