import cvutil.help.Helper;
import cvutil.move.MotionDetector;
import cvutil.object.ObjectRecognizer;

public class Prueba {
	
	public static void main(String[] args) {		
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Helper.loadLibrary();
		
		MotionDetector m = new MotionDetector();
		//Helper.resizeImg("resized.jpg", "resized.jpg", 1280, 720);
		Helper.resizeImg("balls.jpg", "balls-resized.jpg", 500, 500);
		System.out.println(m.checkMovement("casera3.jpeg", "casera4.jpeg"));
		
		ObjectRecognizer o = new ObjectRecognizer();
		o.setThresholdValues(70, 210);
		o.getContours("me1.jpg");
		//o.proofVideo("video1.mp4");
		//o.webCam();
	}
	
}