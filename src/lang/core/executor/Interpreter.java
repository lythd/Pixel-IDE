package lang.core.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.MainFrame;
import lang.core.general.StructureParser;
import lang.core.structures.InstructionObject;
import static tree.TreePanel.sep;

public class Interpreter implements Executor {
	
	/* TO-DO
	 * -add way to make new objects
	 * -making strings from ""
	 * -line by line for interpreter
	 * -ran by the tree
	 * -make sure all variables have % in instruction parser
	 * -jump instruction
	 * -if else while for def yada yada and code blocks
	 * -global local
	 * -copy this for a runtime environment and try to work that, make it have a console, and also work with a byte array to do cool stuff storing in there
	 */
	
	//make these actual variables stored in the memory, not just in the interpreter
	//^ that is needed for actual runtime environment so should do it here
	public double DIV_ZERO = 9999;
	public double ZERO_TO_ZERO = 1;
	
	private InstructionObject[] instructions;//store the instructions to execute, needs to be loaded
	private ArrayList<Object> memory;//all the things stored in the memory, this is all data from variables and other things
	private ArrayList<String> memdata;//this data is to go along with whatever is in memory, for example for variables the type would be stored here
	private Map<String,Integer> variables;//links the variable name to its index in memory
	private Map<Integer,Integer> tmp;//links the tmp variable to its index in memory
	
	private int curInstruction;
	
	public void loadFromInstructions(InstructionObject[] instructions_) {
		instructions=instructions_;
		ridexecute();
	}
	
