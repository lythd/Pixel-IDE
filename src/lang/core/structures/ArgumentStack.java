package lang.core.structures;

import java.util.ArrayList;

import lang.core.general.CodeObjectGeneral;
import lang.core.general.StructureParser;

public class ArgumentStack extends CodeObjectGeneral {

		public ArrayList<String> tokens;
		public ArrayList<StructureObject> structures;
		public int length = 0;
		
		public ArgumentStack(ArrayList<String> tokens_, boolean found, int start) {
			//if found is true, then this means its a function, if found is false, this means its just mathematical brackets
			super();
			length = start;
			tokens = tokens_;
			if(found) {
				if(!tokens.get(tokens.size()-1).equals(",")) tokens.add(",");
				structures = new ArrayList<StructureObject>();
				ArrayList<String> tokns = new ArrayList<String>();
				ArrayList<String> tkns = new ArrayList<String>();
				for(String t : tokens) {
					if(t.equals(",")) {
						if(tkns.size() > 2) {
							ComputeStack cs = new ComputeStack(tkns,length+1);
							for(StructureObject s : cs.structures) structures.add(s);
							length = ComputeStack.newlength(tkns,length) + 1;
						} else {
							tokns.add("%tmp" + ++length);
							for(String s : tkns) {
								if(StructureParser.isVar(s)) tokns.add("%" + s);
								else tokns.add(s);
							}
							structures.add(new StructureObject(tokns,"set"));
						}
						tkns = new ArrayList<String>();
						tokns = new ArrayList<String>();
					} else if(!isFunc(t)) tkns.add(t);
				}
			} else {
				if(tokens.contains(",")) {
					//throw error, underline in red
				} else {
					
				}
			}
		}
		
		private boolean isFunc(String s) {
			for(String[] sa : StructureParser.Functions) if(sa[0].equals(s)) return true;
			return false;
		}
}
