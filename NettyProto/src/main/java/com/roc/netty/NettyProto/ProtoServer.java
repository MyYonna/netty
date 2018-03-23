package com.roc.netty.NettyProto;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LoggingHandler;
/**
 * netty使用protobuf，需要事先准备好protobuf的编译器以及protobuf的java库（生成的类文件依赖于这个库）。然后编写idl文件，利用编译器生成指定语言的代码，
 * netty中利用protobuf进行序列化和反序列化因为只能指定一种消息类型，所以需要在消息中指定一个枚举，然后利用oneof关键字，利用硬编码的方式将枚举值与特定的消息类型进行对应
 * @author Administrator
 *
 */
public class ProtoServer {

	public static void main(String[] args) throws Exception {
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
					pipeline.addLast(new ProtobufVarint32FrameDecoder())
					.addLast(new ProtobufDecoder(MyDataInfo.Animal.getDefaultInstance()))
					.addLast(new ProtobufVarint32LengthFieldPrepender())
					.addLast(new ProtobufEncoder())
					.addLast(new ProtoServerHandler());
				}
			});
			ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(8899)).sync();
			future.channel().closeFuture().sync();
		}finally{
			parent.shutdownGracefully();
			child.shutdownGracefully();
		}
	}

}
