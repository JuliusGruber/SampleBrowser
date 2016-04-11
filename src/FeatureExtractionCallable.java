

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import MatlabInterface.*;

public  class FeatureExtractionCallable implements Callable <Object[]> {

	   final String methodName;
	   final String dirName;
	   final int port;

	    public FeatureExtractionCallable(final String methodName, final String dirName, final int port) {
	      this.methodName = methodName;
	 	  this.dirName = dirName;
	      this.port  = port;
	     
	
	    }
	    

	    
	    
	    public Object [] call()  {
	   
	    	System.out.println("call() with: "+methodName);
	    	
	    	
	    	
	        Registry reg = null;
	        MatlabInterfaceRemote dTypes = null;
	        Object[] results = null;
	        
	        try{
	        reg = LocateRegistry.getRegistry(port);
	        String lookupString  = "MatlabInterface"+port;
	        dTypes = (MatlabInterfaceRemote)reg.lookup(lookupString);

	        
	    
	        
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
	        
	        
	        if(methodName.equals("getFileInfo")){
	    		results  = dTypes.getAllFileNames(1, dirName);
				
	        }
				
				
	        } catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
				
				
	    
	        
	        
	        
				
		
	    	 
	        
	       

	      return results;
	     

}
}
