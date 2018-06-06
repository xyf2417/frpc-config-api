package xyf.frpc.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxyFactory implements ProxyFactory{

	public Object getProxy(Class cinterface, Object target) {
		return Proxy.newProxyInstance(cinterface.getClassLoader(), new Class[]{cinterface}, new JDKInvocationHandler(target));
	}
	

	public Object getProxy(Provider provider) {
		return getProxy(provider.getInterface(), provider.getTarget());
	}
	
	

}

class JDKInvocationHandler implements InvocationHandler {
	
	public JDKInvocationHandler(Object target) {
		this.proxied = target;
	}
	private Object proxied;
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		return method.invoke(proxied, args);
	}
	
}
