package zookeeper.DistributedLock;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

public class BaseDistributedLock {
	private final ZkClient zkClient;
	private final String path;
	private final String basePath;
	private final String lockName;
	private static final Integer MAX_RETRY_COUNT = 10;
	
	
	public BaseDistributedLock(ZkClient zkClient, String basePath, String lockName) {
		super();
		this.zkClient = zkClient;
		this.basePath = basePath;
		this.path = basePath.concat("/").concat(lockName);
		this.lockName = lockName;
	}
	
	private void deleteOurPath(String ourPath) throws Exception{
		zkClient.delete(ourPath);
	}
	
	private String createLockNode(ZkClient zkClient,String path) throws Exception{
		try{
			return zkClient.createEphemeralSequential(path, null);
		}catch(Exception e ){
			zkClient.createPersistent(basePath, true);
			return zkClient.createEphemeralSequential(path, null);
		}
	}
	/**
	 * 
	 * @param startMillis 开始时间
	 * @param millisToWait 等待时长
	 * @param ourPath 创建的临时节点路径
	 * @return
	 * @throws Exception
	 */
	private boolean waitToLock(long startMillis,Long millisToWait,String ourPath) throws Exception{
		boolean haveTheLock = false;
		boolean doDelete = false;
		try{
			while(!haveTheLock){
				//按照Lock下临时节点的序号进行排序
				List<String> children = getSortedChildren();
				//截取父节点下的子节点名，剔除掉了/
				String sequenceNodeName = ourPath.substring(basePath.length()+1);
				//获得这个节点在排序后的临时节点列表中的位置
				int ourIndex = children.indexOf(sequenceNodeName);
				//如果不存在，则抛出异常，可能因为网络的关系没有创建成功
				if(ourIndex<0){
					throw new ZkNoNodeException("节点没有找到："+sequenceNodeName);
				}
				//判断是否为第一位，如果是，则认为已经获得锁了
				boolean isGetTheLock = ourIndex == 0;
				//如果没有获取锁，则监听上一个节点的状态变化（主要是删除）
				String pathToWatch = isGetTheLock?null:children.get(ourIndex-1);
				if(isGetTheLock){
					haveTheLock = true;
				}else{
					//上一个节点的完整路径
					String previousSequencePath = basePath.concat("/").concat(pathToWatch);
					//添加一个多线程计数器，用于阻塞线程
					final CountDownLatch latch = new CountDownLatch(1);
					//状态监听器
					final IZkDataListener previousListener = new IZkDataListener() {
						//监听的节点一旦被删除，计数器停止
						public void handleDataDeleted(String dataPath) throws Exception {
							// TODO Auto-generated method stub
							latch.countDown();
						}
						
						public void handleDataChange(String dataPath, Object data) throws Exception {
							// TODO Auto-generated method stub
							
						}
					};
					try{
						//订阅上一个节点的状态
						zkClient.subscribeDataChanges(previousSequencePath, previousListener);
						if(millisToWait != null){
							millisToWait -=(System.currentTimeMillis()-startMillis);
							startMillis = System.currentTimeMillis();
							//如果超过等待时间
							if(millisToWait<=0){
								doDelete = true;
								break;
							}
							//阻塞线程，当超时或者调用了cuntDown后才接着往下执行
							latch.await(millisToWait,TimeUnit.MICROSECONDS);
						}else{
							//阻塞线程
							latch.await();
						}
					}catch(Exception e){
						
					}finally{
						//超时或者上个节点被删除后，取消对它的监听
						zkClient.unsubscribeDataChanges(previousSequencePath, previousListener);
					}
				}
			}
		}catch(Exception e){
			doDelete = true;
			throw e;
		}finally{
			if(doDelete){
				//如果发生异常，则删除本临时节点
				deleteOurPath(ourPath);
			}
		}
		return haveTheLock;
	}
	
	List<String> getSortedChildren() throws Exception{
		try{
			List<String> child = zkClient.getChildren(basePath);
			Collections.sort(child, new Comparator<String>() {

				public int compare(String o1, String o2) {
					// TODO Auto-generated method stub
					return getLockNodeNumber(o1, lockName).compareTo(getLockNodeNumber(o2, lockName));
				}
			});
			return child;
		}catch(Exception e){
			zkClient.createPersistent(basePath, true);
			return getSortedChildren();
		}
	}
	
	private String getLockNodeNumber(String str,String lockName){
		int index = str.lastIndexOf(lockName);
		if(index>=0){
			index += lockName.length();
			return index<=str.length()?str.substring(index):"";
		}
		return str;
	}
	
	protected void releaseLock(String lockPath) throws Exception{
		zkClient.delete(lockPath);
	}
	
	protected String attemptLock(long time,TimeUnit unit) throws Exception{
		final long startMillis = System.currentTimeMillis();
		final Long millisToWait = (unit != null)?unit.toMillis(time):null;
		
		String ourPath = null;
		boolean hasTheLock = false;
		boolean isDone = false;
		int retryCount = 0;
		
		while(!isDone){
			try{
				ourPath = createLockNode(zkClient, path);
				hasTheLock = waitToLock(startMillis, millisToWait, ourPath);
			}catch(Exception e){
				if(retryCount++ < MAX_RETRY_COUNT){
					isDone = false;
				}else{
					throw e;
				}
			}
			if(hasTheLock){
				return ourPath;
			}
		}
		return null;
	}
} 
