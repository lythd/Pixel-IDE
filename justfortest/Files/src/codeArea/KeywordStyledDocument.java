package codeArea;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import data.MainFrame;

public class KeywordStyledDocument extends DefaultStyledDocument  {
	private static final long serialVersionUID = 1L;
	private Style _defaultStyle;
	private Style _cwStyle;
	private MainFrame _mf;
	public static List<String> objectNames = new ArrayList<String>();
	public static List<String> globalNames = new ArrayList<String>();
	public static List<String> localNames = new ArrayList<String>();
	
	public KeywordStyledDocument(Style defaultStyle, Style cwStyle, MainFrame mf) {
		_defaultStyle =  defaultStyle;
		_cwStyle = cwStyle;
		_mf = mf;
		objectNames.add("Block");
		globalNames.add("block");
		localNames.add("b");
	}
	
	public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offset, str, a);
		refreshDocument();
	}
	
	public void remove (int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		refreshDocument();
	}
	
	public void manRefresh() throws BadLocationException {
		refreshDocument();
	}
	
	private synchronized void refreshDocument() throws BadLocationException {
		String text = getText(0, getLength());
		final List<HiliteWord> localList = processLocalWords(text);
		final List<HiliteWord> globalList = processGlobalWords(text);
		final List<HiliteWord> objectList = processObjectWords(text);
		final List<HiliteWord> list = processWords(text);
		final List<HiliteWord> punctuationList = processPunctuation(text);
		final List<HiliteWord> speechList = processSpeech(text);
		final List<HiliteWord> commentList = processCommentWords(text);
		processLines(localList);
		processLines(globalList);
		processLines(objectList);
		processLines(list);
		processLines(punctuationList);
		processLines(speechList);
		processLines(commentList);

		StyleConstants.setForeground(_defaultStyle, _mf.defaultText);
		setCharacterAttributes(0, text.length(), _defaultStyle, true);  
		
		if(_mf.pixelPanel.highlight) {
			StyleConstants.setForeground(_cwStyle, _mf.localText);
			for(HiliteWord word : localList) {
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
			}
			StyleConstants.setForeground(_cwStyle, _mf.globalText);
			for(HiliteWord word : globalList) {
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
			}
			StyleConstants.setForeground(_cwStyle, _mf.staticText);
			for(HiliteWord word : objectList) {
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
			}
			StyleConstants.setForeground(_cwStyle, _mf.specialText);
			for(HiliteWord word : list) {
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
			}
			
			StyleConstants.setForeground(_cwStyle, _mf.specialText);
			for(HiliteWord word : list) {
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
			}
			
			StyleConstants.setForeground(_cwStyle, _mf.punctuationText);
			for(HiliteWord word : punctuationList) {
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
			}
			
			StyleConstants.setForeground(_cwStyle, _mf.speechText);
			for(HiliteWord word : speechList) {
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
			}
			
			StyleConstants.setForeground(_cwStyle, _mf.commentText);
			for(HiliteWord word : commentList) {
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
			}
		}
	}       
	
	private static  List<HiliteWord> processLocalWords(String content) {
		content += " ";
		List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();
		
		for(int index=0; index < data.length; index++) {
			char ch = data[index];
			if(!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '/' || ch == '\n')) {
				lastWhitespacePosition = index;
				if(word.length() > 0) {
					if(isLocalName(word)) {
						hiliteWords.add(new HiliteWord(word,(lastWhitespacePosition - word.length())));
					}
					word = "";
				}
			}
			else {
				word += ch;
			}
		}
		return hiliteWords;
	}      
	
	private static  List<HiliteWord> processGlobalWords(String content) {
		content += " ";
		List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();
		
		for(int index=0; index < data.length; index++) {
			char ch = data[index];
			if(!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '/' || ch == '\n')) {
				lastWhitespacePosition = index;
				if(word.length() > 0) {
					if(isGlobalName(word)) {
						hiliteWords.add(new HiliteWord(word,(lastWhitespacePosition - word.length())));
					}
					word = "";
				}
			}
			else {
				word += ch;
			}
		}
		return hiliteWords;
	}       
	
	private static  List<HiliteWord> processObjectWords(String content) {
		content += " ";
		List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();
		
		for(int index=0; index < data.length; index++) {
			char ch = data[index];
			if(!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '/' || ch == '\n')) {
				lastWhitespacePosition = index;
				if(word.length() > 0) {
					if(isObjectName(word)) {
						hiliteWords.add(new HiliteWord(word,(lastWhitespacePosition - word.length())));
					}
					word = "";
				}
			}
			else {
				word += ch;
			}
		}
		return hiliteWords;
	}       
	
	private static  List<HiliteWord> processWords(String content) {
		content += " ";
		List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();
		
		for(int index=0; index < data.length; index++) {
			char ch = data[index];
			if(!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '/' || ch == '\n')) {
				lastWhitespacePosition = index;
				if(word.length() > 0) {
					if(isReservedWord(word)) {
						hiliteWords.add(new HiliteWord(word,(lastWhitespacePosition - word.length())));
					}
					word = "";
				}
			}
			else {
				word += ch;
			}
		}
		return hiliteWords;
	}
	
	private static  List<HiliteWord> processPunctuation(String content) {
		content += " ";
		List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
		char[] data = content.toCharArray();
		
		for(int index=0; index < data.length; index++) {
			char ch = data[index];
			if(!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '/' || ch == '\n')) {
				if(ch == '.' || ch == ',' || ch == '+' || ch == '!' || ch == '%' || ch == '^' || ch == '*' || ch == '=' || ch == '-' || ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}') {
					hiliteWords.add(new HiliteWord(Character.toString(ch),(index)));
				}
			}
		}
		return hiliteWords;
	}
	
	private static  List<HiliteWord> processSpeech(String content) {
		content += " ";
		List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();
		boolean commented = false;
		boolean changed = false;
		boolean lastQuote = false;
		
		for(int index = 0; index < data.length; index++) {
			char ch = data[index];
			if(Character.toString(ch).equals("\"") && !commented) changed = true;
			if(Character.toString(ch).equals("\"") && commented) word += ch;
			if(!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '/' || ch == '\n')) {
				lastWhitespacePosition = index;
				if(word.length() > 0 && commented && !(Character.toString(ch).equals("\"") && commented)) hiliteWords.add(new HiliteWord(word,(lastWhitespacePosition - word.length())));
				if(word.length() > 0 && commented && (Character.toString(ch).equals("\"") && commented)) hiliteWords.add(new HiliteWord(word,(lastWhitespacePosition - word.length() + 1)));
				word = "";
			}
			if(!Character.isWhitespace(ch)){
				word += ch;
				if(ch == '"' && changed) word = "\"";
			}
			if(lastQuote) commented = false;
			lastQuote = false;
			if(Character.toString(ch).equals("\"") && commented && !changed) lastQuote = true;
			if(changed) commented = true;
			changed = false;
		}
		return hiliteWords;
	}
	
	private static  List<HiliteWord> processCommentWords(String content) {
		content += " ";
		List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();
		boolean commented = false;
		
		for(int index=0; index < data.length; index++) {
			char ch = data[index];
			if(!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '/' || ch == '\n')) {
				lastWhitespacePosition = index;
				if(word.length() > 0) {
					if(word.contains("//") || word.contains("#")) {
						commented = true;
					}
					if(commented) {
						hiliteWords.add(new HiliteWord(word,(lastWhitespacePosition - word.length())));
					}
					word = "";
				}
			}
			else {
				word += ch;
			}
			if(ch == '\n') commented = false;
		}
		return hiliteWords;
	}
	
	private static List<HiliteWord> processLines(List<HiliteWord> hw) {
		List<HiliteWord> hwr = new ArrayList<HiliteWord>();
		for(HiliteWord w : hw) hwr.add(w);
		return hwr;
	}
	
