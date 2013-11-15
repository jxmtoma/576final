import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class VideoQuery {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String videoName = args[0];
		String audioName = args[1];
		FileInputStream inputStream;
		try {
		    inputStream = new FileInputStream(audioName);
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		    return;
		}

		// initializes the playSound Object
		PlaySound playSound = new PlaySound(inputStream);
		imageReader ir = new imageReader(videoName, playSound);
		
		Thread t1 = new Thread(ir);
		Thread t2 = new Thread(playSound);
		t1.start();
		t2.start();
	}

}
