package lang.core.general;

import java.util.ArrayList;

import codeArea.KeywordStyledDocument;
import lang.core.structures.StructureObject;

public class StructureParser {

	public ArrayList<StructureObject> parse(ArrayList<String> tokens_) {
		ArrayList<StructureObject> structures = new ArrayList<StructureObject>();
		int lastSpecial = 0;
		int i = 0;
		for(String token : tokens_) {
			if(KeywordStyledDocument.isReservedWord(token)) {
				ArrayList<String> tokens = new ArrayList<String>();
				for(int j = lastSpecial; j < i; j++) tokens.add(tokens_.get(j));
				structures.add(new StructureObject(tokens));
				lastSpecial = i;
			}
			i++;
		}
		return structures;
	}
	
}
