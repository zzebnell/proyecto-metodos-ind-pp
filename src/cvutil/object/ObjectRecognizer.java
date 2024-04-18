package cvutil.object;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import cvutil.Recognizer;
import cvutil.help.Helper;

public class ObjectRecognizer extends Recognizer {

	public void detectFaces(String imgName) {
		Mat img = Imgcodecs.imread(imgName);
		Mat grayImg = new Mat();

		Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);

		CascadeClassifier faceCascade = new CascadeClassifier("haarcascade_frontalface_alt.xml");
		MatOfRect faces = new MatOfRect();
		faceCascade.detectMultiScale(grayImg, faces);

		List<Rect> faceList = new ArrayList<Rect>(faces.toList());
		for (Rect face : faceList) {
			Imgproc.rectangle(img, face, new Scalar(0, 255, 0), 2);
		}

		Imgcodecs.imwrite("facesImage.jpg", img);
	}

	public void proofVideo(String videoName) {
		VideoCapture capture = new VideoCapture(videoName);
		Mat frame = new Mat();

		HighGui.namedWindow("Video", HighGui.WINDOW_NORMAL);
		HighGui.resizeWindow("Video", 1280, 720);
		int frames = 0;

		while (capture.read(frame)) {
			frames++;
			Mat resizedFrame = Helper.getResizedFrame(frame, 1280, 720);
			Mat grayFrame = new Mat();
			Imgproc.cvtColor(resizedFrame, grayFrame, Imgproc.COLOR_BGR2GRAY);
			HighGui.imshow("Video", grayFrame);
			HighGui.waitKey(15);
		}

		capture.release();
		HighGui.destroyWindow("Video");

		System.out.println("FIN DEL VIDEO" + " " + frames + " Frames mostrados");
	}

	public void webCam() {
		VideoCapture capture = new VideoCapture(0);
		Mat frame = new Mat();
		boolean[] windowOpen = { false };
		// Crea un JFrame para mostrar los frames de la cámara
		JFrame window = new JFrame("Camera");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				windowOpen[0] = false;
				capture.release();
			}
		});

		/*
		 * if (capture.isOpened()) { while (capture.read(frame)) { Mat newFrame =
		 * getFrameFaceDetection(frame); int key = HighGui.waitKey(100);
		 * HighGui.toBufferedImage(newFrame); if (key == 27) { break; } } }
		 */

		if (!capture.isOpened()) {
			return;
		}

		JLabel label = new JLabel();
		capture.read(frame);
		window.getContentPane().add(label);
		window.setSize(frame.width(), frame.height());
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		windowOpen[0] = true;

		while (windowOpen[0]) {
			if (capture.read(frame)) {
				Mat faceFrame = getFrameFaceDetection(frame);
				ImageIcon image = new ImageIcon(Helper.matToBufferedImage(faceFrame));
				label.setIcon(image);
			} else {
				System.out.println("No se pudo leer frame");
			}
		}

		System.out.println("Fuera del bucle");

	}

	public Mat getFrameFaceDetection(Mat frame) {
		Mat frameFace = new Mat();
		Mat grayFrame = new Mat();

		frame.copyTo(frameFace);
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGRA2GRAY);
		CascadeClassifier faceCascade = new CascadeClassifier("haarcascade_frontalface_alt.xml");

		MatOfRect faces = new MatOfRect();
		faceCascade.detectMultiScale(grayFrame, faces);

		List<Rect> faceList = new ArrayList<Rect>(faces.toList());
		for (Rect face : faceList) {
			Imgproc.rectangle(frameFace, face, new Scalar(0, 255, 0), 1);
		}

		return frameFace;
	}

	public void getContours(String s) {
		Mat frame = Imgcodecs.imread(s);

		Mat blurredImage = new Mat();
		Mat grayImage = new Mat();
		Mat detectedEdges = new Mat();

		Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(grayImage, blurredImage, new Size(5, 5), 0);
		
		Imgproc.Canny(blurredImage, detectedEdges, minThresholdVal, maxThresholdVal, 3, false);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(detectedEdges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		System.out.println("Objects: " + contours.size());


		for (int i = 0; i < contours.size(); i++) {
			Imgproc.drawContours(frame, contours, i, new Scalar(170, 0, 80), 2);
		}

		Imgcodecs.imwrite("blurred.jpg", blurredImage);
		Imgcodecs.imwrite("edges-blurred.jpg", detectedEdges);
		Imgcodecs.imwrite("image-with-contours.jpg", frame);

		//return blurredImage;
	}

}