package xyf.frpc.config;

import java.util.HashMap;
import java.util.Map;

public class ProtocolConfig extends AbstractConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4612092777943783648L;

	/**
	 * The name of the frpc, the default protocol is frpc
	 */
	private String name;
	
	/**
	 * The server port which this server binded to.
	 */
	private int port;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
}
