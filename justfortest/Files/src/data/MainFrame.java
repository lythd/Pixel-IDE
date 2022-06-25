package data;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;

import tree.BuffNode;
import tree.TreeEvent;
import tree.TreeListener;
import tree.TreePanel;

import codeArea.PixelPanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public PixelPanel pixelPanel;
	public TreePanel treePanel;
	public ConsolePanel consolePanel;
	private JScrollPane scroll;
	private JMenu settings, themes;  
	private MenuListener ml;
	private JMenuBar mb;
	private File settingsFile;
    public JMenuItem i1, i2, i3, i4, i0, i5, i6;
    public ArrayList<JMenuItem> tItems;
    public ArrayList<File> tFiles;
    public String theme = "mild";
    public Color background = new Color(0,0,0), textArea = new Color(0,0,0), defaultText = new Color(0,0,0),
    		specialText = new Color(0,0,0), speechText = new Color(0,0,0), punctuationText = new Color(0,0,0),
    		staticText = new Color(0,0,0), globalText = new Color(0,0,0), localText = new Color(0,0,0),
    		commentText = new Color(0,0,0);
	
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
	
	public void setup1() {
		setLayout(new BorderLayout());
		
		tItems = new ArrayList<JMenuItem>();
		tFiles = new ArrayList<File>();
		
		ml = new MenuListener(this);
        mb = new JMenuBar();  
        settings = new JMenu("Settings");  
        themes = new JMenu("Choose Theme");
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
        i6 = new JMenuItem("Relaunch");  
        i6.addActionListener(ml);
        settings.add(i1); 
        themes.add(i0);
        themes.add(i2);
        themes.add(i3);
        themes.add(i4);
        settings.add(i5); 
        settings.add(i6); 
        settings.add(themes);
        mb.add(settings);
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
					pixelPanel.run();
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
				} else if(event.getType() == "Delete") {
					pixelPanel.setCode("");
				} else if(event.getType() == "Relaunch") {
					Object[] options = { "OK", "CANCEL" };
					if(JOptionPane.showOptionDialog(null, "You are about to relaunch the program, all unsaved work will be lost.\n"
							+ "Click OK to continue.", "Warning",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[0]) == 0) PIBoot.relaunch();
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
		settings.remove(themes);
        mb.remove(settings);
		JMenuItem i = new JMenuItem(f.getName().substring(0,f.getName().lastIndexOf(".pxt")));
        i.addActionListener(ml);
		tItems.add(i);
		tFiles.add(f);
        themes.add(i);
        settings.add(themes);
        mb.add(settings);
        colorBar();
	}
	
	private void add(String name, File f) {
        settings.remove(themes);
        mb.remove(settings);
		JMenuItem i = new JMenuItem(name);
        i.addActionListener(ml);
		tItems.add(i);
		tFiles.add(f);
        themes.add(i);
        settings.add(themes);
        mb.add(settings);
        colorBar();
	}
	
	private void colorBar() {
		mb.setBackground(background);
	    settings.setBackground(background);
	    themes.setBackground(background);
	    i0.setBackground(background);
	    i1.setBackground(background);
	    i2.setBackground(background);
	    i3.setBackground(background);
	    i4.setBackground(background);
	    i5.setBackground(background);
	    i6.setBackground(background);
        mb.setForeground(defaultText);
	    settings.setForeground(defaultText);
	    themes.setForeground(defaultText);
	    i0.setForeground(defaultText);
	    i1.setForeground(defaultText);
	    i2.setForeground(defaultText);
	    i3.setForeground(defaultText);
	    i4.setForeground(defaultText);
	    i5.setForeground(defaultText);
	    i6.setForeground(defaultText);
	    mb.setOpaque(true);
	    settings.setOpaque(true);
	    themes.setOpaque(true);
	    i0.setOpaque(true);
	    i1.setOpaque(true);
	    i2.setOpaque(true);
	    i3.setOpaque(true);
	    i4.setOpaque(true);
	    i5.setOpaque(true);
	    i6.setOpaque(true);
	    if(tItems.size() > 0) for (JMenuItem i : tItems) {
	    	i.setBackground(background);
	    	i.setForeground(defaultText);
	    	i.setOpaque(true);
	    }
	}
}
