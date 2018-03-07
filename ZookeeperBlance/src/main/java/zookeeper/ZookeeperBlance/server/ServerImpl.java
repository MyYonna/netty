package zookeeper.ZookeeperBlance.server;

import org.I0Itec.zkclient.ZkClient;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerImpl implements Server {

	private ZkClient zkClient;
	private ServerData serverData;
	private String serversPath;
	private String serverPath;
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workGroup = new NioEventLoopGroup();
	private ServerBootstrap bootStrap = new ServerBootstrap();
	private ChannelFuture cf;
	private boolean binded = false;
    private final RegistProvider registProvider;
	public void bind() {
		// TODO Auto-generated method stub
		if(binded){
			return;
		}
		System.out.println(serverData.getPort()+":binding.....");
		
		try{
			initRunning();
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
		
		bootStrap.group(bossGroup, workGroup)
				 .channel(NioServerSocketChannel.class)
				 .option(ChannelOption.SO_BACKLOG, 1024)
				 .childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// TODO Auto-generated method stub
						ChannelPipeline p = ch.pipeline();
						p.addLast(new ServerHandler(new DefaultBalanceUpdateProvider(serverPath,zkClient)));
					}
				});
		
		try{
			cf = bootStrap.bind(serverData.getPort()).sync();
			binded = true;
			System.out.println(serverData.getPort()+":binded...");
			cf.channel().closeFuture().sync();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	
	public ServerImpl(ZkClient zkClient, ServerData serverData, String serversPath) {
		super();
		this.zkClient = zkClient;
		this.serverData = serverData;
		this.serversPath = serversPath;
		this.registProvider = new DefaultRegistProvider();
	}


	public void initRunning(){
		String mePath = serversPath.concat("/").concat(serverData.getHost()).concat(":").concat(serverData.getPort().toString());
		registProvider.regist(new ZooKeeperRegistContext(mePath, zkClient, serverData));
		serverPath = mePath;
	}
	public ZkClient getZkClient() {
		return zkClient;
	}
	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}
	public ServerData getServerData() {
		return serverData;
	}
	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}
	public String getServersPath() {
		return serversPath;
	}
	public void setServersPath(String serversPath) {
		this.serversPath = serversPath;
	}
	public String getServerPath() {
		return serverPath;
	}
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	public EventLoopGroup getBossGroup() {
		return bossGroup;
	}
	public void setBossGroup(EventLoopGroup bossGroup) {
		this.bossGroup = bossGroup;
	}
	public EventLoopGroup getWorkGroup() {
		return workGroup;
	}
	public void setWorkGroup(EventLoopGroup workGroup) {
		this.workGroup = workGroup;
	}
	public ServerBootstrap getBootStrap() {
		return bootStrap;
	}
	public void setBootStrap(ServerBootstrap bootStrap) {
		this.bootStrap = bootStrap;
	}
	public ChannelFuture getCf() {
		return cf;
	}
	public void setCf(ChannelFuture cf) {
		this.cf = cf;
	}
	public boolean isBinded() {
		return binded;
	}
	public void setBinded(boolean binded) {
		this.binded = binded;
	}

}
