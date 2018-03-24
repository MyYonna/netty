namespace java com.roc.netty.Thrift

typedef  i32 int;
typedef  string String;
typedef  bool Boolean;

enum Sex{
	GIRL=1,
	BOY=2
}

struct Person{
	1:int id,
	2:String name,
	3:Boolean ifAdult,
	4:Sex sex,
	5:String address
}

service PersonService{
	Person getPersonById(1:int id),
	void savePerson(1:Person person)
}

exception PersonException{
	1:int errorCode,
	2:String errorMsg
}