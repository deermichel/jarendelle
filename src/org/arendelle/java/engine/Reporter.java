
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

public class Reporter {
	
	/** reported errors */
	public static String errors = "";
	

	/** This is the Reporter kernel, where it reports an error.
	 * @param message The error message.
	 * @param line Line where the error occurs.
	 */
	public static void report(String message, int line) {
		errors += message + " (line " + line + ")\n";
	}
	
}
