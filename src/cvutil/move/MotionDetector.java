package cvutil.move;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cvutil.Recognizer;

public class MotionDetector extends Recognizer {

	public boolean checkMovement(String img1, String img2) {
		Mat image1 = Imgcodecs.imread(img1);
		Mat image2 = Imgcodecs.imread(img2);

		Mat grayImg1 = new Mat();
		Mat grayImg2 = new Mat();

		Imgproc.cvtColor(image1, grayImg1, Imgproc.COLOR_BGR2GRAY); // Hacer un gaussian blur (recomendable)
		Imgproc.cvtColor(image2, grayImg2, Imgproc.COLOR_BGR2GRAY);
		
		Mat diffImage = new Mat();
		// si es muy grande el diffImage (en porcentaje, puede ser) entonces son imagenes distintas
		Core.absdiff(grayImg1, grayImg2, diffImage);
		Imgcodecs.imwrite("diferencia.jpg", diffImage);
		
		double thresholdValue = 70.0;
		Mat thresholdImage = new Mat();
		Imgproc.threshold(diffImage, thresholdImage, thresholdValue, 255, Imgproc.THRESH_BINARY);
		
		int diffPixels = Core.countNonZero(thresholdImage);
		System.out.println("Pixels diff: " + diffPixels); // REMOVE THIS
		
		int allowDiff = 10000; // Cantidad de pixeles que determina si hubo o no movimiento
		
		Imgcodecs.imwrite("tresholdimg.jpg", thresholdImage);
		
		return (diffPixels > allowDiff);
	}

}