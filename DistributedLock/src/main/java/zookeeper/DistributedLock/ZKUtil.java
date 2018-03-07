package zookeeper.DistributedLock;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class ZKUtil {
	public static final String SERVERS_PATH = "/Mutex";
    private static final Integer SESSION_TIME_OUT = 10000;
    private static final Integer CONNECT_TIME_OUT = 10000;
	public static ZkClient getZKClient(){
		return new ZkClient("localhost:2181,localhost:2182,localhost:2183",SESSION_TIME_OUT,CONNECT_TIME_OUT,new SerializableSerializer());
	}
}
