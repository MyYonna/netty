package zookeeper.ZookeeperBlance.client;

public interface BalanceProvider<T> {
	public T getBalanceItem();
}
