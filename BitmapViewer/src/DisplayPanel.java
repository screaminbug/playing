import java.awt.AlphaComposite;
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
          
	      g2d.setPaint(new Color(100,100,100, 30));
	      //g2d.setComposite(makeComposite(0.5f));
  	      
	      Rectangle2D.Double rect;
	      while ((rect = rectQueue.poll()) != null) {
	    	  g2d.fill(rect);
	      }
		  


	}
	
	private AlphaComposite makeComposite(float alpha) {
		  int type = AlphaComposite.SRC_OVER;
		  return(AlphaComposite.getInstance(type, alpha));
	}
	

}
