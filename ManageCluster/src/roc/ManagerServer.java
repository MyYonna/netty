package roc;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ManagerServer {
	private ZkClient zkClient;
	//配置路径
	private String configPath;
	//命令路径
	private String commandPath;
	//server节点路径
	private String serverPath;
	//用于监听command路径下数据的变化
	private IZkDataListener dataListener;
	//用于监听servers路径下的子节点
	private IZkChildListener childListener;
	//工作服务器列表
	private List<String> workServers;
	
	private DBServerConfig dbServerConfig;

	
	
	public ManagerServer(){
		
	}
	
	public ManagerServer(ZkClient zkClient, String configPath, String commandPath,String serverPath,
			DBServerConfig dbServerConfig) {
		super();
		this.zkClient = zkClient;
		this.configPath = configPath;
		this.serverPath = serverPath;
		this.commandPath = commandPath;
		this.dbServerConfig = dbServerConfig;
		this.dataListener = new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				String cmd = (String)data;
				System.out.println("执行的命令："+cmd);
				execCmd(cmd);
			}
		};
		
		this.childListener = new IZkChildListener() {
			
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				// TODO Auto-generated method stub
				workServers = currentChilds;
				System.out.println("工作服务器的列表发生变化，变化后的列表为： "+workServers);
			}
		};
	}

	public void execCmd(String cmd){
		switch(cmd){
			case "create" : createConfig();break;
			case "update" : updateConfig();break;
		}
	}
	
	public void createConfig(){
		if(!zkClient.exists(configPath)){
			try{
				zkClient.createPersistent(configPath, dbServerConfig);
			}catch(Exception e){
				zkClient.writeData(configPath, dbServerConfig);
			}
		}else{
			zkClient.writeData(configPath, dbServerConfig);
		}
	}
	
	public void updateConfig(){
		dbServerConfig.setDbUser(dbServerConfig.getDbUser()+"_UPDATE");
		createConfig();
	}
	
	public void start(){
		initRunning();
	}
	
	public void stop(){
		zkClient.unsubscribeChildChanges(serverPath, childListener);
		zkClient.unsubscribeDataChanges(commandPath, dataListener);
		zkClient.close();
		System.out.println("ManagerServer has been Stoped");
	}
	public void initRunning(){
		zkClient.subscribeChildChanges(serverPath, childListener);
		zkClient.subscribeDataChanges(commandPath, dataListener);
	}
	public ZkClient getZkClient() {
		return zkClient;
	}

	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public String getCommandPath() {
		return commandPath;
	}

	public void setCommandPath(String commandPath) {
		this.commandPath = commandPath;
	}

	public IZkDataListener getDataListener() {
		return dataListener;
	}

	public void setDataListener(IZkDataListener dataListener) {
		this.dataListener = dataListener;
	}

	public IZkChildListener getChildListener() {
		return childListener;
	}

	public void setChildListener(IZkChildListener childListener) {
		this.childListener = childListener;
	}

	public List<String> getWorkServers() {
		return workServers;
	}

	public void setWorkServers(List<String> workServers) {
		this.workServers = workServers;
	}

	public DBServerConfig getDbServerConfig() {
		return dbServerConfig;
	}

	public void setDbServerConfig(DBServerConfig dbServerConfig) {
		this.dbServerConfig = dbServerConfig;
	}

	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	
	
	
}
