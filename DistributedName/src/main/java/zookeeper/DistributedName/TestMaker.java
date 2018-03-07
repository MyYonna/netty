package zookeeper.DistributedName;

import zookeeper.DistributedName.IdMaker.RemoveMethod;

public class TestMaker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String server = "localhost:2181,localhost:2182,localhost:2183";
		String root = "/NameService/IdGen";
		String nodeName = "ID";
		try {
			final IdMaker idMaker = new IdMaker(server, root, nodeName);
			idMaker.start();
			for(int i=0;i<1000;i++){
				final int count = i;
				Thread thread = new Thread(new Runnable(){

					public void run() {
						// TODO Auto-generated method stub
						try {
							System.out.println("i="+count+"..................id="+idMaker.generateId(RemoveMethod.IMMEDIATELY));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				});
				thread.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
