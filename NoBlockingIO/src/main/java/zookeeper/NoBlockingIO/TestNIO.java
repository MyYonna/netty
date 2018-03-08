package zookeeper.NoBlockingIO;

public class TestNIO {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread serverThread = new Thread(new Runnable(){

			public void run() {
				// TODO Auto-generated method stub
				NIOServer server = new NIOServer();
				server.initServer(8080);
				server.listen();
			}

		});
		serverThread.start();
		for(int i=0;i<1000;i++){
			final int count = i;
			Thread clientThread = new Thread(new Runnable(){
				
				public void run() {
					// TODO Auto-generated method stub
					NIOClient client = new NIOClient();
					client.initClient("127.0.0.1", 8080);
					client.listen("hello server,i am client#"+count);
				}
				
			});
			clientThread.start();
		}
		
	}

}
