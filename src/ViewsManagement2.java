import java.util.ArrayList;
import java.util.HashMap;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class ViewsManagement2 extends MaxObject {
	private ArrayList<View2> viewsList;
	private ArrayList<String> viewNamesList;
	private HashMap <String, Sample> inBasketLookUp;
	private HashMap<String,Sample> polyAdressLookUp;
	
	private MaxPatcher parentPatcher;
	//private MaxBox sonoAreaSend;
	
	public ViewsManagement2(){
		viewsList = 	new ArrayList<View2>();
		viewNamesList  = new ArrayList<String>();
		inBasketLookUp = new HashMap<String, Sample>();
		polyAdressLookUp = new HashMap<String,Sample>();
		
		parentPatcher = this.getParentPatcher();
		//sonoAreaSend = parentPatcher.getNamedBox("sonoAreaSend");

		
		for (int i = 0; i<20; i++){
			String viewName = "view"+ i;
			viewNamesList.add(viewName);
			View2 thisView =  new View2(i, parentPatcher, viewName);
			viewsList.add(thisView);
			
		}
	}
	
	
	public void setViewData(Atom [] viewData){
		post("setViewData() metod was called");
		for(View2 view : viewsList){
			if(!view.isUsed()){
				view.setUsed(true);
				ArrayList<Sample> sampleList = createSamplesArrayList(viewData);
				view.setSampleList(sampleList);
				view.getJsui().send("list",viewData);
				break;
			}
		}
	
		
	}
	
	public void resetAllViews(){
		for (View2 view : viewsList){
			view.getJsui().send("clearView", null);
			view.setUsed(false);
			view.setSelected(false);
			view.setSampleList(new ArrayList<Sample>());
			view.getViewPanel().send("bgfillcolor", new Atom []{Atom.newAtom(1), Atom.newAtom (1),Atom.newAtom(1), Atom.newAtom (1)});
		}
	}
	
	public void resetAllSamplesToUntouchedSampleColor(){
		post("resetAllSamplesToUntouchedSampleColor() was called");
		for (int i = 0; i < this.viewsList.size(); i++) {
			viewsList.get(i).getJsui().send("resetAllSamplesToUntouchedSampleColor", null);
		}
		
		MaxBox sonoAreaSend = parentPatcher.getNamedBox("sonoAreaSend");
		sonoAreaSend.send("resetAllSamplesToUntouchedSampleColor", null);
		
		inBasketLookUp = new HashMap<String, Sample>();
	}
	
	
	public void selectSamplesInAllViews(Atom  [] filePathAndShapeArray){
		post("selectSamplesInAllViews() method was called");
		
		//send selected Samples to all views
		for (int i = 0; i < this.viewsList.size(); i++) {
			viewsList.get(i).getJsui().send("list", filePathAndShapeArray);
			
		}
		MaxBox sonoAreaSend = parentPatcher.getNamedBox("sonoAreaSend");
		sonoAreaSend.send("list", filePathAndShapeArray);
		
		//add selected Samples to inBasketLookUp
		for (int i =1; i < filePathAndShapeArray.length;i = i+2){
			Sample curSample  =  new Sample(filePathAndShapeArray[i].getString(),filePathAndShapeArray[i+1].getString() );
			inBasketLookUp.put(curSample.getFilePath(), curSample);
		}
		
	}
	
	public void unSelectSamplesInAllViews(Atom [] filePathArray){
		post("VM unSelectSamplesInAllViews() method was called");
		
		//send  unselected Samples to all Views
		for (int i = 0; i < this.viewsList.size(); i++) {
			viewsList.get(i).getJsui().send("list", filePathArray);
		}
		
		MaxBox sonoAreaSend = parentPatcher.getNamedBox("sonoAreaSend");
		sonoAreaSend.send("list", filePathArray);
		
		
		//remove unselected Samples from inBasketLookUp
		for (int i =1; i < filePathArray.length;i++){
			Sample curSample  =  new Sample(filePathArray[i].getString());
			inBasketLookUp.remove(curSample.getFilePath());
		}
	}
	
	private ArrayList<Sample> createSamplesArrayList(Atom[] sampleAtomArray) {
		
		ArrayList <Sample> returnList = new ArrayList<Sample>();
		
		for (int i = 1; i < sampleAtomArray.length; i = i+4){// starting from 1 because the sampleAtomAray has the method name as  the first argument 
			String filePath = sampleAtomArray[i].getString();
			String fileName = sampleAtomArray[i+1].getString();
			float xPosition = sampleAtomArray[i+2].toFloat();
			float yPosition = sampleAtomArray[i+3].toFloat();
			
			//post("addView x: "+xPosition);
			//post("addView y: "+yPosition);
			
			//look up the polyAdress
			Sample lookUpSample = polyAdressLookUp.get(filePath);
			int polyAdress = lookUpSample.getPolyAdress();
			Sample curSample = new Sample(filePath, fileName, xPosition, yPosition, polyAdress, false, "circle");
			

			returnList.add(curSample);
		}
		
		
		return returnList;
	
		
	}
	
	public void getSelectedView(){
		for (int i = 0; i < this.viewsList.size(); i++) {
		
			if(viewsList.get(i).isSelected()){
				post("Currently selected view: "+viewsList.get(i).getViewName());
			}
			
		}
	}
	
	
	public void setSelectedView(String viewName){
		
		System.out.println("VIEW MANAGER setSelctedView() method was called with: " +viewName);
		
		int indexSelectedView  = viewNamesList.indexOf(viewName);
		View2 selectedView  = viewsList.get(indexSelectedView);
		selectedView.getViewPanel().send("bgfillcolor", new Atom []{Atom.newAtom(0.69), Atom.newAtom (0.69),Atom.newAtom(0.69), Atom.newAtom (1)});
		selectedView.setSelected(true);
		for(int i = 0; i < viewsList.size(); i++){
			if(i != indexSelectedView){
				viewsList.get(i).setSelected(false);
				viewsList.get(i).getViewPanel().send("bgfillcolor", new Atom []{Atom.newAtom(1), Atom.newAtom (1),Atom.newAtom(1), Atom.newAtom (1)});
			}
		}
	
	}
	
	public void sendSelectedViewDataToSonoArea(String viewName){
		int indexSelectedView  = viewNamesList.indexOf(viewName);
		View2 selectedView  = viewsList.get(indexSelectedView);
		ArrayList<Sample> sampleList = selectedView.getSampleList();
		Atom [] sonoAreaAtomArray = createSonoAreaAtomArray(sampleList);
		MaxBox sonoAreaSend = parentPatcher.getNamedBox("sonoAreaSend");
		sonoAreaSend.send("list", sonoAreaAtomArray);
	}
	
	private Atom [] createSonoAreaAtomArray(ArrayList <Sample> sampleList){
		Atom [] returnAtomArray = new Atom [sampleList.size()*7 +1];
		String messageString = "setSampleData";
		returnAtomArray[0]= Atom.newAtom(messageString);
		
		int loopCounter = 1;
		for(Sample sample : sampleList){
			String filePath = sample.getFilePath();
			String fileName = sample.getFileName();
			int polyAdress = sample.getPolyAdress();
			double x = sample.getxPosition();
			double y = sample.getyPostion();
			boolean isInBasket = false;
			String shape = sample.getShape();
			if(inBasketLookUp.containsKey(sample.getFilePath())){
			 isInBasket = true;
			 Sample inBasketSample = inBasketLookUp.get(sample.getFilePath());
			 shape = inBasketSample.getShape();
			}
			returnAtomArray[loopCounter]= Atom.newAtom(filePath);
			returnAtomArray[loopCounter+1]= Atom.newAtom(fileName);
			returnAtomArray[loopCounter+2]= Atom.newAtom(polyAdress);
			returnAtomArray[loopCounter+3]= Atom.newAtom(x);
			returnAtomArray[loopCounter+4]= Atom.newAtom(y);
			returnAtomArray[loopCounter+5]= Atom.newAtom(isInBasket);
			returnAtomArray[loopCounter+6]= Atom.newAtom(shape);
			
			loopCounter = loopCounter+7;
		}
		
		
		
		
		return returnAtomArray;
		
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


	public ArrayList<View2> getViewsList() {
		return viewsList;
	}


	public void setViewsList(ArrayList<View2> viewsList) {
		this.viewsList = viewsList;
	}


	public ArrayList<String> getViewNamesList() {
		return viewNamesList;
	}


	public void setViewNamesList(ArrayList<String> viewNamesList) {
		this.viewNamesList = viewNamesList;
	}


	public HashMap<String, Sample> getInBasketLookUp() {
		return inBasketLookUp;
	}


	public void setInBasketLookUp(HashMap<String, Sample> inBasketLookUp) {
		this.inBasketLookUp = inBasketLookUp;
	}


	public HashMap<String, Sample> getPolyAdressLookUp() {
		return polyAdressLookUp;
	}


	public void setPolyAdressLookUp(HashMap<String, Sample> polyAdressLookUp) {
		this.polyAdressLookUp = polyAdressLookUp;
	}
	
	
}
