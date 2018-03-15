package com.roc.netty.NettyUDP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class LogEventBroadcaster {
	private final Bootstrap bootstrap;
	private final EventLoopGroup group;
	private File file;
	
	public LogEventBroadcaster(InetSocketAddress address,File file){
		this.file = file;
		bootstrap = new Bootstrap();
		group = new NioEventLoopGroup();
		final InetSocketAddress remoteAddress = address;
		bootstrap.group(group)
				 .channel(NioDatagramChannel.class)
				 .handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						// TODO Auto-generated method stub
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new LogEventEncoder(remoteAddress));
					}
				})
				 .option(ChannelOption.SO_BROADCAST, true);
	}
	
	public void run () throws Exception{
		Channel ch = bootstrap.bind(0).syncUninterruptibly().channel();
		System.out.println("LogEventBroadcaster running...");
		long pointer = 0;
		while(true){
			long len = file.length();
			if(pointer < len){
				//追加了日志到日志文件
				//任意读取文件类
				RandomAccessFile accessFile = new RandomAccessFile(file, "r");
				accessFile.seek(pointer);
				String line;
				while((line = accessFile.readLine()) != null){
					ch.writeAndFlush(new LogEvent(null, file.getAbsolutePath(), line, -1));
				}
				pointer = accessFile.getFilePointer();
				accessFile.close();
				//隔一秒轮询，访问file中添加的行
				try{
					Thread.sleep(1000);
				}catch(InterruptedException e){
					Thread.interrupted();
					break;
				}
			}else if(pointer > len){
				//重置了日志文件
				pointer = 0;
			}
		}
		
	
	}
	
	
	public void stop(){
		group.shutdownGracefully();
	}
	
	public static void main(String[] args) {
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255",8888), new File("E:\\log.txt"));  //8
        try {
            broadcaster.run();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            broadcaster.stop();
        }
	}
	
}
