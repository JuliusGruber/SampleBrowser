


import java.util.ArrayList;

import com.cycling74.max.*;


public class View1 extends MaxObject {
	
	MaxBox jsui;
	MaxPatcher parentPatcher;
	boolean isSelected;
	int viewNumber;
	String viewName;
	MaxBox viewPanel;
	ArrayList<Sample> sampleList;
	
	public View1(int viewNumber, MaxPatcher viewParentPatcher, ArrayList<Sample> sampleList, Atom [] sampleAtomArray ){

		parentPatcher =viewParentPatcher;
		this.viewNumber = viewNumber;
		viewName = "view"+ viewNumber;
		this.sampleList = sampleList;
		
		int xPosition = 190*viewNumber;
		int yPosition = 50;
		
		jsui = parentPatcher.newDefault( xPosition, yPosition, "jsui",null);
		
		
		jsui.send("filename", new Atom []{ Atom.newAtom("C:/Users/Julius Gruber/Dropbox/Master/maxMSPpatches/viewScript.js")});
		
		 Atom [] arg1 = new Atom [] { Atom.newAtom(160), Atom.newAtom (160)};
		jsui.send("size", arg1);
		
		 Atom [] arg3 = new Atom[] {Atom.newAtom(true)};
		 jsui.send("presentation", arg3);
		 
		 
		 Atom [] arg2 = new Atom[] {Atom.newAtom(viewName)};
		 jsui.send("setName", arg2);
		 
		
	
		 jsui.send("list", sampleAtomArray);
		 //jsui.send("setSampleData", sampleAtomArray);
		 
		 isSelected = false;
		 
		 
		viewPanel = parentPatcher.newDefault( xPosition -10, yPosition -10, "panel", null);
		 Atom [] arg4 = new Atom [] { Atom.newAtom(180), Atom.newAtom (180)};
		viewPanel.send("size", arg4);
		viewPanel.send("border", new Atom []{Atom.newAtom(10)});
		viewPanel.send("bordercolor", new Atom []{Atom.newAtom(1), Atom.newAtom (1),Atom.newAtom(1), Atom.newAtom (1)});
		viewPanel.send("ignoreclick", new Atom []{Atom.newAtom(1)});
	   
	
	}
	
	
	

	

	
	
}
