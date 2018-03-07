package roc;

import org.I0Itec.zkclient.ZkClient;

public class ZKUtil {
	public static final String Master_NODE_NAME = "/master";
	
	public static ZkClient getZKClient(){
		return new ZkClient("localhost:2181,localhost:2182,localhost:2183");
	}
}
