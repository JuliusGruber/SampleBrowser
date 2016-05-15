

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cycling74.max.Atom;
import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWCellArray;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import MatlabFE.FeRemote;



public  class FeatureExtractionCallable implements Callable <Atom[]> {

	   final String methodName;
	   final String dirName;
	   final int port;

	    public FeatureExtractionCallable(final String methodName, final String dirName, final int port) {
	      this.methodName = methodName;
	 	  this.dirName = dirName;
	      this.port  = port;
	     
	
	    }
	    

	    
	    
	    public Atom[] call()  {
	   
	    	System.out.println("call() with: "+methodName);
	    	
	    	
	    	
	        Registry reg = null;
	        FeRemote dTypes = null;
	        Object[] results = null;
	        
	        try{
	        reg = LocateRegistry.getRegistry(port);
	        String lookupString  = "Fe"+port;
	        dTypes = (FeRemote)reg.lookup(lookupString);

	        
	    
	        
	            System.out.println("Call the associated server (port: " + port + ") for thread " + methodName); 
	    
	     

	        }
	        catch (NotBoundException ex) {
	            Logger.getLogger(MaxMatlabInterface.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        catch (AccessException ex) {
	            Logger.getLogger(MaxMatlabInterface.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        catch(RemoteException remote_ex){
	            remote_ex.printStackTrace(); 
	        }
	    	
	    	
	    	
	    	
	    	
	        try {
	    	
	        if(methodName.equals("zero")){
	    		results  = dTypes.feAndDimensionReduction(1, dirName);
	    
	        }
	        
	        
	        if(methodName.equals("one")){
	    		results  = dTypes.feAndDimensionReduction1(1, dirName);
	    	
	        }
	        
	        
	        if(methodName.equals("two")){
	    		results  = dTypes.feAndDimensionReduction2(1, dirName);
				
	        }
	        
	        
	        if(methodName.equals("three")){
	    		
					results  = dTypes.feAndDimensionReduction3(1, dirName);
				
				//System.out.println("Output: "+results[0]);
	    	
	        }
	        
	        

				
				
	        } catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
				
				
	    
	        
	        
	        
				
		
	       return  getSampleAtomArray(results) ;
	        
	       

	     
	     

}
	    
		private static Atom [] getSampleAtomArray(Object [] results){
		  	
			
			MWCellArray mwCellArray  = null;
			MWArray singleCell  = null;
			MWNumericArray numericArray = null;
			Atom [] viewAtomArray = null;
			
			try {
			
				
			
				
				mwCellArray = (MWCellArray) results[0];
				int [] dimArray = mwCellArray.getDimensions();
				int rowNumber = dimArray[0];
				int columnNumber = dimArray[1];
				
				int [] initArray = new int [rowNumber*columnNumber+1];
				viewAtomArray = Atom.newAtom(initArray);
				viewAtomArray[0]=Atom.newAtom("setSampleData");
				int counterAtomArray = 1;
				
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
					
					viewAtomArray[counterAtomArray] = filePathAtom;
					
					viewAtomArray[counterAtomArray+1] = fileNameAtom;
				
					viewAtomArray[counterAtomArray+2] = xPositionAtom;
				
					viewAtomArray[counterAtomArray+3] = yPositionAtom;
					
					counterAtomArray = counterAtomArray+4;
				}
				
//				for(int i = 0; i<sampleAtomArray.length; i++){
//					System.out.println(sampleAtomArray[i]);
//				}
				
				return viewAtomArray;
				
			}finally{
				
				mwCellArray.dispose();
				singleCell.dispose();
				numericArray.dispose();
				
				mwCellArray = null;
				singleCell = null;
				numericArray = null;
				
				
			}

	  }
	    
	    

}
