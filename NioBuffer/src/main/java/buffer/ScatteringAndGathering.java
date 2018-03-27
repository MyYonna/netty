package buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**Scattering Gathering
 * 这是channel操作buffer的两个特性，发散和聚合
 * 发散是指从channel中读取数据的时候可以传入buffer的数组，并依次将buffer数组写满，直到channel中无数据
 * 聚合是指往channel中写入数据时可以传入buffer数据，并依次将buffer数据中的数据读入channel中，直到无数据
 * @author Administrator
 *
 */
public class ScatteringAndGathering {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			InetSocketAddress address = new InetSocketAddress(8899);
			serverSocketChannel.bind(address);
			
			SocketChannel socketChannel = serverSocketChannel.accept();
			ByteBuffer[] byteBuffers = new ByteBuffer[3];
			byteBuffers[0] = ByteBuffer.allocate(2);
			byteBuffers[1] = ByteBuffer.allocate(3);
			byteBuffers[2] = ByteBuffer.allocate(4);
			long capacity = byteBuffers[0].capacity()+byteBuffers[1].capacity()+byteBuffers[2].capacity();
			System.out.println(capacity);
			//这样写主要是为了保持连接不断开，并且能一直读取channel的数据
			while(true){
				long i = 0;
				for(int j=0;j<byteBuffers.length;j++){
					byteBuffers[j].clear();
				}
				while(i<capacity){
					i += socketChannel.read(byteBuffers);
					System.out.println(i);
				}
				for(int j=0;j<byteBuffers.length;j++){
					byteBuffers[j].flip();
				}
				System.out.println("开始写入");
				socketChannel.write(byteBuffers);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
