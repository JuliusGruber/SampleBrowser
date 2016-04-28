import java.util.ArrayList;
import java.util.HashSet;


import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class BasketManager1 extends MaxObject{
	HashSet <Sample> currentlyInBasket;
	ArrayList<BasketItem> basketItems;
	MaxPatcher parentPatcher;
	
	public BasketManager1(){
		currentlyInBasket =  new HashSet<Sample>();
		basketItems =  new ArrayList<BasketItem>();
		parentPatcher = this.getParentPatcher();
	}
	
	public void appendSamples(String [] filePathArray){
		post("appendSample() method was called");
		
		for(int i = 0; i< filePathArray.length; i++){
			post(filePathArray[i]);
			
			Sample testSample = new Sample(filePathArray[i]);
			if(!currentlyInBasket.contains(testSample)){
				addSampleToBasket(filePathArray[i]);
			
			}else{
				post("Is already in Basket: "+ testSample.getFilePath());
			}
		}
		
		
		
	
		
	}
	
	
	
	public void addSampleToBasket(String filePath){
		BasketItem newBasketItem = new BasketItem(filePath, parentPatcher, basketItems.size());
		basketItems.add(newBasketItem);
	}
	
	
	
	public void removeBasketItem(String filePath){
		post("removeBasketItem() method: "+filePath);
		
		int removeIndex = 0;
		for(BasketItem basketItem : basketItems){
			if(basketItem.getFilePath().equals(filePath)){
				basketItem.getSymButton().getJsui().remove();
				basketItem.getSymButton().getPanel().remove();
				basketItem.getPlaylistObject().remove();
				basketItem.getDict().remove();
				basketItem.getDictListenerButton().remove();
				basketItem.getDictListener().remove();
				removeIndex = basketItems.indexOf(basketItem);
				basketItems.remove(removeIndex);
				break;
			}
		}
		
		
		
	}
	
	
}
