package com.skilrock.lms.coreEngine.instantPrint.service;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import com.skilrock.ipe.instantprint.InstantPrint;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;

//Code commented because of compilation problems...

public class ServiceDelegate implements IServiceDelegate {
	private static ServiceDelegate serviceDelegate;
	// on lines of Singleton pattern
	static {
		serviceDelegate = new ServiceDelegate();
	}

	static public ServiceDelegate getInstance() {
		return serviceDelegate;
	}

	private InstantPrint instantPrintRemote = null;

	private ServiceDelegate() {
		// Blank implementation
	}

	/**
	 * This method invokes the SessionBean method.
	 * 
	 * @param sReq -
	 *            ServiceRequest coarse grain object containing bean data
	 * @return - ServiceResponse object containing bean data
	 */
	public ServiceResponse getResponse(ServiceRequest sReq) {
		ServiceResponse sResponse = null;
//		try {
//			instantPrintRemote = initializeRemote();
//			sResponse = instantPrintRemote.getResponse(sReq);
//		} catch (RemoteException CreateException) {
//			CreateException.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				instantPrintRemote.remove();
//			} catch (RemoteException re) {
//				// TODO Auto-generated catch block
//				re.printStackTrace();
//			} catch (RemoveException re) {
//				// TODO Auto-generated catch block
//				re.printStackTrace();
//			}
//		}
		return sResponse;
	}

	/**
	 * This method initializes the remote EJB.
	 * 
	 * @return - the remote class object.
	 */
	private InstantPrint initializeRemote() {
//		try {
//			// Look up home using Service Locator pattern
//			InstantPrint drawGameHome = (InstantPrint) ServiceLocator
//					.getInstance().lookupHome("InstantPrintEngineRemote",
//							InstantPrint.class);
//			instantPrintRemote = drawGameHome.create();
//		} catch (NamingException ne) {
//			ne.printStackTrace();
//		} catch (RemoteException re) {
//			re.printStackTrace();
//		} catch (CreateException ce) {
//			ce.printStackTrace();
//		}

		return instantPrintRemote;

	}

}
