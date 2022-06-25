package tree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import data.MainFrame;
import data.MenuListener;
import data.Theme;

public class TreePanel extends JPanel{

	public boolean pop;
	public String extended, path, settings, test, pathFi, userDir, seperate, themes;
	private static final long serialVersionUID = 1L;
	private MainFrame mf;
	private JTree tree;
	private DefaultMutableTreeNode selectedNode;
	private BuffNode selectedbNode;
    private DefaultMutableTreeNode files; 
    private DefaultMutableTreeNode fFiles; 
    private BuffNode buffFiles;
    private BuffNode fbuffFiles;
    private DefaultMutableTreeNode n;
	private BuffNode b;
	private EventListenerList listenerList = new EventListenerList();
	private JPopupMenu menu;
	private Action itemAdd, itemRename, itemEdit, itemSave, itemPlay, itemExport, itemDelete;
	
	public TreePanel(MainFrame mf) {
		this.mf = mf;
		extended = "Files";
		setup();
	    
	}
	
	public TreePanel(MainFrame mf, String extended_) {
		this.mf = mf;
		extended = extended_;
	    setup();
	}
	
	public void setup() {

	    ImageIcon pixelIDEIcon = createImageIcon("images/pixel_ide.png");
	    ImageIcon pixelIcon = createImageIcon("images/pixel.png");
	    ImageIcon pixelExportedIcon = createImageIcon("images/pixel_exported.png");
	    ImageIcon pixelClassIcon = createImageIcon("images/pixel_class.png");
	    ImageIcon pixelExportIcon = createImageIcon("images/pixel_export.png");
	    ImageIcon pixelLibraryIcon = createImageIcon("images/pixel_library.png");
	    ImageIcon pixelSettingsIcon = createImageIcon("images/pixel_settings.png");
	    ImageIcon pixelThemeIcon = createImageIcon("images/pixel_theme.png");
	    ImageIcon pixelErrorIcon = createImageIcon("images/pixel_error.png");
	    ImageIcon pixelRunIcon = createImageIcon("images/pixel_runnable.png");
	    ImageIcon folderIcon = createImageIcon("images/folder.png");
	    ImageIcon packageIcon = createImageIcon("images/package.png");
	    ImageIcon projectIcon = createImageIcon("images/project.png");
	    ImageIcon imageIcon = createImageIcon("images/image.png");
	    ImageIcon textIcon = createImageIcon("images/text.png");
	    ImageIcon functionIcon = createImageIcon("images/function.png");
	    ImageIcon alloIcon = createImageIcon("images/allo.png");
	    ImageIcon nullIcon = createImageIcon("images/null.png");
	    
		userDir = System.getProperty("user.dir");
		
		String filename = "Pixel";
		String workingDir = userDir;
		String your_os = System.getProperty("os.name").toLowerCase();
		
		path = "";
		
		File file = new File("src/pixel/path.pxs");
		String readFile = "";
		String completeFile = "";
		
		if(file.exists()) readFile = readFile(file);
		if(!file.exists() || path.equals("")) 
		
		if(readFile.contains("\n")) for(String s : readFile.split("\n")) completeFile += s;
		if(!completeFile.equals("")) workingDir = completeFile;
		
		test = "test.txt";
		settings = "theme.pxs";
		seperate = "";
		themes = "";
		
		path = workingDir;
		
		if (your_os.indexOf("win") >= 0) {
			//if windows
			if(workingDir.equals(userDir)) path += "\\" + filename;
			extended = "\\" + extended;
			settings = "\\" + settings;
			test = "\\" + test;
			pathFi = "\\src\\pixel\\path.pxs";
			seperate = "\\";
			themes = "\\themes\\";
		} else if (your_os.indexOf("nix") >= 0 || 
                           your_os.indexOf("nux") >= 0 || 
                           your_os.indexOf("mac") >= 0) {
			//if unix, linux or mac 
			if(workingDir.equals(userDir)) path += "/" + filename;
			extended = "/" + extended;
			settings = "/" + settings;
			test = "/" + test;
			pathFi = "/src/pixel/path.pxs";
			seperate = "/";
			themes = "/themes/";
		}else{
			//unknown os hope for the best
			if(workingDir.equals(userDir)) path += "/" + filename;
			extended = "/" + extended;
			settings = "/" + settings;
			test = "/" + test;
			pathFi = "/src/pixel/path.pxs";
			seperate = "/";
			themes = "/themes/";
		}
		if((new File(path)).mkdir()) {
			(new File(path + seperate + "Files")).mkdir();
			(new File(path + seperate + "exports")).mkdir();
			(new File(path + seperate + "errors")).mkdir();
			(new File(path + seperate + "libraries")).mkdir();
			(new File(path + seperate + "runnables")).mkdir();
			(new File(path + seperate + "themes")).mkdir();
			try {
				File set = new File(path + settings);
				set.createNewFile();
				BufferedWriter bw;
				bw = new BufferedWriter(new FileWriter(set));
				bw.write("mild");
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				File tempF = new File(path + extended + test);
				tempF.createNewFile();
				BufferedWriter bw;
				bw = new BufferedWriter(new FileWriter(tempF));
				bw.write("Hello this is a test file in your new workspace! Feel free to delete this is just to make sure your workspace works correctly! :) \n~Spart");
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				File pFile = new File(userDir + pathFi);
				pFile.createNewFile();
				BufferedWriter bw;
				bw = new BufferedWriter(new FileWriter(pFile));
				bw.write(path);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				File themeFile = new File(path + themes + "Developers Choice.pxt");
				themeFile.createNewFile();
				BufferedWriter bw;
				bw = new BufferedWriter(new FileWriter(themeFile));
				bw.write("30,30,30,this is background color\r\n" + 
						"45,40,45,this is text area color\r\n" + 
						"25,185,25,this is default text color\r\n" + 
						"165,95,10,this is special text color\r\n" + 
						"175,95,175,this is speech text color\r\n" + 
						"200,200,200,this is punctuation color\r\n" + 
						"165,15,165,this is static text color\r\n" + 
						"5,95,100,this is global text color\r\n" + 
						"15,195,200,this is local text color\r\n" + 
						"175,95,175,this is commenting color");
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		try {
			File pathsettings = new File(path + settings);
			if(pathsettings.exists()) mf.handleSettings(readFile(pathsettings),pathsettings);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	    files = new DefaultMutableTreeNode("pixel"); 
	    fFiles = new DefaultMutableTreeNode(path + extended); 
	    buffFiles = new BuffNode(new File("pixel"), "IDE", null, files);
	    fbuffFiles = new BuffNode(new File(path + extended), "FOLDER", null, fFiles);
	    
		Dimension size = getPreferredSize();
		size.width = 300;
		size.height = 350;
		setPreferredSize(size);
	    setBackground(mf.background);
		setBorder(BorderFactory.createTitledBorder(null, "File Explorer", javax.swing.border.
	    	      TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.
	    	      TitledBorder.DEFAULT_POSITION, null, mf.defaultText));

	    appendFilesFromDir(files, buffFiles, path + extended);

	    menu = new JPopupMenu();
	    itemAdd = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			@Override
	        public void actionPerformed(ActionEvent e) {
	            if (selectedNode != null && selectedbNode != null && !pop) {
	            	if (selectedbNode.type() == "FOLDER" || selectedbNode.type() == "PIXEL_IDE" || selectedbNode.type() == "PACKAGE" || selectedbNode.type() == "PROJECT") fireTreeEvent(new TreeEvent(this, "Name", "Add1"));
	            	else fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is not a folder and can not have a file added onto it.", "Error"));
	            }
	            else if(pop) fireTreeEvent(new TreeEvent(this, "There is a popup open.", "Error"));
	            else fireTreeEvent(new TreeEvent(this, "Name", "Add1"));
	        }
	    };
	    menu.add(itemAdd);
	    
	    itemRename = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			@Override
	        public void actionPerformed(ActionEvent arg0) {
	            if (selectedNode != null && selectedbNode != null && !pop && selectedbNode.type() != "PIXEL_SETTINGS") fireTreeEvent(new TreeEvent(this, selectedbNode, "Rename"));
	            else if(selectedbNode != null && selectedbNode.type() == "PIXEL_SETTINGS") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is a restricted file and can not be renamed.", "Error"));
	            else if(pop) fireTreeEvent(new TreeEvent(this, "There is a popup open.", "Error"));
	            else fireTreeEvent(new TreeEvent(this, "File not found.", "Error"));
	        }
	    };
	    menu.add(itemRename);

	    itemEdit = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			@Override
	        public void actionPerformed(ActionEvent arg0) {
	            if (selectedNode != null && selectedbNode != null && !pop) {
	            	if (!(selectedbNode.type() == "FOLDER" || selectedbNode.type() == "IMAGE" || selectedbNode.type() == "PIXEL_SETTINGS" || selectedbNode.type().contains("_EXPORTED"))) fireTreeEvent(new TreeEvent(this, selectedbNode.file(), "Edit"));
	            	else if (selectedbNode.type() == "FOLDER") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is a folder and can not be edited.", "Error"));
	            	else if (selectedbNode.type() == "IMAGE") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is an image and can not be edited.", "Error"));
	            	else if (selectedbNode.type() == "PIXEL_SETTINGS") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is a restricted file and can not be edited.", "Error"));
	            	else if (selectedbNode.type().contains("_EXPORTED")) fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is exported and can not be edited.", "Error"));
	            }
	            else if(pop) fireTreeEvent(new TreeEvent(this, "There is a popup open.", "Error"));
	            else fireTreeEvent(new TreeEvent(this, "File not found.", "Error"));
	        }
	    };
	    menu.add(itemEdit);

	    itemSave = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			@Override
	        public void actionPerformed(ActionEvent arg0) {
	            if (selectedNode != null && selectedbNode != null && !pop) {
	            	if (!(selectedbNode.type() == "FOLDER" || selectedbNode.type() == "IMAGE" || selectedbNode.type() == "PIXEL_SETTINGS" || selectedbNode.type().contains("_EXPORTED"))) fireTreeEvent(new TreeEvent(this, selectedbNode.file(), "Save"));
	            	else if (selectedbNode.type() == "FOLDER") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is a folder and can not be saved.", "Error"));
	            	else if (selectedbNode.type() == "IMAGE") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is an image and can not be saved.", "Error"));
	            	else if (selectedbNode.type() == "PIXEL_SETTINGS") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is a restricted file and can not be saved.", "Error"));
	            	else if (selectedbNode.type().contains("_EXPORTED")) fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is exported and can not be saved.", "Error"));
	            }
	            else if(pop) fireTreeEvent(new TreeEvent(this, "There is a popup open.", "Error"));
	            else fireTreeEvent(new TreeEvent(this, "File not found.", "Error"));
	        }
	    };
	    menu.add(itemSave);

