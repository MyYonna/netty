package com.roc.netty.Thrift;

import java.io.IOException;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
/**
 * 客户端使用同步的方式获取服务器中的数据
 * @author lenovo
 *
 */
public class ThriftClient {

	public static void main(String[] args) throws IOException, TException {
		// TODO Auto-generated method stub
		
		TSocket socket = new TSocket("127.0.0.1", 8899, 3000);
		TFramedTransport transport = new TFramedTransport(socket);//指定传输方式
		TProtocol protocol = new TCompactProtocol(transport);//执行传输协议
		PersonService.Client client = new PersonService.Client(protocol);
		try {
			transport.open();//打开连接
			Person person = client.getPersonById(0);
			System.out.println(person.getId()+"  "+person.getName()+"  "+person.getAddress()+"  "+person.getSex());
			client.savePerson(person);
		}finally {
			
		}
		
	}

}
