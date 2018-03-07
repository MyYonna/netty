package roc;

import java.io.Serializable;

public class DBServerConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dbUrl;
	private String dbPwd;
	private String dbUser;
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getDbPwd() {
		return dbPwd;
	}
	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	
	
	public String toString(){
		return "ServerConfig [dbUrl="+dbUrl+",dbPwd="+dbPwd+",dbUser="+dbUser+"]";
	}
}
