package xyf.frpc.config;

public class Provider extends AbstractConfig{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6648981211041211555L;
	
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
	
}
