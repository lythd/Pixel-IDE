package lang.core.general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import lang.core.structures.ArgumentStack;
import lang.core.structures.ComputeStack;
import lang.core.structures.StructureObject;
import tree.TreePanel;

public class StructureParser {

	//structures are init (name, datatype/object), set (name, value), codeblock, compute (name1, operation, name2, tmp num), else, enum, for, 
	//foreach, if, init, while
	//% indicates a variable (tmp included) that must be resolved to a value, before the structure can perform its designated action.
	//tmp(num) is a variable that represents a place storing a temporary value for a compute operation
	//locals is a list of length 3 arrays: name, type and location (path.class.function)
	//globals is a list of length 2 arrays: name and type
	//globals is a list of length 2+ arrays: name, location, and the type for each argument
	
	public static ArrayList<String> Primitives;
	public static ArrayList<String> Objects;
	public static ArrayList<String[]> Globals;
	public static ArrayList<String[]> Locals;
	public static ArrayList<String[]> Functions;
	
	public static void init() {
		Primitives = new ArrayList<String>();
		Objects = new ArrayList<String>();
		Globals = new ArrayList<String[]>();
		Locals = new ArrayList<String[]>();
		Functions = new ArrayList<String[]>();
		Objects.add("String");
		Objects.add("Object");
		Primitives.add("int");
		Primitives.add("bool");
		Primitives.add("float");
		Globals.add(new String[] {"false","bool"});
		Globals.add(new String[] {"true","bool"});
		Globals.add(new String[] {"null","Object"});
		Functions.add(new String[] {"print","executor.print","void","int"});
		Functions.add(new String[] {"exit","executor.exit","void","int"});
		//Functions.add(new String[] {"root","lang.core.math.root","float","int","int"});
		//Functions.add(new String[] {"importmath","lang.core.math.import","void"});
		//Functions.add(new String[] {"multiply","lang.core.math.multiply","int","int","int"});
		Functions.add(new String[] {"rect","lang.core.canvas.rect","void","int","int","int","int"}); //test function not actually going to exist
	}
	
