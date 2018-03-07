package zookeeper.DistributedLock;

import org.I0Itec.zkclient.ZkClient;

public class TestDistributedLock {

	//主题思路就是在创建临时节点之后立马去获取锁，如果没有获取到则监听上一个节点信息，并阻塞线程，如果上一个节点被删除，则取消对上一个节点的监听，取消阻塞，进入下一轮锁的获取，依次循环
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final ZkClient zkClient1 = ZKUtil.getZKClient();
		final SimpleDistributedLockMutex mutex1 = new SimpleDistributedLockMutex(zkClient1, ZKUtil.SERVERS_PATH);
		
		final ZkClient zkClient2 = ZKUtil.getZKClient();
		final SimpleDistributedLockMutex mutex2 = new SimpleDistributedLockMutex(zkClient2, ZKUtil.SERVERS_PATH);
		
		try{
			mutex1.acquire();
			System.out.println("Client1 locked.....");
			
			Thread client2Thd = new Thread(new Runnable(){

				public void run() {
					// TODO Auto-generated method stub
					try {
						mutex2.acquire();
						System.out.println("Client2 locked....");
						mutex2.release();
						System.out.println("Client2 released lock");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			});
			client2Thd.start();
			
			
			Thread.sleep(5000);
			mutex1.release();
			System.out.println("Client1 released lock....");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
