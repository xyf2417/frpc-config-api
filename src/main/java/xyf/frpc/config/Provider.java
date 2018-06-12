package xyf.frpc.config;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import xyf.frpc.config.util.ExtensionLoader;
import xyf.frpc.remoting.config.Exporter;
import xyf.frpc.remoting.config.Protocol;

public class Provider extends AbstractConfig implements InitializingBean, ApplicationContextAware{

	private final static Log logger = LogFactory.getLog(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 6648981211041211555L;
	
	private ApplicationContext applicationContext;
	
	/**
	 * The implementation of the proxied interface
	 */
	private Object target;
	
	
	private Class interfaceClass;
	
	private ProtocolConfig protocolConfig;
	
	private Protocol protocol;
	
	private Exporter<?> exporter;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Class getInterface() {
		return interfaceClass;
	}

	public void setInterface(String inter){
		try{
			this.interfaceClass = Class.forName(inter);
		} catch(Exception e) {
			throw new IllegalStateException("Can't load the interface with name " + inter);
		}
	}

	public void afterPropertiesSet() throws FrpcIlleConfigException  {
		//find the protocolConfig from the applicationContext
		if(protocolConfig == null) {
			Map<String, ProtocolConfig> pcs = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProtocolConfig.class, false, false);
			if(pcs == null || pcs.size() == 0 ) {
				throw new FrpcIlleConfigException("Must has a <frpc:protocol/>");
			}
			if(pcs.size() != 1) {
				throw new FrpcIlleConfigException("Just one <frpc:protocol/> are allowed, but find " + pcs.size() + " tags");
			}
			
			this.protocolConfig = pcs.entrySet().iterator().next().getValue();
		}
		synchronized(protocol) {
			if(protocol == null) {
				protocol = (Protocol) ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(protocolConfig.getName());
			}
		}
		exporter = protocol.export(interfaceClass.getName(), protocolConfig, this);
		
		if(logger.isInfoEnabled()){
			logger.info("frpc: export " + interfaceClass.getName());
		}
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.applicationContext = context;
	}
	
}
