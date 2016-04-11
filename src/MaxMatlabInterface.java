import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.io.FileUtils;

import com.cycling74.max.*;
import com.mathworks.toolbox.javabuilder.*;


public class MaxMatlabInterface extends MaxObject {
	
	public void feExtractDimRed(final String dirName) throws MWException{
		System.out.println("feExtractDimRed() dirName: "+dirName);
		final MaxPatcher p = this.getParentPatcher();
		
		Thread t = new Thread(){
			public void run(){
				System.out.println("Thread recieved: "+dirName); 
				
				
				MaxBox myBpatcher = p.getNamedBox("myBpatcher");
				MaxPatcher viewPatcher  = myBpatcher.getSubPatcher();
				MaxBox viewManager = viewPatcher.getNamedBox("viewManager");
			
				
			
				

				final ExecutorService pool = Executors.newFixedThreadPool(5);
				final CompletionService<Object[]> service = new ExecutorCompletionService<Object[]>(pool);
				List<? extends Callable<Object[]>> callables;
				try {
						callables = Arrays.asList(
							
							new FeatureExtractionCallable("one", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1103),
							new FeatureExtractionCallable("two", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1104),
						    new FeatureExtractionCallable("three", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1105));
				
						
						//get the first view as fast a possible		
						FeatureExtractionCallable firstOne = 	new FeatureExtractionCallable("zero", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1102);
						service.submit(firstOne);
						//wait for the first one to finish
						Future<Object[]> future = service.take();
					    Object [] results = future.get();
			   	        Atom [] resultsAtomArray  = getSampleAtomArray(results);
			   	        viewManager.send("addView", resultsAtomArray);
			   	        
//				   	        
//				   	 	FeatureExtractionCallable secondOne = 	new FeatureExtractionCallable("one", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1102);
//						service.submit(secondOne);
//						//wait for the first one to finish
//						future = service.take();
//					    results = future.get();
//			   	        resultsAtomArray  = getSampleAtomArray(results);
//			   	        viewManager.send("addView", resultsAtomArray);
//		   	        
//		   	        
//				   	 	FeatureExtractionCallable thirdOne = 	new FeatureExtractionCallable("two", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1102);
//						service.submit(thirdOne);
//						//wait for the first one to finish
//						future = service.take();
//					    results = future.get();
//			   	        resultsAtomArray  = getSampleAtomArray(results);
//			   	        viewManager.send("addView", resultsAtomArray);
//	   	        
//	   	        
//				   	 	FeatureExtractionCallable fourthOne = 	new FeatureExtractionCallable("three", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1102);
//						service.submit(fourthOne);
//						//wait for the first one to finish
//						future = service.take();
//					    results = future.get();
//			   	        resultsAtomArray  = getSampleAtomArray(results);
//			   	        viewManager.send("addView", resultsAtomArray);
			   	        
						
				for (final Callable<Object[]> callable : callables) {
						      service.submit(callable);
						     
				}	
//				
//				for (final Callable<Object[]> callable : callables) {
//					Future<Object[]> future1 = service.take();
//		   	        System.out.println("A THREAD HAS DONE ITS WORK...");
//		  	        //System.out.println("This is the result: "+future.get().toString());
//		   	        Object [] results1 = future1.get();
//		   	        Atom [] resultsAtomArray1  = getSampleAtomArray(results1);
//		   	        viewManager.send("addView", resultsAtomArray1);
//				     
//				}
							
			   pool.shutdown();
			    
			   
			   ArrayList < Object []> resultObjectsList = new ArrayList<Object []>();
	    	   synchronized(resultObjectsList){	
		    	for(int i = 0; i< 3;i++){
		  	     // while (!pool.isTerminated()) {
			        Future<Object[]> future1 = service.take();
		   	        System.out.println("A THREAD HAS DONE ITS WORK...");
		  	        //System.out.println("This is the result: "+future.get().toString());
		   	        Object [] results1 = future1.get();
		   	        resultObjectsList.add(results1);
		    	  }
						     
	    	   }
	    	   
	    	   for(Object [] objectArray : resultObjectsList){
				   Atom [] resultsAtomArray1  = getSampleAtomArray(objectArray);
		   	        viewManager.send("addView", resultsAtomArray1);
				}
	    	   
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    
				
				 
				
				
				
			}
			};
			
			t.start();
		
		
	}
	
	private static Atom [] getSampleAtomArray(Object [] results){
	  	
		
		MWCellArray mwCellArray  = null;
		MWArray singleCell  = null;
		MWNumericArray numericArray = null;
		Atom [] sampleAtomArray = null;
		
		try {
		
			
		
			
			mwCellArray = (MWCellArray) results[0];
			int [] dimArray = mwCellArray.getDimensions();
			int rowNumber = dimArray[0];
			int columnNumber = dimArray[1];
			
			int [] initArray = new int [rowNumber*columnNumber];
			sampleAtomArray = Atom.newAtom(initArray);
			
			int counterAtomArray = 0;
			
			for(int i =1; i< rowNumber+1; i++ ){
				
				int [] dimArrayGet =  new int []{i,1};
				singleCell = mwCellArray.getCell(dimArrayGet);
				String filePathString = singleCell.toString();
				Atom filePathAtom = Atom.newAtom(filePathString);
				//System.out.println(filePathString);
				
				
				dimArrayGet =  new int []{i,2};
				singleCell = mwCellArray.getCell(dimArrayGet);
				String fileNameString = singleCell.toString();
				Atom fileNameAtom = Atom.newAtom(fileNameString);
				//System.out.println(fileNameString);
				
				
				dimArrayGet =  new int []{i,3};
				singleCell = mwCellArray.getCell(dimArrayGet);
				numericArray = (MWNumericArray)singleCell;
				float xPosition  =numericArray.getFloat();
				Atom xPositionAtom = Atom.newAtom(xPosition);
				//System.out.println(xPosition);
				
				
				
				dimArrayGet =  new int []{i,4};
				singleCell = mwCellArray.getCell(dimArrayGet);
				numericArray = (MWNumericArray)singleCell;
				float yPosition  = numericArray.getFloat();
				Atom yPositionAtom = Atom.newAtom(yPosition);
				//System.out.println(yPosition);
				
				sampleAtomArray[counterAtomArray] = filePathAtom;
				counterAtomArray++;
				sampleAtomArray[counterAtomArray] = fileNameAtom;
				counterAtomArray++;
				sampleAtomArray[counterAtomArray] = xPositionAtom;
				counterAtomArray++;
				sampleAtomArray[counterAtomArray] = yPositionAtom;
				counterAtomArray++;
			}
			
//			for(int i = 0; i<sampleAtomArray.length; i++){
//				System.out.println(sampleAtomArray[i]);
//			}
			
			return sampleAtomArray;
			
		}finally{
			
			mwCellArray.dispose();
			singleCell.dispose();
			numericArray.dispose();
			
			mwCellArray = null;
			singleCell = null;
			numericArray = null;
			
			
		}

  }

	
	private static  Atom[] createAtomArrayforRandomViews(Object [] results){
    	System.out.println("createAtomArrayforRandomViews() method was called");
    	MWCellArray mwCellArray  = null;
		MWArray singleCell  = null;
	
		Atom [] sampleAtomArray = null;
		
		try {
		
			
		
			
			mwCellArray = (MWCellArray) results[0];
			int [] dimArray = mwCellArray.getDimensions();
			int rowNumber = dimArray[0];
			//int columnNumber = dimArray[1];
			
		
			
			int [] initArray = new int [rowNumber*4];
			sampleAtomArray = Atom.newAtom(initArray);
			
			int counterAtomArray = 0;
			
			for(int i =1; i< rowNumber+1; i++ ){
				
				int [] dimArrayGet =  new int []{i,1};
				singleCell = mwCellArray.getCell(dimArrayGet);
				String filePathString = singleCell.toString();
				Atom filePathAtom = Atom.newAtom(filePathString);
				//System.out.println(filePathString);
				
//				
//				dimArrayGet =  new int []{i,2};
//				singleCell = mwCellArray.getCell(dimArrayGet);
//				String fileNameString = singleCell.toString();
				Atom fileNameAtom = Atom.newAtom("fakeFileName");
//				System.out.println(fileNameString);
				
				float min = -1;
				float max = 1;
				
				float randomX = min + (float)(Math.random() * ((max - min) ));
				Atom xPositionAtom = Atom.newAtom(randomX);
			
				
				
				
				float randomY = min + (float)(Math.random() * ((max - min)));
				Atom yPositionAtom = Atom.newAtom(randomY);
			
				
				sampleAtomArray[counterAtomArray] = filePathAtom;
				counterAtomArray++;
				sampleAtomArray[counterAtomArray] = fileNameAtom;
				counterAtomArray++;
				sampleAtomArray[counterAtomArray] = xPositionAtom;
				counterAtomArray++;
				sampleAtomArray[counterAtomArray] = yPositionAtom;
				counterAtomArray++;
			}
			
			for(int i = 0; i<sampleAtomArray.length; i++){
				System.out.println(sampleAtomArray[i]);
			}
			
			return sampleAtomArray;
			
		}finally{
			
			mwCellArray.dispose();
			singleCell.dispose();
			
			
			mwCellArray = null;
			singleCell = null;
			
			
		}
	
    	
    	
    }
	
	
		public void createRandomViews(final String dirName){
		final MaxPatcher p = this.getParentPatcher();
		
		Thread t = new Thread(){
			public void run(){
				System.out.println("Thread recieved: "+dirName); 
				
				
				MaxBox myBpatcher = p.getNamedBox("myBpatcher");
				MaxPatcher viewPatcher  = myBpatcher.getSubPatcher();
				MaxBox viewManager = viewPatcher.getNamedBox("viewManager");
			
				
			
				

				final ExecutorService pool = Executors.newFixedThreadPool(5);
				final CompletionService<Object[]> service = new ExecutorCompletionService<Object[]>(pool);
				List<? extends Callable<Object[]>> callables;
				try {
						callables = Arrays.asList(
							
							//new FeatureExtractionCallable("one", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1103),
							//new FeatureExtractionCallable("two", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1104),
						    new FeatureExtractionCallable("getFileInfo", "C:/Users/Julius Gruber/Dropbox/Master/matlab/Datenbank/rastafarian/", 1105));
				
						
					
			   	        

						
				for (final Callable<Object[]> callable : callables) {
						      service.submit(callable);
						     
				}	

							
			   pool.shutdown();
			    
			   
			   ArrayList < Object []> resultObjectsList = new ArrayList<Object []>();
	    	   synchronized(resultObjectsList){	
		    	for(int i = 0; i< callables.size();i++){
		  	     // while (!pool.isTerminated()) {
			        Future<Object[]> future1 = service.take();
		   	        System.out.println("A THREAD HAS DONE ITS WORK...");
		  	        //System.out.println("This is the result: "+future.get().toString());
		   	        Object [] results1 = future1.get();
		   	        resultObjectsList.add(results1);
		    	  }
						     
	    	   }
	    	   
	    	   for(Object [] objectArray : resultObjectsList){
			
		   	        for(int i = 0; i< 1;i++){
		   	        	Atom [] resultsAtomArray1  = createAtomArrayforRandomViews(objectArray);
		   	        	viewManager.send("addView", resultsAtomArray1);
		   	        }
				}
	    	   
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    
				
				 
				
				
				
			}
			};
			
			t.start();
		
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
		
		
		public void addServeralViews(String dirName){
			
			File dir = new File(dirName);
			String[] extensions = new String[] { "aif", "aiff" , "flac", "mp3", "snd", "wav" };
			List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
//			for (File file : files) {
//				try {
//					
//					System.out.println("filePath: " + file.getCanonicalPath());
//					System.out.println("fileName: " + file.getName());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			
			for (int i = 0; i<40;i++){
			int arrayInit = files.size()*4+1; //+1 for the method name needed for jsui
			//add the method name as the first entry in the atom array handed to the jsui
			Atom [] viewAtomArray =  new Atom [arrayInit];
			viewAtomArray[0]=Atom.newAtom("setSampleData");
			int arrayCounter = 1;
			for (File file : files) {
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
			
			final MaxPatcher p = this.getParentPatcher();
			MaxBox myBpatcher = p.getNamedBox("myBpatcher");
			MaxPatcher viewPatcher  = myBpatcher.getSubPatcher();
			MaxBox viewManager = viewPatcher.getNamedBox("viewManager");
			
			
			viewManager.send("addView", viewAtomArray);
			}
		}

}
