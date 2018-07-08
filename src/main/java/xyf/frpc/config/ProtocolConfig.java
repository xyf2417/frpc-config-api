package xyf.frpc.config;


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
	
	private String transport;

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

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}
	
	public String getServerKey() {
		return "localhost:" + port;
	}
	
	@Override
	public String toString() {
		return "ProtocolConfig:" + this.getName();
	}
	
}
