package selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
/**
 * channel注册到selector上，会返回一个SelectionKey.一个selector会持有三种不同类型的selection key 的set集合
 * key set:它是一个包含了在这个selector上的所有注册channel过的感兴趣的set集合,
 * 			可以通过selector.keys()返回；通过 SelectableChannel.register()添加key到这个集合中。除了这种方式，不能直接怼这个set进行操作。
 * selected-key set :它是一个selection operation（即selector.select()）操作期间，每个注册的channel感兴趣的并且已经准备好出发的key的集合，
 * 			可以通过selector.selectedKeys()返回。可以通过set.remove或者iterator.remove方法进行删除。不能直接添加。
 * cancelled-key :是已经被取消的key，但是所对应的channel并没有被注销的key的集合，
 * 			这个集合除了使用key.cancel方法或者channel.close方法进行添加外不能够被直接操作，这个会导致下一次selection operation操作时，会将这个key从keyset中删除。
 * 
 * 重点：一定不要忘记flip以及清除掉已经处理的selected-key中的key
 */
public class NioChatServer {
	
	public static Map<String,SocketChannel> clientMap = new HashMap<String,SocketChannel>();
	public static void main(String[] args) throws Exception {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		//如果没有对serversocketchannel绑定一个端口地址，就调用accept方法将会抛出异常
		serverSocketChannel.bind(new InetSocketAddress(8899));
		//必须将channel设置成非阻塞
		serverSocketChannel.configureBlocking(false);
		//默认会使用SelectorProvider.openSelector来创建selector,也可以使用自定义的selector provider
		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("服务启动，监听8899端口");
		while(true){
			selector.select();
			Set<SelectionKey> keys = selector.selectedKeys();
			for(SelectionKey selectionKey:keys){
				if(selectionKey.isAcceptable()){
					System.out.println("有一个新的客户端连接上服务器。。。。。");
					//因为我们这次响应的是一个accept事件，所以对应的channel就是我们在最开始注册的ServerSocketChannel
					ServerSocketChannel serverSocketChannel2 = (ServerSocketChannel)selectionKey.channel();
					SocketChannel socketChannel = serverSocketChannel2.accept();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ);
					ByteBuffer byteb = ByteBuffer.allocate(100);
					byteb.put("你已经成功连接到了服务器。。。。".getBytes());
					byteb.flip();
					socketChannel.write(byteb);
					clientMap.put("["+UUID.randomUUID().toString()+"#client]", socketChannel);
				}else if(selectionKey.isReadable()){
					//因为我们这次响应的是一个read事件，所以对应的channel就是我们注册的SocketChannel
					SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
					ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
					String message = "";
					try{
						socketChannel.read(byteBuffer);
						//对buffer中的数据进行读取一定要记得进行flip,解码也是一个道理，也需要从中读取数据
						byteBuffer.flip();
						String sendClientId = null;
						for(Entry<String, SocketChannel> entry:clientMap.entrySet()){
							if(entry.getValue() == socketChannel){
								sendClientId = entry.getKey();
							}
						}
						//利用charset对字节码进行解码成utf-8的字符
						Charset charset = Charset.forName("utf-8");
					    message = sendClientId+":"+String.valueOf(charset.decode(byteBuffer).array());
						
					}catch(IOException e){
						//先将此客户单从客户端列表中删除，然后将这个消息发送给其他客户端
						String clientId = "";
						for(Entry<String, SocketChannel> entry:clientMap.entrySet()){
							if(entry.getValue() == socketChannel){
								message = "客户端"+entry.getKey()+"断开连接了。。。。。";
								clientId = entry.getKey();
							}
						}
						clientMap.remove(clientId);
						socketChannel.close();
					}finally{
						System.out.println(message);
						//重新对byteBuffer进行清空
						byteBuffer.clear();
						byteBuffer.put(message.getBytes());
						for(Entry<String, SocketChannel> entry:clientMap.entrySet()){
							SocketChannel channel = entry.getValue();
							byteBuffer.flip();
							channel.write(byteBuffer);
						}
					}

				}
			}
			//循环完此次的keys之后，一定要清除selected-key中的key,否则会出异常
			keys.clear();
		}
		
	}
	

}
