package com.roc.netty.NettyProto;

import com.roc.netty.NettyProto.MyDataInfo.Animal;
import com.roc.netty.NettyProto.MyDataInfo.Person;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoServerHandler extends SimpleChannelInboundHandler<MyDataInfo.Animal> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Animal msg) throws Exception {
		// TODO Auto-generated method stub
		
		switch(msg.getAnimalType()){
		case CAT : 
				MyDataInfo.Cat cat = msg.getCat();
				System.out.println(cat.getId());
				System.out.println(cat.getName());
				System.out.println(cat.getColor());
				break;
		case DOG : 
			MyDataInfo.Dog dog = msg.getDog();
			System.out.println(dog.getId());
			System.out.println(dog.getName());
			System.out.println(dog.getLength());
			break;
		case PERSON : 
			MyDataInfo.Person person = msg.getPerson();
			System.out.println(person.getId());
			System.out.println(person.getName());
			System.out.println(person.getAddress());
			break;
			
		}
	}

}
