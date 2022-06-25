package lang.core.general;

import java.util.ArrayList;

public class TokenParser {

	public static ArrayList<String> parse(String text) {
		ArrayList<String> tokens = new ArrayList<String>();
		boolean inString = false;
		boolean inComment = false;
		boolean skip = false;
		boolean reset = false;
		char c2 = '\0';
		String current = "";
		for(char c : text.toCharArray()) {
			if(c == '#' || (c == '/' && c2 == '/')) inComment = true;
			if(c == '"' && !inComment) inString = !inString;
			if(c == '\r') skip = true;
			if(c == '\n') {
				skip = true;
				inComment = false;
			}
			if(c == '\t') skip = true;
			if(c == ' ') {
				skip = true;
				reset = true;
			}
			if(c == ';') reset = true;
			if(!inComment && !skip && !reset) current += c;
			if(!inComment && !inString && reset) {
				tokens.add(current);current = "";
				if(!skip) tokens.add("" + c);
			}
			c2 = c;
			skip = false;
			reset = false;
		}
		return tokens;
	}
	
}
