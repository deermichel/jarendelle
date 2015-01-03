package org.arendelle.java.engine;

import java.util.HashMap;

public class Arrays {

	/** puts a value with an index into a space array
	 * @param value
	 * @param index
	 * @param name
	 * @param spaces
	 */
	public static void put(String value, String index, String name, HashMap<String, String> spaces) {
		
		String rawSpace = spaces.get(name);
		if (rawSpace == null) rawSpace = index + "," + value;
		HashMap<String, String> array = getArray(rawSpace);
		array.put(index, value);
		for (int i = 0; i < Integer.valueOf(index); i++) if (!array.containsKey(String.valueOf(i))) array.put(String.valueOf(i), "0");
		rawSpace = getRawSpace(array);
		spaces.put(name, rawSpace);
		
	}
	
	/** splits a raw space to an array
	 * @param rawSpace
	 * @return array
	 */
	public static HashMap<String, String> getArray(String rawSpace) {
		
		HashMap<String, String> array = new HashMap<String, String>();
		for (String item : rawSpace.split(";")) {
			array.put(item.split(",")[0], item.split(",")[1]);
		}
		
		return array;
	}
	
	/** creates a raw space of an array
	 * @param array
	 * @return rawSpace
	 */
	public static String getRawSpace(HashMap<String, String> array) {
		
		String rawSpace = "";
		for (String index : array.keySet()) {
			rawSpace += index + "," + array.get(index) + ";";
		}
		rawSpace = rawSpace.substring(0, rawSpace.length() - 1);
		
		return rawSpace;
	}
	
}