	public static ArrayList<StructureObject> parse(ArrayList<String> tokens_) {
		init();
		ArrayList<StructureObject> structures = new ArrayList<StructureObject>();
		String token1 = "";
		String token2 = "";
		String token3 = "";
		ArrayList<String> compute = new ArrayList<String>();
		boolean computeStack = false;
		ArrayList<String> argument = new ArrayList<String>();
		boolean argumentStack = false;
		for(String token : tokens_) {
			if(!argumentStack && token.equals("=")) {
				computeStack = true;
				if((Objects.contains(token2) || Primitives.contains(token2)) && !isVar(token1) && 
						!(token1.length() > 3 && token1.substring(0,3).equals("tmp") && isInteger(token1.substring(3)))) {
					ArrayList<String> tokens = new ArrayList<String>();
					tokens.add("%" + token1);
					tokens.add(token2);
					structures.add(new StructureObject(tokens,"init"));
					Locals.add(new String[] {token1,token2,"???"}); //need to add location
				}
			}
			if(!computeStack && token.equals("(")) argumentStack = true;
			if(computeStack) for(String[] sa : Functions) if(token.equals(sa[0])) {
				argumentStack = true;
				token1 = token;
			}
			if(token.equals(")")) {
				boolean found = false;
				for(String[] s : Functions) if(s[0].equals(token2)) {found=true;break;}
				//resolve argument stack
				ArgumentStack as = null;
				if(argumentStack) {
					argumentStack = false;
					if(argument.size() > 0) {
						as = new ArgumentStack(argument,found,0);
					}
				} else {
					//throw error (red highlight)
				}
				if(found) {
					if(as != null && as.structures != null && as.structures.size() > 0) 
						for(StructureObject o : as.structures) 
							structures.add(o);
					
					ArrayList<String>tokens = new ArrayList<String>();
					tokens.add(token2);
					if(as==null) {
						for(String[] sa : Functions) if(token2.equals(sa[0])) {
							if(sa[2].equals("void")) tokens.add("%null"); //won't actually set to null, just represents that it won't have a return
						}
						structures.add(new StructureObject(tokens,"func"));
					} else {
						int length = as.length;
						int start = 1;
						for(String[] sa : Functions) if(token2.equals(sa[0])) {
							if(sa[2].equals("void")) tokens.add("%null"); //won't actually set to null, just represents that it won't have a return
							else start=0;
						}
						for(int i = start; i <= length; i++) tokens.add("%tmp" + i);
						structures.add(new StructureObject(tokens,"func"));
						if(start==0) {
							tokens = new ArrayList<String>();
							tokens.add("%" + token3);
							tokens.add("%tmp0");
							structures.add(new StructureObject(tokens,"set"));
						}
					}
				}
			}
			else if(token.equals(";")) {
				if(token2.equals("import")) {
					String importfunc = "";
					try {
						//System.out.println(System.getProperty("user.dir"));
						String path = System.getProperty("user.dir")+TreePanel.sep+"src"+TreePanel.sep;
						for(String s : token1.split("\\.")) path+=s+TreePanel.sep;
						path+="function.pxs";
//						System.out.println(path);
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
						String line;
						while ((line = br.readLine()) != null) {
							String[] splits = line.split(",");
							if(splits[0].contains("import-"))importfunc=splits[0];
							Functions.add(line.split(","));
						}
					} catch (Exception e) {
						System.out.println("ERROR: IMPORT FILE NOT FOUND");
						//do something when cant file the import file
					}
					if(importfunc.length()>0) {
						ArrayList<String> tokens = new ArrayList<String>();
						tokens.add(importfunc);
						structures.add(new StructureObject(tokens,"func"));
					}
					continue;
				}
				if(token3.equals("import")) {
					ArrayList<String> tokens = new ArrayList<String>();
					tokens.add(token1);
					//do something with token 2 nerd
					structures.add(new StructureObject(tokens,"import"));
					continue;
				}
				//resolve compute stack
				ComputeStack cs = null;
				if(computeStack) {
					computeStack = false;
					if(compute.size() > 0) {
						cs = new ComputeStack(compute,1);
					}
				}

				//all initialisers
				if((Objects.contains(token3) || Primitives.contains(token3)) && !isVar(token2) && 
						!(token2.length() > 3 && token2.substring(0,3).equals("tmp") && isInteger(token2.substring(3))) && token1.equals("=")) {
					
					
					
					if(cs != null && cs.structures != null && cs.structures.size() > 0) for(StructureObject o : cs.structures) structures.add(o);
					
					ArrayList<String> tokens = new ArrayList<String>();
					tokens.add("%" + token2);
					tokens.add("%tmp1");
					structures.add(new StructureObject(tokens,"set"));

					token3 = "";
					token2 = "";
					token1 = "";
				}
				//sets
				if(isVar(token2) && token1.equals("=")) {
					
					if(cs != null && cs.structures != null && cs.structures.size() > 0) for(StructureObject o : cs.structures) structures.add(o);
					
					ArrayList<String> tokens = new ArrayList<String>();					
					tokens = new ArrayList<String>();
					tokens.add("%" + token2);
					tokens.add("%tmp1");
					structures.add(new StructureObject(tokens,"set"));

					token3 = "";
					token2 = "";
					token1 = "";
				}
				if(token1.equals("=") && token2.equals("+") && isVar(token3)) {
					
					if(cs != null && cs.structures != null && cs.structures.size() > 0) 
						for(StructureObject o : cs.structures) 
							structures.add(o);
					
					ArrayList<String> tokens = new ArrayList<String>();
					tokens.add("%" + token3);
					tokens.add("+");
					tokens.add("%tmp1");
					tokens.add("0");
					structures.add(new StructureObject(tokens,"compute"));
					
					tokens = new ArrayList<String>();
					tokens.add("%" + token3);
					tokens.add("%tmp0");
					structures.add(new StructureObject(tokens,"set"));

					token3 = "";
					token2 = "";
					token1 = "";
				}
				compute = new ArrayList<String>();
				argument = new ArrayList<String>();
			}
			else {
				boolean stacked = false;
				if(computeStack && !argumentStack && !token.equals("=") && (stacked=true)) compute.add(token);
				if(argumentStack && !token.equals("(") && (stacked=true)) argument.add(token);
				if(!stacked) {
					token3 = token2;
					token2 = token1;
					token1 = token;
				}
			}
		}
		ArrayList<String>tokens = new ArrayList<String>();
		tokens.add("exit");
		tokens.add("%null");
		tokens.add("\"Program has reached the end as expected.\"");
		structures.add(new StructureObject(tokens,"func"));
		return structures;
	}
	
	public static boolean isInteger(String strNum) {
	    try {
	        @SuppressWarnings("unused")
			int i = Integer.parseInt(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static boolean isNumeric(String strNum) {
	    try {
	        @SuppressWarnings("unused")
			double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static boolean isVar(String name) {
		for(String[] sa : Globals) if(sa[0].equals(name)) return true;
		for(String[] sa : Locals) if(sa[0].equals(name)) return true;
		return false;
	}
	
	public static boolean isString(String str) {
		int count = 0;
		for(char c : str.toCharArray()) {
			if(c == '"') count++;
			if(count == 2) return true;
		}
		return false;
	}
	
}
