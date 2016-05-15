


import java.util.ArrayList;

import com.cycling74.max.*;


public class View2 extends MaxObject {
	
	private MaxBox jsui;
	private MaxPatcher parentPatcher;
	private boolean isSelected;
	private boolean isUsed;
	private int viewNumber;
	private String viewName;
	private MaxBox viewPanel;
	private MaxBox titleMessageBox;
	private ArrayList<Sample> sampleList;
	


	
	
	public View2(int viewNumber, MaxPatcher viewParentPatcher, String viewName) {
		parentPatcher =viewParentPatcher;
		this.viewNumber = viewNumber;
		this.viewName = viewName;
		
		isSelected = false;
		isUsed  = false;
		
		int xPosition = 190*viewNumber;
		int yPosition = 0;
		
		 
		viewPanel = parentPatcher.newDefault( xPosition, yPosition, "panel", null);
		viewPanel.send("size", new Atom [] { Atom.newAtom(180), Atom.newAtom(210)});
		viewPanel.send("bgfillcolor", new Atom []{Atom.newAtom(1), Atom.newAtom (1),Atom.newAtom(1), Atom.newAtom (1)});
		viewPanel.send("ignoreclick", new Atom []{Atom.newAtom(1)});
		viewPanel.send("presentation",new Atom []{Atom.newAtom(true)});
		viewPanel.send("background",new Atom []{Atom.newAtom(1)});
		
		titleMessageBox  = parentPatcher.newDefault( xPosition + 10, yPosition +10, "message", null);
		titleMessageBox.send("presentation",new Atom []{Atom.newAtom(true)});
		titleMessageBox.send("set",new Atom []{Atom.newAtom(viewName)});
		titleMessageBox.send("textjustification",new Atom []{Atom.newAtom(1)});
		titleMessageBox.send("size", new Atom [] { Atom.newAtom(160), Atom.newAtom (30)});
		
		jsui = parentPatcher.newDefault( xPosition+10, yPosition+40, "jsui",null);
		jsui.send("filename", new Atom []{ Atom.newAtom("C:/Users/Julius Gruber/Dropbox/Master/maxMSPpatches/viewScript.js")});
		jsui.send("size", new Atom [] { Atom.newAtom(160), Atom.newAtom (160)});
		jsui.send("presentation", new Atom[] {Atom.newAtom(true)});
		jsui.send("setName", new Atom[] {Atom.newAtom(viewName)});
		
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


	public MaxBox getTitleMessageBox() {
		return titleMessageBox;
	}


	public void setTitleMessageBox(MaxBox titleMessageBox) {
		this.titleMessageBox = titleMessageBox;
	}


	public boolean isUsed() {
		return isUsed;
	}


	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}


	

	
	
}
