package zookeeper.DistributedQueue;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.ZkClient;

public class TestDistributedBlockingQueue {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ZkClient zkClient = ZKUtil.getZKClient();
		
		final DistributedBlockingQueue<User> queue1 = new  DistributedBlockingQueue<User>(zkClient, ZKUtil.QUEUE_PATH);
		
		Thread thread = new Thread(new Runnable(){

			public void run() {
				User user1 = new User();
				user1.setId("1");
				user1.setName("roc");
				
				try {
					queue1.offer(user1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		thread.start();
		
		
		final CountDownLatch latch2 = new CountDownLatch(1);
		Thread thread2 = new Thread(new Runnable(){

			public void run() {
				try {
					latch2.await();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				User user2 = new User();
				user2.setId("2");
				user2.setName("daisy");
				try {
					queue1.offer(user2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		thread2.start();
		try {
			User user = queue1.poll();
			System.out.println(user.getName());
			latch2.countDown();
			User user2 = queue1.poll();
			System.out.println(user2.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
