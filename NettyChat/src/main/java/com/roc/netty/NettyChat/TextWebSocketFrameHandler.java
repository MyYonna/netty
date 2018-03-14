package com.roc.netty.NettyChat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private final ChannelGroup group;
	
	
	public TextWebSocketFrameHandler(ChannelGroup group) {
		super();
		this.group = group;
	}

    @SuppressWarnings("deprecation")
	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
        	ctx.pipeline().remove(HttpRequestHandler.class);
        	group.writeAndFlush(new TextWebSocketFrame("Client "+ctx.channel()+" joined"));
        	group.add(ctx.channel());
        }else{
        	ctx.fireUserEventTriggered(evt);
        }
    }
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		// TODO Auto-generated method stub
		group.writeAndFlush(msg.retain());
	}

}
