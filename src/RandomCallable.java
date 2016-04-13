


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import com.cycling74.max.Atom;




public  class RandomCallable implements Callable <Atom[]> {


	   final Collection<File> fileCollection;
	

	    public RandomCallable( 	Collection<File> fileCollection) {
	      this.fileCollection = fileCollection;
	 	
	    }
	    

	    
	    
	    public Atom [] call()  {
	   
	    	Atom [] returnArray = getReturnAtomArray(fileCollection);
			

		
	    	return returnArray;
	     

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
}
