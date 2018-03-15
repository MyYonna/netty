package com.roc.netty.NettyUDP;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
/**
 * 解码报文（解码器在收到报文后，ctx会调用write方法，里面会调用抽象方法encode实现消息编码）
 * @author Administrator
 *
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket > {

	@Override
	protected void decode(ChannelHandlerContext ctx, DatagramPacket  datagramPacket , List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf data = datagramPacket.content();
		int i = data.indexOf(0, data.readableBytes(), LogEvent.Separator);
		String filename = data.slice(0, i).toString(CharsetUtil.UTF_8);
		String  logMsg = data.slice(i+1, data.readableBytes()).toString(CharsetUtil.UTF_8);
		LogEvent event  = new LogEvent(datagramPacket.recipient(),filename, logMsg,System.currentTimeMillis());
		out.add(event);
		
	}

}
