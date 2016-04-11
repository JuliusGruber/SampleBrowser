import com.cycling74.max.Callback;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxClock;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class Maxboxtest extends MaxObject {
        
        private MaxPatcher _p = null;
        private Callback cb;
        private MaxClock cl;
        
   public     Maxboxtest() {
                _p = this.getParentPatcher();
        
        }
        
        
  public void printboxes()
       {
           MaxBox[] boxes = _p.getAllBoxes();
           for(int i = 0; i < boxes.length; i++)
           {
              MaxBox b = boxes[i];
              post("Box "+i+": "+b.getName()+"  "+b.getMaxClass());
           } 
        }
 }