package xyf.frpc.config.register;

import xyf.frpc.config.RegistryConfig;

public interface ProviderRegister {
	/**
	 * ��ע������ע���ṩ��
	 * @param value ע���ֵ
	 * @param address ע�����ĵ�ַ
	 * @return ���صĽڵ����ݣ���ڵ�Ŀ¼�ȣ�
	 */
	public String register(String value, RegistryConfig.RegistryAddress address);
}
