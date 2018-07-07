package xyf.frpc.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import xyf.frpc.config.util.ExtensionLoader;
import xyf.frpc.remoting.RpcException;
import xyf.frpc.remoting.client.FrpcInvoker;
import xyf.frpc.remoting.config.BindInfo;
import xyf.frpc.remoting.config.Protocol;
import xyf.frpc.rpc.DefaultInvoker;
import xyf.frpc.rpc.Invoker;
import xyf.frpc.rpc.proxy.ProxyFactory;

public class Reference extends AbstractConfig implements FactoryBean, InitializingBean {

	private static final long serialVersionUID = 1L;

	private final static ProxyFactory proxyFactory = (ProxyFactory) ExtensionLoader
			.getExtensionLoader(ProxyFactory.class).getExtension("jdk");

	private final static Log logger = LogFactory.getLog(Reference.class);

	private ApplicationContext applicationContext;

	private ProtocolConfig protocolConfig;

	private Protocol protocol;

	private String name;

	private volatile Object ref;

	private Class interfaceClass;
	
	private String host;

	public Class getInterface() {
		return interfaceClass;
	}

	public void setInterface(String inter) {
		try {
			this.interfaceClass = Class.forName(inter);
		} catch (Exception e) {
			throw new IllegalStateException(
					"Can't load the interface with name " + inter);
		}
	}

	public void init() throws FrpcIllegalConfigException {
		if (ref != null) {
			return;
		}
		Object thisObject = this;
		// find the protocolConfig from the applicationContext
		/*if (protocolConfig == null) {
			Map<String, ProtocolConfig> pcs = applicationContext == null ? null
					: BeanFactoryUtils.beansOfTypeIncludingAncestors(
							applicationContext, ProtocolConfig.class, false,
							false);
			if (pcs == null || pcs.size() == 0) {
				this.protocolConfig = applicationContext.getBean(ProtocolConfig.class);
			} else if (pcs.size() != 1) {
				throw new FrpcIllegalConfigException(
						"Just one <frpc:protocol/> are allowed, but find "
								+ pcs.size() + " tags");
			} else {
				this.protocolConfig = pcs.entrySet().iterator().next().getValue();
			}
			
			if(protocolConfig == null) {
				throw new FrpcIllegalConfigException("Must has a <frpc:protocol/>");
			}
		}*/

		if (protocol == null) {
			protocol = (Protocol) ExtensionLoader.getExtensionLoader(
					Protocol.class).getExtension("frpc");//protocolConfig.getName());
		}
		BindInfo bindInfo = new BindInfo();
		bindInfo.setIp(this.getHost());
		bindInfo.setPort(8080);//protocolConfig.getPort());
		
		Invoker invoker = new DefaultInvoker();
		invoker.setInterface(interfaceClass);
				
		FrpcInvoker frpcInvoker = null;
		try {
			frpcInvoker = (FrpcInvoker) protocol.refer(bindInfo, invoker);
		} catch (RpcException e) {
			logger.error("frpc: reference refer error");
		}
		
		ref = proxyFactory.getProxy(interfaceClass, frpcInvoker, false);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.applicationContext = context;
		System.out.println("-----------------------------------sac");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Object getObject() throws Exception {
		System.out.println("---------------------------------------geto");
		if(ref == null) {
			init();
		}
		return ref;
	}

	public Class getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void afterPropertiesSet() throws Exception {
		//getObject();
		System.out.println("-------------------rerernce aps");
	}
}
