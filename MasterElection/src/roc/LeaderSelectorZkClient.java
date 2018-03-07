package roc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LeaderSelectorZkClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<WorkServer> servers = new ArrayList<WorkServer>();
		try{
			for(int i=0;i<10;i++){
				RunningData runningData = new RunningData();
				runningData.setId(String.valueOf(i));
				runningData.setName("client #"+i);
				
				WorkServer workServer = new WorkServer(runningData);
				servers.add(workServer);
				workServer.start();
				
			}
			System.out.println("敲回车键退出！\n");
			String i = new BufferedReader(new InputStreamReader(System.in)).readLine();
			servers.get(Integer.valueOf(i)).releaseMaster();
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		}catch(Exception e){
			
		}finally{
			System.out.println("停止服务器");
			for(WorkServer workServer : servers){
				try {
					workServer.stop();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
