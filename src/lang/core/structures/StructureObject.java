package lang.core.structures;

import java.util.ArrayList;

import lang.core.general.CodeObjectGeneral;

public class StructureObject extends CodeObjectGeneral {
	
	public ArrayList<String> data;
	public String type;
	
	public StructureObject(ArrayList<String> data_, String type_) {
		super();
		data = data_;
		type = type_;
	}
	
}
