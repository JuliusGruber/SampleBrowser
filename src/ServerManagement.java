

import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;
import com.cycling74.max.MaxWindow;


import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.remoting.*;

import MatlabFE.Fe;
import MatlabFE.FeRemote;

import java.rmi.*;
import java.rmi.registry.*;

public class ServerManagement extends MaxObject {
	
	


	


	public  void startServers(){
//		
//		MaxPatcher parentPatcher = this.getParentPatcher();
//		MaxWindow window = parentPatcher.getWindow();
//		window.setVisible(false);
//		
		startServer(3000);
//    	startServer(1103);
//    	startServer(1104);
//    	startServer(1105);
    	
    	
//    	window.setVisible(true);
	}
	
	
private static  void startServer(int port){
	       System.out.println("Please wait for the server registration notification.");
	        long start = System.currentTimeMillis();
	        Registry reg = null;

	        
	        
	        
	         try{
	           Fe cls = new Fe();
	           FeRemote clsRem =
	                    (FeRemote)RemoteProxy.newProxyFor(cls,/*object that handles remote method invocations*/
	                                                                  FeRemote.class,/*remote interface for the proxy object*/
	                                                                  false/*flag to decide whether or not MWArray-derived method outputs
	                                                                         should be converted to their corresponding Java types. Setting
	                                                                         it to false will retun values as MWArray derived class*/);
	            reg = LocateRegistry.createRegistry( port);
	            String bindString = "Fe"+port;
	            reg.rebind(bindString, clsRem);
	            //reg.rebind("MatlabInterface", clsRem);
	            System.out.println("Server registered and running successfully. Time needed " + ((double)(System.currentTimeMillis()-start))/1000 + "s");
	        }
	        catch(RemoteException remote_ex)
	        {
	            remote_ex.printStackTrace();            
	        }        
	        catch(MWException mw_ex)
	        {
	            mw_ex.printStackTrace();
	        }
	}
}
