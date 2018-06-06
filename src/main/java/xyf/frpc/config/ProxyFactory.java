package xyf.frpc.config;

public interface ProxyFactory {
	public Object getProxy(Class cinterface, Object target);
}
