import java.util.HashSet;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class BasketManager extends MaxObject {
	HashSet <Sample> currentlyInBasket =  new HashSet<Sample>();
	HashSet<Sample> nextInBasket =  new HashSet<Sample>();
	MaxPatcher parentPatcher;
	MaxBox symButtonManager;
	
	public BasketManager(){
		declareIO(1,2);
		parentPatcher = this.getParentPatcher();
		symButtonManager = parentPatcher.getNamedBox("symButtonManager");
	}
	
	public void appendSamples(String [] filePathArray){
		post("appendSample() method was called");
		
		for(int i = 0; i< filePathArray.length; i++){
			//post(filePathArray[i]);
			
			Sample testSample = new Sample(filePathArray[i]);
			if(!currentlyInBasket.contains(testSample)){
				//add to the playlist
				outlet(0, "append", filePathArray[i]);
				//add SymButtons
				symButtonManager.send("addSymbolButton", new Atom []{Atom.newAtom(filePathArray[i])});
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
		//post("nextInBasketCopy size: "+nextInBasketCopy.size());
		
		
		nextInBasketCopy.removeAll(currentlyInBasket);
		
		//post("nextInBasketCopy size: "+nextInBasketCopy.size());
		
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

	
	public void checkIfSingleRemove(String [] filePathArray){
		post("checkIfSingleRemove() method was called");
		HashSet <Sample> compareSet =  new HashSet <Sample>();
		for(int i = 0; i< filePathArray.length; i++){
			//post(filePathArray[i]);
			
			Sample curSample  = new Sample(filePathArray[i]);
			compareSet.add(curSample);
			
		}
		//if it is a remove operation the compare set is a subset of currentlyInBasket
		if(currentlyInBasket.containsAll(compareSet)){
			post("compareSet is a subset of currentlyInBasket");
			HashSet <Sample> currentlyInBasketCopy = new HashSet<Sample>(currentlyInBasket);
			currentlyInBasketCopy.removeAll (compareSet);
			if(currentlyInBasketCopy.size() == 1){
				post("currentlyInBasketCopy size: "+ currentlyInBasketCopy.size());
				Sample[] removeSampleArray  = currentlyInBasketCopy.toArray(new Sample [currentlyInBasketCopy.size()]);
				Sample removeSample  = removeSampleArray[0];
				post("Removing Sample: "+removeSample.getFilePath());
				
				
				
				//send message to reset the color
				MaxPatcher parentPatcher = this.getParentPatcher();
				MaxBox viewManagerSend = parentPatcher.getNamedBox("viewManagerSend");
			
				Atom [] sendAtomArray  = new Atom [2];
				sendAtomArray[0] = Atom.newAtom("unSelectSamples");
				sendAtomArray [1] = Atom.newAtom(removeSample.getFilePath());
				viewManagerSend.send("unSelectSamplesInAllViews", sendAtomArray);
				
				//remove the SymButton
				symButtonManager.send("removeSingleButton", new Atom []{Atom.newAtom(removeSample.getFilePath())});
				
				
				//remove the sample
				currentlyInBasket.remove(removeSample);
				
			
			}
		}
		
	}
	
	public void removeAllSamples(){
		//get a handle for the viewManger Receiver
		MaxPatcher parentPatcher = this.getParentPatcher();
		MaxBox viewManagerSend = parentPatcher.getNamedBox("viewManagerSend");
		
		viewManagerSend.send("resetAllSamplesToUntouchedSampleColor", null);
		
		symButtonManager.send("removeAllButtons", null);
		
		currentlyInBasket =  new HashSet<Sample>();
		nextInBasket =  new HashSet<Sample>();
	}
	
	

}
