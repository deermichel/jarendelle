
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

import java.util.Random;

public class CodeScreen {

	public int x;
	public int y;
	public int z;
	public int color;
	public int width;
	public int height;
	public int screen[][];
	public Random rand;
	
	public CodeScreen(int width, int height) {
		
		this.x = 0;
		this.y = 0;
		this.z = 0;
		
		this.color = 0;
		
		this.width = width;
		this.height = height;
		this.screen = new int[width][height];
		
		this.rand = new Random();
		
	}
	
}
