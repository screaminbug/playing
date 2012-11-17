import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Main  {
	
	private static List<Boolean> data = new ArrayList<>(); 
	private static int height;
	private static int width = 64;
	private int offset;

	
	private final JSpinner controlX = new JSpinner();
	private final JSpinner spinnerOffset = new JSpinner();
    private final JSpinner  controlY = new JSpinner(); //new JSlider(JSlider.HORIZONTAL);
    private final DisplayPanel displayPanel = new DisplayPanel();;
    
    private final JTextArea positionsArea = new JTextArea();
    private static int size;
    private Coordinate coordinate;
	private boolean readyForBackground = true;

	public static void main(String[] args) {
		
//		if (args.length == 0) returnError();
		
		
//		int byteRead;
//		try {
//			FileInputStream fis = new FileInputStream(args[0]);
//			try {
//				byteRead = fis.read();
//				while (byteRead != -1) {
//					for (int bit = 7; bit >= 0; bit--) {
//						data.add((1 & (byteRead >> bit)) == 1);
//					}
//					byteRead = fis.read();
//					
//				}
//			} finally {
//				fis.close();
//			}	
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			System.out.println("No such file");
//			System.exit(-2);
//		} catch (IOException ioe) {
//			System.out.println("There was a problem with file IO.");
//			System.exit(-3);
//		}
		
		String enigma = "69I960EHE0A4A0IVG0EHE02500R4R0G1T30PLJ00V6V0EHE0V1U01V10U5U0VGV0V4R";
		
		for (int i= 0; i < enigma.length(); i++) {
			int character = (0b1111111 & enigma.charAt(i));
			int byteRead =  (character >= '0') && (character <= '9') ? character - '0' : character - 55;
			for (int bit = 0; bit < 8; bit++) {
				data.add((1 & (byteRead >> bit)) == 1);
			}
		}
		
//		int toPad = data.size() % 8;
//		for (int i = 0; i < toPad; i++) data.add(0);
//		
		Main app = new Main();
		app.startApp();
				
	}
	
	public Main() {
		//constructBitmap();
	}
	
	private static void returnError() {
		System.out.println("Binary filename not specified. Exiting...");
		System.exit(-1);
	}
	
	private void constructUI() {
	    //Make sure we have nice window decorations.
        //JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("Bitmap Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        controlX.setValue(height);
        controlX.setEnabled(false);
        

        
        controlY.setValue(width);
        frame.setSize(height, width);
     
        frame.add(displayPanel, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel();
        
        
        
//        controlY.setMinimum(2);
//        controlY.setMaximum(data.size()/10);
        
        controlY.addChangeListener(new UpdateListener());
        
        spinnerOffset.addChangeListener(new UpdateListener());
        
        displayPanel.addHierarchyBoundsListener(new HierarchyBoundsListener() {
			
			@Override
			public void ancestorResized(HierarchyEvent arg0) {
				constructBitmap();
							
			}
			
			@Override
			public void ancestorMoved(HierarchyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        
        
        FlowLayout controlLayout = new FlowLayout();
        controlLayout.setAlignment(FlowLayout.CENTER);
        
        controlPanel.setLayout(controlLayout);        		
        
        controlPanel.add(controlX);
        controlPanel.add(controlY);
        controlPanel.add(spinnerOffset);
        JScrollPane scrollpane = new JScrollPane(positionsArea);
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(scrollpane, BorderLayout.WEST);

        //Display the window.
        frame.pack();      
        frame.setVisible(true);
	}
	
	private void startApp() {
		
		constructBitmap();
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				constructUI();
								
			}
		});
	}
	
	private class UpdateListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			constructBitmap();
			
		}
		
	}
		
	private class BitmapConstructor extends SwingWorker<Queue<Rectangle2D.Double>, Object> {
		StringBuilder b = new StringBuilder();
		@Override
		protected Queue<Rectangle2D.Double> doInBackground() {
			
			Queue<Rectangle2D.Double> rectQueue = new ArrayDeque<>();
			int size = data.size();

			height = (int) Math.ceil (data.size()) / width;
			
			offset = (int) spinnerOffset.getValue();
			// construct a bitmap
			Integer bite = 0;
			
			Dimension rectSize = displayPanel.getSize();
		    Insets rectInsets = displayPanel.getInsets();

		    double h =  (rectSize.height - rectInsets.top - rectInsets.bottom) / (double)height; 
		    double w =  (rectSize.width - rectInsets.left - rectInsets.right) / (double)width;			
			
			for (int i = offset; i < size; i++) {
				double yPos = ((i-offset)  % height) * h; 
				double xPos = ((i-offset ) / height) * w;
				
				if (data.get(i % size)) {
					rectQueue.add(new Rectangle2D.Double(xPos, yPos , w, h));
					b.append(String.format("%04d:  x = %04d  y = %04d\n", i, (int)xPos, (int)yPos));
				}
					
				
					
			}
			return rectQueue;
		}
		
		@Override
		protected void done() {
			try {
				width = (int)controlY.getValue();
				controlX.setValue(new Integer(height));
				positionsArea.setText(b.toString());
				displayPanel.setRect(get());
				displayPanel.repaint();
				readyForBackground = true;
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void constructBitmap() {
		if (readyForBackground) {
			BitmapConstructor bitmapConstructor = new BitmapConstructor();
			bitmapConstructor.execute();
			readyForBackground = false;
		}
		
	}
		

}
