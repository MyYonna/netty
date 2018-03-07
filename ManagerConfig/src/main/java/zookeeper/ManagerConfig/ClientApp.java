package zookeeper.ManagerConfig;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ClientApp {
	private FtpConfig ftpConfig;
	private FtpConfig getFtpConfig(){
		if(ftpConfig == null){
			ZkClient zkClient = ZKUtil.getZKClient();
			ftpConfig = (FtpConfig)zkClient.readData(ZKUtil.FTP_CONFIG_NODE_NAME);
			zkClient.subscribeDataChanges(ZKUtil.FTP_CONFIG_NODE_NAME, new IZkDataListener(){

				public void handleDataChange(String dataPath, Object data) throws Exception {
					// TODO Auto-generated method stub
					System.out.println(dataPath+"上配置文件更新了"+".................");
					System.out.println("配置文件更新为："+(FtpConfig)data+"..................");
					ftpConfig = (FtpConfig)data;
				}

				public void handleDataDeleted(String dataPath) throws Exception {
					// TODO Auto-generated method stub
					System.out.println(dataPath+"上数据删除了.................");
					ftpConfig = null;
				}
				
			});
		}
		
		return ftpConfig;
	}
	
	public void run(){
		getFtpConfig();
	}
	
	public void upload(){
		System.out.println("配置文件:"+ftpConfig+"..................");
	}
}
