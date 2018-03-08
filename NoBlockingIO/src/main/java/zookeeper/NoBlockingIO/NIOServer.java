package zookeeper.NoBlockingIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
/**
 * 为何回事非阻塞呢，因为相对于阻塞io而言，阻塞IO在对sokct进行输入和读写时，整个线程是阻塞的，（可以通过多线程解决，但是连接太多会产生大量多线程，会造成资源的大量浪费），而非阻塞IO的重点在于当内核数据准备完毕之后才会通知channel进行数据读取，并不会需要等待内核将数据准备完毕。
 * 当内核将数据准备完毕后，会在channel的监听器selector中添加事件，而selector存在事件之后，会结束阻塞状态，对应事件的channel则可以读取或者写入数据了。
 * @author Administrator
 *
 */
public class NIOServer {
	
	Selector selector;//Channel监视器，用于监听一个或者多个channel上的事件，channel必须事先注册在selector上
	
	public void initServer(int port){
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(port));
			serverSocketChannel.configureBlocking(false);
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void listen(){
		while(true){
			try {
				selector.select();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Set<SelectionKey> selectKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectKeys.iterator();
			if(iterator.hasNext()){
				SelectionKey key = iterator.next();
				iterator.remove();
				if(key.isAcceptable()){
					ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
					try {
						SocketChannel socketChannel = serverSocketChannel.accept();
						socketChannel.configureBlocking(false);
						
						socketChannel.write(ByteBuffer.wrap(new String("服务端响客户端发送数据。。。。").getBytes()));
						
						
						socketChannel.register(selector, SelectionKey.OP_READ);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(key.isReadable()){
					read(key);
				}
			}
		}
	}
	
	public void read(SelectionKey key){
		SocketChannel socketChannel =	(SocketChannel)key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			socketChannel.read(buffer);
			byte[] data = buffer.array();
			String msg = new String(data);
			System.out.println("服务端收到消息："+msg);
			String response = "你好，你的请求成功了";
			socketChannel.write(ByteBuffer.wrap(response.getBytes()));//返回响应给客户端
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void stop(){
		try {
			selector.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
