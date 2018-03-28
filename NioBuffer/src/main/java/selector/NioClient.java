package selector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 与服务端的编写大体类似，只是我们不再是用ServerSocektChannel,而是使用ScoketChannel来与服务器建立连接通道
 * @author Administrator
 *
 */
public class NioClient {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));
		Selector selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		
		while(true){
			int i = selector.select();
			if(i>0){
				Set<SelectionKey> keys = selector.selectedKeys();
				
				for(SelectionKey key:keys){
					if(key.isConnectable()){
						final SocketChannel channel = (SocketChannel)key.channel();
						if(channel.isConnectionPending()){
							channel.finishConnect();
						}
						final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
						//通过一个线程持续监听键盘的输入，并将信息发送给服务端
						ExecutorService executors = Executors.newSingleThreadExecutor();
						executors.submit(new Runnable() {
							@Override
							public void run() {
								try {
									System.out.println("开始监听键盘事件。。。。。");
									BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
									while(true){
										byteBuffer.clear();
										String message = reader.readLine();
										byteBuffer.put(message.getBytes());
										byteBuffer.flip();
										channel.write(byteBuffer);
									}
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});
						channel.configureBlocking(false);
						channel.register(selector, SelectionKey.OP_READ);
					}else if(key.isReadable()){
						SocketChannel channel = (SocketChannel)key.channel();
						ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
						channel.read(byteBuffer);
						byteBuffer.flip();
						//利用charset工具对字节进行解码成UTF-8的字符
						Charset charset = Charset.forName("UTF-8");
						CharsetDecoder charsetDecoder = charset.newDecoder();
						CharBuffer charBuffer = charsetDecoder.decode(byteBuffer);
						System.out.println(new String(charBuffer.toString()));
					}
					keys.remove(key);
				}
			}
			
		}
	}

}
