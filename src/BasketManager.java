import java.util.HashSet;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class BasketManager extends MaxObject {
	HashSet <Sample> currentlyInBasket =  new HashSet<Sample>();
	HashSet<Sample> nextInBasket =  new HashSet<Sample>();
	
	
	public BasketManager(){
		declareIO(1,2);
	}
	
	public void appendSamples(String [] filePathArray){
		post("appendSample() method was called");
		
		for(int i = 0; i< filePathArray.length; i++){
			//post(filePathArray[i]);
			
			Sample testSample = new Sample(filePathArray[i]);
			if(!currentlyInBasket.contains(testSample)){
				currentlyInBasket.add(testSample);
				outlet(0, "append", filePathArray[i]);
				//this bang gets the playlist dict and sends it to the checkBasketChanges() method
				outlet(1, "bang");
			}else{
				post("Is already in Basket: "+ testSample.getFilePath());
			}
		}
		
		
		
	
		
	}
	
	
	public void checkBasketChanges(String [] filePathArray){
		post("checkBasketChanges() method was called");
		
		nextInBasket =  new HashSet<Sample>();
		
		for(int i = 0; i< filePathArray.length; i++){
			//post(filePathArray[i]);
			
			Sample curSample  = new Sample(filePathArray[i]);
			nextInBasket.add(curSample);
			
		}
		
		//get a handle for the viewManger Receiver
		MaxPatcher parentPatcher = this.getParentPatcher();
		MaxBox viewManagerSend = parentPatcher.getNamedBox("viewManagerSend");
		
		//////check for Samples that need to be selected
		int loopCounter  = 1;
		Atom [] atomSendArray;  
		
		
		HashSet <Sample> nextInBasketCopy  = new HashSet<Sample>(nextInBasket);
		nextInBasketCopy.removeAll(currentlyInBasket);
		
		atomSendArray= new Atom[nextInBasketCopy.size() +1];
		atomSendArray[0]= Atom.newAtom("setSelectedSamples");
		
		if(nextInBasketCopy.size()>0){
			
		
			for(Sample sample : nextInBasketCopy){
				atomSendArray[loopCounter] = Atom.newAtom(sample.getFilePath());
				loopCounter++;
			}
		
			viewManagerSend.send("selectSamplesInAllViews", atomSendArray);
		}
		
		
		
		
		//////check for samples that need to be unselected
		HashSet<Sample> currentlyInBasketCopy  = new HashSet<Sample>(currentlyInBasket);
		currentlyInBasketCopy.removeAll(nextInBasket);
		
		if(currentlyInBasketCopy.size()>0){
			atomSendArray  = new Atom[currentlyInBasketCopy.size() +1];
			atomSendArray[0]= Atom.newAtom("unSelectSamples");
			currentlyInBasketCopy.removeAll(nextInBasket);
		
			loopCounter = 1;
			for(Sample sample : currentlyInBasketCopy){
				atomSendArray[loopCounter] = Atom.newAtom(sample.getFilePath());
				loopCounter++;
			}
		
			viewManagerSend.send("unSelectSamplesInAllViews", atomSendArray);
		}
		
		//finally replace the old with the new basket Set
		currentlyInBasket = new HashSet <Sample>(nextInBasket);
		
	}
	
	
	

}
