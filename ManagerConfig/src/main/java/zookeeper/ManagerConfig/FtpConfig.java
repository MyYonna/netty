package zookeeper.ManagerConfig;

import java.io.Serializable;

public class FtpConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int port;
	private String host;
	private String user;
	private String password;
	
	FtpConfig(){
		
	}
	FtpConfig(String host,int port,String user,String password){
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString(){
		return user+"/"+password+"@"+host+":"+port;
	}

}
