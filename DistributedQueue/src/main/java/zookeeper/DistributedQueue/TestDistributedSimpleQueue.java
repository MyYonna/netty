package zookeeper.DistributedQueue;

import org.I0Itec.zkclient.ZkClient;

public class TestDistributedSimpleQueue {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ZkClient zkClient = ZKUtil.getZKClient();
		
		final DistributedSimpleQueue<User> queue = new DistributedSimpleQueue<User>(zkClient, ZKUtil.QUEUE_PATH);
		
		User user1 = new User();
		user1.setId("1");
		user1.setName("roc");
		
		User user2 = new User();
		user2.setId("2");
		user2.setName("daisy");
		
		try{
			queue.offer(user1);
			queue.offer(user2);
			
			Thread thread1 = new Thread(new Runnable(){

				public void run() {
					// TODO Auto-generated method stub
					try {
						User u1 = queue.poll();
						System.out.println(u1.getName());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			});
			
			Thread thread2 = new Thread(new Runnable(){

				public void run() {
					// TODO Auto-generated method stub
					try {
						User u2 = queue.poll();
						System.out.println(u2.getName());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			});
			thread1.start();
			thread2.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
