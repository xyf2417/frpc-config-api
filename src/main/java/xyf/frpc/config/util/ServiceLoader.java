package xyf.frpc.config.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceLoader {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private static final String SERVICE_PREFIX = "META-INF/frpc/services/frpc-service";
	
	private static final String SERVICE_URL_TO_TRIM = "file:/";
	
	private static final Object NO_INITIALIZED_OBJECT = new Object();
	
	private static final Pattern SERVICE_PATTERN = Pattern.compile("^[a-zA-Z0-9]+=(\\w+\\.)*\\w+$");
	
	private static final ClassLoader loader = (Thread.currentThread().getContextClassLoader() != null) ?
			 Thread.currentThread().getContextClassLoader()
			 : ClassLoader.getSystemClassLoader();
	
	private static Map<String, Object> servicesCache = new ConcurrentHashMap<String, Object>();
	
	
	public static <T> T getService(String serviceName){
		Object obj = servicesCache.get(serviceName);
		if(obj == null ) {
			try{
			Enumeration<URL> urls = ClassLoader.getSystemResources(SERVICE_PREFIX);	
			while(urls.hasMoreElements()) {
				URL url = urls.nextElement();
				File file = new File(url.toString());
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));  
				 String line = null;  
                 while ((line = reader.readLine()) != null) {
                	 if(SERVICE_PATTERN.matcher(line).matches()) {
                		 System.out.println("----serviceLoader line:" + line);
							String [] pairs = line.split("=");
							String name = pairs[0];
							String fullName = pairs[1];
							if(!servicesCache.containsKey(name)) {
								Object service = Class.forName(fullName).newInstance();
								servicesCache.put(name, service);
							}
						}	
				}			
			}
			}catch(Exception e) {
				System.out.println("----serviceLoader catch:" + e.getMessage());
			}
		}//if obj == null;
		obj = servicesCache.get(serviceName);
		return (T)obj;
	}
	
}
