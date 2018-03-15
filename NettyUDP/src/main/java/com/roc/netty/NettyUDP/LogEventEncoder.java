package com.roc.netty.NettyUDP;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
/**
 * 解码报文（解码器在收到报文后，ctx会调用write方法，里面会调用抽象方法encode实现消息解码）
 * @author Administrator
 *
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

   private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {  //1
        this.remoteAddress = remoteAddress;
    }
	@Override
	protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
		byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
		ByteBuf buf = ctx.alloc().buffer(file.length+msg.length+1);
		buf.writeBytes(file);
		buf.writeByte(LogEvent.Separator);
		buf.writeBytes(msg);
		out.add(new DatagramPacket(buf, remoteAddress));
		
	}

}
