package lang.core.structures;

import java.util.ArrayList;

import lang.core.general.CodeObjectGeneral;

public class StructureObject extends CodeObjectGeneral {
	
	ArrayList<String> tokens;
	
	public StructureObject(ArrayList<String> tokens_) {
		super();
		tokens = tokens_;
	}
	
}
