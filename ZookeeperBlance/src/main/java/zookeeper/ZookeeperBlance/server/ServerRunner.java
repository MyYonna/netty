package zookeeper.ZookeeperBlance.server;

import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import zookeeper.ZookeeperBlance.util.ZKUtil;

public class ServerRunner {
	private static final int SERVER_QTY = 3;

	public static void main(String[] args) {
		List<Thread> threadList = new ArrayList<Thread>();
		for(int i=0;i<SERVER_QTY;i++){
			final Integer count = i; 
			Thread thread = new Thread(new Runnable(){
				public void run() {
					// TODO Auto-generated method stub
					ZkClient zkClient = ZKUtil.getZKClient();
					ServerData  serverData = new ServerData("127.0.0.1",6000+count);
					Server server = new ServerImpl(zkClient, serverData, ZKUtil.SERVERS_PATH);
					server.bind();
				}
				
			});
			threadList.add(thread);
			thread.start();
		}
		
		for(int i=0;i<threadList.size();i++){
			try{
				threadList.get(i).join();
			}catch(Exception e){
				
			}
		}
	}

}
