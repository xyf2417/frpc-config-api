package xyf.frpc.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import xyf.frpc.rpc.AbstractInvoker;
import xyf.frpc.rpc.DefaultInvoker;
import xyf.frpc.rpc.Invoker;

public class Application extends AbstractConfig implements ApplicationContextAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1002712148114177544L;
	
	private static Application application;
	
	public static Application getApplication() {
		return application;
	}
	
	public void initGlobalApplicaiton() {
		if(application == null){
			Map<String, Application> applications = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, Application.class, false, false);
		}
	}
	
	Map<String, Provider> knownProviders = new ConcurrentHashMap<String, Provider>();
	
	private ApplicationContext applicationContext;
	
	private ProxyFactory proxyFactory = new JDKProxyFactory();
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void initProviders() {
		Map<String, Provider> providersMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, Provider.class, false, false);
		if(providersMap != null && providersMap.size() > 0){
			for(Map.Entry<String, Provider> entry : providersMap.entrySet()) {
				Provider provider = entry.getValue();
				knownProviders.put(provider.getInterface().getName(), provider);
			}
		}
	}
	
	public <T> Invoker<T> resolveInovoker(String fullName)
	{
		if(knownProviders.size() == 0) {
			initProviders();
		}
		if(knownProviders.size() == 0) {
			//todo no registered providers
		}
		
		Provider provider = knownProviders.get(fullName);
		
		if(provider == null) {
			//todo can't find the registered provider for this interface
		}
		
		Object proxy = proxyFactory.getProxy(provider.getInterface(), provider.getTarget());
		AbstractInvoker invoker = new DefaultInvoker();
		invoker.setProxy(proxy);
		invoker.setInterface(provider.getInterface());
		return invoker;
	}


	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
