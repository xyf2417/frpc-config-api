package xyf.frpc.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import xyf.frpc.remoting.server.ProviderServer;

public class Application extends AbstractConfig implements ApplicationContextAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1002712148114177544L;
	
	private static Application application;
	
	private ProviderServer providerServer;
	
	public static Application getApplication() {
		return application;
	}
	
	public static void setApplication(Application application) {
		Application.application = application;
	}
	
	Map<String, Service> knownProviders = new ConcurrentHashMap<String, Service>();
	
	private ApplicationContext applicationContext;
	
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public String toString() {
		return "Frpc application: " + this.getName();
	}
}


