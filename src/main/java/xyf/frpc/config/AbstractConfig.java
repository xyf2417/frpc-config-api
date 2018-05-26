package xyf.frpc.config;

/**
 * The abstract config of Feng-RPC
 * @author xyf
 *
 */
public class AbstractConfig implements Config {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7989845807234206774L;
	
	/**
	 * The ID of this config
	 */
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
