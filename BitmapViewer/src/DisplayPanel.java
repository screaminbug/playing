import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Queue;

import javax.swing.JComponent;


public class DisplayPanel extends JComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3212535335401964850L;
	
	private Queue<Rectangle2D.Double> rectQueue;
		
	public void setRect(Queue<Rectangle2D.Double> rectlist) {
		this.rectQueue = rectlist;
	}

	public void paintComponent(Graphics g) {
	      if (rectQueue == null) return;
	      Graphics2D g2d = (Graphics2D) g;
          
	      g2d.setPaint(Color.red);
  	      
	      Rectangle2D.Double rect;
	      while ((rect = rectQueue.poll()) != null) {
	    	  g2d.fill(rect);
	      }
		  


	}
	

}
