package org.arendelle.java.engine;

import java.util.SortedMap;
import java.util.TreeMap;

public class ConditionParser {

	/** This is the ConditionParser kernel, where it parses and runs a condition.
	 * @param arendelle a given Arendelle instance
	 * @param screen Screen.
	 * @param spaces Spaces.
	 * @throws Exception 
	 */
	public static void parse(Arendelle arendelle, CodeScreen screen, SortedMap<String, String> spaces) throws Exception {
		
		String expression = "";
		for (int i = arendelle.i + 1; arendelle.code.charAt(i) != ','; i++) {
			expression += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		expression = Replacer.replaceRND(expression, screen);
		
		String trueCode = "";
		int nestedGrammars = 0;
		for (int i = arendelle.i + 2; !((arendelle.code.charAt(i) == '}' || arendelle.code.charAt(i) == ',') && nestedGrammars == 0); i++) {
			
			trueCode += arendelle.code.charAt(i);
			
			switch (arendelle.code.charAt(i)) {
			case '[':
			case '{':
			case '(':
				nestedGrammars++;
				break;
			case ']':
			case '}':
			case ')':
				nestedGrammars--;
				break;
			}
			
			arendelle.i = i;
		}
		
		String falseCode = "";
		if (arendelle.code.charAt(arendelle.i + 1) == ',') {
			for (int i = arendelle.i + 2; !(arendelle.code.charAt(i) == '}' && nestedGrammars == 0); i++) {
				
				falseCode += arendelle.code.charAt(i);
				
				switch (arendelle.code.charAt(i)) {
				case '[':
				case '{':
				case '(':
					nestedGrammars++;
					break;
				case ']':
				case '}':
				case ')':
					nestedGrammars--;
					break;
				}
				
				arendelle.i = i;
			}
		}
		
		arendelle.i++;
		
		Arendelle trueArendelle = new Arendelle(trueCode);
		Arendelle falseArendelle = new Arendelle(falseCode);
		SortedMap<String, String> conditionSpaces = new TreeMap<String, String>(spaces.comparator());
		conditionSpaces.putAll(spaces);
		
		if (new Expression(Replacer.replace(expression, screen, conditionSpaces)).eval().intValue() != 0) {
			Kernel.eval(trueArendelle, screen, conditionSpaces);
		} else {
			Kernel.eval(falseArendelle, screen, conditionSpaces);
		}
		
		for (String name : spaces.keySet()) {
			spaces.put(name, conditionSpaces.get(name));
		}
		
	}
	
}
