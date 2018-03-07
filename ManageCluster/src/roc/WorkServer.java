package roc;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class WorkServer {

	private DBServerConfig dbServerConfig;
	private IZkDataListener dataListener;
	private ZkClient zkClient;
	private String configPath;
	//在servers子路径下注册临时节点，所以创建之前serverPath必须被创建
	private String serversPath;
	private ServerData serverData;
	
	public WorkServer(){
		
	}
	public WorkServer(DBServerConfig dbServerConfig,ZkClient zkClient, String configPath,
			String serversPath, ServerData serverData) {
		super();
		this.dbServerConfig = dbServerConfig;
		this.zkClient = zkClient;
		this.configPath = configPath;
		this.serversPath = serversPath;
		this.serverData = serverData;
		//用于监听configPath上的路径变化
		this.dataListener = new IZkDataListener(){

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				DBServerConfig dbServerConfig = (DBServerConfig)data;
				updateConfig(dbServerConfig);
				System.out.println("new Work server config is:"+dbServerConfig.toString());
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
	
	public void updateConfig(DBServerConfig dbServerConfig){
		this.dbServerConfig = dbServerConfig;
		System.out.println("server config update success");
	}
	
	public void start(){
		System.out.println("work server start .......");
		initRunning();
	}
	
	public void stop(){
		System.out.println("work server stopeed .......");
		zkClient.unsubscribeDataChanges(configPath, dataListener);
	}
	public void initRunning(){
		registMe();
		zkClient.subscribeDataChanges(configPath, dataListener);
	}
	
	public void registMe(){
		String mePath = serversPath.concat("/").concat(serverData.getAddress());
		try{
			zkClient.createEphemeral(mePath, serverData);
		}catch(Exception e){
			zkClient.createPersistent(serversPath,true);
			registMe();
		}
	}
	public DBServerConfig getDbServerConfig() {
		return dbServerConfig;
	}
	public void setDbServerConfig(DBServerConfig dbServerConfig) {
		this.dbServerConfig = dbServerConfig;
	}
	public IZkDataListener getDataListener() {
		return dataListener;
	}
	public void setDataListener(IZkDataListener dataListener) {
		this.dataListener = dataListener;
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
	public String getServersPath() {
		return serversPath;
	}
	public void setServersPath(String serversPath) {
		this.serversPath = serversPath;
	}
	public ServerData getServerData() {
		return serverData;
	}
	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}
	
	
}
