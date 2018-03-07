package zookeeper.ZookeeperBlance.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import zookeeper.ZookeeperBlance.server.ServerData;

public class ClientImpl implements Client {

	private final BalanceProvider<ServerData> provider;
	private EventLoopGroup group = null;
	private Channel channel = null;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	public ClientImpl(BalanceProvider<ServerData> provider) {
		super();
		this.provider = provider;
	}

	public void connect() throws Exception {
		// TODO Auto-generated method stub
		ServerData serverData = provider.getBalanceItem();
		System.out.println("Connecting to " + serverData.getHost()+":"+serverData.getPort()+" ,it's balance:"+serverData.getBalance());

		group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group)
		.channel(NioSocketChannel.class)
		.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				ChannelPipeline p = ch.pipeline();
				p.addLast(new ClientHandler());
				System.out.println("started success");
			}
			
		});
		
		ChannelFuture f = b.connect(serverData.getHost(), serverData.getPort()).syncUninterruptibly();
		channel = f.channel();
	}

	public void disConnect() throws Exception {
		// TODO Auto-generated method stub
		try{
			if(channel!= null){
				channel.close().syncUninterruptibly();
			}
			group.shutdownGracefully();
			group = null;
			log.debug("disconnected...................");
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}

	public BalanceProvider<ServerData> getProvider() {
		return provider;
	}

}
