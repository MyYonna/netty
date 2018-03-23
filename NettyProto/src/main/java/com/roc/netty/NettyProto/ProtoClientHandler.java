package com.roc.netty.NettyProto;

import java.util.Random;

import com.roc.netty.NettyProto.MyDataInfo.Animal;
import com.roc.netty.NettyProto.MyDataInfo.Animal.AnimalType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoClientHandler extends SimpleChannelInboundHandler<MyDataInfo.Animal>{
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		int i = new Random().nextInt(3);
		if(i == 0){
			MyDataInfo.Cat cat = MyDataInfo.Cat.newBuilder().setId(1).setName("咪咪").setColor("黑白").build();
			ctx.channel().writeAndFlush(MyDataInfo.Animal.newBuilder().setAnimalType(AnimalType.CAT).setCat(cat));		
		}
		if(i == 1){
			MyDataInfo.Dog dog = MyDataInfo.Dog.newBuilder().setId(1).setName("旺旺").setLength(6).build();
			ctx.channel().writeAndFlush(MyDataInfo.Animal.newBuilder().setAnimalType(AnimalType.DOG).setDog(dog));		

		}
		if(i == 2){
			MyDataInfo.Person person = MyDataInfo.Person.newBuilder().setId(1).setName("张鹏").setAddress("深圳").build();
			ctx.channel().writeAndFlush(MyDataInfo.Animal.newBuilder().setAnimalType(AnimalType.PERSON).setPerson(person));		

		}
		
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Animal msg) throws Exception {
		// TODO Auto-generated method stub
	}
}
