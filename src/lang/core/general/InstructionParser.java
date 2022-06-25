package lang.core.general;

import static tree.TreePanel.sep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lang.core.structures.InstructionObject;
import lang.core.structures.StructureObject;

public class InstructionParser {

	public static ArrayList<InstructionObject> parse(ArrayList<StructureObject> structures_) {

		ArrayList<InstructionObject> instructions = new ArrayList<InstructionObject>();
		ArrayList<String> data;
		for(StructureObject s : structures_) {
			data = new ArrayList<String>();
			if(s.type.equals("init")) {
				data.add(s.data.get(0));
				if(s.data.get(1).equals("int")) instructions.add(new InstructionObject(data,"iint"));
				else if(s.data.get(1).equals("String")) instructions.add(new InstructionObject(data,"istring"));
				else if(s.data.get(1).equals("bool")) instructions.add(new InstructionObject(data,"ibool"));
				else if(s.data.get(1).equals("float")) instructions.add(new InstructionObject(data,"ifloat"));
				else {
					data.add(s.data.get(1));
					instructions.add(new InstructionObject(data,"iobject"));
				}
			} else if(s.type.equals("compute")) {
				data.add(s.data.get(0));
				if(!s.data.get(1).equals("!")) data.add(s.data.get(2));
				data.add("%tmp"+s.data.get(3));
				if(s.data.get(1).equals("+")) instructions.add(new InstructionObject(data,"add"));
				else if(s.data.get(1).equals("-")) instructions.add(new InstructionObject(data,"subtract"));
				else if(s.data.get(1).equals("*")) instructions.add(new InstructionObject(data,"multiply"));
				else if(s.data.get(1).equals("/")) instructions.add(new InstructionObject(data,"divide"));
				else if(s.data.get(1).equals("%")) instructions.add(new InstructionObject(data,"modulo"));
				else if(s.data.get(1).equals("^")) instructions.add(new InstructionObject(data,"power"));
				else if(s.data.get(1).equals("!")) instructions.add(new InstructionObject(data,"not"));
				else if(s.data.get(1).equals("&")) instructions.add(new InstructionObject(data,"and"));
				else if(s.data.get(1).equals("|")) instructions.add(new InstructionObject(data,"or"));
			} else if(s.type.equals("set")) {
				instructions.add(new InstructionObject(s.data,"set"));
			} else if(s.type.equals("func")) {
				if(getPath(s.data.get(0)).substring(5).startsWith("executor")) {
					data.add(s.data.get(0));
					if(s.data.size()>1) for(int i=1;i<s.data.size();i++) {
						data.add(s.data.get(i));
					}
					instructions.add(new InstructionObject(data,"execute"));
				} else for(InstructionObject i : getFunc(s.data)) instructions.add(i);
			}
		}
		return instructions;
	}
	
	public static InstructionObject[] fullparse(String parse) {
		return asArray(simplify(parse(StructureParser.parse(TokenParser.parse(parse)))));
	}
	
	public static InstructionObject[] asArray(ArrayList<InstructionObject> in) {
		InstructionObject[] out = new InstructionObject[in.size()];
		for(int i=0;i<in.size();i++)out[i]=in.get(i);
		return out;
	}
	
	//calls through all the simplify functions
	public static ArrayList<InstructionObject> simplify(ArrayList<InstructionObject> in) {
		return simplify3(simplify2(simplify1(in)));
	}
	
	/*	for any two consecutive set functions, compact through useless temp variables, ie:
	*		set [%tmp1, 4]
	*		set [i, %tmp1]
	*	will turn into:
	*		set [i, 4]
	*/
	private static ArrayList<InstructionObject> simplify1(ArrayList<InstructionObject> in) {
		ArrayList<InstructionObject> out = new ArrayList<InstructionObject>();
		boolean lastset = false;
		ArrayList<String> data;
		InstructionObject cur,prev=null;
		for(int i=0;i<in.size();i++) {
			cur=in.get(i);
			if(in.get(i).type.equals("set")) {
				if(lastset) {
					if(prev.data.get(0).contains("tmp")&&prev.data.get(0).equals(cur.data.get(1))) {
						data = new ArrayList<String>();
						data.add(cur.data.get(0));
						data.add(prev.data.get(1));
						out.add(new InstructionObject(data,"set"));
						lastset=false;
					} else out.add(prev);
				} else lastset=true;
			} else {
				if(lastset) {
					out.add(prev);
					lastset=false;
				}
				out.add(cur);
			}
			if(i+1>=in.size()&&lastset) out.add(cur);
			prev=cur;
		}
		return out;
	}
	
