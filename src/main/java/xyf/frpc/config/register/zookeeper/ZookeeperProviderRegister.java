package xyf.frpc.config.register.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import xyf.frpc.config.ConfigConstants;
import xyf.frpc.config.RegistryConfig.RegistryAddress;
import xyf.frpc.config.register.ProviderRegister;

public class ZookeeperProviderRegister implements ProviderRegister {

	public String register(String value, RegistryAddress address) {
		
		
		CountDownLatch latch = new CountDownLatch(1);
		try{
			ZooKeeper zookeeper = new ZooKeeper("localhost:2181",
					5000,
					new ConnectWatcher(latch));
			System.out.println(zookeeper.getState());
			try {
				latch.await();
			}catch(InterruptedException e) {
				//no op
			}
			String path = ConfigConstants.ZOOKEEPER_ROOT_PATH;
			return zookeeper.create(path, 
					value.getBytes(), 
					Ids.OPEN_ACL_UNSAFE, 
					CreateMode.PERSISTENT_SEQUENTIAL);
		}
		catch(Exception e) {
			return null;
		}
	}

}

class ConnectWatcher implements Watcher {
	private CountDownLatch sem;
	public ConnectWatcher(CountDownLatch cdl) {
		this.sem = cdl;
	}
	public void process(WatchedEvent event) {
		if(event.getState() == Watcher.Event.KeeperState.SyncConnected)
		{
			sem.countDown();
		}
	}
}
