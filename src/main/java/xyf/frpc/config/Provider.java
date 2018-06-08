package xyf.frpc.config;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Provider extends AbstractConfig implements InitializingBean, ApplicationContextAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6648981211041211555L;
	
	private ApplicationContext applicationContext;
	
	/**
	 * The implementation of the proxied interface
	 */
	private Object target;
	
	
	private Class cInterface;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Class getInterface() {
		return cInterface;
	}

	public void setInterface(String inter) throws ClassNotFoundException {
		this.cInterface = Class.forName(inter);
	}

	public void afterPropertiesSet()  {
		try{
			if(Application.getApplication() == null) {
				Map<String, Application> applications = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, Application.class, false, false);
				if(applications == null || applications.size() == 0 ) {
					throw new RuntimeException("Must has an applicaiton");
				}
				if(applications.size() != 1) {
					throw new RuntimeException("Must has just one application");
				}
				Application.setApplication(applications.entrySet().iterator().next().getValue());
				Application.getApplication().initProviderServer();
			}
		}
		catch(Throwable e) {
			System.out.println("----------------------------------------provider afterPropertiesSet catch");
		}
		
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.applicationContext = context;
	}
	
}
