import java.util.ArrayList;
import java.util.HashMap;



import com.cycling74.max.*;


public class ViewsManagement1 extends MaxObject{
	
	private ArrayList <View1> viewsArray; 
	private HashMap <String,Sample>  polyAdressLookUp;
	
	public ViewsManagement1 (){
		viewsArray = 	new ArrayList<View1>();
		polyAdressLookUp = new HashMap<String,Sample>();
		
	}
	
	public void update(){
		
	}
	

	
	public  void addView(Atom [] viewData){
		//System.out.println("addView() method was called");
		
//		for(int i = 0; i<viewData.length;i++){
//			post(viewData[i].toString());
//			
//		}
	
		int viewNumber = viewsArray.size();
		ArrayList<Sample> sampleList = createSamplesArrayList(viewData);
		MaxPatcher p = this.getParentPatcher();
		MaxBox viewsBpatcher = p.getNamedBox("viewsBpatcher");
		MaxPatcher viewsPatcher = viewsBpatcher.getSubPatcher();
		
		View1 thisView =  new View1(viewNumber, viewsPatcher, sampleList, viewData);
		//thisView.jsui.send("anything", viewData);
		viewsArray.add(thisView);
		
		

	}
	
	
	private ArrayList<Sample> createSamplesArrayList(Atom[] sampleAtomArray) {
		
		ArrayList <Sample> returnList = new ArrayList<Sample>();
		
		for (int i = 1; i < sampleAtomArray.length; i = i+4){// starting from 1 because the sampleAtomAray has the method name as  the first argument 
			String filePath = sampleAtomArray[i].getString();
			String fileName = sampleAtomArray[i+1].getString();
			float xPosition = sampleAtomArray[i+2].toFloat();
			float yPosition = sampleAtomArray[i+3].toFloat();
			//look up the polyAdress
			Sample lookUpSample = polyAdressLookUp.get(filePath);
			int polyAdress = lookUpSample.getPolyAdress();
			Sample curSample = new Sample(filePath, fileName, xPosition, yPosition, polyAdress);
			

			returnList.add(curSample);
		}
		
		
		return returnList;
	
		
	}
	

	
	public void getSelectedView(){
		for (int i = 0; i < this.viewsArray.size(); i++) {
		
			if(viewsArray.get(i).isSelected){
				post("Currently selected view: "+viewsArray.get(i).viewName);
			}
			
		}
	}
	
	
	public void setSelectedView(String viewName){
		
		System.out.println("VIEW MANAGER setSelctedView() method was called with: " +viewName);
		
		View1 selectedView = null;
		for (int i = 0; i < this.viewsArray.size(); i++) {
			String compareName = viewsArray.get(i).viewName;
			if(viewName.equals(compareName)){
				selectedView = viewsArray.get(i);
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
		
		//prepare the data for the jsui of the sonoArea
		ArrayList<Sample> sampleList = selectedView.sampleList;
		Atom [] sonoAreaAtomArray = createSonoAreaAtomArray(sampleList);
		
		//send the sample data to the sonoArea
		MaxPatcher p = this.getParentPatcher();
		MaxBox sonoAreaBpatcher = p.getNamedBox("sonoAreaBpatcher");
		MaxPatcher sonoArea = sonoAreaBpatcher.getSubPatcher();
		MaxBox jsuiSonoArea = sonoArea.getNamedBox("jsuiSonoArea");
		
		jsuiSonoArea.send("list", sonoAreaAtomArray);


	}
	
	private Atom [] createSonoAreaAtomArray(ArrayList <Sample> sampleList){
		Atom [] returnAtomArray = new Atom [sampleList.size()*5 +1];
		String messageString = "setSampleData";
		returnAtomArray[0]= Atom.newAtom(messageString);
		
		int loopCounter = 1;
		for(Sample sample : sampleList){
			String filePath = sample.getFilePath();
			String fileName = sample.getFileName();
			int polyAdress = sample.getPolyAdress();
			double x = sample.getxPosition();
			double y = sample.getyPostion();
			
			returnAtomArray[loopCounter]= Atom.newAtom(filePath);
			returnAtomArray[loopCounter+1]= Atom.newAtom(fileName);
			returnAtomArray[loopCounter+2]= Atom.newAtom(polyAdress);
			returnAtomArray[loopCounter+3]= Atom.newAtom(x);
			returnAtomArray[loopCounter+4]= Atom.newAtom(y);
			
			loopCounter = loopCounter+5;
		}
		
		
		
		
		return returnAtomArray;
		
	}
	
	
	public void deleteAllViews(){
		
		
		
		viewsArray = new ArrayList<View1>();
		MaxPatcher p = this.getParentPatcher();
		MaxBox viewBpatcher = p.getNamedBox("viewsBpatcher");
	    MaxPatcher viewsPatcher = viewBpatcher.getSubPatcher();
	    MaxBox [] boxes = viewsPatcher.getAllBoxes();
        for(int i = 0; i < boxes.length; i++)
        {
           MaxBox b = boxes[i];
           if(b.getMaxClass().equals("jsui")|| b.getMaxClass().equals("panel")) {
        	   b.remove();
           }
        
           //post("Box "+i+": "+b.getName()+"  "+b.getMaxClass());
        }
		
		
		
	}
	
	

	

	
	//called from Threader before the FE starts
	public void setPolyLookUp(Atom [] polyNoFilePath) {
		post("setPolyLookUp() method was called");
		
		HashMap <String, Sample> sampleMap =  new HashMap <String, Sample>();
		for(int i = 0; i< polyNoFilePath.length; i= i+2){
			post("polyNumber: "+polyNoFilePath[i]+ " filePath: "+polyNoFilePath[i+1]);
			int polyNumber =  polyNoFilePath[i].getInt();
			String filePath =  polyNoFilePath[i+1].getString();
			Sample curSample =  new Sample (polyNumber, filePath);
			sampleMap.put(curSample.getFilePath(), curSample);
		}
	
		this.setPolyAdressLookUp(sampleMap);
	}

	public HashMap<String,Sample> getPolyAdressLookUp() {
		return polyAdressLookUp;
	}

	public void setPolyAdressLookUp(HashMap<String, Sample> polyAdressLookUp) {
		this.polyAdressLookUp = polyAdressLookUp;
	}
	
	
}
