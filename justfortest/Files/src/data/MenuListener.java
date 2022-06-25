package data;

import java.awt.event.*;
import java.io.IOException;
import java.awt.Color;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import codeArea.KeywordStyledDocument;

public class MenuListener implements ActionListener{

	private MainFrame mf;
	
	public MenuListener(MainFrame mf) {
		this.mf = mf;
	}
	
	public void actionPerformed(ActionEvent e) {    
		if(e.getSource() == mf.i1 && Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI("https://wontonpixel.weebly.com/"));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource( ) == mf.i2) {
			  try {
				sunbright(mf);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == mf.i3) {
			  try {
				mild(mf);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == mf.i4) {
			  try {
				obsidian(mf);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == mf.i0) {
			JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));    
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY); 
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PIXEL THEME FILES (.pxt)", "pxt");
            fc.setFileFilter(filter);
            fc.setDialogTitle("Choose Theme");
		    int i = fc.showOpenDialog(mf);
		    if(i == JFileChooser.APPROVE_OPTION)
				try {
					mf.treePanel.takeTheme(fc.getSelectedFile());
					mf.add(fc.getSelectedFile());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			else mf.treePanel.takentTheme();
		}
		if(e.getSource() == mf.i5) {
			JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));    
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
            fc.setDialogTitle("Choose Workspace");
		    int i = fc.showOpenDialog(mf);
		    if(i == JFileChooser.APPROVE_OPTION) mf.treePanel.takeWorkspace((fc.getSelectedFile()).getPath()); 
		    else mf.treePanel.takentWorkspace();
		}
		if(e.getSource() == mf.i6) {
			mf.treePanel.relaunch();
		}
		if(mf.tItems.contains(e.getSource())) {
			try {
				mf.treePanel.takeTheme(mf.tFiles.get(mf.tItems.indexOf(e.getSource())));
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void sunbright(MainFrame mf) throws BadLocationException {   
		  mf.theme = "sunbright";
		  mf.background = new Color(250,250,250);
		  mf.textArea = new Color(225,255,255);
		  mf.defaultText = new Color(25,25,25);
		  mf.specialText = new Color(255,225,20);
		  mf.speechText = new Color(255,200,255);
		  mf.punctuationText = new Color(255,200,255);
		  mf.staticText = new Color(225,175,190);
		  mf.globalText = new Color(185,235,185);
		  mf.localText = new Color(225,165,205);
		  mf.commentText = new Color(255,200,255);
		  mf.color();
		  mf.updateSettings();
		  ((KeywordStyledDocument) mf.pixelPanel.codeArea.getStyledDocument()).manRefresh();
	}
	
	public static void mild(MainFrame mf) throws BadLocationException {
		  mf.theme = "mild";
		  mf.background = new Color(200,200,200);
		  mf.textArea = new Color(225,225,225);
		  mf.defaultText = new Color(15,15,15);
		  mf.specialText = new Color(10,10,235);
		  mf.speechText = new Color(150,150,150);
		  mf.punctuationText = new Color(150,150,150);
		  mf.staticText = new Color(200,150,165);
		  mf.globalText = new Color(150,195,200);
		  mf.localText = new Color(225,205,185);
		  mf.commentText = new Color(150,150,150);
		  mf.color();
		  mf.updateSettings();
		  ((KeywordStyledDocument) mf.pixelPanel.codeArea.getStyledDocument()).manRefresh();
	}
	
	public static void obsidian(MainFrame mf) throws BadLocationException {
		  mf.theme = "obsidian";
		  mf.background = new Color(10,10,10);
		  mf.textArea = new Color(30,30,30);
		  mf.defaultText = new Color(200,200,200);
		  mf.specialText = new Color(10,185,10);
		  mf.speechText = new Color(100,100,100);
		  mf.punctuationText = new Color(100,100,100);
		  mf.staticText = new Color(125,15,95);
		  mf.globalText = new Color(95,10,75);
		  mf.localText = new Color(125,65,135);
		  mf.commentText = new Color(100,100,100);
		  mf.color();
		  mf.updateSettings();
		  ((KeywordStyledDocument) mf.pixelPanel.codeArea.getStyledDocument()).manRefresh();
	}
	
	public static void custom(MainFrame mf, Theme t) throws BadLocationException {
		  mf.theme = t.f.getAbsolutePath();
		  mf.background = new Color(t.background1,t.background2,t.background3);
		  mf.textArea = new Color(t.textArea1,t.textArea2,t.textArea3);
		  mf.defaultText = new Color(t.defaultText1,t.defaultText2,t.defaultText3);
		  mf.specialText = new Color(t.specialText1,t.specialText2,t.specialText3);
		  mf.speechText = new Color(t.speechText1,t.speechText2,t.speechText3);
		  mf.punctuationText = new Color(t.punctuationText1,t.punctuationText2,t.punctuationText3);
		  mf.staticText = new Color(t.staticText1,t.staticText2,t.staticText3);
		  mf.globalText = new Color(t.globalText1,t.globalText2,t.globalText3);
		  mf.localText = new Color(t.localText1,t.localText2,t.localText3);
		  mf.commentText = new Color(t.commentText1,t.commentText2,t.commentText3);
		  mf.color();
		  mf.updateSettings();
		  ((KeywordStyledDocument) mf.pixelPanel.codeArea.getStyledDocument()).manRefresh();
	}
}    