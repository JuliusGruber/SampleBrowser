import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	MaxBox jsuiSonoArea;
	MaxBox viewManager;
	MaxBox myPoly;
	MaxBox polyLoopOnOffSend;
	MaxBox polyFilePathSend;
	
	public Threader(){
		results = new ArrayList<Atom[]>();
		numberOfThreads =20;
		
		MaxPatcher p = this.getParentPatcher();
		MaxBox sonoAreaBpatcher = p.getNamedBox("sonoAreaBpatcher");
		MaxPatcher sonoArea = sonoAreaBpatcher.getSubPatcher();
		jsuiSonoArea = sonoArea.getNamedBox("jsuiSonoArea");
		viewManager = p.getNamedBox("viewManager");
		
		MaxBox audioPbatcher = p.getNamedBox("audioBpatcher");
		MaxPatcher audioPatcher  = audioPbatcher.getSubPatcher();
		polyFilePathSend =  audioPatcher.getNamedBox("polyFilePathSend");
		polyLoopOnOffSend  = audioPatcher.getNamedBox("polyLoopOnOffSend");
		myPoly =  audioPatcher.getNamedBox("myPoly");
	}
	
	

	
	public void loadSamplesToPoly(String dirName){
	
		deleteViews();
		resetSonoArea();
		//get the file list handed to the callables and the load method
		Collection <File> filePathColl = getFilePathCollection(dirName);
		sendFilePathInfoToPoly(filePathColl);
		System.out.println("Loading folder has finished");
		setPolyNumberCompareList(filePathColl);
		System.out.println("The polyAdressLookUp List was set");
		
		randomViewsThread(filePathColl);
		
	
		
	}
	
	
	private void deleteViews(){
		viewManager.send("deleteAllViews", null);
	}
	
	private void resetSonoArea(){
		jsuiSonoArea.send("clearSonoArea", null);
	}
	
	
	
	public void readFromPointFile() throws IOException{
		
		BufferedReader in = new BufferedReader(new FileReader("C:/Users/Julius Gruber/Desktop/pointData.txt"));
		
		 int numSamples = 0;
		 String line;
		 ArrayList<String> splittStringList = new ArrayList<String>();
		
		while((line = in.readLine()) != null)
		{
			String [] stringArray = line.split("\\$");
			//post("size of stringArray: "+stringArray.length);
			if(stringArray.length == 4){
				
				String x = stringArray[0];
				String y = stringArray[1];
				String filePath = stringArray[2];
				String fileName = stringArray[3];
			
//				if(!x.startsWith("-")){
//					String plus = "+";
//					x = plus.concat(x);
//				}
//				
//				if(!y.startsWith("-")){
//					String plus = "+";
//					y = plus.concat(y);
//				}
				
//				post("filePath: " +filePath);
//				post("filename: " +fileName);
//				post("x: "+x);
//				post("y: "+y);
				
				splittStringList.add(filePath);
				splittStringList.add(fileName);
				splittStringList.add(x);
				splittStringList.add(y);
				numSamples++;
				//post(line);
			}
		}
		in.close();
		
		 Atom [] viewAtomArray =  new Atom [4*numSamples+1];
		 viewAtomArray[0]=Atom.newAtom("setSampleData");
		 
		 post("numSamples: "+ numSamples);
		 post("size stringSplittList: "+ splittStringList.size());
	
		 Collection <File> filePathColl =  new ArrayList<File>();
		 for(int i = 0; i< splittStringList.size(); i=i+4){
			 viewAtomArray[i+1] = Atom.newAtom(splittStringList.get(i));
			 viewAtomArray[i+2] = Atom.newAtom(splittStringList.get(i+1));
			 viewAtomArray[i+3] = Atom.newAtom(Double.valueOf(splittStringList.get(i+2)));
			 viewAtomArray[i+4] = Atom.newAtom(Double.valueOf(splittStringList.get(i+3)));
			 
			 filePathColl.add(new File(splittStringList.get(i)));
		 }
		 
		 
		 
			deleteViews();
			resetSonoArea();
			//get the file list handed to the callables and the load method
//			String dirName = "C:/Users/Julius Gruber/Desktop/Sample_Datenbanken/1000";
//			Collection <File> filePathColl = getFilePathCollection(dirName);
			
			sendFilePathInfoToPoly(filePathColl);
			System.out.println("Loading folder has finished");
			setPolyNumberCompareList(filePathColl);
			System.out.println("The polyAdressLookUp List was set");
		
			outlet(0,"addView",viewAtomArray );
		
	}
	

 	private void setPolyNumberCompareList(Collection <File> filePathColl){
	
		
		Atom [] polyNoFilePathArray = new Atom [filePathColl.size()*2];
		int polyNumCounter = 1;
		int arrayCounter = 0;
		for(File file : filePathColl){
			Atom polyNumAtom = Atom.newAtom(polyNumCounter);
			String filePath = null;
			try {
				filePath = file.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Atom filePathAtom =  Atom.newAtom(filePath);
			
			polyNoFilePathArray[arrayCounter]= polyNumAtom;
			polyNoFilePathArray[arrayCounter+1]= filePathAtom;
			
			polyNumCounter++;
			arrayCounter = arrayCounter+2;
		}
		
		viewManager.send("setPolyLookUp", polyNoFilePathArray);
	}
	
	private void  sendFilePathInfoToPoly(Collection <File> filePathColl) {
	    post("sendFilePathInfoToPoly..");
	 
	  
		
		myPoly.send("voices", new Atom []{Atom.newAtom(filePathColl.size())});
		
		
		int loopCounter = 1;
		for(File file : filePathColl){
			
			Atom[] polyNumber = new Atom [] {Atom.newAtom(loopCounter)};
			String filePath = null;
			try {
				filePath = file.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			filePath = filePath.replace("\\", "/");
			//post("filePath: "+filePath);
			
			polyFilePathSend.send("target", polyNumber);
			polyFilePathSend.send(filePath, null);
			
			polyLoopOnOffSend.send("target", polyNumber);
			polyLoopOnOffSend.send(1);
			
			
		
			
			
			
			loopCounter++;
		}
		
	
	}
	
	public void matlabFE(final Collection <File> filePathColl){
		
		
		Thread t = new Thread(){
			public void run(){
				//System.out.println("Thread recieved: "+dirName); 
				
				

				
				
				results = new ArrayList<Atom[]>();
				
				
			
				

				final ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
				final CompletionService<Atom[]> service = new ExecutorCompletionService<Atom[]>(pool);
				List<? extends Callable<Atom[]>>  callables;
				RandomCallable [] arrayCallables = new RandomCallable[numberOfThreads];
				for(int i = 0; i< numberOfThreads; i++){
					arrayCallables[i] = new RandomCallable(filePathColl);
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
	
	public void randomViewsThread(final Collection <File> filePathColl) {
		
	
		
		Thread t = new Thread(){
			public void run(){
				//System.out.println("Thread recieved: "+dirName); 
				
				

				
				
				results = new ArrayList<Atom[]>();
				
				//get the file list handed to the callables
				//Collection <File> filePathCollection = getFilePathCollection(dirName);
			
				

				final ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
				final CompletionService<Atom[]> service = new ExecutorCompletionService<Atom[]>(pool);
				List<? extends Callable<Atom[]>>  callables;
				RandomCallable [] arrayCallables = new RandomCallable[numberOfThreads];
				for(int i = 0; i< numberOfThreads; i++){
					arrayCallables[i] = new RandomCallable(filePathColl);
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
						//System.out.println("A THREAD HAS DONE ITS WORK...");
						synchronized(results){
						results.add( future.get());
						}
						//printThreadResults(results);
						
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


		
	public void addServeralRandomViews(Collection <File> sampleInfoColl){

			
			for (int i = 0; i<40;i++){
			int arrayInit = sampleInfoColl.size()*4+1; //+1 for the method name needed for jsui
			//add the method name as the first entry in the atom array handed to the jsui
			Atom [] viewAtomArray =  new Atom [arrayInit];
			viewAtomArray[0]=Atom.newAtom("setSampleData");
			int arrayCounter = 1;
			for (File file : sampleInfoColl) {
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
