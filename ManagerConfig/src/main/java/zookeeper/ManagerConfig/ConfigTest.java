package zookeeper.ManagerConfig;

public class ConfigTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigManager cm = new ConfigManager();
		cm.loadConfigFromDB();
		cm.syscFtpConfigToZK();
		ClientApp ca = new ClientApp();
		ca.run();
		
		ca.upload();
		cm.updateFtpConfigToDB("127.0.0.1", 8888, "daisy", "123456");
		cm.syscFtpConfigToZK();
		ca.upload();
	}

}
