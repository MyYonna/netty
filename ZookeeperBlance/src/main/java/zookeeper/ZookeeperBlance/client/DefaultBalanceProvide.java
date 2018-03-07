package zookeeper.ZookeeperBlance.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import zookeeper.ZookeeperBlance.server.ServerData;

public class DefaultBalanceProvide extends AbstractBalanceProvider<ServerData> {

	private final ZkClient zkClient;
	
	private final String serversPath;
	
	
	@Override
	protected ServerData balanceAlgorithm(List<ServerData> items) {
		// TODO Auto-generated method stub
		if(items.size()>0){
			Collections.sort(items);
			return items.get(0);
		}
		return null;
	}

	@Override
	protected List<ServerData> getBalanceItems() {
		// TODO Auto-generated method stub
		List<ServerData> sdList = new ArrayList<ServerData>();
		
		List<String> children = zkClient.getChildren(serversPath);
		for(int i=0;i<children.size();i++){
			ServerData sd = zkClient.readData(serversPath+"/"+children.get(i));
			sdList.add(sd);
		}
		
		return sdList;
	}

	public ZkClient getZkClient() {
		return zkClient;
	}

	public String getServersPath() {
		return serversPath;
	}

	DefaultBalanceProvide(ZkClient zkClient, String serversPath) {
		this.zkClient = zkClient;
		this.serversPath = serversPath;
	}

	
}
