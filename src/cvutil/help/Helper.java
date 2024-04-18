package cvutil.help;

import java.awt.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Helper {
	
	public static void resizeImg(String imgName, String newImgName, double width, double height) {
		Mat image = Imgcodecs.imread(imgName);
		Mat resizedImage = new Mat();
		
		Imgproc.resize(image, resizedImage, new Size(width, height));
		
		Imgcodecs.imwrite(newImgName, resizedImage);
	}
	
	public static Mat getResizedFrame(Mat src, double width, double height) {
		Mat resized = new Mat();
		Imgproc.resize(src, resized, new Size(width, height));
		return resized;
	}
	
	public static Image matToBufferedImage(Mat mat) {
		return HighGui.toBufferedImage(mat);
	}
	
	public static void loadLibrary() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
}