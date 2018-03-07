package zookeeper.ZookeeperBlance.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import zookeeper.ZookeeperBlance.server.ServerData;
import zookeeper.ZookeeperBlance.util.ZKUtil;


public class ClientRunner {

	public static final int CLIENT_QTY = 30;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Thread> threadList = new ArrayList<Thread>(CLIENT_QTY);
		final List<Client> clientList = new ArrayList<Client>();
		ZkClient zkClient = ZKUtil.getZKClient();
		final BalanceProvider<ServerData> balanceProvider = new DefaultBalanceProvide(zkClient, ZKUtil.SERVERS_PATH);
		try{
			for(int i = 0;i<CLIENT_QTY;i++){
				Thread thread = new Thread(new Runnable(){

					public void run() {
						// TODO Auto-generated method stub
						Client client = new ClientImpl(balanceProvider);
						clientList.add(client);
						try{
							client.connect();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					
				});
				thread.start();
				threadList.add(thread);
				Thread.sleep(2000);
			}
			System.out.println("回车退出");
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			for(int i=0;i<clientList.size();i++){
				try{
					clientList.get(i).disConnect();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			for(int i=0;i<threadList.size();i++){
				threadList.get(i).interrupt();
				try{
					threadList.get(i).join();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

}
