package netty.NettyEcho;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

	private final String host;
	private final int port;
	
	
	
	public EchoClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EchoClient client = new EchoClient("127.0.0.1",8080);
		try {
			client.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void start() throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.remoteAddress(new InetSocketAddress(host, port))
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(new EchoClientHandler());
				}
			});
			ChannelFuture f = b.connect().sync();
			f.channel().closeFuture().sync();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			group.shutdownGracefully().sync();
		}
	}
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

}
