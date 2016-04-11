
public class Sample {
	
	private String  filePath;
	private String fileName;
	private double xPosition;
	private double yPosition;
	
	
	public Sample(String filePath, String fileName,double xPosition, double yPosition){
		this.filePath = filePath;
		this.fileName = fileName;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	public Sample(){
		this.filePath = null;
		this.fileName = null;
		this.xPosition = 0;
		this.yPosition = 0;
	}

	public String toString(){
		String sampleString = fileName+" with position: "+xPosition+" "+yPosition;
		
		return sampleString;
		
	}
	
	//get and set Methods
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public double getxPosition() {
		return xPosition;
	}
	public void setxPosition(double xPosition2) {
		this.xPosition = xPosition2;
	}
	public double getyPostion() {
		return yPosition;
	}
	public void setyPostion(double yPosition) {
		this.yPosition = yPosition;
	}
	
	

}
