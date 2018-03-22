package com.roc.netty.NettyHeart;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

public class HeartServer {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		EventLoopGroup parent = new NioEventLoopGroup();
		EventLoopGroup child = new NioEventLoopGroup();
		try{
			serverBootstrap.group(parent, child).channel(NioServerSocketChannel.class).handler(new LoggingHandler()).childHandler(new ChannelInitializer<SocketChannel>() {
				
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ChannelPipeline pipeline = ch.pipeline();
					System.out.println("通道建立成功");
					pipeline.addLast(new IdleStateHandler(80, 5, 5,TimeUnit.SECONDS))
					.addLast(new WhenPrepareHandler())
					.addLast(new HeartHandler());
				}
			});
			ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(8899)).sync();
			new BufferedReader(new InputStreamReader(System.in, CharsetUtil.UTF_8)).readLine();
			future.channel().closeFuture().sync();
		}finally{
			parent.shutdownGracefully();
			child.shutdownGracefully();
		}
	}

}
