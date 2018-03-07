package zookeeper.ZookeeperBlance.server;

import org.I0Itec.zkclient.ZkClient;

public class ZooKeeperRegistContext {
	private String mePath;
	private ZkClient zkClient;
	private ServerData serverData;
	
	
	public ZooKeeperRegistContext(String mePath, ZkClient zkClient, ServerData serverData) {
		super();
		this.mePath = mePath;
		this.zkClient = zkClient;
		this.serverData = serverData;
	}
	public String getMePath() {
		return mePath;
	}
	public void setMePath(String mePath) {
		this.mePath = mePath;
	}
	public ZkClient getZkClient() {
		return zkClient;
	}
	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}
	public ServerData getServerData() {
		return serverData;
	}
	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}
	
	
}
