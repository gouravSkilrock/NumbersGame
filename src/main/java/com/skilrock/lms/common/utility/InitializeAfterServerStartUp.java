package com.skilrock.lms.common.utility;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import com.skilrock.lms.coreEngine.instantPrint.gameMgmt.common.NewGameUploadHelper;


public class InitializeAfterServerStartUp extends Thread {
private Object check(){

	MBeanServerConnection server;
	
	try {
		server = (MBeanServerConnection) new InitialContext()
				.lookup("jmx/rmi/RMIAdaptor");
		ObjectName on = new ObjectName("jboss.system:type=Server");
		Object var = server.getAttribute(on, "Started");
		return var;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return null;
	
}
	public void run() {
	
	while(check()==null&& ((Boolean)check())!=true){
			try {
				InitializeAfterServerStartUp.sleep(10000);
				check();
			} catch (InterruptedException e) {
					e.printStackTrace();
			}
				
			
		}
// Here add code to call helper
	
// Initializing IPE Serivce Data Starts
	System.out.println("Initializing IPE Serivce Data Starts ");
	NewGameUploadHelper.onStartGame();
	System.out.println("Initializing IPE Serivce Data Ends ");
// Ends		


	}

}
