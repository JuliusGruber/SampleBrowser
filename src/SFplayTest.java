import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class SFplayTest extends MaxObject{
	
	public void sfTest(){
		post("sfTest () method was called" );
		MaxPatcher p = this.getParentPatcher();
		
		MaxBox sfPlay  = p.newObject("sfplay~", null);
		
		
	}

}
