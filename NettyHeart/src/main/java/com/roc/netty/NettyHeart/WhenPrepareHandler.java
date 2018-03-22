package com.roc.netty.NettyHeart;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class WhenPrepareHandler extends ChannelInboundHandlerAdapter {

	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler handlerAdded");
		super.handlerAdded(ctx);
	}

	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler handlerRemoved");
		super.handlerRemoved(ctx);
	}

	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler channelRegistered");
		super.channelRegistered(ctx);
	}

	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler channelUnregistered");
		super.channelUnregistered(ctx);
	}

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler channelActive");
		super.channelActive(ctx);
	}

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler channelInactive");
		super.channelInactive(ctx);
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler channelRead");
		super.channelRead(ctx,msg);
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler channelReadComplete");
		super.channelReadComplete(ctx);
	}

	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler userEventTriggered");
		super.userEventTriggered(ctx,evt);
	}

	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler channelWritabilityChanged");
		super.channelWritabilityChanged(ctx);
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handler exceptionCaught");
		super.exceptionCaught(ctx,cause);
	}

}
