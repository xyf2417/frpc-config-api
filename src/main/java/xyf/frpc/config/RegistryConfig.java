package xyf.frpc.config;

import java.util.ArrayList;
import java.util.List;

public class RegistryConfig extends AbstractConfig {
	
	public static class RegistryAddress
	{
		private final String host;
		
		private final int port;
		
		public RegistryAddress(String host, int port)
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
		
		
		public String toUrlFormat() {
			return host + ":" + port;
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
	
	
	private List<RegistryAddress> addresses;
	

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public void setAddresses(List<String> addrs) {
		if(addresses == null) {
			addresses = new ArrayList<RegistryAddress>();
		}
		for(String ad : addrs)
		{
			String host = ad.split(":")[0];
			String port = ad.split(":")[1];
			int iport = Integer.valueOf(port);
			addresses.add(new RegistryAddress(host,iport));
		}
	}
	
	public List<RegistryAddress> getAddresses()
	{
		return addresses;
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("Registry(");
		for(RegistryAddress address : addresses) {
			res.append(address);
			res.append(",");
		}
		res.replace(res.length() - 1, res.length(), ")");
		return res.toString(); 
	}
	
}
