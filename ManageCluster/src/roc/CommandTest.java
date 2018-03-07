package roc;

import org.I0Itec.zkclient.ZkClient;

public class CommandTest {
	public static void main(String[] args){
		ZkClient zkClient = ZKUtil.getZKClient();
		zkClient.writeData(ClusterManagerClient.COMMAND_PATH, "create");
	}
}
