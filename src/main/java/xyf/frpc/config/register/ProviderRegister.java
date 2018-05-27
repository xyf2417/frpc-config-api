package xyf.frpc.config.register;

import xyf.frpc.config.RegistryConfig;

public interface ProviderRegister {
	/**
	 * 向注册中心注册提供者
	 * @param value 注册的值
	 * @param address 注册中心地址
	 * @return 返回的节点数据（如节点目录等）
	 */
	public String register(String value, RegistryConfig.RegistryAddress address);
}
