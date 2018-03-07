package zookeeper.DistributedLock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {
	public void acquire() throws Exception;
	
	public boolean acquire(long time,TimeUnit unit) throws Exception;
	
	public void release() throws Exception;
}
