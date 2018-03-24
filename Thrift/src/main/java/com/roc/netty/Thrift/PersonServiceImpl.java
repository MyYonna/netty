package com.roc.netty.Thrift;

import org.apache.thrift.TException;

import com.roc.netty.Thrift.PersonService.Iface;

public class PersonServiceImpl implements Iface {

	@Override
	public Person getPersonById(int id) throws TException {
		// TODO Auto-generated method stub
		Person person = new Person();
		person.setAddress("深圳");
		person.setId(0);
		person.setName("zhangpeng");
		person.setIfAdult(true);
		return person;
	}

	@Override
	public void savePerson(Person person) throws TException {
		// TODO Auto-generated method stub
		System.out.println(person.getId()+"  "+person.getName()+"  "+person.getAddress()+"  "+person.getSex());

	}

}
