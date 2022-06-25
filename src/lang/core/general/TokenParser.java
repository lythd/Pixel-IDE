package lang.core.general;

import java.util.ArrayList;

public class TokenParser {

	public static ArrayList<String> parse(String input) {
		ArrayList<String> tokens = new ArrayList<String>();
		boolean inString = false;
		boolean inComment = false;
		boolean skip = false;
		boolean reset = false;
		String current = "";
		String text = input + "\n";
		for(char c : text.toCharArray()) {
			if(c == '\n') {
				skip = true;
				inComment = false;
			}
			if(c == '#') inComment = true;
			if(!inComment) {
				if(c == '"') inString = !inString;
				if(!inString) {
					if(c == '\r') skip = true;
					if(c == '\t') skip = true;
					if(c == ' ') {
						skip = true;
						reset = true;
					}
					if(c == '=' || c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%' || c == '!' || c == '&'
							|| c == '|' || c == '(' || c == ')' || c == ',') {
						reset = true;
					}
					if(c == ';') reset = true;
				}
			}
			if(!inComment && !inString) {
				if(reset) {
					if(current.equals("not")) current = "!";
					if(current.equals("and")) current = "&";
					if(current.equals("or")) current = "|";
					if(current.equals("in")) current = ":";
					if(current.equals("boolean")) current = "bool";
					if(current.equals("integer")) current = "int";
					if(current.length() > 0) tokens.add(current);
					if(!skip) tokens.add("" + c);
					current = "";
				}
				else if(!skip) current += c;
			}
			else if (inString) {
				current += c;
			}
			skip = false;
			reset = false;
		}
		return tokens;
	}
	
}
