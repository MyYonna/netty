package buffer;

import java.nio.IntBuffer;
import java.security.SecureRandom;
/**
 * 根据打印结果可知，flip方法只是单纯的将position的值赋予的limit,并将自身的的position值设置为0,所以在多次flip之后，limit的值将会变小，能写入数据的长度也会小于capacity的大小
 * @author Administrator
 *
 */
public class BufferTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IntBuffer intBuffer = IntBuffer.allocate(10);
		for(int i=0;i<5;i++){
			int data = new SecureRandom().nextInt(20);
			intBuffer.put(data);
		}
		System.out.println("数据读入buffer之后。。。。。");
		System.out.println("读： flip 0 capacity="+intBuffer.capacity());
		System.out.println("读： flip 0 limit="+intBuffer.limit());
		System.out.println("读： flip 0 position="+intBuffer.position());
		intBuffer.flip();
		System.out.println("数据读入buffer之后进行flip之后。。。。。");
		System.out.println("写：  flip 1 capacity="+intBuffer.capacity());
		System.out.println("写：   flip 1 limit="+intBuffer.limit());
		System.out.println("写：  flip 1 position="+intBuffer.position());
		intBuffer.get();
		intBuffer.get();
		System.out.println("数据写出buffer之后。。。。。");
		System.out.println("读：  flip 2 capacity="+intBuffer.capacity());
		System.out.println("读：  flip 2 limit="+intBuffer.limit());
		System.out.println("读：  flip 2 position="+intBuffer.position());
		
		intBuffer.flip();
		System.out.println("写：  flip 3 capacity="+intBuffer.capacity());
		System.out.println("写：  flip 3 limit="+intBuffer.limit());
		System.out.println("写：  flip 3 position="+intBuffer.position());
		for(int i=0;i<intBuffer.capacity();i++){
			System.out.println(intBuffer.get(i));
		}
		//因为limit变成了2，所以下面将会抛出indexOutOfBoundsException
		intBuffer.put(new SecureRandom().nextInt(20));
		intBuffer.put(new SecureRandom().nextInt(20));
		intBuffer.put(new SecureRandom().nextInt(20));
		System.out.println("写：  flip 4 capacity="+intBuffer.capacity());
		System.out.println("写：  flip 4 limit="+intBuffer.limit());
		System.out.println("写：  flip 4 position="+intBuffer.position());
		
	}

}
