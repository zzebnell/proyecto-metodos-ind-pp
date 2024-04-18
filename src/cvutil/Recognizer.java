package cvutil;

public abstract class Recognizer {
	
	protected double minThresholdVal = 70;
	protected double maxThresholdVal = 210;
	
	public void setThresholdValues(double minThresholdVal, double maxThresholdVal) {
		this.minThresholdVal = minThresholdVal;
		this.maxThresholdVal = maxThresholdVal;
	}
	
}