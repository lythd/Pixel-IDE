package data;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

public class PIBoot {

	private static JFrame frame, extended;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new MainFrame("Pixel IDE");
					frame.setSize(768,  768);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);		
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void relaunch() {
		try {
			frame.dispose();
			frame = new MainFrame("Pixel IDE");
			frame.setSize(768,  768);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);		
		} catch (BadLocationException e) {
			e.printStackTrace();
		}		
	}
	
	public static void launch(String extended_) {
		try {
			if(extended != null) extended.dispose();
			extended = new MainFrame("Pixel IDE", extended_);
			extended.setSize(768,  768);
			extended.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			extended.setVisible(true);		
		} catch (BadLocationException e) {
			e.printStackTrace();
		}		
	}
}
