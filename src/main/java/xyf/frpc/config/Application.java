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
	
	Map<String, Provider> knownProviders = new ConcurrentHashMap<String, Provider>();
	
	private ApplicationContext applicationContext;
	
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
//	public void initProviders() {
//		Map<String, Provider> providersMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, Provider.class, false, false);
//		if(providersMap != null && providersMap.size() > 0){
//			for(Map.Entry<String, Provider> entry : providersMap.entrySet()) {
//				Provider provider = entry.getValue();
//				knownProviders.put(provider.getInterface().getName(), provider);
//			}
//		}
//	}
//	
//	public <T> Invoker<T> getInovoker(String fullName)
//	{
//		System.out.println("--Applicaiton getInvoker:" + fullName);
//		if(knownProviders.size() == 0) {
//			initProviders();
//		}
//		if(knownProviders.size() == 0) {
//			return null;
//		}
//		
//		Provider provider = knownProviders.get(fullName);
//		
//		if(provider == null) {
//			return null;
//		}
//		
//		Object proxy = proxyFactory.getProxy(provider.getInterface(), provider.getTarget());
//		AbstractInvoker invoker = new DefaultInvoker();
//		invoker.setProxy(proxy);
//		invoker.setInterface(provider.getInterface());
//		return invoker;
//	}
//
//
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
//
//	public ProviderServer getProviderServer() {
//		return providerServer;
//	}
//	
//	public void initProviderServer() {
//		System.out.println("------------------------------Applicaiton initProviderServer");
//		
//		providerServer = ServiceLoader.getService("providerServer");
//		
//		new ServerThread().start();
//		
//	}
//	
//	class ServerThread extends Thread {
//		public void run() {
//			try {
//				System.out.println("------------------------------Applicaiton beforebind");
//				providerServer.bind(8080);
//				System.out.println("------------------------------Applicaiton endbind");
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				System.out.println("------------------------------Applicaiton initProviderServer catch");
//			}
//			System.out.println("------------------------------Applicaiton initProviderServer end");
//		}
//	}
}


