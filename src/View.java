
import java.util.ArrayList;

import com.cycling74.jitter.*;
import com.cycling74.max.*;


public class View extends MaxObject implements JitterNotifiable{
	
	JitterObject sketch;
	JitterObject render;
	MaxBox pWindow;
	JitterListener jitterListener;
	boolean isSelected;
	String contextName;
	ArrayList<Sample> sampleList;
	
	
	public View(MaxPatcher p, int viewNumber){
		contextName =  "contextName"+viewNumber;
		System.out.println(contextName);
		
		int xPosition = 180*viewNumber;
		int yPosition = 50;
		
		pWindow = p.newDefault( xPosition, yPosition, "jit.pwindow", null);
		 Atom [] arg1 = new Atom [] { Atom.newAtom(160), Atom.newAtom (160)};
		 pWindow.send("size", arg1);
		 Atom [] arg2 = new Atom[] {Atom.newAtom(contextName)};
		 pWindow.send("name", arg2);
		 Atom [] arg3 = new Atom[] {Atom.newAtom(true)};
		 pWindow.send("presentation", arg3);
		 
		 
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
		 float aspect = 1;
	     float [] args1 = {-aspect, aspect, -1, 1, -1, 100};
	     sketch.call("glortho", args1);
		 sketch.setAttr("glmatrixmode","modelview");
	     float [] args = {0, 0, 2, 0, 0, 0, 0, 0, 1};
	     sketch.call("glulookat", args);
	 	 sketch.call("glclear");
	     sketch.setAttr("glenable","blend");
	     sketch.setAttr("glblendfunc", new Atom[] {Atom.newAtom("src_alpha"),Atom.newAtom("one_minus_src_alpha")});
	   
	     
	     
	     
	     
	     
	     
	

			
		 render = new JitterObject("jit.gl.render", contextName);
		 Atom [] renderEraseColor = new Atom []{Atom.newAtom(1.),Atom.newAtom(1.), Atom.newAtom(1.), Atom.newAtom(1.)};
		 render.setAttr("erase_color", renderEraseColor);
		 render.setAttr("ortho", new Atom [] {Atom.newAtom(2)});
		 render.setAttr("doublebuffer", new Atom [] {Atom.newAtom(1)});
		
		
		 
		 
		 jitterListener = new JitterListener(contextName,this);
		 
		 isSelected = false;
	}
	
	
	public View (MaxPatcher p, int viewNumber, ArrayList<Sample> sampleArrayList){
		contextName =  "contextName"+viewNumber;
		System.out.println(contextName);
		
		int xPosition = 180*viewNumber;
		int yPosition = 50;
		
		pWindow = p.newDefault( xPosition, yPosition, "jit.pwindow", null);
		 Atom [] arg1 = new Atom [] { Atom.newAtom(160), Atom.newAtom (160)};
		 pWindow.send("size", arg1);
		 Atom [] arg2 = new Atom[] {Atom.newAtom(contextName)};
		 pWindow.send("name", arg2);
		 Atom [] arg3 = new Atom[] {Atom.newAtom(true)};
		 pWindow.send("presentation", arg3);
		 
		 
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
		 float aspect = 1;
	     float [] args1 = {-aspect, aspect, -1, 1, -1, 100};
	     sketch.call("glortho", args1);
		 sketch.setAttr("glmatrixmode","modelview");
	     float [] args = {0, 0, 2, 0, 0, 0, 0, 0, 1};
	     sketch.call("glulookat", args);
	 	 sketch.call("glclear");
	     sketch.setAttr("glenable","blend");
	     sketch.setAttr("glblendfunc", new Atom[] {Atom.newAtom("src_alpha"),Atom.newAtom("one_minus_src_alpha")});
	   
	     
	     
	     
	     
	     
	     
	

			
		 render = new JitterObject("jit.gl.render", contextName);
		 Atom [] renderEraseColor = new Atom []{Atom.newAtom(1.),Atom.newAtom(1.), Atom.newAtom(1.), Atom.newAtom(1.)};
		 render.setAttr("erase_color", renderEraseColor);
		 render.setAttr("ortho", new Atom [] {Atom.newAtom(2)});
		 render.setAttr("doublebuffer", new Atom [] {Atom.newAtom(1)});
		
		
		 
		 
		 jitterListener = new JitterListener(contextName,this);
		 
		 isSelected = false;
		 
		 this.sampleList = sampleArrayList;
			
	}

	@Override
	public void notify(JitterEvent e) {
	     String eventname = e.getEventName();
	    
		 Atom args[] = e.getArgs();
    	
	     if(eventname.equals("mouse")&&args[2].toInt() == 0){
	    	 String subjectName = e.getSubjectName();
	    	 System.out.println("View "+subjectName+" was clicked upon");
	    		MaxPatcher p = this.pWindow.getPatcher();
	    		Atom [] atomArray = new Atom[]{Atom.newAtom(subjectName)};
	    		
	    	   MaxBox viewsManagement = p.getNamedBox("viewManager");
	    	   viewsManagement.send("setSelectedView", atomArray);
	           
	     
		
	}

}
	
	public void bang(){
		//System.out.println(this.contextName+" recieved bang");
		
		
		
		sketch.call("reset");
		
		for(Sample sample : sampleList){
		sketch.call("moveto", new Atom[] {Atom.newAtom(sample.getxPosition()), Atom.newAtom(sample.getyPostion())  });
		sketch.call("glcolor", new Atom[] { 
	            Atom.newAtom(1.), 
	            Atom.newAtom(0.), 
	            Atom.newAtom(0.),
	            Atom.newAtom(1.) });
		sketch.call("circle", new Atom[] {Atom.newAtom(0.03)});
		}

		render.call("erase");// erase the drawing context, Overwrites the image buffer with the color and opacity selected by the erase_color attribute. 
		render.call("drawclients");// draw the client objects
		render.call("swap");//swap in the new drawing, Copy the rendered image to the destination  jit.pwindow 
		
 
	}
	
	
}
