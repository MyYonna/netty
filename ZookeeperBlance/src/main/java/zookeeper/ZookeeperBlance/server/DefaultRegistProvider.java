package zookeeper.ZookeeperBlance.server;

import org.I0Itec.zkclient.ZkClient;
/**
 * 往zookeper上注册服务端临时节点
 * @author Administrator
 *
 */
public class DefaultRegistProvider implements RegistProvider {
	
	

	public void regist(ZooKeeperRegistContext context) {
		// TODO Auto-generated method stub
		String path = context.getMePath();
		ZkClient zkClient = context.getZkClient();
		try{
			zkClient.createEphemeral(path, context.getServerData());
		}catch(Exception e){
			String parentDir = path.substring(0,path.lastIndexOf("/"));
			zkClient.createPersistent(parentDir, true);
			regist(context);
		}
	}

	public void unRegist(ZooKeeperRegistContext context) {
		// TODO Auto-generated method stub
		String path = context.getMePath();
		ZkClient zkClient = context.getZkClient();
		try{
			if(zkClient.exists(path)){
				zkClient.delete(path);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return;
	}

}
