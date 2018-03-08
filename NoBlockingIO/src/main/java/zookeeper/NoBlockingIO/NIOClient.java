package zookeeper.NoBlockingIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {
	Selector selector;
	
	public void initClient(String ip,int port){
		SocketChannel socketChannel = null;
		try {
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(ip, port));//有可能只是发出连接请求，但没有等待连接成功
			
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void listen(String msg){
		while(true){
			try {
				selector.select();
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while(keys.hasNext()){
					SelectionKey key = keys.next();
					keys.remove();
					if(key.isConnectable()){
						SocketChannel socketChannel = (SocketChannel)key.channel();
						if(socketChannel.isConnectionPending()){
							socketChannel.finishConnect();
						}
						if(!socketChannel.isConnected()){
							socketChannel.finishConnect();
						}
						socketChannel.configureBlocking(false);
						socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
						socketChannel.register(selector, SelectionKey.OP_READ);
					}else if(key.isReadable()){
						SocketChannel socketChannel = (SocketChannel)key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						socketChannel.read(buffer);
						String response = new String(buffer.array());
						System.out.println("客户端收到消息："+response);
						

/*				      ByteBuffer outBuffer = ByteBuffer.wrap("客户端又发送了数据".getBytes());

				      socketChannel.write(outBuffer);// 将消息回送给服务器端
*/					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
