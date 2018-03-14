package netty.NettyEcho;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	private final int port;
	
	public EchoServer(int port){
		this.port = port;
	}
	
	public static void main(String[] args){
	       
	       try {
			new EchoServer(8080).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	}
	
	public void start() throws Exception{
		NioEventLoopGroup group = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(group)
			.channel(NioServerSocketChannel.class)
			.localAddress(new InetSocketAddress(port))
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(new EchoServerHandler());
				}
			});
			ChannelFuture f = b.bind().sync();
			System.out.println(EchoServer.class.getName()+"started and listen on "+f.channel().localAddress());
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully().sync();
		}
	}
}
