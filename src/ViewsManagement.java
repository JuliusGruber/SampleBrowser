import java.util.ArrayList;

import com.cycling74.jitter.*;
import com.cycling74.max.*;


public class ViewsManagement extends MaxObject{
	
	private ArrayList <View> viewsArray ;
	private ArrayList <Sample>  polyNumberFilePath ;
	
	public ViewsManagement (){
		viewsArray = 	new ArrayList<View>();
		polyNumberFilePath = (new ArrayList<Sample>());
		
	}
			
			
	public void update(){
		
	}
	
	public void bang(){
		for (int i = 0; i < this.viewsArray.size(); i++) {
			
	
				viewsArray.get(i).bang();
		}
	}
	
	
	public void addView(){
		System.out.println("addView() method was called");
		int viewNumber = viewsArray.size();
		
		MaxPatcher p = this.getParentPatcher();
		View thisView =  new View(p,viewNumber);
		viewsArray.add(thisView);
		
		outlet(0,"bang");

	}
	
	
	
	public void addView(Atom [] sampleAtomArray){
		System.out.println("addView() method was called");
		int viewNumber = viewsArray.size();
		ArrayList <Sample> returnList = createSamplesArrayList(sampleAtomArray);
		MaxPatcher p = this.getParentPatcher();
		View thisView =  new View(p,viewNumber, returnList);
		viewsArray.add(thisView);
		
		outlet(0,"bang");

		 
	}
	
	public void getSelectedView(){
		for (int i = 0; i < this.viewsArray.size(); i++) {
		
			if(viewsArray.get(i).isSelected){
				System.out.println("Currently selected view: "+viewsArray.get(i).pWindow.getName().toString());
			}
			
		}
	}
	
	
	public void setSelectedView(Atom [] args){
		System.out.println("setSelctedView(): "+args[0]);
		for (int i = 0; i < this.viewsArray.size(); i++) {
			String newSelectedName = args[0].toString();
			String compareName = viewsArray.get(i).pWindow.getName();
			if(newSelectedName.equals(compareName)){
				viewsArray.get(i).isSelected = true;
			}else{
				viewsArray.get(i).isSelected = false;
			}
			
		}
		
		//getSelectedView();
	}
	
	
	public void deleteAllViews(){
		viewsArray.clear();
		MaxPatcher p = this.getParentPatcher();
	    MaxBox[] boxes = p.getAllBoxes();
        for(int i = 0; i < boxes.length; i++)
        {
           MaxBox b = boxes[i];
           if(b.getMaxClass().equals("jit.pwindow")) {
        	   b.remove();
           }
           //post("Box "+i+": "+b.getName()+"  "+b.getMaxClass());
        }
		
		
		
	}
	
	
	public void removeView(){
		
	}
	
	private ArrayList<Sample> createSamplesArrayList(Atom[] sampleAtomArray) {
		
		ArrayList <Sample> returnList = new ArrayList<Sample>();
		for (int i = 0; i < sampleAtomArray.length; i = i+4){
			String filePath = sampleAtomArray[i].getString();
			String fileName = sampleAtomArray[i+1].getString();
			float xPosition = sampleAtomArray[i+2].toFloat();
			float yPosition = sampleAtomArray[i+3].toFloat();
			Sample curSample = new Sample(filePath, fileName, xPosition, yPosition);
			returnList.add(curSample);
		}
		
		
		return returnList;
	
		
	}


	public ArrayList <Sample> getPolyNumberFilePath() {
		return polyNumberFilePath;
	}



	
	
}
