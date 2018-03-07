package roc;

import org.I0Itec.zkclient.ZkClient;

public class ZKUtil {
	public static final String FTP_CONFIG_NODE_NAME = "/config/ftp";
    private static final Integer SESSION_TIME_OUT = 10000;
    private static final Integer CONNECT_TIME_OUT = 10000;
	public static ZkClient getZKClient(){
		return new ZkClient("localhost:2181,localhost:2182,localhost:2183");
	}
}
