package com.roc.netty.NettyProto;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ProtoClient {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.remoteAddress(new InetSocketAddress("127.0.0.1", 8899))
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
					.addLast(new ProtobufDecoder(MyDataInfo.Animal.getDefaultInstance()))
					.addLast(new ProtobufVarint32LengthFieldPrepender())
					.addLast(new ProtobufEncoder())
					.addLast(new ProtoClientHandler());
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

}
