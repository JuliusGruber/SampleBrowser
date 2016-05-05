import java.util.ArrayList;
import java.util.HashSet;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class BasketManager2 extends MaxObject{
	HashSet <Sample> currentlyInBasket;
	ArrayList<BasketItem2> basketItems;
	ArrayList <Sample> samplesInBasketList;
	MaxPatcher parentPatcher;
	MaxBox viewManagerSend;
	MaxBox basketGate;
	
	
	public BasketManager2(){
		currentlyInBasket =  new HashSet<Sample>();
		basketItems =  new ArrayList<BasketItem2>();
		samplesInBasketList  = new ArrayList<Sample>();
		parentPatcher = this.getParentPatcher();
		viewManagerSend = parentPatcher.getNamedBox("viewManagerSend");
		basketGate = parentPatcher.getNamedBox("basketGate");
		
	}
	
	
	public void createBasketItems(int numberOfBasketItems){
		for (int i = 0; i <numberOfBasketItems; i++){
			
			basketItems.add(new BasketItem2("", parentPatcher,i));
		}
	}
	
	public void appendSamplesToBasket(String [] filePathArray){
		System.out.println("appendSamplesToBasket() method was called");
		ArrayList <String>  samplesToUpdate = new ArrayList<String>();
	
		for(int i = 0; i< filePathArray.length; i++){
			post(filePathArray[i]);
			
			Sample testSample = new Sample(filePathArray[i]);
			if(!currentlyInBasket.contains(testSample)){
				
				//what is the position of the newly added Sample in the basket?
				int positionInBasket = samplesInBasketList.size();
				//get the Index of the next free basket Item
				int  indexNextFreeBasketItem = 0;
				for (int k =0; k < basketItems.size(); k++){
					if(!basketItems.get(k).isInBasket()){
						indexNextFreeBasketItem = k;
						break;
					}
					
				}
				basketItems.get(indexNextFreeBasketItem).showBasketItem(filePathArray[i], positionInBasket);
				
				
				samplesInBasketList.add(testSample);
				currentlyInBasket.add(testSample);
				samplesToUpdate.add( filePathArray[i]);
				
				post("size samplesInBasketList: "+samplesInBasketList.size());
				
			}else{
				post("Is already in Basket: "+ testSample.getFilePath());
			}
		}
		
		//update the views
		if(samplesToUpdate.size()>0){
			sendAppendInfoToViews(samplesToUpdate);
		}
		
	
		
	}
	
	private void sendAppendInfoToViews(ArrayList <String> filePathList){
		post("sendAppendInfoToViews() method was called");
//		for(String filePath : filePathList){
//			post(filePath);
//		}
		
		Atom [] atomSendArray  = new Atom [filePathList.size()*2+1];
		atomSendArray[0]= Atom.newAtom("setSelectedSamples");
		
		int loopCounter = 1;
		for(int i = 0; i< filePathList.size(); i++){
			atomSendArray[loopCounter]= Atom.newAtom(filePathList.get(i));
			atomSendArray[loopCounter+1]= Atom.newAtom("quad");
			loopCounter = loopCounter +2;
			
		}
		
	
		
		viewManagerSend.send("selectSamplesInAllViews", atomSendArray);
	}
	
	public void sendButtonInfoToAllViews(String [] buttonInfo){
		post("sendButtonInfoToAllViews() method was called");
		post("filePath: "+buttonInfo[0]);
		post("shape: "+ buttonInfo[1]);
		Atom [] sendArray  = new Atom []{Atom.newAtom("setSelectedSamples"),Atom.newAtom(buttonInfo[0]), Atom.newAtom(buttonInfo[1])};
		viewManagerSend.send("selectSamplesInAllViews", sendArray);
	}
	

	
	
	public void removeBasketItem(String filePath){
		
		
		
			post("removeBasketItem() method was called : "+filePath);
			
			Atom [] sendArray  = new Atom []{Atom.newAtom("unSelectSamples"),Atom.newAtom(filePath), Atom.newAtom("circle")};
			viewManagerSend.send("unSelectSamplesInAllViews", sendArray);
		
			currentlyInBasket.remove(new Sample(filePath));
			
			int removeIndex = 0;
			for(int i = 0; i< samplesInBasketList.size(); i++){
				if(samplesInBasketList.get(i).getFilePath().equals(filePath)){
					samplesInBasketList.remove(i);
					removeIndex  = i;
					break;
				}
			}
			
			
			for(BasketItem2  basketItem : basketItems ){
				if(basketItem.getFilePath().equals(filePath)){
					basketItem.hideBasketItem(filePath);
					break;
				}
			}
			
			
			
			
			//move the basketItems after the removed basketItem upwards
			HashSet <Sample> samplesToMoveUpwards = new HashSet <Sample>();
			for(int k = removeIndex; k < samplesInBasketList.size(); k++){
				samplesToMoveUpwards.add(samplesInBasketList.get(k));
			}
			
			for(BasketItem2  basketItem : basketItems){
				if(samplesToMoveUpwards.contains(new Sample(basketItem.getFilePath()))){
					basketItem.moveUpOneSlot();
				}
			}
			
		
		
		post("size currentlyInBasket after single remove: "+currentlyInBasket.size());
		
	}
	
	public void clearAllPlaylistObjects(){
	
		
		for(BasketItem2  basketItem : basketItems){
		
				basketItem.getPlaylistObject().send("clear", null);
			
		}
	}
	
	
	public void removeAllSamples(){
		post("removeAllSampels() method was called");
	
		//close the gate
		//basketGate.send(0);
		
		//clearAllPlaylistObjects();
		
		
		Atom [] sendArray =  new Atom [basketItems.size()*2 +1];
		sendArray[0]= Atom.newAtom("unSelectSamples");
		int loopCounter = 1;
		for(BasketItem2 basketItem : basketItems){
			
			sendArray[loopCounter]= Atom.newAtom(basketItem.getFilePath());
			sendArray[loopCounter+1]= Atom.newAtom("circle");
			
			//basketItem.hideBasketItem(basketItem.getFilePath());
			//basketItem.clearPlaylistObject();
			
			loopCounter  = loopCounter+2;
		}
		viewManagerSend.send("unSelectSamplesInAllViews", sendArray);
		

		
		
		
		for(int i = 0; i< basketItems.size();i++){
				post("Trying to remove: "+basketItems.get(i).getFilePath());
				//basketItem.getPlaylistObject().send("clear", new Atom []{});
				
				basketItems.get(i).clearPlaylistObject();
				
				basketItems.get(i).hideBasketItem(basketItems.get(i).getFilePath());
				
				
			
			
		}
		
		
		currentlyInBasket =  new HashSet<Sample>();
		samplesInBasketList  = new ArrayList<Sample>();
	
	}
	
	
}
