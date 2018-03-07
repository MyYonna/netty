package roc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;

public class ClusterManagerClient {
	public static final String CONFIG_PATH="/config";
	public static final String COMMAND_PATH = "/command";
	public static final String SERVERS_PATH = "/servers";
	
	private static final int CLIENT_QTY = 5;
	
	public static void main(String[] args){
		List<WorkServer>  workServers = new ArrayList<WorkServer>();
		try{
			DBServerConfig initConfig = new DBServerConfig();
			initConfig.setDbUrl("jdbc:mysql://localhost:3306/mydb");
			initConfig.setDbPwd("123456");
			initConfig.setDbUser("root");
			
			ZkClient clientManage = ZKUtil.getZKClient();
			ManagerServer manageServer = new ManagerServer(clientManage, CONFIG_PATH, COMMAND_PATH, SERVERS_PATH, initConfig);
			manageServer.start();
			
			for(int i=0;i<CLIENT_QTY;i++){
				ServerData serverData = new ServerData();
				serverData.setAddress("192.168.1."+i);
				serverData.setId(i);
				serverData.setName("WorkServer #"+i);
				
				ZkClient zkClient = ZKUtil.getZKClient();
				WorkServer workServer = new WorkServer(initConfig, zkClient, CONFIG_PATH, SERVERS_PATH, serverData);
				workServers.add(workServer);
				workServer.start();
			}
			
			System.out.println("ÇÃ»Ø³µ¼üÍË³ö£¡\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
            manageServer.stop();
		}catch(Exception e){
			
		}finally {
            System.out.println("Shutting down...");

            for ( WorkServer workServer : workServers ) {
                try {
                    workServer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }               
            }
        }
	}
}
