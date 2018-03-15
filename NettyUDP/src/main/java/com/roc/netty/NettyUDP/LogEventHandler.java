package com.roc.netty.NettyUDP;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder(); //3
        builder.append(event.getReceivedTime());
        builder.append(" [");
        builder.append(event.getSource().toString());
        builder.append("] [");
        builder.append(event.getLogfile());
        builder.append("] : ");
        builder.append(event.getMsg());

        System.out.println(builder.toString()); //4
	}

}
