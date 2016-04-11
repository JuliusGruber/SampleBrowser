

import com.cycling74.jitter.JitterEvent;
import com.cycling74.jitter.JitterListener;
import com.cycling74.jitter.JitterNotifiable;
import com.cycling74.jitter.JitterObject;
import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class SketchTest extends MaxObject implements JitterNotifiable{
	
	
	 JitterObject sketch;
	 JitterObject render;
	 MaxBox viewAreaPwindow;
	 MaxPatcher parentPatcher = null;
	
	String contextName;
	JitterListener jitterListener;
	
	float height;
	float width;
	static float aspect;
	float [] randomXpoints;
	float [] randomYpoints;
	
	public SketchTest(){
		
		parentPatcher = this.getParentPatcher();
		
		height = 300;
		width = 1700;
		aspect =   width/height;
		
		float minX = -aspect;
		float maxX = aspect;
		float minY = -1;
		float maxY = 1;
		randomXpoints = new float[100];
		randomYpoints = new float[100];
		
		for(int i = 0;i<100;i++){
		float randomX = minX + (float)(Math.random() * ((maxX - minX)+1));
		float randomY = minY + (float)(Math.random() * ((maxY - minY)));
		randomXpoints[i] = randomX;
		randomYpoints[i] = randomY;
		}
		
	
		
		
		
		contextName = "contexxxt";
		
		 
		
		viewAreaPwindow =  parentPatcher.getNamedBox("viewArea");
		//viewAreaPwindow = parentPatcher.newDefault( 50, 50, "jit.pwindow", null);
		Atom [] arg1 = new Atom [] { Atom.newAtom(width), Atom.newAtom (height)};
		viewAreaPwindow.send("size", arg1);
		Atom [] arg2 = new Atom[] {Atom.newAtom(contextName)};
		viewAreaPwindow.send("name", arg2);
		Atom [] arg3 = new Atom[] {Atom.newAtom(true)};
		viewAreaPwindow.send("presentation", arg3);
		 
		 jitterListener = new JitterListener(contextName,this);
		
		 render = new JitterObject("jit.gl.render", contextName);
		 Atom [] renderEraseColor = new Atom []{Atom.newAtom(1.),Atom.newAtom(1.), Atom.newAtom(1.), Atom.newAtom(1.)};
		 render.setAttr("erase_color", renderEraseColor);
		 render.setAttr("ortho", new Atom [] {Atom.newAtom(2)});
		 render.setAttr("doublebuffer", new Atom [] {Atom.newAtom(1)});
		
		 
		 sketch = new JitterObject("jit.gl.sketch", contextName);
		 sketch.setAttr("drawto", contextName);
		 float [] arg4 = {1,1,1,1};
	     sketch.setAttr("glclearcolor", arg4);
	     sketch.setAttr("glpolygonmode", new Atom[] {Atom.newAtom("front_and_back"),Atom.newAtom("fill")});
	     sketch.setAttr("glpointsize", new Atom[] {Atom.newAtom(1.)});
	     sketch.setAttr("gllinewidth", new Atom[] {Atom.newAtom(1.)});
	     sketch.setAttr("gldisable", "depth_test");
	     sketch.setAttr("gldisable", "fog");
	     sketch.setAttr("glcolor", new Atom[] { Atom.newAtom(0.), Atom.newAtom(0.), Atom.newAtom(0.), Atom.newAtom(1.) });
	     sketch.setAttr("glshademodel", "smooth");
	     sketch.setAttr("gldisable", "lighting");
	     sketch.setAttr("gldisable", "normalize");
	     sketch.setAttr("gldisable", "texture");
	     sketch.setAttr("glmatrixmode", "projection");
	     sketch.call("glloadidentity");
	     float [] args1 = {-aspect, aspect, -1, 1, -1, 100};
	     sketch.call("glortho", args1);
		 sketch.setAttr("glmatrixmode","modelview");
	     float [] args = {0, 0, 2, 0, 0, 0, 0, 0, 1};
	     sketch.call("glulookat", args);
	 	 sketch.call("glclear");
	     sketch.setAttr("glenable","blend");
	     sketch.setAttr("glblendfunc", new Atom[] {Atom.newAtom("src_alpha"),Atom.newAtom("one_minus_src_alpha")});
	   
	     
	     
	 
	     
	     
	     
	


	}
	
	


	public void printAllBoxes(){
		post("printAllBoxes() method was called");
		MaxBox [] boxArray = parentPatcher.getAllBoxes();
		for(int i = 0; i < boxArray.length; i++){
			post("Box "+i+": "+boxArray[i].getName()+"  "+boxArray[i].getMaxClass());
		}
		
	}
	
	private static Atom []  getRandomPosition(){
		float minX = -aspect;
		float maxX = aspect;
		float minY = -1;
		float maxY = 1;
		
	
		float randomX = minX + (float)(Math.random() * ((maxX - minX)));
		Atom xPositionAtom = Atom.newAtom(randomX);
		post("randomX: "+randomX);
		
		
		
		float randomY = minY + (float)(Math.random() * ((maxY - minY)));
		Atom yPositionAtom = Atom.newAtom(randomY);
		post("randomY: "+randomY);
		
		
		Atom [] returnArray=  new Atom[]{xPositionAtom , yPositionAtom};
		return returnArray;
	}
	
	public void randomDrawing(){
		for(int i = 0; i<randomXpoints.length;i++){
		
			sketch.call("moveto", new Atom[] {Atom.newAtom(randomXpoints[i]), Atom.newAtom(randomYpoints[i])});
			sketch.call("glcolor", new Atom[] { 
		            Atom.newAtom(1.), 
		            Atom.newAtom(0.), 
		            Atom.newAtom(0.),
		            Atom.newAtom(1.) });
			sketch.call("circle", new Atom[] {Atom.newAtom(0.03)});
			}
	}
	
	
	public void bang(){
	
		
		
		
		sketch.call("reset");
		
		randomDrawing();
		
		
//		sketch.call("moveto", new Atom[] {Atom.newAtom(aspect), Atom.newAtom(0)});
//		sketch.call("glcolor", new Atom[] { 
//	            Atom.newAtom(1.), 
//	            Atom.newAtom(0.), 
//	            Atom.newAtom(0.),
//	            Atom.newAtom(1.) });
//		sketch.call("circle", new Atom[] {Atom.newAtom(0.03)});
		

		render.call("erase");// erase the drawing context, Overwrites the image buffer with the color and opacity selected by the erase_color attribute. 
		render.call("drawclients");// draw the client objects
		render.call("swap");//swap in the new drawing, Copy the rendered image to the destination  jit.pwindow 
		
 
	}


	@Override
	public void notify(JitterEvent e) {
		Atom args[] = e.getArgs();
	    String subjectName = e.getSubjectName();
	    //post("subject name: "+subjectName);
	    String eventName = e.getEventName();
	    if(eventName.equals("mouse")){
	    post("event Name: "+eventName);
	    int xy[] = new int[] {args[0].toInt(),args[1].toInt()};
	    post("mouseXpostion: "+xy[0]);
	    }
	    if(eventName.equals("mouseidle")){
		    post("event Name: "+eventName);
		    int xy[] = new int[] {args[0].toInt(),args[1].toInt()};
		    post("mouseXpostion: "+xy[0]+" mouseYposition: "+xy[1]);
		}
	}
}
