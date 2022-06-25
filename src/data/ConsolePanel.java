package data;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsolePanel extends JPanel {


	private static final long serialVersionUID = 1L;
	private JButton conBtn;
	private JTextArea conArea = new JTextArea();
	private JScrollPane scroll = new JScrollPane(conArea);
	private MainFrame mf;
	
	public ConsolePanel(MainFrame mf) {
		this.mf = mf;
		Dimension size = getPreferredSize();
		size.width = 350;
		size.height = 256;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(null, "Console", javax.swing.border.
	    	      TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.
	    	      TitledBorder.DEFAULT_POSITION, null, mf.defaultText));
		setBackground(mf.background);
		updatefont();
		conArea.setRows(10);
		conArea.setColumns(50);
		conArea.setBackground(mf.textArea);
		conArea.setForeground(mf.commentText);
		
		conBtn = new JButton("Clear");
		conBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
 		});
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.anchor = GridBagConstraints.CENTER;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		add(scroll, gc);
		
		gc.weighty = 0.2;
		gc.gridx = 0;
		gc.gridy = 1;
		add(conBtn, gc);
	}
	
	public void updatefont() {
        conArea.setFont(new Font("Courier New", Font.PLAIN, mf.fontsize()));
	}
	
	public void color() {
		setBackground(mf.background);
		conArea.setBackground(mf.textArea);
		conArea.setForeground(mf.commentText);
		setBorder(BorderFactory.createTitledBorder(null, "Console", javax.swing.border.
	    	      TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.
	    	      TitledBorder.DEFAULT_POSITION, null, mf.defaultText));
	}
	
	public void clear() {
		conArea.setText("");
	}
	
	public void giveCharacter(char c) {
		conArea.setText(conArea.getText() + c);
	}
	
	public void giveLine(String line) {
		conArea.setText(conArea.getText() + "\n" + line);
	}
}
