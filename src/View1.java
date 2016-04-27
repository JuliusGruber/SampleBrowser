


import java.util.ArrayList;

import com.cycling74.max.*;


public class View1 extends MaxObject {
	
	private MaxBox jsui;
	private MaxPatcher parentPatcher;
	private boolean isSelected;
	private int viewNumber;
	private String viewName;
	private MaxBox viewPanel;
	private ArrayList<Sample> sampleList;
	

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
	
	
	public MaxBox getJsui() {
		return jsui;
	}

	public void setJsui(MaxBox jsui) {
		this.jsui = jsui;
	}

	public MaxPatcher getParentPatcher() {
		return parentPatcher;
	}

	public void setParentPatcher(MaxPatcher parentPatcher) {
		this.parentPatcher = parentPatcher;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getViewNumber() {
		return viewNumber;
	}

	public void setViewNumber(int viewNumber) {
		this.viewNumber = viewNumber;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public MaxBox getViewPanel() {
		return viewPanel;
	}

	public void setViewPanel(MaxBox viewPanel) {
		this.viewPanel = viewPanel;
	}

	public ArrayList<Sample> getSampleList() {
		return sampleList;
	}

	public void setSampleList(ArrayList<Sample> sampleList) {
		this.sampleList = sampleList;
	}


	

	
	
}
