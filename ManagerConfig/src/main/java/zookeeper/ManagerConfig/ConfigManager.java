package zookeeper.ManagerConfig;

import org.I0Itec.zkclient.ZkClient;

public class ConfigManager {

	public FtpConfig ftpConfig;
	
	public void loadConfigFromDB(){
		ftpConfig = new FtpConfig("192.168.1.101", 21, "roc", "123456");
	}
	
	public void updateFtpConfigToDB(String host,int port,String user,String password){
		
		System.out.println("更新FTP配置文件到数据库中开始。。。。。。。。。。。。。。。。。。。。。。。。。");
		if(ftpConfig == null){
			ftpConfig = new FtpConfig();
		}
		
		ftpConfig.setHost(host);
		ftpConfig.setUser(user);
		ftpConfig.setPassword(password);
		ftpConfig.setPort(port);
		System.out.println("更新FTP配置文件到数据库中完成。。。。。。。。。。。。。。。。。。。。。。。。。");
	}
	
	public void syscFtpConfigToZK(){
		System.out.println("更新FTP配置文件到zookeeper集群中开始。。。。。。。。。。。。。。。。。。。。。。。。。");
		ZkClient zkClient = ZKUtil.getZKClient();
		if(!zkClient.exists(ZKUtil.FTP_CONFIG_NODE_NAME)){
			zkClient.createPersistent(ZKUtil.FTP_CONFIG_NODE_NAME, true);
		}
		zkClient.writeData(ZKUtil.FTP_CONFIG_NODE_NAME, ftpConfig);
		zkClient.close();
		System.out.println("更新FTP配置文件到zookeeper集群中完成。。。。。。。。。。。。。。。。。。。。。。。。。");
	}
}
