package xyf.frpc.config.register;

import xyf.frpc.config.RegistryConfig;

public interface ProviderRegister {
	
	public String register(String value, RegistryConfig.RegistryAddress address);
}
