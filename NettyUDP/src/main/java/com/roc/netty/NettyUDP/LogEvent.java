package com.roc.netty.NettyUDP;

import java.io.Serializable;
import java.net.InetSocketAddress;

public class LogEvent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final byte Separator = (byte)':';
	private final InetSocketAddress source;
	private final String logfile;
	private final String msg;
	private final long receivedTime;
	
	
	public LogEvent(String logfile, String msg) {
		this(null,logfile,msg,-1);
	}
	public LogEvent(InetSocketAddress source, String logfile, String msg, long receivedTime) {
		super();
		this.source = source;
		this.logfile = logfile;
		this.msg = msg;
		this.receivedTime = receivedTime;
	}
	public static byte getSeparator() {
		return Separator;
	}
	public String getLogfile() {
		return logfile;
	}
	public String getMsg() {
		return msg;
	}
	public InetSocketAddress getSource() {
		return source;
	}
	public long getReceivedTime() {
		return receivedTime;
	}
	
}
