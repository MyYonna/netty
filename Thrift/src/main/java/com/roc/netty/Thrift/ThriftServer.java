package com.roc.netty.Thrift;

import java.net.InetSocketAddress;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;

import com.roc.netty.Thrift.PersonService.Processor;

public class ThriftServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(new InetSocketAddress(8899));
			//处理器
			PersonService.Processor<PersonServiceImpl> processor = new Processor<PersonServiceImpl>( new PersonServiceImpl());;
			THsHaServer.Args arg = new THsHaServer.Args(serverTransport);
			
			arg.transportFactory(new TFramedTransport.Factory());
			//transport传输方式主要有四种：TSocket(阻塞式socket) 
			//TFramedTransport(以frame为单位进行传输 专门为非阻塞式服务)  
			//TFileTransport（以文件形式进行传输）  
			//TMemoryTransport 
			//TZibTransport   这是对servertransport的功能加强
			arg.protocolFactory(new TCompactProtocol.Factory());
			//protocol协议主要有：TCompactTransport(压缩传输协议)  
			//TBinaryProtocol(二进制传输协议) 
			//TJSONProtocol(JSON传输协议文本)，
			//TSimpleJSONProtocol(只写json协议，很少使用)
			arg.processorFactory(new TProcessorFactory(processor));
			TServer server = new THsHaServer(arg);
			//支持的服务模型：TSimpleServer 简单单线程模型  TThreadPoolServer 
			//多线程服务模型（阻塞式IO）  TNonblockingServer(用于非阻塞式IO ) 
			//THsHaServer(半同步半异步服务模型,对io进行异步处理，对业务进行同步处理)
			
			System.out.println("服务启动。。。。");
			server.serve();//死循环
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
		}
	}

}
