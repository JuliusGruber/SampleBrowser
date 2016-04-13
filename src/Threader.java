import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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


public class Threader extends MaxObject  {
	ArrayList<Atom[]> results;
	int numberOfThreads;
	
	
	public Threader(){
		results = new ArrayList<Atom[]>();
		numberOfThreads =20;
		
	}
	
	public void matlabFE(final String dirName){
		final MaxPatcher p = this.getParentPatcher();
		
		Thread t = new Thread(){
			public void run(){
				System.out.println("Thread recieved: "+dirName); 
				
				

				
				
				results = new ArrayList<Atom[]>();
				
				//get the file list handed to the callables
				Collection <File> filePathCollection = getFilePathCollection(dirName);
			
				

				final ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
				final CompletionService<Atom[]> service = new ExecutorCompletionService<Atom[]>(pool);
				List<? extends Callable<Atom[]>>  callables;
				RandomCallable [] arrayCallables = new RandomCallable[numberOfThreads];
				for(int i = 0; i< numberOfThreads; i++){
					arrayCallables[i] = new RandomCallable(filePathCollection);
				}
				
				callables = Arrays.asList(arrayCallables);
				       
						
				for (final Callable<Atom[]> callable : callables) {
						      service.submit(callable);
						     
				}	

							
			   pool.shutdown();
			    
			   
			  
	    
		    	for(int i = 0; i< callables.size();i++){
		  	     // while (!pool.isTerminated()) {
			        Future<Atom[]> future;
					try {
						future = service.take();
						System.out.println("A THREAD HAS DONE ITS WORK...");
						synchronized(results){
						results.add( future.get());
						}
						//printThreadResults(results);
						//viewManager.send("addView", results);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
						     
	    	 	


				
				
				boolean hasNotFinsihed = true;
				int nextCounter = 0;
				
					while(hasNotFinsihed ){
						if(results.size() > 0){//if there is at least one result
							if( nextCounter < results.size()){
								//viewManager.send("addView", results.get(nextCounter));
								Atom [] singleResultAtomArray = null;
								synchronized(results){
									singleResultAtomArray = results.get(nextCounter);
								}
								outlet(0,"addView",singleResultAtomArray );
							
								nextCounter++;
							}
							if(nextCounter == numberOfThreads){
								hasNotFinsihed = false;
							}
						}
					}
				
		    	
		    	
				
				
			}
		};
		
		results = new ArrayList<Atom[]>();
		t.start();
		
	}
	
	public void randomViewsThread(final String dirName) {
		
		final MaxPatcher p = this.getParentPatcher();
		
		Thread t = new Thread(){
			public void run(){
				System.out.println("Thread recieved: "+dirName); 
				
				

				
				
				results = new ArrayList<Atom[]>();
				
				//get the file list handed to the callables
				Collection <File> filePathCollection = getFilePathCollection(dirName);
			
				

				final ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
				final CompletionService<Atom[]> service = new ExecutorCompletionService<Atom[]>(pool);
				List<? extends Callable<Atom[]>>  callables;
				RandomCallable [] arrayCallables = new RandomCallable[numberOfThreads];
				for(int i = 0; i< numberOfThreads; i++){
					arrayCallables[i] = new RandomCallable(filePathCollection);
				}
				
				callables = Arrays.asList(arrayCallables);
				       
						
				for (final Callable<Atom[]> callable : callables) {
						      service.submit(callable);
						     
				}	

							
			   pool.shutdown();
			    
			   
			  
	    
		    	for(int i = 0; i< callables.size();i++){
		  	     // while (!pool.isTerminated()) {
			        Future<Atom[]> future;
					try {
						future = service.take();
						System.out.println("A THREAD HAS DONE ITS WORK...");
						synchronized(results){
						results.add( future.get());
						}
						//printThreadResults(results);
						//viewManager.send("addView", results);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
						     
	    	 	


				
				
				boolean hasNotFinsihed = true;
				int nextCounter = 0;
				
					while(hasNotFinsihed ){
						if(results.size() > 0){//if there is at least one result
							if( nextCounter < results.size()){
								//viewManager.send("addView", results.get(nextCounter));
								Atom [] singleResultAtomArray = null;
								synchronized(results){
									singleResultAtomArray = results.get(nextCounter);
								}
								outlet(0,"addView",singleResultAtomArray );
							
								nextCounter++;
							}
							if(nextCounter == numberOfThreads){
								hasNotFinsihed = false;
							}
						}
					}
				
		    	
		    	
				
				
			}
		};
		
		results = new ArrayList<Atom[]>();
		t.start();
		
		

		
		
	
		
		
	}
	
	
	
	
	protected void printThreadResults(Atom [] results) {
		for(Atom atom : results){
			System.out.println(atom.toString());
		}
		
	}




	protected Collection<File> getFilePathCollection(String dirName) {
		File dir = new File(dirName);
		String[] extensions = new String[] { "aif", "aiff" , "flac", "mp3", "snd", "wav" };
		Collection<File> files =   FileUtils.listFiles(dir, extensions, true);
		System.out.println("There are "+files.size()+" samples in this folder");
		return files;
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