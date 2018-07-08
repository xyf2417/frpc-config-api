package xyf.frpc.config.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xyf.frpc.config.FrpcIllegalConfigException;

public class ExtensionLoader<T> {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private static final String PATH = "META-INF/frpc/services/";
	
	private static final Object NO_INITIALIZED_OBJECT = new Object();
	
	private static final Pattern EXTENSION_PATTERN = Pattern.compile("^[a-zA-Z0-9]+=(\\w+\\.)*\\w+$");
	
	private static final Map<Class<?>, ExtensionLoader<?>> CACHED_LOADERS = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();
	
	private static final ClassLoader classloader = (Thread.currentThread().getContextClassLoader() != null) ?
			 Thread.currentThread().getContextClassLoader()
			 : ClassLoader.getSystemClassLoader();
	
	private Map<String, Object> instances = new ConcurrentHashMap<String, Object>();
	
	private Map<String, Class<?>> classes;
	
	private Class<?> type;
	
	
	@SuppressWarnings("unchecked")
	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
		ExtensionLoader<T> extension = (ExtensionLoader<T>) CACHED_LOADERS.get(type);
		if(extension == null) {
			extension = new ExtensionLoader<T>(type);
			CACHED_LOADERS.put(type, extension);
		}
		return (ExtensionLoader<T>) CACHED_LOADERS.get(type);
	}
	
	public ExtensionLoader(Class<?> type) {
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized T getExtension(String name) {
		Object instance = instances.get(name);
		try{
			if(instance == null ) {
				if(classes == null) {
					findClasses();
				}
				Class<?> clazz = classes.get(name);
				if(clazz == null)
				{
					throw new IllegalStateException("Can't find the class of " + this.type.getName() + " with name " + name);
				}//if clazz == null
				instances.put(name, clazz.newInstance());
			}//if instance == null
		} catch(Exception e) {
			logger.error("frpc: fail to get the extension with name " + name + " of " + this.type.getName());
		}
		instance = instances.get(name);
		return (T)instance;
	}
	
	private void findClasses(){
		if(classes != null)
			return;
		classes = new ConcurrentHashMap<String, Class<?>>();
		try{
			Enumeration<URL> urls = ClassLoader.getSystemResources(PATH + type.getName());	
			while(urls.hasMoreElements()) {
				URL url = urls.nextElement();
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));  
				String line = null;  
		        while ((line = reader.readLine()) != null) {
			        if(EXTENSION_PATTERN.matcher(line).matches()) {
						String [] pairs = line.split("=");
						String serviceName = pairs[0];
						String fullName = pairs[1];
						if(!classes.containsKey(serviceName)) {
							Class<?> clazzService = Class.forName(fullName);
							classes.put(serviceName, clazzService);
						}
					} else {
						throw new FrpcIllegalConfigException("The extension config of " + type.getName() +" is not correct");
					}
		        }			
			}
		}catch(Exception e) {
			logger.error("frpc: fail to find the extension of " + this.type.getName() + ", the nested reason is " + e.getMessage());
		}
	}
}