	/* 	for any of the compute functions followed by a set function, compact through useless temp variables, ie:
	 * 		multiply [3, 2, %tmp1]
	 * 		set [j, %tmp1]
	 * 	will turn into:
	 * 		multiply [3, 2, j]
	 */
	private static ArrayList<InstructionObject> simplify2(ArrayList<InstructionObject> in) {
		ArrayList<InstructionObject> out = new ArrayList<InstructionObject>();
		boolean lastcomp = false;
		ArrayList<String> data;
		InstructionObject cur,prev=null;
		for(int i=0;i<in.size();i++) {
			cur=in.get(i);
			if(in.get(i).type.equals("set")) {
				if(lastcomp) {
					lastcomp=false;
					int lasti = prev.data.size()-1;
					String last = prev.data.get(lasti);
					if(last.contains("tmp")&&last.equals(cur.data.get(1))) {
						data = new ArrayList<String>();
						data.add(prev.data.get(0));
						if(lasti>1)data.add(prev.data.get(1));
						data.add(cur.data.get(0));
						out.add(new InstructionObject(data,prev.type));
					} else {
						out.add(prev);
						out.add(cur);
					}
				} else out.add(cur);
			} else if(isCompute(in.get(i).type)) {
				if(lastcomp)out.add(prev);
				else lastcomp=true;
			} else {
				if(lastcomp) {
					out.add(prev);
					lastcomp=false;
				}
				out.add(cur);
			}
			if(i+1>=in.size()&&lastcomp) out.add(cur);
			prev=cur;
		}
		return out;
	}
	
	/* 	remove any set instructions that are remnants of a blank initialise, ie:
	 * 		set [k, ?]
	 */
	private static ArrayList<InstructionObject> simplify3(ArrayList<InstructionObject> in) {
		ArrayList<InstructionObject> out = new ArrayList<InstructionObject>();
		for(InstructionObject instruction : in) if(!(instruction.type.equals("set")&&instruction.data.get(1).equals("?"))) out.add(instruction);
		return out;
	}
	
	private static boolean isCompute(String s) {
		String[]computes=new String[]{"add","subtract","multiply","divide","modulo","power","not","and","or"};
		for(String c:computes)if(c.equals(s))return true;
		return false;
	}
	
	private static ArrayList<InstructionObject> getFunc(ArrayList<String> in) {
		ArrayList<InstructionObject> out = new ArrayList<InstructionObject>();
		
		String path = getPath(in.get(0));
		if(path.equals("NO_PATH")) return out;
		File file = new File(System.getProperty("user.dir") + path);
		String st;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((st = br.readLine()) != null) out.add(parseFrom(st,in));
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
	private static String modifyPath(String pathin) {
		String path=sep+"src"+sep;
		for(char c:pathin.toCharArray()){
			if(c=='.')path+=sep;
			else path+=c;
		}
		path+=".pxl";
		return path;
	}
	
	private static String getPath(String name) {
		for(String[]sa:StructureParser.Functions)if(sa[0].equals(name))return modifyPath(sa[1]);
		return "NO_PATH";
	}
	
	private static InstructionObject parseFrom(String string,ArrayList<String>in) {
		String[] split = string.split(",");
		ArrayList<String> splitted = new ArrayList<String>();
		for(int i=1;i<split.length;i++)splitted.add(resolveArg(split[i],in));
		return new InstructionObject(splitted,split[0]);
	}
	
	private static String resolveArg(String string,ArrayList<String>in) {
		String s="";
		String i="";
		boolean isi=false;
		for(char c:string.toCharArray()) {
			if(c=='`')isi=true;
			else if(c=='~') {
				isi=false;
				s+=in.get(Integer.parseInt(i)+1);
			}
			else if(isi)i+=c;
			else s+=c;
		}
		return s;
	}
}
