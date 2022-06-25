package data;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class ImageCanvas extends Canvas {
    
	private static final long serialVersionUID = 1L;
	public static String path;
	
	public void paint(Graphics g) {  
  
        Toolkit t = Toolkit.getDefaultToolkit();  
        Image i = t.getImage(path);  
        g.drawImage(i, 5, 5, this);  
          
    }
	
}
