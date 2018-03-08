package zookeeper.NoBlockingIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

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
