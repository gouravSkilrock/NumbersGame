package com.skilrock.lms.coreEngine.instantPrint.service;

import static com.skilrock.lms.common.filter.LMSFilterDispatcher.getServDelegateUrl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;


public class ServiceLocator {
	private static ServiceLocator me;
	// on lines of Singleton pattern
	static {
		me = new ServiceLocator();
	}

	static public ServiceLocator getInstance() {
		return me;
	}

	private Map<Object, EJBHome> cache;

	private Context ctx;

	
	private ServiceLocator() {
		try {
			cache = Collections.synchronizedMap(new HashMap<Object, EJBHome>());
			Hashtable<String, String> props = new Hashtable<String, String>();
			props.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			props.put("java.naming.factory.url.pkgs",
					"org.jboss.naming:org.jnp.interfaces");
			props.put(Context.PROVIDER_URL, "jnp://" + getServDelegateUrl());
			ctx = new InitialContext(props);
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	
	@SuppressWarnings("unchecked")
	public EJBHome lookupHome(String jndiName, Class homeClass)
			throws NamingException {
		EJBHome home = cache.get(homeClass);
		if (home == null) {
			home = (EJBHome) PortableRemoteObject.narrow(ctx.lookup(jndiName),
					homeClass);
			// cache the home for repeated use
			cache.put(homeClass, home);
		}
		return home;
	}

}
