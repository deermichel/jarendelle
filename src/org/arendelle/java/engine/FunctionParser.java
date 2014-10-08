package org.arendelle.java.engine;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.SortedMap;
import java.util.TreeMap;

public class FunctionParser {

	/** This is the FunctionParser kernel, where it parses and runs a function.
	 * @param arendelle a given Arendelle instance
	 * @param screen Screen.
	 * @param spaces Spaces.
	 * @throws Exception 
	 */
	public static void parse(Arendelle arendelle, CodeScreen screen, SortedMap<String, String> spaces) throws Exception {
		
		String functionName = "";
		for (int i = arendelle.i + 1; arendelle.code.charAt(i) != '('; i++) {
			functionName += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		SortedMap<String, String> functionSpaces = new TreeMap<String, String>(spaces.comparator());
		functionSpaces.put("return", "0");
		
		String parameters = "";
		for (int i = arendelle.i + 2; arendelle.code.charAt(i) != ')'; i++) {
			parameters += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		String[] functionParameters = parameters.split(",");
		
		String functionPath = screen.mainPath + "/" + functionName.replace('.', '/') + ".arendelle";
		
		String functionCode = new String(Files.readAllBytes(Paths.get(functionPath)), StandardCharsets.UTF_8);
		
	}
	
}
