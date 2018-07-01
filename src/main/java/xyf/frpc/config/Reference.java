package xyf.frpc.config;

public class Reference extends AbstractConfig {

	private String name;

	private Class interfaceClass;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
