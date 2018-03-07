package roc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class WorkServer {
	private RunningData serverInfo = null;
	
	private RunningData masterInfo = null;
	
	private ZkClient zkClient = null;
	
	private boolean running = false;
	
	private IZkDataListener zkDataListener = null;	
	
	private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
	WorkServer(RunningData rd){
		serverInfo = rd;
		zkDataListener = new IZkDataListener(){

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				fightForMaster();
//				if(masterInfo != null && masterInfo.getId().equals(serverInfo.getId())){
//					fightForMaster();
//				}else{
//					delayExector.schedule(new Runnable(){
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							fightForMaster();
//						}
//						
//					}, 5, TimeUnit.SECONDS);
//				}
			}
			
		};
	}
	
	public void start() throws Exception{
		if(running){
			throw new Exception("server has benn started");
		}
		zkClient = ZKUtil.getZKClient();
		running = true;
		zkClient.subscribeDataChanges(ZKUtil.Master_NODE_NAME, zkDataListener);
		fightForMaster();

	}
	
	public void stop() throws Exception{
		if(!running){
			throw new Exception("server has been stoped");
		}
		running = false;
		zkClient.unsubscribeDataChanges(ZKUtil.Master_NODE_NAME, zkDataListener);
		delayExector.shutdown();
		releaseMaster();
		zkClient.close();
	}
	
	private void fightForMaster(){
		if(!running){
			return ;
		}
		
		try{
			zkClient.create(ZKUtil.Master_NODE_NAME, serverInfo, CreateMode.EPHEMERAL);
			masterInfo = serverInfo;
		    System.out.println(serverInfo.getName()+" is master");
		    
/*		    delayExector.schedule(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(checkMaster()){
						releaseMaster();
					}
				}
		    	
		    }, 5, TimeUnit.SECONDS);*/
		}catch(Exception e){

			RunningData runningData = zkClient.readData(ZKUtil.Master_NODE_NAME);
			if(runningData  != null){
				masterInfo = runningData;
			}
		}
	}
	
	public void releaseMaster(){
		if(checkMaster()){
			zkClient.delete(ZKUtil.Master_NODE_NAME);
		}
	}
	
	public boolean checkMaster(){
		try{
			RunningData runningData = zkClient.readData(ZKUtil.Master_NODE_NAME);
			if(runningData.getId().equals(serverInfo.getId())){
				return true;
			}
			masterInfo = runningData;
			return false;
		}catch(Exception e){
			return false;
		}
	}
}
