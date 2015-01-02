package org.arendelle.java.engine;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

public class Arrays {

	/** puts a value with an index into a space array
	 * @param value
	 * @param index
	 * @param name
	 * @param spaces
	 */
	public static void put(String value, String index, String name, SortedMap<String, String> spaces) {
		
		String rawSpace = spaces.get(name);
		if (rawSpace == null) rawSpace = index + "," + value;
		SortedMap<String, String> array = getArray(rawSpace);
		array.put(index, value);
		for (int i = 0; i < Integer.valueOf(index); i++) if (!array.containsKey(String.valueOf(i))) array.put(String.valueOf(i), "0");
		rawSpace = getRawSpace(array);
		spaces.put(name, rawSpace);
		
	}
	
	/** splits a raw space into an array
	 * @param rawSpace
	 * @return array
	 */
	public static SortedMap<String, String> getArray(String rawSpace) {
		
		SortedMap<String, String> array = 
				new TreeMap<String, String>(new Comparator<String>() {
					public int compare(String s1, String s2) {
						int lengthComparison = s2.length() - s1.length();
						if (lengthComparison != 0) {
							return lengthComparison;
						}
						return s1.compareTo(s2);
					}
		});
		for (String item : rawSpace.split(";")) {
			array.put(item.split(",")[0], item.split(",")[1]);
		}
		
		return array;
	}
	
	public static String getRawSpace(SortedMap<String, String> array) {
		
		String rawSpace = "";
		for (String index : array.keySet()) {
			rawSpace += index + "," + array.get(index) + ";";
		}
		rawSpace = rawSpace.substring(0, rawSpace.length() - 1);
		
		return rawSpace;
	}
	
	public static SortedMap<String, String> getReplacerArray(SortedMap<String, String> spaces) {
		
		SortedMap<String, String> array = 
				new TreeMap<String, String>(new Comparator<String>() {
					public int compare(String s1, String s2) {
						int lengthComparison = s2.length() - s1.length();
						if (lengthComparison != 0) {
							return lengthComparison;
						}
						return s1.compareTo(s2);
					}
		});
		SortedMap<String, String> temp;
		for (String name : spaces.keySet()) {
			temp = getArray(spaces.get(name));
			for (String index : temp.keySet()) {
				array.put(name + ":" + index, temp.get(index));
			}
			array.put(name, temp.get("0"));
			array.put(name + ":?", String.valueOf(temp.size()));
		}
		
		return array;
	}
	
}
