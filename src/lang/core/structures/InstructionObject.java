package lang.core.structures;

import java.util.ArrayList;

import lang.core.general.CodeObjectGeneral;

public class InstructionObject extends CodeObjectGeneral {
	
	public ArrayList<String> data;
	public String type;
	
	public InstructionObject(ArrayList<String> data_, String type_) {
		super();
		data = data_;
		type = type_;
	}

}
