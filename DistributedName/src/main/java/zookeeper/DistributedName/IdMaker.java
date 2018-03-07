package zookeeper.DistributedName;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

public class IdMaker {
	private ZkClient client = null;
	private final String  server;
	private final String root;
	private final String nodeName;
	
	private volatile boolean running = false;
	
	private ExecutorService cleanExecutor = null;
	
	public enum RemoveMethod{
		NONE,IMMEDIATELY,DELAY
	}

	public IdMaker(String server, String root, String nodeName) {
		super();
		this.server = server;
		this.root = root;
		this.nodeName = nodeName;
	}
	
	
	public void start() throws Exception{
		if(running){
			throw new Exception("server has started...");
		}
		running = true;
		init();
	}
	
	public void stop() throws Exception{
		if(!running){
			throw new Exception("server has stoped...");
		}
		running = false;
		freeResource();
	}
	private void  init(){
		client = new ZkClient(server, 5000,5000, new BytesPushThroughSerializer());
		cleanExecutor = Executors.newFixedThreadPool(10);
		try{
			client.createPersistent(root,true);
		}catch(Exception e){
			
		}
	}
	
	private void freeResource(){
		cleanExecutor.shutdown();
		
		try{
			cleanExecutor.awaitTermination(2, TimeUnit.SECONDS);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cleanExecutor = null;
		}
		if(client != null){
			client.close();
			client = null;
		}
	}
	
	public void checkRunning() throws Exception{
		if(!running){
			throw new Exception("请先调用start");
		}
	}
	
	public synchronized  String generateId(RemoveMethod removeMethod) throws Exception{
		checkRunning();
		
		final String fullNodePath = root.concat("/").concat(nodeName);
		final String ourPath = client.createEphemeralSequential(fullNodePath, null);
		if(removeMethod.equals(RemoveMethod.IMMEDIATELY)){
			client.delete(ourPath);
		}else if(removeMethod.equals(RemoveMethod.DELAY)){
			cleanExecutor.execute(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					client.delete(ourPath);
				}
			});
		}
		
		return extractId(ourPath);
	}
	
	public String extractId(String path){
		int index = path.lastIndexOf(nodeName);
		if(index >= 0){
			index += nodeName.length();
			return index<=path.length()? path.substring(index):"";
		}
		return path;
	}
}
