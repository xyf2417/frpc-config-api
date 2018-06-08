package xyf.frpc.config.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceLoader {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private static final String SERVICE_PREFIX = "META-INF/frpc/services/";
	
	private static final String SERVICE_URL_TO_TRIM = "file:/";
	
	private static final Object NO_INITIALIZED_OBJECT = new Object();
	
	private static final Pattern SERVICE_PATTERN = Pattern.compile("^[a-zA-Z0-9]+=(\\w+\\.)*\\w+$");
	
	private ClassLoader loader = (Thread.currentThread().getContextClassLoader() != null) ?
			 Thread.currentThread().getContextClassLoader()
			 : ClassLoader.getSystemClassLoader();
	
	private Map<String, Object> servicesCache = new ConcurrentHashMap<String, Object>();
	
	
	private void init() {
		if(logger.isErrorEnabled()) {
			logger.error("The ServiceLoader is initialized already!");
		}
		throw new RuntimeException("The ServiceLoader is initialized already!");
	}
	
	public Object getService(String serviceName) throws IOException {
		Object obj = servicesCache.get(serviceName);
		if(obj == null ) {
			Enumeration<URL> urls = ClassLoader.getSystemResources(SERVICE_PREFIX);
			while(urls.hasMoreElements()) {
				String urlPath = urls.nextElement().toString();
				if(urlPath.startsWith(SERVICE_URL_TO_TRIM)) {
					urlPath = urlPath.substring(SERVICE_URL_TO_TRIM.length());
					File file = new File(urlPath);
					String [] fileNames = file.list();
					if(fileNames != null && fileNames.length > 0) {
						for(String filename : fileNames) {
							BufferedReader reader = new BufferedReader(new FileReader(urlPath + "/" + filename));
							String s;
							while((s = reader.readLine()) != null) {
								if(SERVICE_PATTERN.matcher(s).matches()) {
									
								}
								else
								{
									
								}
							}
						}
					}
				}
			}
		}
		return obj;
	}
	
}
