package com.skilrock.lms.common.utility;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class PropertyLoader {
	private static Properties applicationProperties = null;

	private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;

	static Log logger = LogFactory.getLog(PropertyLoader.class);

	private static final boolean THROW_ON_LOAD_FAILURE = true;

	/**
	 * Get the property of the given key value from the loaded properties file
	 * 
	 * @param propertyName
	 *            name of the property to retrieve.
	 * @return the retrieved property
	 */
	public static String getProperty(String propertyName) {
		String propertyValue = null;
		try {
			propertyValue = applicationProperties.getProperty(propertyName);
		} catch (Exception e) {
			// ignore this and return null
		}
		return propertyValue;
	}

	/**
	 * A convenience overload of {@link #loadProperties(String, ClassLoader)}
	 * that uses the current thread's context classloader.
	 */
	public static Properties loadProperties(final String name) {
		return loadProperties(name, Thread.currentThread()
				.getContextClassLoader());
	}

	public static Properties loadProperties(String name, ClassLoader loader) {
		if (name == null) {
			throw new IllegalArgumentException("null input: name");
		}
		InputStream in = null;
		try {

			in = loader.getResourceAsStream(name);
			if (in != null) {
				applicationProperties = new Properties();
				applicationProperties.load(in); // can throw IOException
			}
			/*
			 * else { // try to load using absolute path String configFile =
			 * "D:/ID/Project-Interactive
			 * Gaming/ProjectWorkspace/ITG/properties/"+name; in = new
			 * java.io.FileInputStream(configFile); applicationProperties = new
			 * Properties(); applicationProperties.load (in); // can throw
			 * IOException System.err.println("File:"+configFile+" is loaded
			 * using absolute path"); }
			 */
		} catch (Exception e) {
			System.err.println("Exception in loading files " + e.getMessage());
			applicationProperties = null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable ignore) {
				}
			}
		}

		if (THROW_ON_LOAD_FAILURE && applicationProperties == null) {
			throw new IllegalArgumentException("could not load ["
					+ name
					+ "]"
					+ " as "
					+ (LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle"
							: "a classloader resource"));
		}

		return applicationProperties;
	}

	private PropertyLoader() {
	} // this class is not extentsible

} // end of class
