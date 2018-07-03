package xyf.frpc.config;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import xyf.frpc.config.util.ExtensionLoader;
import xyf.frpc.remoting.config.Protocol;
import xyf.frpc.rpc.proxy.ProxyFactory;

public class Reference extends AbstractConfig {

	private static final long serialVersionUID = 1L;

	private final static ProxyFactory proxyFactory = (ProxyFactory) ExtensionLoader
			.getExtensionLoader(ProxyFactory.class).getExtension("jdk");

	private final static Log logger = LogFactory.getLog(Reference.class);

	private ApplicationContext applicationContext;

	private ProtocolConfig protocolConfig;

	private Protocol protocol;

	private String name;

	private Object ref;

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

	public void init() throws FrpcIlleConfigException {
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
				throw new FrpcIlleConfigException("Must has a <frpc:protocol/>");
			}
			if (pcs.size() != 1) {
				throw new FrpcIlleConfigException(
						"Just one <frpc:protocol/> are allowed, but find "
								+ pcs.size() + " tags");
			}

			this.protocolConfig = pcs.entrySet().iterator().next().getValue();
		}

		if (protocol == null) {
			protocol = (Protocol) ExtensionLoader.getExtensionLoader(
					Protocol.class).getExtension(protocolConfig.getName());
		}

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
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
