package data;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

import codeArea.PixelPanel;
import lang.core.executor.Executor;
import lang.core.executor.Interpreter;
import lang.core.general.InstructionParser;
import tree.BuffNode;
import tree.TreeEvent;
import tree.TreeListener;
import tree.TreePanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
    
	public PixelPanel pixelPanel;
	public TreePanel treePanel;
	public static ConsolePanel consolePanel;
	private JScrollPane scroll;
	public static Executor interpreter;
	
	private JMenu settings, themes, extended, edit, appearance;  
	private MenuListener ml;
	private JMenuBar mb;
	private File settingsFile;
    public JMenuItem i0, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16;
    public ArrayList<JMenuItem> tItems;
    public ArrayList<File> tFiles;
    
    public String theme = "mild";
    public Color background = new Color(200,200,200);
	public Color textArea = new Color(225,225,225);
	public Color defaultText = new Color(15,15,15);
	public Color specialText = new Color(10,10,235);
	public Color speechText = new Color(150,150,150);
	public Color punctuationText = new Color(150,150,150);
	public Color classText = new Color(200,150,165);
	public Color globalText = new Color(150,195,200);
	public Color localText = new Color(225,205,185);
	public Color commentText = new Color(150,150,150);
	public Color functionText = new Color(100,200,100);
	
	private int fontsize = 15;
	
	public MainFrame(String title) throws BadLocationException {
		super(title);
		setup1();
		treePanel = new TreePanel(this);
		setup2();
	}
	
	public MainFrame(String title, String extended) throws BadLocationException {
		super(title);
		setup1();
		treePanel = new TreePanel(this, extended);
		setup2();
	}
	
	public int fontsize() {
		return fontsize;
	}
	
	public void fontsize(int size) {
		fontsize=size;
		pixelPanel.updatefont();
	}
	
	public void setup1() {
		interpreter = new Interpreter();
		
		setLayout(new BorderLayout());
		
		tItems = new ArrayList<JMenuItem>();
		tFiles = new ArrayList<File>();
		
		ml = new MenuListener(this);
        mb = new JMenuBar();  
        settings = new JMenu("Settings");  
        themes = new JMenu("Choose Theme");
        edit = new JMenu("Edit");
        extended = new JMenu("Launch Extended");
        extended.setMnemonic(KeyEvent.VK_E);
        appearance = new JMenu("Appearance");
        i0 = new JMenuItem("Find");  
        i0.addActionListener(ml);
        i1 = new JMenuItem("Help");  
        i1.addActionListener(ml);
        i2 = new JMenuItem("Sunbright");  
        i2.addActionListener(ml);
        i3 = new JMenuItem("Mild"); 
        i3.addActionListener(ml);
        i4 = new JMenuItem("Obsidian");
        i4.addActionListener(ml);
        i5 = new JMenuItem("Change Workspace");  
        i5.addActionListener(ml);
        i5.setMnemonic(KeyEvent.VK_C);
		KeyStroke keyStrokei5 = KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK);
		i5.setAccelerator(keyStrokei5);
        i6 = new JMenuItem("Relaunch");  
        i6.addActionListener(ml);
        i6.setMnemonic(KeyEvent.VK_R);
		KeyStroke keyStrokei6 = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);
		i6.setAccelerator(keyStrokei6);
        i7 = new JMenuItem("Errors");
        i7.addActionListener(ml);
        i8 = new JMenuItem("Exports");
        i8.addActionListener(ml);
        i9 = new JMenuItem("Libraries");
        i9.addActionListener(ml);
        i10 = new JMenuItem("Runnables");
        i10.addActionListener(ml);
        i11 = new JMenuItem("Files");
        i11.addActionListener(ml);
        i12 = new JMenuItem("Save");
        i12.addActionListener(ml);
        i12.setMnemonic(KeyEvent.VK_S);
		KeyStroke keyStrokei12 = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		i12.setAccelerator(keyStrokei12);
        i13 = new JMenuItem("Clear");
        i13.addActionListener(ml);
        i13.setMnemonic(KeyEvent.VK_C);
		KeyStroke keyStrokei13 = KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK);
		i13.setAccelerator(keyStrokei13);
        i14 = new JMenuItem("Undo");
        i14.addActionListener(ml);
        i14.setMnemonic(KeyEvent.VK_U);
		KeyStroke keyStrokei14 = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
		i14.setAccelerator(keyStrokei14);
		i15 = new JMenuItem("Redo");
        i15.addActionListener(ml);
        i15.setMnemonic(KeyEvent.VK_R);
		KeyStroke keyStrokei15 = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);
		i15.setAccelerator(keyStrokei15);
		i16 = new JMenuItem("Change Font Size");
		i16.addActionListener(ml);
        settings.add(i1); 
        extended.add(i11);
        extended.add(i7);
        extended.add(i8);
        extended.add(i9);
        extended.add(i10);
        themes.add(i0);
        themes.add(i2);
        themes.add(i3);
        themes.add(i4);
        settings.add(i5); 
        settings.add(i6); 
        settings.add(extended);
        edit.add(i12);
        edit.add(i13);
        edit.add(i14);
        edit.add(i15);
        appearance.add(i16);
        mb.add(settings);
        mb.add(themes);
        mb.add(edit);
        mb.add(appearance);
        setJMenuBar(mb);
        
		colorBar();
        
		pixelPanel = new PixelPanel(this);
	}
	
	public void setup2() {
		consolePanel = new ConsolePanel(this);
		scroll = new JScrollPane(pixelPanel);
		scroll.setViewportView(pixelPanel.codeArea);
		treePanel.addTreeListener(new TreeListener() {
			public void treeEventOccured(TreeEvent event) {
				if(event.getType() == "Edit") {
					try {
						pixelPanel.giveFile((File) event.getData());
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
				else if(event.getType() == "Save") {
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter((File) event.getData()));
						bw.write(pixelPanel.getCode());
						bw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else if(event.getType() == "Play") {
					consolePanel.clear();
					consolePanel.giveLine("Started program");
					interpreter.loadFromInstructions(InstructionParser.fullparse(pixelPanel.getCode()));
					interpreter.run();
				}
				else if(event.getType() == "Run") {
					consolePanel.clear();
				}
				else if(event.getType() == "Open") { 
					ImageCanvas.path = (String) event.getData();
					ImageCanvas i = new ImageCanvas(); 
			        JFrame f = new JFrame();  
			        f.add(i);  
			        f.setSize(800,800);  
			        f.setVisible(true);  
				}
				else if(event.getType() == "Log") consolePanel.giveLine((String) event.getData());
				else if(event.getType() == "Add1") {
					treePanel.pop = true;
					String s = (String) JOptionPane.showInputDialog(null, "Name", "Pixel : Add File", JOptionPane.QUESTION_MESSAGE, null, null, "");
					if ((s != null) && (s.length() > 0)) treePanel.addNode1(s);
					treePanel.pop = false;
				} else if(event.getType() == "Add2") {
					treePanel.pop = true;
					Object[] possibilities = {"Folder", "Project", "Package", "Pixel Class", "McFunction Class", "Allo Class"};
					String s = (String) JOptionPane.showInputDialog(null, "Type", "Pixel : Add File", JOptionPane.QUESTION_MESSAGE, null, possibilities, possibilities[0]);
					if ((s != null) && (s.length() > 0)) treePanel.addNode2(s);
					treePanel.pop = false;
				} else if(event.getType() == "Rename") {
					treePanel.pop = true;
					String s = (String) JOptionPane.showInputDialog(null, "Rename", "Pixel : Rename File", JOptionPane.QUESTION_MESSAGE, null, null, "");
					if ((s != null) && (s.length() > 0)) treePanel.rename(s, (BuffNode) event.getData());
					treePanel.pop = false;				
				} else if(event.getType() == "Export") {
					treePanel.error("Can't export yet");
				} else if(event.getType() == "Delete") {
					pixelPanel.setCode("");
				} else if(event.getType() == "Relaunch") {
					Object[] options = { "OK", "CANCEL" };
					if(JOptionPane.showOptionDialog(null, "You are about to relaunch the program, all unsaved work will be lost.\n"
					+ "Click OK to continue.", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]) == 0) PIBoot.relaunch();
				} else if(event.getType() == "CondRelaunch") {
					Object[] options = { "YES", "NO" };
					if(JOptionPane.showOptionDialog(null, (String) event.getData()
					+ "\nDo you want to relaunch?", "Pixel : Relaunch", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null, options, options[0]) == 0) PIBoot.relaunch();
				} else if(event.getType() == "Error") {
					JOptionPane.showMessageDialog(null, event.getData(), "Pixel : Error", JOptionPane.ERROR_MESSAGE);
				} else if(event.getType() == "Info") JOptionPane.showMessageDialog(null, event.getData(), "Pixel : Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		Container c = getContentPane();
		c.add(scroll, BorderLayout.CENTER);
		c.add(treePanel, BorderLayout.WEST);
		c.add(consolePanel, BorderLayout.SOUTH);
		
		addThemes();
	}
	
	public void changefontsize() {
		treePanel.pop = true;
		
		JOptionPane optionPane = new JOptionPane();
	    JSlider slider = getSlider(optionPane);
	    optionPane.setMessage(new Object[] { "Select a value: ", slider });
	    optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
	    optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
	    JDialog dialog = optionPane.createDialog(null, "Font Size");
	    dialog.setVisible(true);	
		treePanel.pop = false;
		Object value = optionPane.getInputValue();
		if(value instanceof String) {
			if(value=="uninitializedValue")fontsize(15);
		} else if (value instanceof Integer)fontsize((int)value);
	}
	
	private JSlider getSlider(final JOptionPane optionPane) {
	    JSlider slider = new JSlider(5,25);
	    
	    slider.setMajorTickSpacing(5);
	    slider.setPaintTicks(true);
	    slider.setPaintLabels(true);
	    ChangeListener changeListener = new ChangeListener() {
	      public void stateChanged(ChangeEvent changeEvent) {
	        JSlider theSlider = (JSlider) changeEvent.getSource();
	        if (!theSlider.getValueIsAdjusting()) {
	          optionPane.setInputValue((Integer)theSlider.getValue());
	        }
	      }
	    };
	    slider.addChangeListener(changeListener);
	    return slider;
	  }
	
	public void undo() {
		if(pixelPanel.undoes < pixelPanel.undol) {
			pixelPanel.setCodeSneaky(pixelPanel.undo[pixelPanel.undoes]);
			pixelPanel.undoes++;
		}
	}
	
	public void redo() {
		if(pixelPanel.undoes > 0 ) {
			pixelPanel.undoes--;
			pixelPanel.setCodeSneaky(pixelPanel.undo[pixelPanel.undoes]);
		}
		else {
			pixelPanel.setCodeSneaky(pixelPanel.past);
		}
	}
	
	public void clear() {
		pixelPanel.setCodeSneaky("");
	}
	
	public void save() {
		if(pixelPanel.file != null) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(pixelPanel.file));
				bw.write(pixelPanel.getCode());
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				treePanel.error("A saving error");
				return;
			}
			treePanel.success("Saving the file");
		} else {
			treePanel.fileNotFound("Current file");
		}
	}
	
	private void addThemes() {
		File themes = new File(treePanel.path + treePanel.themes);
		if(themes.listFiles() != null) for(File f : themes.listFiles()) {
			if(f.getName().lastIndexOf(".pxt") >= 0) add(f.getName().substring(0,f.getName().lastIndexOf(".pxt")),f);
		}
	}
	
	public void handleSettings(String s, File settingsFile_) throws BadLocationException {
		if(s.contains("\n")) {
			String newS = "";
			for(char c : s.toCharArray()) if(c != '\n') newS += c;
			s = newS;
		}
		if(s.contains("sunbright")) MenuListener.sunbright(this);
		if(s.contains("mild")) MenuListener.mild(this);
		if(s.contains("obsidian")) MenuListener.obsidian(this);
		else {
			File f = new File(s);
			if(f.exists()) TreePanel.setTheme(this,f);
			else {
				BufferedWriter bw;
				try {
					bw = new BufferedWriter(new FileWriter(settingsFile_));
					bw.write("mild");
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				MenuListener.mild(this);
			}
		}
		settingsFile = settingsFile_;
	}
	
	public void updateSettings() {
		try {
			if(settingsFile != null && settingsFile.exists()) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(settingsFile));
				bw.write(theme);
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void color() {
		colorBar();
		if(pixelPanel != null) pixelPanel.color();
		if(treePanel != null) treePanel.color();
		if(consolePanel != null) consolePanel.color();
	}
	
	public void add(File f) {
        mb.remove(themes);
		JMenuItem i = new JMenuItem(f.getName().substring(0,f.getName().lastIndexOf(".pxt")));
        i.addActionListener(ml);
		tItems.add(i);
		tFiles.add(f);
        themes.add(i);
        mb.add(themes);
        colorBar();
	}
	
	private void add(String name, File f) {
        mb.remove(themes);
		JMenuItem i = new JMenuItem(name);
        i.addActionListener(ml);
		tItems.add(i);
		tFiles.add(f);
        themes.add(i);
        mb.add(themes);
        colorBar();
	}
	
	private void colorBar() {
		mb.setBackground(background);
	    settings.setBackground(background);
	    extended.setBackground(background);
	    themes.setBackground(background);
	    edit.setBackground(background);
	    appearance.setBackground(background);
	    i0.setBackground(background);
	    i1.setBackground(background);
	    i2.setBackground(background);
	    i3.setBackground(background);
	    i4.setBackground(background);
	    i5.setBackground(background);
	    i6.setBackground(background);
	    i7.setBackground(background);
	    i8.setBackground(background);
	    i9.setBackground(background);
	    i10.setBackground(background);
	    i11.setBackground(background);
	    i12.setBackground(background);
	    i13.setBackground(background);
	    i14.setBackground(background);
	    i15.setBackground(background);
	    i16.setBackground(background);
        mb.setForeground(defaultText);
	    settings.setForeground(defaultText);
	    extended.setForeground(defaultText);
	    themes.setForeground(defaultText);
	    edit.setForeground(defaultText);
	    appearance.setForeground(defaultText);
	    i0.setForeground(defaultText);
	    i1.setForeground(defaultText);
	    i2.setForeground(defaultText);
	    i3.setForeground(defaultText);
	    i4.setForeground(defaultText);
	    i5.setForeground(defaultText);
	    i6.setForeground(defaultText);
	    i7.setForeground(defaultText);
	    i8.setForeground(defaultText);
	    i9.setForeground(defaultText);
	    i10.setForeground(defaultText);
	    i11.setForeground(defaultText);
	    i12.setForeground(defaultText);
	    i13.setForeground(defaultText);
	    i14.setForeground(defaultText);
	    i15.setForeground(defaultText);
	    i16.setForeground(defaultText);
	    mb.setOpaque(true);
	    settings.setOpaque(true);
	    extended.setOpaque(true);
	    themes.setOpaque(true);
	    edit.setOpaque(true);
	    appearance.setOpaque(true);
	    i0.setOpaque(true);
	    i1.setOpaque(true);
	    i2.setOpaque(true);
	    i3.setOpaque(true);
	    i4.setOpaque(true);
	    i5.setOpaque(true);
	    i6.setOpaque(true);
	    i7.setOpaque(true);
	    i8.setOpaque(true);
	    i9.setOpaque(true);
	    i10.setOpaque(true);
	    i11.setOpaque(true);
	    i12.setOpaque(true);
	    i13.setOpaque(true);
	    i14.setOpaque(true);
	    i15.setOpaque(true);
	    i16.setOpaque(true);
	    if(tItems.size() > 0) for (JMenuItem i : tItems) {
	    	i.setBackground(background);
	    	i.setForeground(defaultText);
	    	i.setOpaque(true);
	    }
	}
	
}
