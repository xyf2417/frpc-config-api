package xyf.frpc.config;

import java.util.ArrayList;
import java.util.List;

public class RegistryConfig extends AbstractConfig {
	
	public static class RegistryConfigAddress
	{
		private final String host;
		
		private final int port;
		
		public RegistryConfigAddress(String host, int port)
		{
			this.host = host;
			this.port = port;
		}
		
		public String getHost() {
			return host;
		}


		public int getPort() {
			return port;
		}

		
		@Override
		public String toString() {
			return "Registry(" + host + ":" + port + ")";
		}
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7715718518557788713L;
	
	/**
	 * The protocol type of this registry
	 */
	private String type;
	
	
	private List<RegistryConfigAddress> addresses;
	

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public void setAddress(String address) {
		if(addresses == null) {
			addresses = new ArrayList<RegistryConfigAddress>();
		}
		String [] ads = address.split(",");
		for(String ad : ads)
		{
			String host = ad.split(":")[0];
			String port = ad.split(":")[1];
			int iport = Integer.valueOf(port);
			addresses.add(new RegistryConfigAddress(host,iport));
		}
	}
	
	public List<RegistryConfigAddress> getAddresses()
	{
		return addresses;
	}
	
}
