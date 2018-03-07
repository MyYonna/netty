package zookeeper.ZookeeperBlance.server;

public interface RegistProvider {
	public void regist(ZooKeeperRegistContext context);
	public void unRegist(ZooKeeperRegistContext context);
}
