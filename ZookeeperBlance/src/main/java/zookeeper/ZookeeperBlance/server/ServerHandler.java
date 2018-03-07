package zookeeper.ZookeeperBlance.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
/**
 * 客户端连接服务端的处理类
 * @author Administrator
 *
 */
public class ServerHandler extends ChannelHandlerAdapter {
	private final BalanceUpdateProvider balanceUpdater;
	private static final Integer BALANCE_STEP = 1;
	public ServerHandler(BalanceUpdateProvider balanceUpdater) {
		super();
		this.balanceUpdater = balanceUpdater;
	}
	public BalanceUpdateProvider getBalanceUpdater() {
		return balanceUpdater;
	}
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("one client connecting,.......");
		balanceUpdater.addBalance(BALANCE_STEP);
	}
	
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
		System.out.println("one client disconnecting,.......");
		balanceUpdater.reduceBalance(BALANCE_STEP);
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable th){
		th.printStackTrace();
		ctx.close();
	}
}
