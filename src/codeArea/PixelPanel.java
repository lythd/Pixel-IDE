package codeArea;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import tree.TreePanel;

import data.MainFrame;

public class PixelPanel extends JPanel {


	private static final long serialVersionUID = 1L;
    
	public File file;
	private MainFrame mf;
	private StyleContext styleContext;
	private Style defaultStyle, cwStyle;
	private Caret hidden, normal;
	public JTextPane codeArea;
	public boolean highlight = false;
	public String past, present;
	public int undoes;
	public String[] undo;
	public int undol = 50;
	private boolean off = false;
	private boolean unsaved = false;

	public PixelPanel(MainFrame mf) {
		this.mf = mf;
		
		past = "";
		present = "";
		undoes = 0;
		undo = new String[undol];
		for(@SuppressWarnings("unused") String undos : undo) undos = "";
		
		setOpaque(true);
		setBackground(mf.background);
        styleContext = new StyleContext();
        defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
        cwStyle = styleContext.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(cwStyle, mf.specialText);
        StyleConstants.setBold(cwStyle, true);
        
		Dimension size = getPreferredSize();
		size.width = 450;
		size.height = 350;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(null, "Pixel Code", javax.swing.border.
			    	      TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.
			    	      TitledBorder.DEFAULT_POSITION, null, mf.defaultText));
		
		codeArea = new JTextPane(new KeywordStyledDocument(defaultStyle, cwStyle, mf));
        updatefont();
		codeArea.setMargin(new Insets(5, 5, 5, 5));
		codeArea.addCaretListener(new VisibleCaretListener());
		codeArea.setBackground(mf.textArea);
		codeArea.setCaretColor(mf.defaultText);
		codeArea.setForeground(mf.defaultText);
		
		normal = codeArea.getCaret();
		hidden = new DefaultCaret() {
			private static final long serialVersionUID = 1L;

			@Override
            public void paint(Graphics g) {
            }

            @Override
            public boolean isVisible() {
                return false;
            }

            @Override
            public boolean isSelectionVisible() {
                return false;
            }
        }; 
		
        codeArea.setCaret(hidden);
        
		codeArea.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(file == null) codeArea.setEditable(false);
				present = codeArea.getText();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				unsaved = true;
				if(e.getKeyCode() != 18 && e.getKeyCode() != 16) {
					if(e.getKeyCode() == 17) off = true;
					if(!off) {
						undoes = 0;
						if(past != present) {
							for(int i = undo.length-1; i > 0; i--) {
								undo[i] = undo[i-1];
							}
							undo[0] = past;
							past = present;
						}
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() != 18 && e.getKeyCode() != 16) {
					if(e.getKeyCode() == 17) off = false;
					if(!off) {
						undoes = 0;
						if(past != present) {
							for(int i = undo.length-1; i > 0; i--) {
								undo[i] = undo[i-1];
							}
							undo[0] = past;
							past = present;
						}
					}
				}
			}
		});		
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.anchor = GridBagConstraints.CENTER;
		gc.weightx = 10;
		gc.weighty = 10;
		gc.gridx = 0;
		gc.gridy = 0;
		add(codeArea, gc);
	}
	
	public void updatefont() {
        codeArea.setFont(new Font("Courier New", Font.PLAIN, mf.fontsize()));
	}
	
	public void color() {
		setBackground(mf.background);
		codeArea.setBackground(mf.textArea);
		codeArea.setCaretColor(mf.defaultText);
		setBorder(BorderFactory.createTitledBorder(null, "Pixel Code", javax.swing.border.
	    	      TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.
	    	      TitledBorder.DEFAULT_POSITION, null, mf.defaultText));
	}
	
	private void offerSave() {
		Object[] options = { "YES", "NO" };
		if(JOptionPane.showOptionDialog(null, "You have unsaved work. Do you want to save first?", "Pixel : Unsaved work", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]) == 0) mf.treePanel.save();
	}
	
	public void setCode(String code) {
		if(file != null && unsaved) offerSave();
		unsaved = false;
		file = null;
        codeArea.setCaret(hidden);
		codeArea.setText(code);
	}
	
	public void setCodeSneaky(String code) {
		codeArea.setText(code);
	}
	
	public String getCode() {
		return codeArea.getText();
	}
	
	public void giveFile(File file) throws BadLocationException {
		if(file != null && unsaved) offerSave();
		unsaved = false;
		this.file = file;
		if(file != null) {
			String string = "";
			String line = null;
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while((line = br.readLine())!= null) string += line + "\n";
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			codeArea.setText(string);
	        codeArea.setCaret(normal);
			if(TreePanel.extension(file.getName()).equals(".pix")) highlight = true;
			else highlight = false;
			((KeywordStyledDocument) codeArea.getStyledDocument()).manRefresh();
		}
	}
	
}