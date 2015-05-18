package org.arendelle.java.engine;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class Modules {
	
	/** Modules kernel which parses and runs a module
	 * @param arendelle a given Arendelle instance
	 * @param screen 
	 * @param spaces
	 */
	public static void parse(Arendelle arendelle, CodeScreen screen, HashMap<String, String> spaces) {
		
		// get module name
		String moduleName = "";
		for (int i = arendelle.i + 1; !(arendelle.code.charAt(i) == ':'); i++) {
			moduleName += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		// get function name
		String functionName = "";
		for (int i = arendelle.i + 2; !(arendelle.code.charAt(i) == '~'); i++) {
			functionName += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		arendelle.i++;
		
		// load module and execute function						# R.I.P. - Pouya didnt allowed it :(
		try {
			
			ClassLoader loader = URLClassLoader.newInstance(new URL[] { new File(moduleName + ".jar").toURI().toURL() }, Modules.class.getClassLoader());
			Class classToLoad = Class.forName("jarendelle.mod." + moduleName, true, loader);
			Method method = classToLoad.getDeclaredMethod(functionName, int[][].class);
			Object instance = classToLoad.newInstance();
			Object result = method.invoke(instance, screen.screen);
			
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
	}
	
}
