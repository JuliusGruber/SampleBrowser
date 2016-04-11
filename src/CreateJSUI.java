
import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;


public class CreateJSUI extends MaxObject {
	MaxPatcher parentPatcher = null;
	
	public CreateJSUI() {
		parentPatcher = this.getParentPatcher();
	}
	
	public void addJSUI(){
		post("addJSUI() method was called");
		
		MaxBox jsui = parentPatcher.newDefault(500,20,"jsui",null);
		jsui.send("filename", new Atom []{ Atom.newAtom("C:/Users/Julius Gruber/Dropbox/Master/maxMSPpatches/viewScript.js")});
	}

}
