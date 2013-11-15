
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class imageReader implements Runnable{

   private String fileName;
   private PlaySound playSound;
   private byte[] bytes;
   private BufferedImage img;
   private InputStream is;
   private final int width = 352;
   private final int height = 288;
   
   public imageReader(String fName, PlaySound pSound) {
	   this.fileName = fName;
	   this.playSound = pSound;
   }
   
   private void play() {
    		
		JFrame frame = new JFrame();
	   	frame.setSize(width, height+22);
		int[] talkLength = {11, 15, 16, 10};
		int[] wreckLength = {12, 16, 11, 13};
		int[] soccerLength = {11, 12, 16, 12};
		int time = 0;
		int fileNum;
		int frameNum = 0;
		JLabel label = new JLabel();
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
		
	    try {
		    File file = new File(fileName);
		    is = new FileInputStream(file);
	
		    long fileLength = file.length();
		    long len = width * height * 3;
		    bytes = new byte[(int)len];
		    frameNum = (int) (fileLength/len);
	        
	        switch (fileName.length()) {
	        case 9:
	        	//talk
	        	fileNum = Character.getNumericValue(fileName.charAt(4));
	        	time = talkLength[fileNum];
	        	break;
	        case 10:
	        	//wreck
	        	fileNum = Character.getNumericValue(fileName.charAt(5));
	        	time = wreckLength[fileNum];
	        	break;
	        case 11:
	        	//soccer
	        	fileNum = Character.getNumericValue(fileName.charAt(6));
	        	time = soccerLength[fileNum];
	        	break;
	        
	        }
	        int fps = frameNum/time;
	        double spf = playSound.getSampleRate()/fps;
			int j = 0;
			
			while (j < Math.round(playSound.getPosition()/spf)) {
				readFile();
				label.setIcon(new ImageIcon(img));
				frame.getContentPane().add(label, BorderLayout.CENTER);
			    frame.repaint();
			    frame.setVisible(true);
			    j++;
			}
			while (j > Math.round(playSound.getPosition()/spf)) {
			}
			
			for (int i=j; i<frameNum; i++) {
				while (i > Math.round(playSound.getPosition()/spf)) {
				}
				while (i < Math.round(playSound.getPosition()/spf)) {
					readFile();
					label.setIcon(new ImageIcon(img));
					frame.getContentPane().add(label, BorderLayout.CENTER);
				    frame.repaint();
				    frame.setVisible(true);
				    i++;
				}
				readFile();
				label.setIcon(new ImageIcon(img));
				frame.getContentPane().add(label, BorderLayout.CENTER);
			    frame.repaint();
			    frame.setVisible(true);
			    i++;
			}
			
			
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    }
	 // Use a label to display the image
	    
	    for (int i = 1; i<frameNum; i++) {
	    	int timeInterval = time * 1000/frameNum;
	    	try {
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	label.setIcon(new ImageIcon(img));
	    	frame.repaint();
	        
	    }
	}
    
    private void readFile() {
    	int offset = 0;
        int numRead = 0;
        try {
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			    offset += numRead;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int ind = 0;
        for(int y = 0; y < height; y++){
			
			for(int x = 0; x < width; x++){
		 
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2]; 
				
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				img.setRGB(x,y,pix);
				
				ind++;
			}
		}
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.play();
	}
  
}