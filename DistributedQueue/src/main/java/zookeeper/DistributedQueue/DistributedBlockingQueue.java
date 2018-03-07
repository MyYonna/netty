package zookeeper.DistributedQueue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

public class DistributedBlockingQueue<T> extends DistributedSimpleQueue<T> {

	public DistributedBlockingQueue(ZkClient zkClient, String root) {
		super(zkClient, root);
	}

	@Override
	public T poll() throws Exception{
		while(true){
			final CountDownLatch latch = new CountDownLatch(1);
				
			IZkChildListener iZkChildListener = new IZkChildListener() {
				
				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
					latch.countDown();
				}
			};
			zkClient.subscribeChildChanges(root, iZkChildListener);
			
			try{
				T node = super.poll();
				if(node == null){
					System.out.println("正在等待数据到来");
					latch.await();
				}else{
					return node;
				}
			}catch(Exception e ){
				
			}finally{
				zkClient.unsubscribeChildChanges(root, iZkChildListener);
			}
			
			
		}
	}
}
