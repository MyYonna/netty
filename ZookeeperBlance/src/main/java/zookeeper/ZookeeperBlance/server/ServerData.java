package zookeeper.ZookeeperBlance.server;

import java.io.Serializable;

public class ServerData implements Serializable,Comparable<ServerData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String host;
	private Integer port;
	private Integer balance = 0;
	public ServerData(){
		
	}
	
	public ServerData(String host, Integer port) {
		super();
		this.host = host;
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	
	public String toString(){
		return "ServerData:[ host="+host+", port="+port+",balance="+balance+"]";
	}
	
	public int compareTo(ServerData o){
		return this.balance.compareTo(o.getBalance());
	}
	private static final Integer SESSION_TIME_OUT = 10000;
	private static final Integer CONNECT_TIME_OUT = 10000;

}
