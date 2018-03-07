package zookeeper.DistributedQueue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.I0Itec.zkclient.ExceptionUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

public class DistributedSimpleQueue<T> {
	protected final ZkClient zkClient;
	protected final String root;
	protected final String Node_Name = "n_";
	public DistributedSimpleQueue(ZkClient zkClient, String root) {
		super();
		this.zkClient = zkClient;
		this.root = root;
	}
	
	public int size(){
		return zkClient.countChildren(root);
	}
	
	
	public boolean isEmpty(){
		return size() == 0;
	}
	
	
	public boolean offer(T element)  throws Exception{
		String nodeFullPath = root.concat("/").concat(Node_Name);
		try{
			zkClient.createEphemeralSequential(nodeFullPath, element);
		}catch(ZkNoNodeException  e){
			zkClient.createPersistent(root,true);
			offer(element);
		}catch(Exception e){
			throw ExceptionUtil.convertToRuntimeException(e);
		}
		return true;
	}
	
	public T poll() throws Exception{
		try{
			List<String> children = zkClient.getChildren(root);
			if(children.size() == 0){
				return null;
			}
			Collections.sort(children, new Comparator<String>() {

				public int compare(String o1, String o2) {
					// TODO Auto-generated method stub
					return getSortedNumber(o1,Node_Name).compareTo(getSortedNumber(o2, Node_Name));
				}
			});
			
			for(String nodeName:children){
				String nodeFullPath = root.concat("/").concat(nodeName);
				try{
					T node = (T)zkClient.readData(nodeFullPath);
					Thread.sleep(1000);
					zkClient.delete(nodeFullPath);
					return node;
				}catch(ZkNoNodeException e){
					e.printStackTrace();
				}
			}
			return null;
		}catch(Exception e){
			throw ExceptionUtil.convertToRuntimeException(e);
		}
	}
	
	public String getSortedNumber(String str,String nodeName){
		int index = str.lastIndexOf(nodeName);
		if(index>=0){
			index += nodeName.length();
			return index <= str.length()?str.substring(index):"";
		}
		return str;
	}
}
