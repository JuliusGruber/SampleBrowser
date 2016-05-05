import java.util.ArrayList;
import java.util.HashSet;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class BasketManager3 extends MaxObject {
	HashSet<Sample> currentlyInBasketSet;
	HashSet<Sample> newSet;
	ArrayList<Sample> currentlyInBasketList;
	ArrayList<Sample> newList;
	MaxPatcher parentPatcher;
	MaxBox playlist;
	
	public BasketManager3(){
		currentlyInBasketSet = new HashSet<Sample>();
		newSet=  new HashSet<Sample>();
		currentlyInBasketList = new ArrayList<Sample>();
		newList = new ArrayList<Sample>();
		parentPatcher = this.getParentPatcher();
		
	}
	
	
	public void checkBasketChange(String [] filePathArray){
		post("checkBasketChange() method was called");
		
//		post("State of currentlyInBasketSet:");
//		for(Sample sample : currentlyInBasketSet){
//			post(sample.getFilePath());
//		}
//		post("State of currentlyInBasketList:");
//		for(Sample sample : currentlyInBasketList){
//			post(sample.getFilePath());
//		}
		
		//check for playlist clear
		boolean allSamplesRemoved =  false;
		if(filePathArray.length == 0){
			post("All samples were removed");
			currentlyInBasketSet = new HashSet<Sample>();
			currentlyInBasketList = new ArrayList<Sample>();
			allSamplesRemoved = true;
		}
		
		if(!allSamplesRemoved){
			//check if user moved the mouse over the drag handle and triggered the dict output
			boolean mouseOverDragHandle  = false;
			for(int k  =0; k< filePathArray.length; k++){
				newSet.add(new Sample(filePathArray[k]));
				newList.add(new Sample(filePathArray[k]));
			}
		
			if(newSet.equals(currentlyInBasketSet )&& newList.equals(currentlyInBasketList)){
				mouseOverDragHandle  = true;
			}
		
			post("mouseOverDragHandle: "  +mouseOverDragHandle);
		
			if(!mouseOverDragHandle){
				
				boolean reorderingHappend  = false;
				if(newSet.equals(currentlyInBasketSet )&& !newList.equals(currentlyInBasketList)){
					post("Some reordering happend");
					reorderingHappend  = true;
					
				}
				
				post("reorderingHappend:"+reorderingHappend);
				
				if(!reorderingHappend){
					ArrayList<Sample> newSamples  = new ArrayList<Sample>();
					for(Sample sample : newList){
						if(!currentlyInBasketSet.contains(sample)){
							newSamples.add(sample);
							currentlyInBasketSet.add(sample);
							currentlyInBasketList.add(0,sample);
						}
					}
		
					for(Sample sample : newSamples){
						post("New Sample: "+sample.getFilePath());
					}
				}

			}
		}
		
		newList  = new ArrayList<Sample>();
		newSet = new HashSet<Sample>();
		
	}

	
	public void appendSamplesToBasket(String [] filePathArray){
		//post("appendSamplesToBasket() method was called");
		MaxBox playList  = parentPatcher.getNamedBox("playlistObject");
		
	
		for(int i = 0; i< filePathArray.length; i++){
			//post(filePathArray[i]);
			Sample testSample = new Sample(filePathArray[i]);
			if(!currentlyInBasketSet.contains(testSample)){
				//currentlyInBasketSet.add(testSample);
				//currentlyInBasketList.add(0,testSample);
				playList.send("append", new Atom []{Atom.newAtom(filePathArray[i]), Atom.newAtom(1)});
			}
		}
		
		
	}
	
	
}//class end
