package netty.NettyEcho;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	  @Override
	  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
		  ByteBuf buffer = (ByteBuf)msg;
		  System.out.println("Server received msg :"+buffer.toString(CharsetUtil.UTF_8));
		  ctx.write(buffer);
	  }
	  
	  @Override
	  public void  channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		  ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(new ChannelFutureListener() {
			
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				  future.channel().close();
			}
		});
	  }
	  
	  @Override
	  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		cause.printStackTrace();
		ctx.close();
	  }
}
