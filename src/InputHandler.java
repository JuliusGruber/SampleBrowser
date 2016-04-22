import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class InputHandler extends MaxObject{

	
	public InputHandler(){
	
		
	}
	
	
	public void addView(Atom[] atomArray){
		//post("addView () method in InputHandler was called");
		//post(atomArray.toString());
		
		
		MaxPatcher p = this.getParentPatcher();
		//MaxBox myBpatcher = p.getNamedBox("myBpatcher");
		//MaxPatcher viewPatcher  = myBpatcher.getSubPatcher();
		MaxBox viewManager =p.getNamedBox("viewManager");
		
		viewManager.send("addView", atomArray);
		
		
	}

}
