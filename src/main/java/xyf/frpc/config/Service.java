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
import xyf.frpc.remoting.config.ExportInfo;
import xyf.frpc.remoting.config.Exporter;
import xyf.frpc.remoting.config.Protocol;
import xyf.frpc.rpc.AbstractInvoker;
import xyf.frpc.rpc.DefaultInvoker;
import xyf.frpc.rpc.RpcException;
import xyf.frpc.rpc.proxy.ProxyFactory;

public class Service extends AbstractConfig implements InitializingBean, ApplicationContextAware{
	
	private final static ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension("jdk");
	
	private final static Log logger = LogFactory.getLog(Service.class);

	private static final long serialVersionUID = 6648981211041211555L;
	
	private ApplicationContext applicationContext;
	
	/**
	 * The implementation of the proxied interface
	 */
	private Object ref;
	
	
	private Class interfaceClass;
	
	private ProtocolConfig protocolConfig;
	
	private Protocol protocol;
	
	private Exporter<?> exporter;

	public Object getRef() {
		return ref;
	}

	public void setRef(Object ref) {
		this.ref = ref;
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

	public void afterPropertiesSet() throws FrpcIllegalConfigException  {
		//find the protocolConfig from the applicationContext
		if(protocolConfig == null) {
			Map<String, ProtocolConfig> pcs = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProtocolConfig.class, false, false);
			if(pcs == null || pcs.size() == 0 ) {
				throw new FrpcIllegalConfigException("Must has a <frpc:protocol/>");
			}
			if(pcs.size() != 1) {
				throw new FrpcIllegalConfigException("Just one <frpc:protocol/> are allowed, but find " + pcs.size() + " tags");
			}
			
			this.protocolConfig = pcs.entrySet().iterator().next().getValue();
		}
		
		if(protocol == null) {
			protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(protocolConfig.getName());
		}
		
		try {
			exporter = export();
		} catch (RpcException e) {
			logger.error("frpc: service " + this.interfaceClass.getName() + " export error");
			e.printStackTrace();
		}
		
		if(logger.isInfoEnabled()){
			logger.info("frpc: exported " + interfaceClass.getName());
		}
	}
	
	private Exporter export() throws RpcException {
		Object proxy = proxyFactory.getProxy(this.getInterface(), this.getRef(), true);
		AbstractInvoker<?> invoker = new DefaultInvoker();
		invoker.setProxy(proxy);
		invoker.setInterface(this.getInterface());
		ExportInfo exportInfo = ExportInfo.getLocalExportInfo(this.protocolConfig.getPort());
		return protocol.export(exportInfo, invoker);
		
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.applicationContext = context;
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("Service: ");
		res.append(interfaceClass.getSimpleName());
		return res.toString(); 
	}
	
}
