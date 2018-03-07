package zookeeper.ZookeeperBlance.server;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;
/**
 * 更新临时节点上的服务器负载信息
 * @author Administrator
 *
 */
public class DefaultBalanceUpdateProvider implements BalanceUpdateProvider {

	private String serverPath;
	private ZkClient zkClient;
	
	
	public DefaultBalanceUpdateProvider(String serverPath, ZkClient zkClient) {
		super();
		this.serverPath = serverPath;
		this.zkClient = zkClient;
	}

	public boolean addBalance(Integer step) {
		// TODO Auto-generated method stub
		Stat stat = new Stat();
		ServerData serverData;
		while(true){
			try{
				serverData = zkClient.readData(this.serverPath,stat);
				serverData.setBalance(serverData.getBalance()+step);
				zkClient.writeData(serverPath, serverData, stat.getVersion());
				return true;
			}catch(Exception e){
				return false;
			}
		}
	}

	public boolean reduceBalance(Integer step) {
		// TODO Auto-generated method stub
		Stat stat = new Stat();
		ServerData serverData;
		while(true){
			try{
				serverData = zkClient.readData(serverPath, stat);
				final Integer currBalance = serverData.getBalance();
				serverData.setBalance(currBalance>step?currBalance-step:0);
				zkClient.writeData(serverPath, serverData, stat.getVersion());
				return true;
			}catch(Exception e){
				return false;
				
			}
		}
	}

}
