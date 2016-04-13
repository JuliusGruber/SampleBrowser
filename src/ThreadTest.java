import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.cycling74.max.Atom;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;

public class ThreadTest extends MaxObject {
	
	
	ArrayList<Atom[]> results;

	
	public ThreadTest(){
		this.results = new ArrayList<Atom[]>();
	}
	
	
	public void startThread(final String dirName){
		
		
		//final MaxPatcher p = this.getParentPatcher();
		

		System.out.println("Thread recieved: "+dirName); 
				
				
		
		
		
	//create a new thread 
		Thread t = new Thread(){
			public void run(){
//				MaxBox myBpatcher = p.getNamedBox("myBpatcher");
//				MaxPatcher viewPatcher  = myBpatcher.getSubPatcher();
//				MaxBox viewManager = viewPatcher.getNamedBox("viewManager");
				Collection <File> fileCollection = getFilePathCollection( dirName);
				Atom [] returnAtomArray = getReturnAtomArray(fileCollection);
				results.clear();
				synchronized(results){
				results.add(returnAtomArray);
				//viewManager.send("addView", returnAtomArray);
				}
				
				System.out.println("Thread has finished");
			}
		};
	
	
	
	t.start(); //start the thread executing
	
	
	MaxPatcher p = this.getParentPatcher();
	MaxBox myBpatcher = p.getNamedBox("myBpatcher");
	MaxPatcher viewPatcher  = myBpatcher.getSubPatcher();
	MaxBox viewManager = viewPatcher.getNamedBox("viewManager");

	
	
	boolean hasNotFinsihed = true;

	while(hasNotFinsihed ){
			if(results.size() > 0){
			viewManager.send("addView", results.get(0));
			hasNotFinsihed = false;
			}
	}

	
	}
	
	private float getRandomXPosition(){
		float min = -1;
		float max = 1;
		float randomX = min + (float)(Math.random() * ((max - min) ));
		return randomX;
	}
	
	
	private float getRandomYPosition(){
		float min = -1;
		float max = 1;
		float randomY = min + (float)(Math.random() * ((max - min) ));
		return randomY;
	}
	
	protected Collection<File> getFilePathCollection(String dirName) {
		File dir = new File(dirName);
		String[] extensions = new String[] { "aif", "aiff" , "flac", "mp3", "snd", "wav" };
		Collection<File> files =   FileUtils.listFiles(dir, extensions, true);
		return files;
	}
	
    
	private Atom[] getReturnAtomArray(Collection<File> fileCollection) {
		int arrayInit = fileCollection.size()*4+1; //+1 for the method name needed for jsui
		//add the method name as the first entry in the atom array handed to the jsui
		Atom [] viewAtomArray =  new Atom [arrayInit];
		viewAtomArray[0]=Atom.newAtom("setSampleData");
		int arrayCounter = 1;
		for (File file : fileCollection) {
			String filePath = "";
			try {
				filePath = file.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String fileName = file.getName();
			float xPosition = getRandomXPosition();
			float yPosition = getRandomYPosition();
			Atom filePathAtom = Atom.newAtom(filePath);
			Atom fileNameAtom = Atom.newAtom(fileName);
			Atom xPositionAtom = Atom.newAtom(xPosition);
			Atom yPositionAtom =Atom.newAtom(yPosition);
			viewAtomArray[arrayCounter]  = filePathAtom;
			viewAtomArray[arrayCounter +1]  = fileNameAtom;
			viewAtomArray[arrayCounter+2]  = xPositionAtom;
			viewAtomArray[arrayCounter+3]  = yPositionAtom;
			arrayCounter = arrayCounter+4;
		}
		return viewAtomArray;
	}

}