//	private static List<HiliteWord> processWord(HiliteWord hw) {
//		List<HiliteWord> hwr = new ArrayList<HiliteWord>();
//		if(hw._word.contains("\n")) {
//			int in = hw._word.indexOf("\n");
//			hwr.add(new HiliteWord(hw._word.substring(0,in),hw._position));
//			for(HiliteWord w : processWord(new HiliteWord(hw._word.substring(in+1),hw._position+in+1))) hwr.add(w);
//		} else hwr.add(hw);
//		return hwr;
//	}
	
	public static final boolean isReservedWord(String word) {
		return(word.trim().equals("abstract") || // abstract
				word.trim().equals("and") || // &&
				word.trim().equals("boolean") || // boolean
				word.trim().equals("class") || // class
				word.trim().equals("else") || // else
				word.trim().equals("enum") || // enum (aka type)
				word.trim().equals("extends") || // extends
				word.trim().equals("false") || // false
				word.trim().equals("float") || // float
				word.trim().equals("for") || // for
				word.trim().equals("global") || // (makes a global variable even if it is defined inside a code block)
				word.trim().equals("import") || // import
				word.trim().equals("if") || // if
				word.trim().equals("implements") || // implements
				word.trim().equals("in") || // :
				word.trim().equals("inst") || // (not static, default)
				word.trim().equals("instance") || // (not static, default)
				word.trim().equals("int") || // int
				word.trim().equals("integer") || // int
				word.trim().equals("interface") || // (creates an interface class)
				word.trim().equals("is") || // ==
				word.trim().equals("isol") || // static
				word.trim().equals("isolated") || // static
				word.trim().equals("local") || // (makes a local variable [technically useless as it will always take the deepest path 
//				anyway however for completeless sake it is added], default)
				word.trim().equals("not") || // !
				word.trim().equals("null") || // null
				word.trim().equals("or") || // ||
				word.trim().equals("private") || // private
				word.trim().equals("protected") || // protected (default)
				word.trim().equals("public") || // public
				word.trim().equals("ref") || // (every time you set something equal to a variable with this setting it will just
//				reference that variable)
				word.trim().equals("reference") || // (every time you set something equal to a variable with this setting it will just
//				reference that variable)
				word.trim().equals("return") || // return
				word.trim().equals("source") || // (every time you set something equal to a variable with this setting it will just
//				reference that variable)
				word.trim().equals("this") || // this
				word.trim().equals("true") || // true
				word.trim().equals("void") || // void
				word.trim().equals("while")); // while
	}
	
	private static final boolean isObjectName(String word) {
		for(String s : objectNames) if(s.trim().equals(word)) return true;
		return false;
	}
	
	private static final boolean isGlobalName(String word) {
		for(String s : globalNames) if(s.trim().equals(word)) return true;
		return false;
	}
	
	private static final boolean isLocalName(String word) {
		for(String s : localNames) if(s.trim().equals(word)) return true;
		return false;
	}
}