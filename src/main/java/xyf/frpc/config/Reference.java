package xyf.frpc.config;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import xyf.frpc.config.util.ExtensionLoader;
import xyf.frpc.remoting.config.BindInfo;
import xyf.frpc.remoting.config.Protocol;
import xyf.frpc.rpc.DefaultInvoker;
import xyf.frpc.rpc.Invoker;
import xyf.frpc.rpc.MockInvoker;
import xyf.frpc.rpc.RpcException;
import xyf.frpc.rpc.StubInvoker;
import xyf.frpc.rpc.proxy.ProxyFactory;
import xyf.frpc.rpc.proxy.StubProxyFactoryWrapper;

public class Reference extends AbstractConfig implements FactoryBean, InitializingBean, ApplicationContextAware {

	private static final long serialVersionUID = 1L;

	private final static ProxyFactory proxyFactory = ExtensionLoader
			.getExtensionLoader(ProxyFactory.class).getExtension("jdk");

	private final static Log logger = LogFactory.getLog(Reference.class);

	private ApplicationContext applicationContext;

	private ProtocolConfig protocolConfig;

	private Protocol protocol;

	private String name;

	private volatile Object ref;

	private Class interfaceClass;
	
	private String host;
	
	private int port;
	
	private Object mock;
	
	private Class stubClass;
	

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
		// find the protocolConfig from the applicationContext
		if (protocolConfig == null) {
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
		}

		if (protocol == null) {
			protocol = ExtensionLoader.getExtensionLoader(
					Protocol.class).getExtension(protocolConfig.getName());
		}
		BindInfo bindInfo = new BindInfo();
		bindInfo.setIp(this.getHost());
		bindInfo.setPort(this.getPort());
		
		Invoker forFrpcinvoker = new DefaultInvoker();
		forFrpcinvoker.setInterface(interfaceClass);
		Invoker invoker = null;
		
		try {
			invoker = protocol.refer(bindInfo, forFrpcinvoker);
		} catch (RpcException e) {
			logger.error("frpc: reference refer error");
			e.printStackTrace();
		}
		
		if(mock != null) {
			MockInvoker mockInvoker = new MockInvoker(invoker);
			mockInvoker.setInterface(interfaceClass);
			mockInvoker.setProxy(mock);
			
			invoker = mockInvoker;
		}
		
		
		if(stubClass != null) {
			StubInvoker stubInvoker = new StubInvoker(invoker, stubClass);
			
			ProxyFactory stubProxyFactory = new StubProxyFactoryWrapper(proxyFactory);
			ref = stubProxyFactory.getProxy(interfaceClass, stubInvoker, false);
		}
		else
		{
			ref = proxyFactory.getProxy(interfaceClass, invoker, false);
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Object getObject() throws Exception {
		if(ref == null) {
			init();
		}
		return ref;
	}



	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.applicationContext = context;
	}

	public void afterPropertiesSet() throws Exception {
		
	}

	public Class getObjectType() {
		return Reference.class;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("Reference: ");
		res.append(interfaceClass.getSimpleName());
		res.append("->");
		res.append(host);
		res.append(":");
		res.append(port);
		return res.toString(); 
	}

	public Object getMock() {
		return mock;
	}

	public void setMock(Object mock) {
		this.mock = mock;
	}

	public Class getStubClass() {
		return stubClass;
	}

	public void setStubClass(Class stubClass) {
		this.stubClass = stubClass;
	}
}
