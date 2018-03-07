package zookeeper.DistributedLock;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import org.I0Itec.zkclient.ZkClient;

public class SimpleDistributedLockMutex extends BaseDistributedLock implements DistributedLock {

	
	private static final String LOCK_NAME = "lock-";
	
	private final String basePath;
	
	private String ourLockPath;
	
	
	public boolean internalLock(long time,TimeUnit unit) throws Exception{
		ourLockPath = attemptLock(time, unit);
		
		return ourLockPath!=null;
	}
	public SimpleDistributedLockMutex(ZkClient zkClient, String basePath) {
		super(zkClient, basePath, LOCK_NAME);
		this.basePath = basePath;
		// TODO Auto-generated constructor stub
	}

	public void acquire() throws Exception {
		// TODO Auto-generated method stub
		if(!internalLock(-1,null)){
			throw new IOException("连接丢失！在路径："+basePath+"下不能获取锁");
		}
	}

	public boolean acquire(long time, TimeUnit unit) throws Exception {
		// TODO Auto-generated method stub
		return internalLock(time,unit);
	}

	public void release() throws Exception {
		// TODO Auto-generated method stub
		releaseLock(ourLockPath);
	}

}
