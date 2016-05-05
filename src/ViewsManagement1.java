import java.util.ArrayList;
import java.util.HashMap;



import com.cycling74.max.*;


public class ViewsManagement1 extends MaxObject{
	
	private ArrayList <View1> viewsList; 
	private HashMap <String,Sample>  polyAdressLookUp;
	private HashMap <String, Sample> inBasketLookUp;
	MaxPatcher p;
	MaxBox jsuiSonoArea;
	MaxPatcher viewsPatcher;
	
	public ViewsManagement1 (){
		viewsList = 	new ArrayList<View1>();
		polyAdressLookUp = new HashMap<String,Sample>();
		inBasketLookUp = new HashMap<String, Sample>();
		
		p = this.getParentPatcher();
		MaxBox sonoAreaBpatcher = p.getNamedBox("sonoAreaBpatcher");
		MaxPatcher sonoArea = sonoAreaBpatcher.getSubPatcher();
		jsuiSonoArea = sonoArea.getNamedBox("jsuiSonoArea");
		
	
		
		
	}
	
	public void selectSamplesInAllViews(Atom  [] filePathAndShapeArray){
		post("selectSamplesInAllViews() method was called");
		
		//send selected Samples to all views
		for (int i = 0; i < this.viewsList.size(); i++) {
			viewsList.get(i).getJsui().send("list", filePathAndShapeArray);
			
		}

		jsuiSonoArea.send("list", filePathAndShapeArray);
		
		//add selected Samples to inBasketLookUp
		for (int i =1; i < filePathAndShapeArray.length;i = i+2){
			Sample curSample  =  new Sample(filePathAndShapeArray[i].getString(),filePathAndShapeArray[i+1].getString() );
			inBasketLookUp.put(curSample.getFilePath(), curSample);
		}
		
	}
	
	public void unSelectSamplesInAllViews(Atom [] filePathArray){
		post("unSelectSamplesInAllViews() method was called");
		
		//send  unselected Samples to all Views
		for (int i = 0; i < this.viewsList.size(); i++) {
			viewsList.get(i).getJsui().send("list", filePathArray);
		}
		

		jsuiSonoArea.send("list", filePathArray);
		
		//remove unselected Samples from inBasketLookUp
		for (int i =1; i < filePathArray.length;i++){
			Sample curSample  =  new Sample(filePathArray[i].getString());
			inBasketLookUp.remove(curSample.getFilePath());
		}
	}
	
	
	public void resetAllSamplesToUntouchedSampleColor(){
		post("resetAllSamplesToUntouchedSampleColor() was called");
		for (int i = 0; i < this.viewsList.size(); i++) {
			viewsList.get(i).getJsui().send("resetAllSamplesToUntouchedSampleColor", null);
		}
		
		jsuiSonoArea.send("resetAllSamplesToUntouchedSampleColor", null);
		
		inBasketLookUp = new HashMap<String, Sample>();
	}
	

	
	public  void addView(Atom [] viewData){
		//System.out.println("addView() method was called");
		
		MaxBox viewsBpatcher = p.getNamedBox("viewsBpatcher");
		MaxPatcher viewsPatcher = viewsBpatcher.getSubPatcher();
	
		int viewNumber = viewsList.size();
		ArrayList<Sample> sampleList = createSamplesArrayList(viewData);
		
		
		View1 thisView =  new View1(viewNumber, viewsPatcher, sampleList, viewData);
		//thisView.jsui.send("anything", viewData);
		viewsList.add(thisView);
		
		

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
		
		View1 selectedView = null;
		for (int i = 0; i < this.viewsList.size(); i++) {
			String compareName = viewsList.get(i).getViewName();
			if(viewName.equals(compareName)){
				selectedView = viewsList.get(i);
				viewsList.get(i).setSelected(true);
				//viewsArray.get(i).viewPanel.send("border", new Atom []{Atom.newAtom(20)});
				viewsList.get(i).getViewPanel().send("bordercolor", new Atom []{Atom.newAtom(1), Atom.newAtom (0),Atom.newAtom(0), Atom.newAtom (1)});
			}else{
				viewsList.get(i).setSelected(false);
				//viewsArray.get(i).viewPanel.send("border", new Atom []{Atom.newAtom(10)});
				viewsList.get(i).getViewPanel().send("bordercolor", new Atom []{Atom.newAtom(1), Atom.newAtom (1),Atom.newAtom(1), Atom.newAtom (1)});
			}
		}
		//getSelectedView();
		
		//prepare the data for the jsui of the sonoArea
		ArrayList<Sample> sampleList = selectedView.getSampleList();
		Atom [] sonoAreaAtomArray = createSonoAreaAtomArray(sampleList);
		

		
		jsuiSonoArea.send("list", sonoAreaAtomArray);


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
	
	
	public void deleteAllViews(){
		
		for(View1 view : viewsList){
			view.getJsui().remove();
			view.getViewPanel().remove();
		}
		

		
        viewsList = 	new ArrayList<View1>();
		polyAdressLookUp = new HashMap<String,Sample>();
		//inBasketLookUp = new HashMap<String, Sample>();
		
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
