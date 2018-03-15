package com.roc.netty.NettyUDP;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
/**
 * 用于在某一个端口监听UDP服务器发过来的报文，解码并打印
 * @author Administrator
 *
 */
public class LogEventMinitor {
	private final Bootstrap bootstrap;
	private final EventLoopGroup group;
	
	public LogEventMinitor(InetSocketAddress address){
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(group)
		.channel(NioDatagramChannel.class)
		.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				// TODO Auto-generated method stub
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new LogEventDecoder())
				.addLast(new LogEventHandler());
			}
		}).option(ChannelOption.SO_BROADCAST, true).localAddress(address);
	}
	
	public Channel bind(){
		return bootstrap.bind().syncUninterruptibly().channel();
	}
	
	public void stop(){
		group.shutdownGracefully();
	}
	public static void main(String[] args) {
		LogEventMinitor logEventMinitor = new LogEventMinitor(new InetSocketAddress(8888));
		try{
			Channel channel = logEventMinitor.bind();
			System.out.println("LogEventMonitor running...");
			channel.closeFuture().await();
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			logEventMinitor.stop();
		}
	}
	
}
