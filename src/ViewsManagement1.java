import java.util.ArrayList;

import com.cycling74.jitter.*;
import com.cycling74.max.*;


public class ViewsManagement1 extends MaxObject{
	
	private ArrayList <View1> viewsArray = new ArrayList<View1>();
	
	public void update(){
		
	}
	
	public void bang(){
		for (int i = 0; i < this.viewsArray.size(); i++) {
			
	
				//viewsArray.get(i).bang();
		}
	}
	
	
	public  void addView(Atom [] viewData){
		System.out.println("addView() method was called");
		
//		for(int i = 0; i<viewData.length;i++){
//			post(viewData[i].toString());
//			
//		}
	
		int viewNumber = viewsArray.size();
		ArrayList<Sample> sampleList = createSamplesArrayList(viewData);
		MaxPatcher p = this.getParentPatcher();
		View1 thisView =  new View1(viewNumber, p, sampleList, viewData);
		//thisView.jsui.send("anything", viewData);
		viewsArray.add(thisView);
		
		//outlet(0,"bang");

	}
	
	
	

	
	public void getSelectedView(){
		for (int i = 0; i < this.viewsArray.size(); i++) {
		
			if(viewsArray.get(i).isSelected){
				post("Currently selected view: "+viewsArray.get(i).viewName);
			}
			
		}
	}
	
	
	public void setSelectedView(String viewName){
		System.out.println("setSelctedView() method was called with: " +viewName);
		for (int i = 0; i < this.viewsArray.size(); i++) {
			String compareName = viewsArray.get(i).viewName;
			if(viewName.equals(compareName)){
				viewsArray.get(i).isSelected = true;
				//viewsArray.get(i).viewPanel.send("border", new Atom []{Atom.newAtom(20)});
				viewsArray.get(i).viewPanel.send("bordercolor", new Atom []{Atom.newAtom(1), Atom.newAtom (0),Atom.newAtom(0), Atom.newAtom (1)});
			}else{
			viewsArray.get(i).isSelected = false;
			//viewsArray.get(i).viewPanel.send("border", new Atom []{Atom.newAtom(10)});
			viewsArray.get(i).viewPanel.send("bordercolor", new Atom []{Atom.newAtom(1), Atom.newAtom (1),Atom.newAtom(1), Atom.newAtom (1)});
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
           if(b.getMaxClass().equals("jsui")|| b.getMaxClass().equals("panel")) {
        	   b.remove();
           }
        
           //post("Box "+i+": "+b.getName()+"  "+b.getMaxClass());
        }
		
		
		
	}
	
	

	
	private ArrayList<Sample> createSamplesArrayList(Atom[] sampleAtomArray) {
		
		ArrayList <Sample> returnList = new ArrayList<Sample>();
		for (int i = 1; i < sampleAtomArray.length; i = i+4){// starting from 1 because the sampleAtomAray has the method name as  the first argument 
			String filePath = sampleAtomArray[i].getString();
			String fileName = sampleAtomArray[i+1].getString();
			float xPosition = sampleAtomArray[i+2].toFloat();
			float yPosition = sampleAtomArray[i+3].toFloat();
			Sample curSample = new Sample(filePath, fileName, xPosition, yPosition);
			returnList.add(curSample);
		}
		
		
		return returnList;
	
		
	}
	
	
}
