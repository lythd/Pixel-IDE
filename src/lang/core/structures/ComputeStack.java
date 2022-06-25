package lang.core.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lang.core.general.CodeObjectGeneral;

public class ComputeStack extends CodeObjectGeneral {

	public ArrayList<String> tokens;
	public ArrayList<StructureObject> structures;
	private int operationssize;
	
	public ComputeStack(ArrayList<String> tokens_, int end) {
		super();
		String[] ops = {"+","-","*","/","^","%","!","&","|"};
		List<String> opss = Arrays.asList(ops);
		tokens = tokens_;
		ArrayList<String> operations = new ArrayList<String>();
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		structures = new ArrayList<StructureObject>();
		ArrayList<ComputeStack> csbrackets = new ArrayList<ComputeStack>();
		boolean bracket = false;
		int starti = -1, encountered = 0;
		int opcount = 1, lastopsize = 0;
		for(int i = 0; i < tokens.size(); i++) {
			String t = tokens.get(i);
			if(t.equals("(")) {
				if(bracket) encountered += 1;
				else {
					encountered = 0;
					starti = i+1;
					bracket = true;
				}
			}
			else if(t.equals(")")) {
				if(starti > -1) {
					if(encountered > 0) encountered -= 1;
					else {
						bracket = false;
						opcount += operations.size() - lastopsize;
						lastopsize = operations.size();
						ComputeStack csbracket = new ComputeStack(splice(tokens,starti,i),opcount+end); //fix this by doing this at the end and calculating using operations.size()
						csbrackets.add(csbracket);
						tokens.set(starti-1,"%tmp"+(opcount+end));
						tokens.set(i,"%tmp"+(opcount+end));
						opcount += csbracket.operationssize;
					}
				} else {
					//throw error or smthn
				}
			}
			if(!bracket) {
				if(lang.core.general.StructureParser.isVar(t)) tokens.set(i,"%" + t);
				if(opss.contains(t)) {
					operations.add(t);
					indexes.add(i);
				}
//				if(t.equals("+")) {
//					operations.add(t);
//					indexes.add(i);
//				}
//				else if(t.equals("-")) {
//					operations.add(t);
//					indexes.add(i);
//				}
//				else if(t.equals("*")) {
//					operations.add(t);
//					indexes.add(i);
//				}
//				else if(t.equals("/")) {
//					operations.add(t);
//					indexes.add(i);
//				}
//				else if(t.equals("^")) {
//					operations.add(t);
//					indexes.add(i);
//				}
//				else if(t.equals("%")) {
//					operations.add(t);
//					indexes.add(i);
//				}
//				else if(t.equals("!")) {
//					operations.add(t);
//					indexes.add(i);
//				}
//				else if(t.equals("&")) {
//					operations.add(t);
//					indexes.add(i);
//				}
//				else if(t.equals("|")) {
//					operations.add(t);
//					indexes.add(i);
//				}
			}
		}
		if(operations.size() == 0) {
			ArrayList<String> tkns = new ArrayList<String>();
			tkns.add("%tmp1");
			tkns.add(tokens.get(0));
			structures.add(new StructureObject(tkns, "set"));
		} else {
			int tmps = operations.size() + end - 1;
			for(int i = 0; i < operations.size(); i++) {
				String o = operations.get(i);
				if(o.equals("!")) {
					ArrayList<String> tkns = new ArrayList<String>();
					tkns.add(tokens.get(indexes.get(i)+1));
					tkns.add("!");
					tkns.add("null");
					tkns.add("" + tmps);
					int left = 0;
					int right = 0;
					for(int j = 0; j < tokens.size(); j++) {
						if (j == indexes.get(i)) {
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)+1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								right = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
					}
					if(left > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+left)) tokens.set(j,"%tmp" + tmps);
					if(right > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+right)) tokens.set(j,"%tmp" + tmps);
					tmps--;
					structures.add(new StructureObject(tkns, "compute"));
				}
			}
			for(int i = 0; i < operations.size(); i++) {
				String o = operations.get(i);
				if(o.equals("^")) {
					ArrayList<String> tkns = new ArrayList<String>();
					tkns.add(tokens.get(indexes.get(i)-1));
					tkns.add("^");
					tkns.add(tokens.get(indexes.get(i)+1));
					tkns.add("" + tmps);
					int left = 0;
					int right = 0;
					for(int j = 0; j < tokens.size(); j++) {
						if(j == indexes.get(i)-1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								left = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)) {
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)+1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								right = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
					}
					if(left > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+left)) tokens.set(j,"%tmp" + tmps);
					if(right > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+right)) tokens.set(j,"%tmp" + tmps);
					tmps--;
					structures.add(new StructureObject(tkns, "compute"));
				}
			}
			for(int i = 0; i < operations.size(); i++) {
				String o = operations.get(i);
				if(o.equals("&")) {
					ArrayList<String> tkns = new ArrayList<String>();
					tkns.add(tokens.get(indexes.get(i)-1));
					tkns.add("&");
					tkns.add(tokens.get(indexes.get(i)+1));
					tkns.add("" + tmps);
					int left = 0;
					int right = 0;
					for(int j = 0; j < tokens.size(); j++) {
						if(j == indexes.get(i)-1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								left = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)) {
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)+1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								right = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
					}
					if(left > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+left)) tokens.set(j,"%tmp" + tmps);
					if(right > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+right)) tokens.set(j,"%tmp" + tmps);
					tmps--;
					structures.add(new StructureObject(tkns, "compute"));
				}
			}
			for(int i = 0; i < operations.size(); i++) {
				String o = operations.get(i);
				if(o.equals("*")) {
					ArrayList<String> tkns = new ArrayList<String>();
					tkns.add(tokens.get(indexes.get(i)-1));
					tkns.add("*");
					tkns.add(tokens.get(indexes.get(i)+1));
					tkns.add("" + tmps);
					int left = 0;
					int right = 0;
					for(int j = 0; j < tokens.size(); j++) {
						if(j == indexes.get(i)-1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								left = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)) {
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)+1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								right = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
					}
					if(left > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+left)) tokens.set(j,"%tmp" + tmps);
					if(right > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+right)) tokens.set(j,"%tmp" + tmps);
					tmps--;
					structures.add(new StructureObject(tkns, "compute"));
				}
				else if(o.equals("/")) {
					ArrayList<String> tkns = new ArrayList<String>();
					tkns.add(tokens.get(indexes.get(i)-1));
					tkns.add("/");
					tkns.add(tokens.get(indexes.get(i)+1));
					tkns.add("" + tmps);
					int left = 0;
					int right = 0;
					for(int j = 0; j < tokens.size(); j++) {
						if(j == indexes.get(i)-1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								left = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)) {
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)+1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								right = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
					}
					if(left > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+left)) tokens.set(j,"%tmp" + tmps);
					if(right > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+right)) tokens.set(j,"%tmp" + tmps);
					tmps--;
					structures.add(new StructureObject(tkns, "compute"));
				}
				else if(o.equals("%")) {
					ArrayList<String> tkns = new ArrayList<String>();
					tkns.add(tokens.get(indexes.get(i)-1));
					tkns.add("%");
					tkns.add(tokens.get(indexes.get(i)+1));
					tkns.add("" + tmps);
					int left = 0;
					int right = 0;
					for(int j = 0; j < tokens.size(); j++) {
						if(j == indexes.get(i)-1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								left = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)) {
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)+1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								right = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
					}
					if(left > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+left)) tokens.set(j,"%tmp" + tmps);
					if(right > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+right)) tokens.set(j,"%tmp" + tmps);
					tmps--;
					structures.add(new StructureObject(tkns, "compute"));
				}
			}
			for(int i = 0; i < operations.size(); i++) {
				String o = operations.get(i);
				if(o.equals("|")) {
					ArrayList<String> tkns = new ArrayList<String>();
					tkns.add(tokens.get(indexes.get(i)-1));
					tkns.add("|");
					tkns.add(tokens.get(indexes.get(i)+1));
					tkns.add("" + tmps);
					int left = 0;
					int right = 0;
					for(int j = 0; j < tokens.size(); j++) {
						if(j == indexes.get(i)-1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								left = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)) {
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)+1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								right = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
					}
					if(left > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+left)) tokens.set(j,"%tmp" + tmps);
					if(right > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+right)) tokens.set(j,"%tmp" + tmps);
					tmps--;
					structures.add(new StructureObject(tkns, "compute"));
				}
			}
			for(int i = 0; i < operations.size(); i++) {
				String o = operations.get(i);
				if(o.equals("+")) {
					ArrayList<String> tkns = new ArrayList<String>();
					tkns.add(tokens.get(indexes.get(i)-1));
					tkns.add("+");
					tkns.add(tokens.get(indexes.get(i)+1));
					tkns.add("" + tmps);
					int left = 0;
					int right = 0;
					for(int j = 0; j < tokens.size(); j++) {
						if(j == indexes.get(i)-1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								left = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)) {
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)+1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								right = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
					}
					if(left > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+left)) tokens.set(j,"%tmp" + tmps);
					if(right > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+right)) tokens.set(j,"%tmp" + tmps);
					tmps--;
					structures.add(new StructureObject(tkns, "compute"));
				}
				else if(o.equals("-")) {
					ArrayList<String> tkns = new ArrayList<String>();
					tkns.add(tokens.get(indexes.get(i)-1));
					tkns.add("-");
					tkns.add(tokens.get(indexes.get(i)+1));
					tkns.add("" + tmps);
					int left = 0;
					int right = 0;
					for(int j = 0; j < tokens.size(); j++) {
						if(j == indexes.get(i)-1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								left = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)) {
							tokens.set(j,"%tmp" + tmps);
						}
						else if (j == indexes.get(i)+1) {
							if(tokens.get(j).length() > 3 && tokens.get(j).substring(0,4).equals("%tmp")) {
								right = Integer.parseInt(tokens.get(j).substring(4));
							}
							tokens.set(j,"%tmp" + tmps);
						}
					}
					if(left > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+left)) tokens.set(j,"%tmp" + tmps);
					if(right > 0) for(int j = 0; j < tokens.size(); j++) if(tokens.get(j).equals("%tmp"+right)) tokens.set(j,"%tmp" + tmps);
					tmps--;
					structures.add(new StructureObject(tkns, "compute"));
				}
			}
			tokens = tokens_;
		}
		if(csbrackets != null) {
			ArrayList<StructureObject> structures_ = new ArrayList<StructureObject>();
			for(ComputeStack cs : csbrackets) for(StructureObject s : cs.structures) structures_.add(s);
			for(StructureObject s : structures) structures_.add(s);
			structures = structures_;
		}
		operationssize = operations.size();
	}
	
	public static int newlength(ArrayList<String> tokens_, int end) {
		ArrayList<String> tokens = tokens_;
		ArrayList<String> operations = new ArrayList<String>();
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < tokens.size(); i++) {
			String t = tokens.get(i);
			if(lang.core.general.StructureParser.isVar(t)) tokens.set(i,"%" + t);
			if(t.equals("+")) {
				operations.add(t);
				indexes.add(i);
			}
			else if(t.equals("-")) {
				operations.add(t);
				indexes.add(i);
			}
			else if(t.equals("*")) {
				operations.add(t);
				indexes.add(i);
			}
			else if(t.equals("/")) {
				operations.add(t);
				indexes.add(i);
			}
			else if(t.equals("^")) {
				operations.add(t);
				indexes.add(i);
			}
			else if(t.equals("%")) {
				operations.add(t);
				indexes.add(i);
			}
			else if(t.equals("!")) {
				operations.add(t);
				indexes.add(i);
			}
			else if(t.equals("&")) {
				operations.add(t);
				indexes.add(i);
			}
			else if(t.equals("|")) {
				operations.add(t);
				indexes.add(i);
			}
		}
		return operations.size() + end;
	}
	
	private ArrayList<String> splice(ArrayList<String> list_, int start, int end) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = start; i < end; i++) {
			list.add(list_.get(i));
		}
		return list;
	}
 }
