package com.roc.netty.NettyChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class ChatServer {
	
	private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	private final EventLoopGroup group = new NioEventLoopGroup();
	private Channel channel;
	private SslContext context;
	
	public ChatServer(SslContext context) {
		this.context = context;
	}

	public ChannelFuture start(SocketAddress address){
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(group)
		.channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				// TODO Auto-generated method stub
//				SslContext context = SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();
				SSLEngine engine = context.newEngine(ch.alloc());
				engine.setUseClientMode(false);
				ch.pipeline()
				.addLast(new HttpServerCodec())
				.addLast(new HttpObjectAggregator(64*1024))
				.addLast(new ChunkedWriteHandler())
				.addLast(new HttpRequestHandler("/ws"))
				.addLast(new WebSocketServerProtocolHandler("/ws"))
				.addLast(new TextWebSocketFrameHandler(channelGroup))
				.addFirst(new SslHandler(engine));
			}
		});
		ChannelFuture channelFuture = bootstrap.bind(address);
		channelFuture.syncUninterruptibly();
		channel = channelFuture.channel();
		return channelFuture;
	}

	public void destroy(){
		if(channel != null){
			channel.close();
		}
		
		channelGroup.close();
		group.shutdownGracefully();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        SslContext context = null;
        SelfSignedCertificate cert = null;
		try {
			try {
				cert = new SelfSignedCertificate();
				context = SslContext.newServerContext(cert.certificate(), cert.privateKey());
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SSLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final ChatServer endpoint = new ChatServer(context);
		endpoint.start(new InetSocketAddress(8888));
		System.out.println("服务启动成功。。。");
		try {
			new BufferedReader(new InputStreamReader(System.in)).readLine();
			endpoint.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