	    itemPlay = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			@Override
	        public void actionPerformed(ActionEvent arg0) {
	            if (selectedNode != null && selectedbNode != null && !pop) {
	            	if (selectedbNode.type() == "PIXEL" || selectedbNode.type() == "MCFUNCTION" || selectedbNode.type() == "ALLO") {
	            		fireTreeEvent(new TreeEvent(this, selectedbNode.file(), "Edit"));
	            		fireTreeEvent(new TreeEvent(this, selectedbNode.file(), "Play"));
	            	} else if (selectedbNode.type() == "PIXEL_EXPORTED") {
	            		fireTreeEvent(new TreeEvent(this, selectedbNode.file(), "Run"));
	            	} else if (selectedbNode.type() == "IMAGE") {
	            		fireTreeEvent(new TreeEvent(this, selectedbNode.file().getPath(), "Open"));
	            	}
	            	else fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is not a runnable file", "Error"));
	            }
	            else if(pop) fireTreeEvent(new TreeEvent(this, "There is a popup open.", "Error"));
	            else fireTreeEvent(new TreeEvent(this, "File not found.", "Error"));
	        }
	    };
	    menu.add(itemPlay);
	    
	    itemExport = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			@Override
	        public void actionPerformed(ActionEvent arg0) {
	            if (selectedNode != null && selectedbNode != null && !pop) {
	            	if (!selectedbNode.type().contains("_EXPORTED") && (selectedbNode.type().equals("PIXEL")
	            			|| selectedbNode.type().equals("MCFUNCTION") || selectedbNode.type().equals("ALLO"))) {
	            		String path = selectedbNode.file().getAbsolutePath();
	            		selectedbNode.removeFile(false);
		        		selectedbNode.setFile(new File(path));
		                selectedbNode.withType(selectedbNode.type() + "_EXPORTED").exportData();
		                BufferedWriter bw;
						try {
							bw = new BufferedWriter(new FileWriter(selectedbNode.file()));
			                bw.write(selectedbNode.data());
			                bw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
		            	fireTreeEvent(new TreeEvent(this, "", "Delete"));
		                tree.repaint();
		                tree.updateUI();
		            }
	            	else if (selectedbNode.type().contains("_EXPORTED")) fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is already exported.", "Error"));
		            else if (selectedbNode.type() == "FOLDER") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is a folder and can not be exported.", "Error"));
		            else if (selectedbNode.type() == "PIXEL_SETTINGS") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is a restricted file and can not be exported.", "Error"));
		            else fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is the wrong file type and can not be exported.", "Error"));
	            }
	            else if(pop) fireTreeEvent(new TreeEvent(this, "There is a popup open.", "Error"));
	            else fireTreeEvent(new TreeEvent(this, "File not found.", "Error"));
	       }
	    };
	    menu.add(itemExport);
	    
	    itemDelete = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			@Override
	        public void actionPerformed(ActionEvent arg0) {
	            if (selectedNode != null && selectedbNode != null && !pop) {
	            	if(selectedbNode.file() != null && selectedbNode.type() != "PIXEL_SETTINGS") selectedbNode.file().delete();
	            	if(selectedbNode.type() != "PIXEL_SETTINGS") fireTreeEvent(new TreeEvent(this, "", "Delete"));
	            	if(selectedbNode.type() != "PIXEL_SETTINGS") delete(selectedbNode);
	            	else if(selectedbNode.type() == "PIXEL_SETTINGS") fireTreeEvent(new TreeEvent(this, selectedbNode.nodeName() + " is a restricted file and can not be deleted.", "Error"));
		            tree.repaint();
		            tree.updateUI();
	            }
	            else if(pop) fireTreeEvent(new TreeEvent(this, "There is a popup open.", "Error"));
	            else fireTreeEvent(new TreeEvent(this, "File not found.", "Error"));
	       }
	    };
	    menu.add(itemDelete);
	    
 	    itemAdd.setEnabled(false);
 	    itemRename.setEnabled(false);
 	    itemEdit.setEnabled(false);
 	    itemSave.setEnabled(false);
 	    itemPlay.setEnabled(false);
 	    itemExport.setEnabled(false);
 	    itemDelete.setEnabled(false);
 	    
	    tree = new JTree(files) {
	    	
			private static final long serialVersionUID = 1L;
			
	        @Override
	        public Point getPopupLocation(MouseEvent e) {
	            if (e != null) {
	               TreePath path = getClosestPathForLocation(e.getX(), e.getY());
	               String type = "PIXEL_IDE";
	               if(buffFiles.childNode(((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject().toString(), true) != null) type = buffFiles.childNode(((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject().toString(), true).type();
	               if(type.equals("FOLDER") || type.equals("PIXEL_IDE") || type.equals("PACKAGE") || type.equals("PROJECT")) {
	            	   itemAdd.putValue(Action.NAME, "New");
	            	   itemAdd.setEnabled(true);
	               } else {
	            	   itemAdd.putValue(Action.NAME, "");
	            	   itemAdd.setEnabled(false);
	               }
            	   if(!type.equals("PIXEL_IDE")) {
                	   itemRename.putValue(Action.NAME, "Rename");
                	   itemRename.setEnabled(true);
            	   } else {
            		   itemRename.putValue(Action.NAME, "");
            		   itemRename.setEnabled(false);
            	   }
            	   if(!type.equals("FOLDER") && !type.contains("_EXPORTED") && !type.equals("PIXEL_IDE")) {
            		   itemEdit.putValue(Action.NAME, "Edit");
            		   itemEdit.setEnabled(true);
            	   } else {
            		   itemEdit.putValue(Action.NAME, "");
            		   itemEdit.setEnabled(false);
            	   }
            	   if(!type.equals("FOLDER") && !type.contains("_EXPORTED") && !type.equals("PIXEL_IDE") && !type.equals("IMAGE")) {
            		   itemSave.putValue(Action.NAME, "Save");
            		   itemSave.setEnabled(true);
            	   } else {
            		   itemSave.putValue(Action.NAME, "");
            		   itemSave.setEnabled(false);
            	   }
            	   if(type.equals("PIXEL") || type.equals("MCFUNCTION") || type.equals("ALLO") || type.equals("IMAGE") || type.equals("PIXEL_EXPORTED")) {
            		   if(type.equals("IMAGE")) itemPlay.putValue(Action.NAME, "Open");
            		   else if(type.equals("PIXEL_EXPORTED")) itemPlay.putValue(Action.NAME, "Run");
            		   else itemPlay.putValue(Action.NAME, "Test Run");
            		   itemPlay.setEnabled(true);
            	   } else {
            		   itemPlay.putValue(Action.NAME, "");
            		   itemPlay.setEnabled(false);
            	   }
            	   if(type.equals("PIXEL") || type.equals("MCFUNCTION") || type.equals("ALLO")) {
            		   itemExport.putValue(Action.NAME, "Export");
            		   itemExport.setEnabled(true);
            	   } else {
            		   itemExport.putValue(Action.NAME, "");
            		   itemExport.setEnabled(false);
            	   }
            	   if(!type.equals("PIXEL_IDE")) {
	            	   itemDelete.putValue(Action.NAME, "Delete");
	            	   itemDelete.setEnabled(true);
            	   } else {
            		   itemDelete.putValue(Action.NAME, "");
            		   itemDelete.setEnabled(false);
            	   }
	               return e.getPoint();
	            }
	            itemAdd.putValue(Action.NAME, ""); 
	            itemRename.putValue(Action.NAME, ""); 
	            itemEdit.putValue(Action.NAME, ""); 
	            itemSave.putValue(Action.NAME, ""); 
	            itemPlay.putValue(Action.NAME, ""); 
	            itemExport.putValue(Action.NAME, ""); 
	            itemDelete.putValue(Action.NAME, ""); 
         	    itemAdd.setEnabled(false);
         	    itemRename.setEnabled(false);
         	    itemEdit.setEnabled(false);
         	    itemSave.setEnabled(false);
         	    itemPlay.setEnabled(false);
         	    itemExport.setEnabled(false);
         	    itemDelete.setEnabled(false);
	            return null;
	        }

	    };
	    tree.setComponentPopupMenu(menu);;  
	    tree.setEditable(true);
	    tree.addMouseListener(getMouseListener());
	    tree.setCellRenderer(new MyRenderer(pixelIDEIcon, pixelIcon, pixelExportedIcon, pixelClassIcon, pixelExportIcon, pixelLibraryIcon, pixelSettingsIcon, 
	    		  pixelThemeIcon, pixelErrorIcon, pixelRunIcon, folderIcon, packageIcon, projectIcon, imageIcon, textIcon, functionIcon, alloIcon, nullIcon));
	    add(tree);
	}
	
	public void takeWorkspace(String s) {
		try {
			File pFile = new File(userDir + pathFi);
			pFile.createNewFile();
			BufferedWriter bw;
			bw = new BufferedWriter(new FileWriter(pFile));
			bw.write(s);
			bw.close();
			fireTreeEvent(new TreeEvent(this, "Your workspace was set to " + s + ".\nIn order to use this workspace please relaunch this program.", "Info"));
		} catch (IOException e) {
			e.printStackTrace();
			fireTreeEvent(new TreeEvent(this, "Your workspace could not be set.\nTry again, if you are stuck please contact Pixel Support\nand they will try to assist you.", "Error"));
		}
	}
	
	public void takentWorkspace() {
		fireTreeEvent(new TreeEvent(this, "You have closed out of the file explorer.\nWorkspace not set.", "Info"));
	}
	
	public void relaunch() {
		fireTreeEvent(new TreeEvent(this, "Relaunch", "Relaunch"));
	}
	
	public void takeTheme(File f) throws BadLocationException {
		if(setTheme(mf,f)) fireTreeEvent(new TreeEvent(this, "Your theme was set, enjoy.", "Info"));
		else fireTreeEvent(new TreeEvent(this, "Your theme was not set.\nTry again, if you are stuck please contact Pixel Support\nand they will try to assist you.", "Error"));
	}
	
	public static boolean setTheme(MainFrame mf, File f) throws BadLocationException {
		Theme t = st(f);
		if (t == null) return false;
		MenuListener.custom(mf,t);
		return true;
	}
	
	public static Theme st(File f) {
		String s = readFile(f);
		Theme t = new Theme();
		t.f = f;
		String[] temp = new String[3];
		int lines = 10;
		for(int j = 0; j < lines; j++) {
			String l = s.split("\n")[j];
			for(int i = 0; i < temp.length; i++) temp[i] = l.split(",")[i];
			for(int i = 0; i < temp.length; i++) {
				String arg = temp[i];
				if(j==0) {
					if(i==0)t.background1=Integer.parseInt(arg);
					if(i==1)t.background2=Integer.parseInt(arg);
					if(i==2)t.background3=Integer.parseInt(arg);
				} else if(j==1) {
					if(i==0)t.textArea1=Integer.parseInt(arg);
					if(i==1)t.textArea2=Integer.parseInt(arg);
					if(i==2)t.textArea3=Integer.parseInt(arg);
				} else if(j==2) {
					if(i==0)t.defaultText1=Integer.parseInt(arg);
					if(i==1)t.defaultText2=Integer.parseInt(arg);
					if(i==2)t.defaultText3=Integer.parseInt(arg);
				} else if(j==3) {
					if(i==0)t.specialText1=Integer.parseInt(arg);
					if(i==1)t.specialText2=Integer.parseInt(arg);
					if(i==2)t.specialText3=Integer.parseInt(arg);
				} else if(j==4) {
					if(i==0)t.speechText1=Integer.parseInt(arg);
					if(i==1)t.speechText2=Integer.parseInt(arg);
					if(i==2)t.speechText3=Integer.parseInt(arg);
				} else if(j==5) {
					if(i==0)t.punctuationText1=Integer.parseInt(arg);
					if(i==1)t.punctuationText2=Integer.parseInt(arg);
					if(i==2)t.punctuationText3=Integer.parseInt(arg);
				} else if(j==6) {
					if(i==0)t.staticText1=Integer.parseInt(arg);
					if(i==1)t.staticText2=Integer.parseInt(arg);
					if(i==2)t.staticText3=Integer.parseInt(arg);
				} else if(j==7) {
					if(i==0)t.globalText1=Integer.parseInt(arg);
					if(i==1)t.globalText2=Integer.parseInt(arg);
					if(i==2)t.globalText3=Integer.parseInt(arg);
				} else if(j==8) {
					if(i==0)t.localText1=Integer.parseInt(arg);
					if(i==1)t.localText2=Integer.parseInt(arg);
					if(i==2)t.localText3=Integer.parseInt(arg);
				} else if(j==9) {
					if(i==0)t.commentText1=Integer.parseInt(arg);
					if(i==1)t.commentText2=Integer.parseInt(arg);
					if(i==2)t.commentText3=Integer.parseInt(arg);
				}
			}
		}
		return t;
	}
	
	public void takentTheme() {
		fireTreeEvent(new TreeEvent(this, "You have closed out of the file explorer.\nTheme not set.", "Info"));
	}
	
	public void color() {
		setBackground(mf.background);
		setBorder(BorderFactory.createTitledBorder(null, "File Explorer", javax.swing.border.
	    	      TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.
	    	      TitledBorder.DEFAULT_POSITION, null, mf.defaultText));
		menu.setBackground(mf.background);
		menu.setForeground(mf.defaultText);
	}

	protected static ImageIcon createImageIcon(String path) {
	    java.net.URL imgURL = TreePanel.class.getResource(path);
	    if (imgURL != null) {
	    	return new ImageIcon(imgURL);
	    } else {
	    	System.err.println("Couldn't find file: " + path);
	    	return null;
	    }
	  }
	
	private class MyRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;
		Icon pixelIDEIcon, pixelIcon, pixelExportedIcon, pixelClassIcon, pixelExportIcon, pixelLibraryIcon, pixelSettingsIcon, pixelThemeIcon, pixelErrorIcon,
		pixelRunIcon, folderIcon, packageIcon, projectIcon, imageIcon, textIcon, functionIcon, alloIcon, nullIcon;

	    public MyRenderer(Icon pixelIDEIcon, Icon pixelIcon, Icon pixelExportedIcon, Icon pixelClassIcon, Icon pixelExportIcon, Icon pixelLibraryIcon, 
	    		Icon pixelSettingsIcon, Icon pixelThemeIcon, Icon pixelErrorIcon, Icon pixelRunIcon, Icon folderIcon, Icon packageIcon, Icon projectIcon, Icon imageIcon, Icon textIcon, 
	    		Icon functionIcon, Icon alloIcon, Icon nullIcon) {
	    	this.pixelIDEIcon = pixelIDEIcon;
	    	this.pixelIcon = pixelIcon;
	    	this.pixelExportedIcon = pixelExportedIcon;
	    	this.pixelClassIcon = pixelClassIcon;
	    	this.pixelExportIcon = pixelExportIcon;
	    	this.pixelLibraryIcon = pixelLibraryIcon;
	    	this.pixelSettingsIcon = pixelSettingsIcon;
	    	this.pixelThemeIcon = pixelThemeIcon;
	    	this.pixelErrorIcon = pixelErrorIcon;
	    	this.pixelRunIcon = pixelRunIcon;
	    	this.folderIcon = folderIcon;
	    	this.packageIcon = packageIcon;
	    	this.projectIcon = projectIcon;
	    	this.imageIcon = imageIcon;
	    	this.textIcon = textIcon;
	    	this.functionIcon = functionIcon;
	    	this.alloIcon = alloIcon;
	    	this.nullIcon = nullIcon;
	    }
	    
	    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
	    	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	    	setIcon(nullIcon);
	    	String type = "";
	    	if(buffFiles.childNode(((DefaultMutableTreeNode) value).getUserObject().toString(), true) != null) 
	    		type = buffFiles.childNode(((DefaultMutableTreeNode) value).getUserObject().toString(), true).type();
	    	if(type == "PIXEL") setIcon(pixelIcon);
	    	else if(type == "PIXEL_EXPORTED") setIcon(pixelExportedIcon);
	    	else if(type == "PIXEL_CLASS") setIcon(pixelClassIcon);
	    	else if(type == "PIXEL_EXPORT") setIcon(pixelExportIcon);
	    	else if(type == "PIXEL_LIBRARY") setIcon(pixelLibraryIcon);
	    	else if(type == "PIXEL_SETTINGS") setIcon(pixelSettingsIcon);
	    	else if(type == "PIXEL_THEME") setIcon(pixelThemeIcon);
	    	else if(type == "PIXEL_ERROR") setIcon(pixelErrorIcon);
	    	else if(type == "PIXEL_RUN") setIcon(pixelRunIcon);
	    	else if(type == "MCFUNCTION") setIcon(functionIcon);
	    	else if(type == "MCFUNCTION_EXPORTED") setIcon(functionIcon);
	    	else if(type == "ALLO") setIcon(alloIcon);
	    	else if(type == "ALLO_EXPORTED") setIcon(alloIcon);
	    	else if(type == "TEXT") setIcon(textIcon);
	    	else if(type == "IMAGE") setIcon(imageIcon);
	    	else if(type == "PACKAGE") setIcon(packageIcon);
	    	else if(type == "PROJECT") setIcon(projectIcon);
	    	else if(type == "FOLDER" || !leaf) {
	    		if(((DefaultMutableTreeNode) value) == files) setIcon(pixelIDEIcon);
	    		else setIcon(folderIcon);
	    	}
	    	return this;
	    }
	}
	
	public static void appendFilesFromDir(DefaultMutableTreeNode node, BuffNode bNode, String dir) {
		File folder = new File(dir);
	    appendFilesForFolder(node, bNode, folder);
	}
	
	public static void appendFilesForFolder(DefaultMutableTreeNode node, BuffNode bNode, File folder) {
		if(folder != null) {
			File [] files = folder.listFiles();
			File file = null;
			String temp;
			if(files != null && files.length > 0) for (int i = 0; i < files.length; i++) {
			    file = files[i];
			    if (file.isDirectory()) {
		    		String name = file.getName();
		    		DefaultMutableTreeNode newNode = null;
			    	if(extension(name).equals(".package") || extension(name).equals(".project")) newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
			    	else newNode = new DefaultMutableTreeNode(name);
			    	node.add(newNode);
			    	BuffNode newbNode = null;
			    	if(extension(name).equals(".package")) newbNode = new BuffNode(file, "PACKAGE", null, newNode, bNode);
			    	else if(extension(name).equals(".project")) newbNode = new BuffNode(file, "PROJECT", null, newNode, bNode);
			    	else newbNode = new BuffNode(file, "FOLDER", null, newNode, bNode);
			    	appendFilesForFolder(newNode, newbNode, file);
			    } else {
			    	if (file.isFile()) {
				    	temp = file.getName();
				    	DefaultMutableTreeNode newNode = null;
				    	@SuppressWarnings("unused")
						BuffNode newbNode = null;
				    	if (extension(temp).equals(".pix") && readFile(file).contains("import Allo")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "ALLO", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pix") && readFile(file).contains("import McFunction")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "MCFUNCTION", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pix")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "PIXEL", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".mcfunction")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "MCFUNCTION_EXPORTED", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pxe")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "PIXEL_EXPORTED", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pxc")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "PIXEL_CLASS", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pxx")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "PIXEL_EXPORT", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pxl")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "PIXEL_LIBRARY", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pxs")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "PIXEL_SETTINGS", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pxt")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "PIXEL_THEME", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pxw")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "PIXEL_ERROR", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".pxr")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "PIXEL_RUN", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".txt")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "TEXT", readFile(file), newNode, bNode);
				    	} else if (extension(temp).equals(".png")) {
				    		String name = file.getName();
				    		newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "IMAGE", readFile(file), newNode, bNode);
				    	} else {
				    		String name = file.getName();
				    		if(name.indexOf('.') >= 0) newNode = new DefaultMutableTreeNode(name.substring(0,name.indexOf('.')));
				    		else newNode = new DefaultMutableTreeNode(name);
				    		node.add(newNode);
				    		newbNode = new BuffNode(file, "NULL", readFile(file), newNode, bNode);
				    	}
			    	}
			    }
			}
		}
	}
	
	public static String extension(String string) {
		if(string != null && string.indexOf('.') != -1) return string.substring(string.indexOf('.'));
		return "";
	}
	
	public static String readFile (File file) {
		String string = "";
		String line = null;
		try {
			BufferedReader br;
			br = new BufferedReader(new FileReader(file));
			while((line = br.readLine())!= null) string += line + "\n";
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string;
	}

	private MouseListener getMouseListener() {
	    return new MouseAdapter() {
	
	        @Override
	        public void mousePressed(MouseEvent arg0) {
	            if(arg0.getButton() == MouseEvent.BUTTON3 && !pop){
	                TreePath pathForLocation = tree.getPathForLocation(arg0.getPoint().x, arg0.getPoint().y);
	                if (pathForLocation != null) {
	                    selectedNode = (DefaultMutableTreeNode) pathForLocation.getLastPathComponent();
	                    selectedbNode = buffFiles.childNode(selectedNode.getUserObject().toString(), true);
	                } else {
	                    selectedNode = null;
	                    selectedbNode = null;
	                }
	
	            }
	            super.mousePressed(arg0);
	        }
	    };
	}
	
	public void delete(BuffNode bn) {
		if(bn.children() != null) for(BuffNode bc : bn.children()) delete(bc);
		delete(bn.node());
		bn.withoutChildren().withoutParent(true).nullify();
	}
	
	public void delete(DefaultMutableTreeNode dn) {
		dn.removeAllChildren();
		dn.removeFromParent();
		dn.equals(null);
	}
	
	public void addNode1(String name) {
		if(selectedbNode == null || selectedNode == null) {
			selectedbNode = buffFiles;
			selectedNode = files;
		}
		b = new BuffNode().withParent(selectedbNode).withType(name);
		if(haveName(name)) fireTreeEvent(new TreeEvent(this, name + " is already a file name.", "Error"));
		else fireTreeEvent(new TreeEvent(this, "Type", "Add2"));
		pop = false;
	}
	
	public boolean haveName(String name) {
		for(BuffNode c : selectedbNode.parent().children()) if(c.name().equals(name)) return true;
		return false;
	}
	
	public void addNode2(String type) {
		File file = null;
		String ending = "";
		String data = "";
		String dir = "Pixel/Files/";
        n = new DefaultMutableTreeNode(b.type());
        selectedNode.add(n);
		try {
			boolean doFile = false;
			if(selectedbNode.file() != null) dir = selectedbNode.file().getAbsolutePath();
			if(selectedbNode == buffFiles) dir = fbuffFiles.file().getAbsolutePath();
			if(type == "Allo Class") {
				type = "ALLO";
				data = "import McFunction;\nimport Allo;\n";
				ending = ".pix";						
				doFile = true;
			}
			else if(type == "McFunction Class") {
				type = "MCFUNCTION";
				data = "import McFunction;\n";
				ending = ".pix";			
				doFile = true;
			}
			else if(type == "Pixel Class") {
				type = "PIXEL";
				data = "";
				ending = ".pix";			
				doFile = true;
			}
			else if(type == "Package") {
				type = "PACKAGE";
				file = new File(dir + "/" +  b.type() + ".package");
				if(!file.mkdir()) fireTreeEvent(new TreeEvent(this, "Package creation insuccessful.", "Error"));
			}
			else if(type == "Project") {
				type = "PROJECT";
				file = new File(dir + "/" +  b.type() + ".project");
				if(!file.mkdir()) fireTreeEvent(new TreeEvent(this, "Project creation insuccessful.", "Error"));
			}
			else if(type == "Folder") {
				type = "FOLDER";
				file = new File(dir + "/" +  b.type());
				if(!file.mkdir()) fireTreeEvent(new TreeEvent(this, "Folder creation insuccessful.", "Error"));
			} 
			if (doFile) {
				file = new File(dir + "/" + b.type() + ending);
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(data);
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(type != "FOLDER" && type != "PACKAGE" && type != "PROJECT") fireTreeEvent(new TreeEvent(this, file, "Edit"));
		b.withFile(file, type, "").withData(data).withNode(n).withParent(selectedbNode);
        tree.repaint();
        tree.updateUI();
		pop = false;
	}
	
	public void rename(String name, BuffNode bn) {
		try {
			String extension = bn.extension();
			bn.file().renameTo(new File(bn.file().getParentFile().getAbsolutePath() + "/" + name + extension));
			if(bn.file().isFile()) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(bn.file()));
				bw.write(bn.data());
				bw.close();
			} else {
				if(!bn.file().mkdir()) fireTreeEvent(new TreeEvent(this, "Folder repathing insuccessful.", "Error"));
			}
			bn.node().setUserObject(name);
			tree.repaint();
			tree.updateUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fireTreeEvent(TreeEvent event) {
		Object[] listeners = listenerList.getListenerList();
		for(int i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == TreeListener.class) {
				((TreeListener) listeners[i+1]).treeEventOccured(event);
			}
		}
	}
	
	public void addTreeListener(TreeListener treeListener) {
		listenerList.add(TreeListener.class, treeListener);
	}
	
	public void removeTreeListener(TreeListener listener) {
		listenerList.remove(TreeListener.class, listener);
	}
}
