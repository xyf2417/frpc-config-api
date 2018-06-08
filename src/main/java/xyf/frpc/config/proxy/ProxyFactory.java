package xyf.frpc.config.proxy;

public interface ProxyFactory {
	public Object getProxy(Class cinterface, Object target);
}