	public void loadFromFile(File file) {
		ArrayList<InstructionObject> list = new ArrayList<InstructionObject>();
		String st;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((st = br.readLine()) != null) {
				String[] split = st.split(",");
				ArrayList<String> splitted = new ArrayList<String>();
				for(int i=1;i<split.length;i++)splitted.add(split[i]);
				list.add(new InstructionObject(splitted,split[0]));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		instructions = new InstructionObject[list.size()];
		for(int i=0;i<list.size();i++)instructions[i]=list.get(i);
		ridexecute();
	}
	
	private void ridexecute() {
		ArrayList<InstructionObject> list = loopedadd(instructions);
		instructions = new InstructionObject[list.size()];
		for(int i=0;i<list.size();i++)instructions[i]=list.get(i);
	}
	
	private ArrayList<InstructionObject> loopedadd(InstructionObject[] instructions) {
		ArrayList<InstructionObject> list = new ArrayList<InstructionObject>();
		for(InstructionObject instruction:instructions) {
			if(instruction.type.equals("execute")) {
				list.addAll(loopedadd(getFunc(instruction.data)));
			} else list.add(instruction);
		}
		return list;
	}
	
	private ArrayList<InstructionObject> loopedadd(ArrayList<InstructionObject> instructions) {
		ArrayList<InstructionObject> list = new ArrayList<InstructionObject>();
		for(InstructionObject instruction:instructions) {
			if(instruction.type.equals("execute")) {
				list.addAll(loopedadd(getFunc(instruction.data)));
			} else list.add(instruction);
		}
		return list;
	}
	
	private ArrayList<InstructionObject> getFunc(ArrayList<String> in) {
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
	
	private String getPath(String name) {
		for(String[]sa:StructureParser.Functions)if(sa[0].equals(name))return modifyPath(sa[1]);
		return "NO_PATH";
	}
	
	private static String modifyPath(String pathin) {
		String path=sep+"src"+sep+"lang"+sep+"core"+sep;
		for(char c:pathin.toCharArray()){
			if(c=='.')path+=sep;
			else path+=c;
		}
		path+=".pxl";
		return path;
	}
	
	private InstructionObject parseFrom(String string,ArrayList<String>in) {
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
	
	public void run() {
		if(instructions==null||instructions.length==0) {
			printToConsole("No instructions given, exitting.",false,-1);
			return;
		}
		DIV_ZERO = 9999;
		ZERO_TO_ZERO = 1;
		memory = new ArrayList<Object>();
		memdata = new ArrayList<String>();
		variables = new HashMap<String,Integer>();
		tmp = new HashMap<Integer,Integer>();
		curInstruction = 0;
		initValues();
		for(InstructionObject o : instructions) System.out.println(o.type + " " + o.data);
		for(int curInstruction=0;curInstruction<instructions.length;curInstruction++) execute(instructions[curInstruction],curInstruction);
	}
	
	private void execute(InstructionObject cur, int i) {
		//printTraceback(i);
		String cur0,cur1,cur2;
		int toSet,tmpid;
		Object value,value2;
		double val1,val2;
		boolean addlater;
		switch(cur.type) {
			case "skip":
				tmpid=-1;
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not an integer: try catch
					else {
						try {
							setFrom=variables.get(cur0.substring(1));
						} catch (Exception e) {
							printToConsole("An attempt to skip by a non-existent variable of "+cur0.substring(1)+".",true,i);
							printTraceback(i);
							return;
						}
					}
					if(memdata.get(setFrom).equals("integer")) value = memory.get(setFrom);
					else {
						if(tmpid>-1) {
							value = memory.get(setFrom);
						} else {
							printToConsole("Cannot skip by a non integer variable.",true,i);
							printTraceback(i);
							return;
						}
					}
				} else {
					int type=-1;
					if(isString(cur0)) {value=removeQuotes(cur0);type=0;}
					else if(isInteger(cur0)) {value=Integer.parseInt(cur0);type=1;}
					else if(isNumeric(cur0)) {value=Double.parseDouble(cur0);type=2;}
					else if(isBoolean(cur0)) {value=Boolean.parseBoolean(cur0);type=3;}
					else {
						printToConsole("An attempt to print a unparseable value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
					if(type!=1) {
						printToConsole("An attempt to skip by a non integer value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
				}
				curInstruction += (int) value - 1;
				break;
			case "setskip":
				tmpid=-1;
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not an integer: try catch
					else {
						try {
							setFrom=variables.get(cur0.substring(1));
						} catch (Exception e) {
							printToConsole("An attempt to set skip reg from a non-existent variable of "+cur0.substring(1)+".",true,i);
							printTraceback(i);
							return;
						}
					}
					if(memdata.get(setFrom).equals("integer")) value = memory.get(setFrom);
					else {
						if(tmpid>-1) {
							value = memory.get(setFrom);
						} else {
							printToConsole("Skip reg being set to a variable of a different type.",true,i);
							printTraceback(i);
							return;
						}
					}
				} else {
					int type=-1;
					if(isString(cur0)) {value=removeQuotes(cur0);type=0;}
					else if(isInteger(cur0)) {value=Integer.parseInt(cur0);type=1;}
					else if(isNumeric(cur0)) {value=Double.parseDouble(cur0);type=2;}
					else if(isBoolean(cur0)) {value=Boolean.parseBoolean(cur0);type=3;}
					else {
						printToConsole("An attempt to print a unparseable value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
					if(type!=1) {
						printToConsole("An attempt to set skip reg by a non integer value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
				}
				memory.set(0, value);
				break;
			case "set":
				tmpid=-1;
				addlater=false;
				toSet=0;
				value=0;
				cur0=cur.data.get(0);
				cur1=cur.data.get(1);
				if(cur0.contains("%")) {
					if(cur0.startsWith("%tmp")) {
						try {
							tmpid=Integer.parseInt(cur0.substring(4));
						} catch (Exception e) {
							printToConsole("A tmp variable has been used with an invalid identifier: "+cur0+".",true,i);
							printTraceback(i);
							return;
						}
						try {
							if(tmpid>-1)toSet=tmp.get(tmpid);
							else {
								printToConsole("A tmp variable has been used with an invalid identifier: "+cur0+".",true,i);
								printTraceback(i);
								return;
							}
						}  catch (Exception e) {
							addlater=true;
						}
					} else {
						try {
							toSet=variables.get(cur0.substring(1));
						} catch (Exception e) {
							printToConsole("An attempt to set a non-existent variable of "+cur0.substring(1)+".",true,i);
							printTraceback(i);
							return;
						}
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur0+".",true,i);
					printTraceback(i);
					return;
				}
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur1.substring(4)));//implement an exit condition if not an integer: try catch
					else {
						try {
							setFrom=variables.get(cur1.substring(1));
						} catch (Exception e) {
							printToConsole("An attempt to set from a non-existent variable of "+cur1.substring(1)+".",true,i);
							printTraceback(i);
							return;
						}
					}
					if(memdata.get(toSet).equals(memdata.get(setFrom))) value = memory.get(setFrom);
					else if (!addlater) {
						if(tmpid>-1) {
							memory.set(toSet,null);
							memdata.set(toSet,memdata.get(setFrom));
							value = memory.get(setFrom);
						} else {
							printToConsole("Variable being set to a variable of a different type.",true,i);
							printTraceback(i);
							return;
						}
					}
					if(addlater) {
						if(tmpid>-1) {
							tmp.put(tmpid,memory.size());
							memory.add(memory.get(setFrom));
							memdata.add(memdata.get(setFrom));
							break;
						} else {
							printToConsole("A tmp variable has been used with an invalid identifier: "+cur0+".",true,i);
							printTraceback(i);
							return;
						}
					}
				} else {
					int type=-1;
					if(isString(cur1)) {value=removeQuotes(cur1);type=0;}
					else if(isInteger(cur1)) {value=Integer.parseInt(cur1);type=1;}
					else if(isNumeric(cur1)) {value=Double.parseDouble(cur1);type=2;}
					else if(isBoolean(cur1)) {value=Boolean.parseBoolean(cur1);type=3;}
					else {
						printToConsole("An attempt to print a unparseable value of "+cur1+".",true,i);
						printTraceback(i);
						return;
					}
					if(addlater) {
						if(tmpid>-1) {
							tmp.put(tmpid,memory.size());
							memory.add(value);
							switch(type) {
							case 0:
								memdata.add("string");
								break;
							case 1:
								memdata.add("integer");
								break;
							case 2:
								memdata.add("float");
								break;
							case 3:
								memdata.add("boolean");
								break;
							}
							break;
						} else {
							printToConsole("A tmp variable has been used with an invalid identifier: "+cur0+".",true,i);
							printTraceback(i);
							return;
						}
					} else {
						switch(type) {
						case 0:
							memdata.set(toSet,"string");
							break;
						case 1:
							memdata.set(toSet,"integer");
							break;
						case 2:
							memdata.set(toSet,"float");
							break;
						case 3:
							memdata.set(toSet,"boolean");
							break;
						}
					}
				}
				memory.set(toSet,null);
				memory.set(toSet,value);
				if(cur0.startsWith("%")&&!cur0.startsWith("%tmp")) {
					if(cur0.substring(1).equals("DIV_ZERO"))DIV_ZERO=(double)value;
					else if(cur0.substring(1).equals("ZERO_TO_ZERO"))ZERO_TO_ZERO=(double)value;
				}
				break;
			case "cast":
				tmpid=-1;
				addlater=false;
				toSet=0;
				value=0;
				cur0=cur.data.get(0);
				cur1=cur.data.get(1);
				if(cur0.contains("%")) {
					if(cur0.startsWith("%tmp")) {
						try {
							tmpid=Integer.parseInt(cur0.substring(4));
						} catch (Exception e) {
							printToConsole("A tmp variable has been used with an invalid identifier: "+cur0+".",true,i);
							printTraceback(i);
							return;
						}
						try {
							if(tmpid>-1) toSet=tmp.get(tmpid);
							else {
								printToConsole("A tmp variable has been used with an invalid identifier: "+cur0+".",true,i);
								printTraceback(i);
								return;
							}
						}  catch (Exception e) {
							addlater=true;
						}
					} else {
						try {
							toSet=variables.get(cur0.substring(1));
						} catch (Exception e) {
							printToConsole("An attempt to set a non-existent variable of "+cur0.substring(1)+".",true,i);
							printTraceback(i);
							return;
						}
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur0+".",true,i);
					printTraceback(i);
					return;
				}
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp")&&cur1.length()>4) {
						int pos=0;
						try {
							pos=Integer.parseInt(cur1.substring(4));
						} catch (Exception e) {
							printToConsole("An incorrect tmp variable of "+cur1+".",true,i);
							printTraceback(i);
							return;
						}
						try {
							setFrom=tmp.get(pos);
						} catch (Exception e) {
							printToConsole("Tmp variable not found: "+cur1+".",true,i);
							printTraceback(i);
							return;
						}
					}
					else {
						try {
							setFrom=variables.get(cur1.substring(1));
						} catch (Exception e) {
							printToConsole("An attempt to set from a non-existent variable of "+cur1.substring(1)+".",true,i);
							printTraceback(i);
							return;
						}
					}
					if(memdata.get(toSet).equals(memdata.get(setFrom))) value = memory.get(setFrom);
					else if (!addlater) {
						memory.set(toSet,null);
						memdata.set(toSet,memdata.get(setFrom));
						value = memory.get(setFrom);
					}
					if(addlater) {
						if(tmpid>-1) {
							tmp.put(tmpid,memory.size());
							memory.add(memory.get(setFrom));
							memdata.add(memdata.get(setFrom));
						} else {
							printToConsole("A tmp variable has been used with an invalid identifier: "+cur0+".",true,i);
							printTraceback(i);
							return;
						}
					}
				} else {
					if(isString(cur1)) value=removeQuotes(cur1);
					else if(isInteger(cur1)) value=Integer.parseInt(cur1);
					else if(isNumeric(cur1)) value=Double.parseDouble(cur1);
					else if(isBoolean(cur1)) value=Boolean.parseBoolean(cur1);
					else {
						printToConsole("An attempt to print a unparseable value of "+cur1+".",true,i);
						printTraceback(i);
						return;
					}
				}
				memory.set(toSet,null);
				memory.set(toSet,value);
				break;
			case "iint":
				variables.put(cur.data.get(0).substring(1),memory.size());
				memory.add(0);
				memdata.add("integer");
				break;
			case "ifloat":
				variables.put(cur.data.get(0).substring(1),memory.size());
				if(cur.data.get(0).substring(1).equals("DIV_ZERO"))memory.add(DIV_ZERO);
				else if(cur.data.get(0).substring(1).equals("ZERO_TO_ZERO"))memory.add(ZERO_TO_ZERO);
				else memory.add(0);
				memdata.add("float");
				break;
			case "ibool":
				variables.put(cur.data.get(0).substring(1),memory.size());
				memory.add(false);
				memdata.add("boolean");
				break;
			case "istring":
				variables.put(cur.data.get(0).substring(1),memory.size());
				memory.add("");
				memdata.add("string");
				break;
			case "iobject":
				variables.put(cur.data.get(0).substring(1),memory.size());
				memory.add(null);
				memdata.add("object");
				break;
			case "add":
				toSet=0;
				value=0;
				value2=0;
				cur2=cur.data.get(2);
				if(cur2.contains("%")) {
					if(cur2.startsWith("%tmp"))toSet=tmp.get(Integer.parseInt(cur2.substring(4)));//implement an exit condition if not an integer: try catch
					else toSet=variables.get(cur2.substring(1));
					if(!memdata.get(toSet).equals("integer")&&!memdata.get(toSet).equals("float")) {
						printToConsole("Only integers and floats can be used for the result of a 'addition' operation, not '"+memdata.get(toSet)+"'.",true,i);
						printTraceback(i);
						return;
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur2+".",true,i);
					printTraceback(i);
					return;
				}
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur0.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'addition' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value=memory.get(setFrom);
				} else {
					if(isString(cur0)) {
						printToConsole("Only integers and floats can be used for the 'addition' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur0)) value=Integer.parseInt(cur0);
					else if(isNumeric(cur0)) value=Double.parseDouble(cur0);
					else if(isBoolean(cur0)) value=Boolean.parseBoolean(cur0);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
				}
				cur1=cur.data.get(1);
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur1.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur1.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'addition' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value2=memory.get(setFrom);
				} else {
					if(isString(cur1)) {
						printToConsole("Only integers and floats can be used for the 'addition' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur1)) value2=Integer.parseInt(cur1);
					else if(isNumeric(cur1)) value2=Double.parseDouble(cur1);
					else if(isBoolean(cur1)) value2=Boolean.parseBoolean(cur1);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur1+".",true,i);
						printTraceback(i);
						return;
					}
				}
				val1=0;
				val2=0;
				try {val1=(double)value;}
				catch (Exception e) {val1=(double)(int)value;}
				try {val2=(double)value2;}
				catch (Exception e) {val2=(double)(int)value2;}
				if(memdata.get(toSet).equals("integer"))memory.set(toSet,(int)(val1+val2));
				else if(memdata.get(toSet).equals("float"))memory.set(toSet,val1+val2);
				else {
					printToConsole("Non-terminal error: Overriding existing value.",true,i);
					printTraceback(i);
					memdata.set(toSet,"float");
					memory.set(toSet,((double)value)+((double)value2));
				}
				break;
			case "subtract":
				toSet=0;
				value=0;
				value2=0;
				cur2=cur.data.get(2);
				if(cur2.contains("%")) {
					if(cur2.startsWith("%tmp"))toSet=tmp.get(Integer.parseInt(cur2.substring(4)));//implement an exit condition if not an integer: try catch
					else toSet=variables.get(cur2.substring(1));
					if(!memdata.get(toSet).equals("integer")&&!memdata.get(toSet).equals("float")) {
						printToConsole("Only integers and floats can be used for the result of a 'subtract' operation, not '"+memdata.get(toSet)+"'.",true,i);
						printTraceback(i);
						return;
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur2+".",true,i);
					printTraceback(i);
					return;
				}
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur0.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'subtract' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value=memory.get(setFrom);
				} else {
					if(isString(cur0)) {
						printToConsole("Only integers and floats can be used for the 'subtract' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur0)) value=Integer.parseInt(cur0);
					else if(isNumeric(cur0)) value=Double.parseDouble(cur0);
					else if(isBoolean(cur0)) value=Boolean.parseBoolean(cur0);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
				}
				cur1=cur.data.get(1);
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur1.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur1.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'subtract' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value2=memory.get(setFrom);
				} else {
					if(isString(cur1)) {
						printToConsole("Only integers and floats can be used for the 'subtract' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur1)) value2=Integer.parseInt(cur1);
					else if(isNumeric(cur1)) value2=Double.parseDouble(cur1);
					else if(isBoolean(cur1)) value2=Boolean.parseBoolean(cur1);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur1+".",true,i);
						printTraceback(i);
						return;
					}
				}
				val1=0;
				val2=0;
				try {val1=(double)value;}
				catch (Exception e) {val1=(double)(int)value;}
				try {val2=(double)value2;}
				catch (Exception e) {val2=(double)(int)value2;}
				if(memdata.get(toSet).equals("integer"))memory.set(toSet,(int)(val1-val2));
				else if(memdata.get(toSet).equals("float"))memory.set(toSet,val1-val2);
				else {
					printToConsole("Non-terminal error: Overriding existing value.",true,i);
					printTraceback(i);
					memdata.set(toSet,"float");
					memory.set(toSet,((double)value)-((double)value2));
				}
				break;
			case "multiply":
				toSet=0;
				value=0;
				value2=0;
				cur2=cur.data.get(2);
				if(cur2.contains("%")) {
					if(cur2.startsWith("%tmp")) {
						//implement an exit condition if not an integer: try catch
						toSet=tmp.get(Integer.parseInt(cur2.substring(4)));
					}
					else toSet=variables.get(cur2.substring(1));
					if(!memdata.get(toSet).equals("integer")&&!memdata.get(toSet).equals("float")) {
						printToConsole("Only integers and floats can be used for the result of a 'multiply' operation, not '"+memdata.get(toSet)+"'.",true,i);
						printTraceback(i);
						return;
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur2+".",true,i);
					printTraceback(i);
					return;
				}
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur0.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'multiply' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value=memory.get(setFrom);
				} else {
					if(isString(cur0)) {
						printToConsole("Only integers and floats can be used for the 'multiply' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur0)) value=Integer.parseInt(cur0);
					else if(isNumeric(cur0)) value=Double.parseDouble(cur0);
					else if(isBoolean(cur0)) value=Boolean.parseBoolean(cur0);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
				}
				cur1=cur.data.get(1);
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur1.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur1.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'multiply' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value2=memory.get(setFrom);
				} else {
					if(isString(cur1)) {
						printToConsole("Only integers and floats can be used for the 'multiply' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur1)) value2=Integer.parseInt(cur1);
					else if(isNumeric(cur1)) value2=Double.parseDouble(cur1);
					else if(isBoolean(cur1)) value2=Boolean.parseBoolean(cur1);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur1+".",true,i);
						printTraceback(i);
						return;
					}
				}
				val1=0;
				val2=0;
				try {val1=(double)value;}
				catch (Exception e) {val1=(double)(int)value;}
				try {val2=(double)value2;}
				catch (Exception e) {val2=(double)(int)value2;}
				if(memdata.get(toSet).equals("integer"))memory.set(toSet,(int)(val1*val2));
				else if(memdata.get(toSet).equals("float"))memory.set(toSet,val1*val2);
				else {
					printToConsole("Non-terminal error: Overriding existing value.",true,i);
					printTraceback(i);
					memdata.set(toSet,"float");
					memory.set(toSet,((double)value)*((double)value2));
				}
				break;
			case "divide":
				toSet=0;
				value=0;
				value2=0;
				cur2=cur.data.get(2);
				if(cur2.contains("%")) {
					if(cur2.startsWith("%tmp"))toSet=tmp.get(Integer.parseInt(cur2.substring(4)));//implement an exit condition if not an integer: try catch
					else toSet=variables.get(cur2.substring(1));
					if(!memdata.get(toSet).equals("integer")&&!memdata.get(toSet).equals("float")) {
						printToConsole("Only integers and floats can be used for the result of a 'divide' operation, not '"+memdata.get(toSet)+"'.",true,i);
						printTraceback(i);
						return;
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur2+".",true,i);
					printTraceback(i);
					return;
				}
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur0.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'divide' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value=memory.get(setFrom);
				} else {
					if(isString(cur0)) {
						printToConsole("Only integers and floats can be used for the 'divide' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur0)) value=Integer.parseInt(cur0);
					else if(isNumeric(cur0)) value=Double.parseDouble(cur0);
					else if(isBoolean(cur0)) value=Boolean.parseBoolean(cur0);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
				}
				cur1=cur.data.get(1);
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur1.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur1.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'divide' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value2=memory.get(setFrom);
				} else {
					if(isString(cur1)) {
						printToConsole("Only integers and floats can be used for the 'divide' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur1)) value2=Integer.parseInt(cur1);
					else if(isNumeric(cur1)) value2=Double.parseDouble(cur1);
					else if(isBoolean(cur1)) value2=Boolean.parseBoolean(cur1);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur1+".",true,i);
						printTraceback(i);
						return;
					}
				}
				val1=0;
				val2=0;
				try {val1=(double)value;}
				catch (Exception e) {val1=(double)(int)value;}
				try {val2=(double)value2;}
				catch (Exception e) {val2=(double)(int)value2;}
				if(val2==0.0d)memory.set(toSet,DIV_ZERO);
				else if(memdata.get(toSet).equals("integer"))memory.set(toSet,(int)(val1/val2));
				else if(memdata.get(toSet).equals("float"))memory.set(toSet,val1/val2);
				else {
					printToConsole("Non-terminal error: Overriding existing value.",true,i);
					printTraceback(i);
					memdata.set(toSet,"float");
					memory.set(toSet,((double)value)/((double)value2));
				}
				break;
			case "modulo":
				toSet=0;
				value=0;
				value2=0;
				cur2=cur.data.get(2);
				if(cur2.contains("%")) {
					if(cur2.startsWith("%tmp"))toSet=tmp.get(Integer.parseInt(cur2.substring(4)));//implement an exit condition if not an integer: try catch
					else toSet=variables.get(cur2.substring(1));
					if(!memdata.get(toSet).equals("integer")&&!memdata.get(toSet).equals("float")) {
						printToConsole("Only integers and floats can be used for the result of a 'modulo' operation, not '"+memdata.get(toSet)+"'.",true,i);
						printTraceback(i);
						return;
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur2+".",true,i);
					printTraceback(i);
					return;
				}
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur0.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'modulo' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value=memory.get(setFrom);
				} else {
					if(isString(cur0)) {
						printToConsole("Only integers and floats can be used for the 'modulo' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur0)) value=Integer.parseInt(cur0);
					else if(isNumeric(cur0)) value=Double.parseDouble(cur0);
					else if(isBoolean(cur0)) value=Boolean.parseBoolean(cur0);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
				}
				cur1=cur.data.get(1);
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur1.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur1.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'modulo' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value2=memory.get(setFrom);
				} else {
					if(isString(cur1)) {
						printToConsole("Only integers and floats can be used for the 'modulo' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur1)) value2=Integer.parseInt(cur1);
					else if(isNumeric(cur1)) value2=Double.parseDouble(cur1);
					else if(isBoolean(cur1)) value2=Boolean.parseBoolean(cur1);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur1+".",true,i);
						printTraceback(i);
						return;
					}
				}
				val1=0;
				val2=0;
				try {val1=(double)value;}
				catch (Exception e) {val1=(double)(int)value;}
				try {val2=(double)value2;}
				catch (Exception e) {val2=(double)(int)value2;}
				if(val2==0.0d)memory.set(toSet, DIV_ZERO);
				if(memdata.get(toSet).equals("integer"))memory.set(toSet,(int)(val1%val2));
				else if(memdata.get(toSet).equals("float"))memory.set(toSet,val1%val2);
				else {
					printToConsole("Non-terminal error: Overriding existing value.",true,i);
					printTraceback(i);
					memdata.set(toSet,"float");
					memory.set(toSet,((double)value)%((double)value2));
				}
				break;
			case "power":
				toSet=0;
				value=0;
				value2=0;
				cur2=cur.data.get(2);
				if(cur2.contains("%")) {
					if(cur2.startsWith("%tmp"))toSet=tmp.get(Integer.parseInt(cur2.substring(4)));//implement an exit condition if not an integer: try catch
					else toSet=variables.get(cur2.substring(1));
					if(!memdata.get(toSet).equals("integer")&&!memdata.get(toSet).equals("float")) {
						printToConsole("Only integers and floats can be used for the result of a 'power' operation, not '"+memdata.get(toSet)+"'.",true,i);
						printTraceback(i);
						return;
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur2+".",true,i);
					printTraceback(i);
					return;
				}
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur0.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'power' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value=memory.get(setFrom);
				} else {
					if(isString(cur0)) {
						printToConsole("Only integers and floats can be used for the 'power' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur0)) value=Integer.parseInt(cur0);
					else if(isNumeric(cur0)) value=Double.parseDouble(cur0);
					else if(isBoolean(cur0)) value=Boolean.parseBoolean(cur0);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur0+".",true,i);
						printTraceback(i);
						return;
					}
				}
				cur1=cur.data.get(1);
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur1.substring(4)));//implement an exit condition if not an integer: try catch
					else setFrom=variables.get(cur1.substring(1));
					if(!memdata.get(setFrom).equals("integer")&&!memdata.get(setFrom).equals("float")) {
						printToConsole("Only integers and floats can be used for the 'power' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value2=memory.get(setFrom);
				} else {
					if(isString(cur1)) {
						printToConsole("Only integers and floats can be used for the 'power' operation.",true,i);
						printTraceback(i);
						return;
					}
					else if(isInteger(cur1)) value2=Integer.parseInt(cur1);
					else if(isNumeric(cur1)) value2=Double.parseDouble(cur1);
					else if(isBoolean(cur1)) value2=Boolean.parseBoolean(cur1);
					else {
						printToConsole("An attempt to use a unparseable value of "+cur1+".",true,i);
						printTraceback(i);
						return;
					}
				}
				val1=0;
				val2=0;
				try {val1=(double)value;}
				catch (Exception e) {val1=(double)(int)value;}
				try {val2=(double)value2;}
				catch (Exception e) {val2=(double)(int)value2;}
				if(val1==0.0d&&val2==0.0d)memory.set(toSet,ZERO_TO_ZERO);
				else if(memdata.get(toSet).equals("integer"))memory.set(toSet,(int)Math.pow(val1, val2));
				else if(memdata.get(toSet).equals("float"))memory.set(toSet,Math.pow(val1, val2));
				else {
					printToConsole("Non-terminal error: Overriding existing value.",true,i);
					printTraceback(i);
					memdata.set(toSet,"float");
					memory.set(toSet,Math.pow(((double)value),((double)value2)));
				}
				break;
			case "not":
				toSet=0;
				value=false;
				value2=false;
				cur2=cur.data.get(2);
				if(cur2.contains("%")) {
					if(cur2.startsWith("%tmp"))toSet=tmp.get(Integer.parseInt(cur2.substring(4)));//implement an exit condition if not a boolean: try catch
					else toSet=variables.get(cur2.substring(1));
					if(!memdata.get(toSet).equals("boolean")) {
						printToConsole("Only booleans can be used for the result of a 'not' operation, not '"+memdata.get(toSet)+"'.",true,i);
						printTraceback(i);
						return;
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur2+".",true,i);
					printTraceback(i);
					return;
				}
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not a boolean: try catch
					else setFrom=variables.get(cur0.substring(1));
					if(!memdata.get(setFrom).equals("boolean")) {
						printToConsole("Only booleans can be used for the 'not' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value=memory.get(setFrom);
				} else {
					value=Boolean.parseBoolean(cur0);//implement an exit condition if not a boolean: try catch
				}
				memory.set(toSet,!((boolean)value));
				break;
			case "and":
				toSet=0;
				value=false;
				value2=false;
				cur2=cur.data.get(2);
				if(cur2.contains("%")) {
					if(cur2.startsWith("%tmp"))toSet=tmp.get(Integer.parseInt(cur2.substring(4)));//implement an exit condition if not a boolean: try catch
					else toSet=variables.get(cur2.substring(1));
					if(!memdata.get(toSet).equals("boolean")) {
						printToConsole("Only booleans can be used for the result of a 'and' operation, not '"+memdata.get(toSet)+"'.",true,i);
						printTraceback(i);
						return;
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur2+".",true,i);
					printTraceback(i);
					return;
				}
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not a boolean: try catch
					else setFrom=variables.get(cur0.substring(1));
					if(!memdata.get(setFrom).equals("boolean")) {
						printToConsole("Only booleans can be used for the 'and' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value=memory.get(setFrom);
				} else {
					value=Boolean.parseBoolean(cur0);//implement an exit condition if not a boolean: try catch
				}
				cur1=cur.data.get(1);
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur1.substring(4)));//implement an exit condition if not a boolean: try catch
					else setFrom=variables.get(cur1.substring(1));
					if(!memdata.get(setFrom).equals("boolean")) {
						printToConsole("Only booleans can be used for the 'and' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value2=memory.get(setFrom);
				} else {
					value2=Boolean.parseBoolean(cur1);//implement an exit condition if not a boolean: try catch
				}
				memory.set(toSet,((boolean)value)&&((boolean)value2));
				break;
			case "or":
				toSet=0;
				value=false;
				value2=false;
				cur2=cur.data.get(2);
				if(cur2.contains("%")) {
					if(cur2.startsWith("%tmp"))toSet=tmp.get(Integer.parseInt(cur2.substring(4)));//implement an exit condition if not a boolean: try catch
					else toSet=variables.get(cur2.substring(1));
					if(!memdata.get(toSet).equals("boolean")) {
						printToConsole("Only booleans can be used for the result of a 'or' operation, not '"+memdata.get(toSet)+"'.",true,i);
						printTraceback(i);
						return;
					}
				} else {
					printToConsole("An attempt to set an internal value of "+cur2+".",true,i);
					printTraceback(i);
					return;
				}
				cur0=cur.data.get(0);
				if(cur0.contains("%")) {
					int setFrom=0;
					if(cur0.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur0.substring(4)));//implement an exit condition if not a boolean: try catch
					else setFrom=variables.get(cur0.substring(1));
					if(!memdata.get(setFrom).equals("boolean")) {
						printToConsole("Only booleans can be used for the 'or' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value=memory.get(setFrom);
				} else {
					value=Boolean.parseBoolean(cur0);//implement an exit condition if not a boolean: try catch
				}
				cur1=cur.data.get(1);
				if(cur1.contains("%")) {
					int setFrom=0;
					if(cur1.startsWith("%tmp"))setFrom=tmp.get(Integer.parseInt(cur1.substring(4)));//implement an exit condition if not a boolean: try catch
					else setFrom=variables.get(cur1.substring(1));
					if(!memdata.get(setFrom).equals("boolean")) {
						printToConsole("Only booleans can be used for the 'or' operation, not '"+memdata.get(setFrom)+"'.",true,i);
						printTraceback(i);
						return;
					} value2=memory.get(setFrom);
				} else {
					value2=Boolean.parseBoolean(cur1);//implement an exit condition if not a boolean: try catch
				}
				memory.set(toSet,((boolean)value)||((boolean)value2));
				break;
			case "print"://instruction specific to this executor, not all executors; used for implementation of the print method within this executor
				printToConsole((String)cur.data.get(0),false,i);//implement an exit condition if cannot be casted to string: try catch
				break;
			case "exit"://instruction specific to this executor, not all executors; used for implementation of the exit method within this executor
				try {
					printToConsole((String)cur.data.get(0),false,i);
				} catch (Exception e) {
					printToConsole("The program has exitted for an unknown reason.",true,i);
					printTraceback(i);
				}
				return;
			default:
				printToConsole("The program has encountered an unknown instruction, exitting...",true,i);
				printTraceback(i);
				return;
		}
	}

	private void printToConsole(String toPrint, boolean error, int i) {//function specific to this executor, not all executors; used for implementation of the print and exit method within this executor
		int tmpid=-1;
		if(toPrint.startsWith("%")) {
			int toSet=-1;
			if(toPrint.startsWith("%tmp")) {
				try {
					tmpid=Integer.parseInt(toPrint.substring(4));
				} catch (Exception e) {
					printToConsole("A tmp variable has been used with an invalid identifier: "+toPrint+".",true,i);
					printTraceback(i);
					return;
				}
				try {
					if(tmpid>-1) toSet=tmp.get(tmpid);
					else {
						printToConsole("A tmp variable has been used with an invalid identifier: "+toPrint+".",true,i);
						printTraceback(i);
						return;
					}
				}  catch (Exception e) {
					printToConsole("You cannot print a tmp variable that has not been set yet: "+toPrint+".",true,i);
					printTraceback(i);
					return;
				}
			} else {
				try {
					toSet=variables.get(toPrint.substring(1));
				} catch (Exception e) {if(toPrint.length()>1)
					printToConsole("An attempt to print a non-existent variable.",true,i);
					printTraceback(i);
					return;
				}
			}
			toPrint=memory.get(toSet).toString();
		} else if(!error) {
			if(isString(toPrint)) toPrint=removeQuotes(toPrint);
			else if(isInteger(toPrint)) toPrint=Integer.toString(Integer.parseInt(toPrint));
			else if(isNumeric(toPrint)) toPrint=Double.toString(Double.parseDouble(toPrint));
			else if(isBoolean(toPrint)) toPrint=Boolean.toString(Boolean.parseBoolean(toPrint));
			else {
				printToConsole("An attempt to print a unparseable value of "+toPrint+".",true,i);
				printTraceback(i);
				return;
			}
		}
		if(error)toPrint="ERROR: "+toPrint;
		MainFrame.consolePanel.giveLine(toPrint);
	}
	
	private void printTraceback(int i) {
		printToConsole("Error occured on line 1, instruction "+i+".",true,i);
		System.out.println("");
		System.out.println(memory);
		System.out.println(memdata);
		System.out.println(variables);
		System.out.println(tmp);
		System.out.println(i);
		System.out.println(instructions[i].type);
		System.out.println(instructions[i].data);
		//Integer.parseInt("pp");//to throw a java error if needed to find the location in java
	}
	
	private void initValues() {
		memory.add(0); //skip register
		memdata.add("integer");
		
		memory.add(null);
		memdata.add("object");
		variables.put("null",1);
		
		memory.add(false);
		memdata.add("boolean");
		variables.put("false",2);
		
		memory.add(true);
		memdata.add("boolean");
		variables.put("true",3);
	}
	
	private boolean isBoolean(String strBool) {
	    try {
	        @SuppressWarnings("unused")
			boolean i = Boolean.parseBoolean(strBool);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	private boolean isInteger(String strNum) {
	    try {
	        @SuppressWarnings("unused")
			int i = Integer.parseInt(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	private boolean isNumeric(String strNum) {
	    try {
	        @SuppressWarnings("unused")
			double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	private boolean isString(String str) {
		int count = 0;
		for(char c : str.toCharArray()) {
			if(c == '"') count++;
			if(count == 2) return true;
		}
		return false;
	}
	
	private String removeQuotes(String str) {
		String st = "";
		int count = 0;
		for(char c : str.toCharArray()) {
			if(c == '"') count++;
			else st+=c;
			if(count == 2) return st;
		}
		return str;
	}
}
