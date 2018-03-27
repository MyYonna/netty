package buffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/**
 * 测试直接缓冲区与堆内缓冲区的性能
 * 因为Nio对数据进行读写操作的时候，需要进行一个数据拷贝的工作，将操作系统内存中的数据拷贝到JVM的内存当中
 * 而直接缓冲区是在JVM 内存之外，直接在系统内存中创建的一个缓冲区，这样就减少了数据拷贝的工作，提高IO性能。
 * 我们在源码中可以看到，直接缓冲区的实例是一个DirectByteBuffer实例，他存放了一个操作系统内存的一个地址，这个地址也是存放了底层数组
 * 而间接缓冲区的实例是HeapByteBuffer实例,他的底层数组是存放在JVM的堆中
 * @author Administrator
 *
 */
public class DirectBufferTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		File file = new File("build.gradle");
		FileInputStream fileInputStream = new FileInputStream(file);
		File bakFile = new File("build.gradle.bak");
		FileOutputStream fileOutputStream = new FileOutputStream(bakFile);
		FileChannel fileChannel = fileInputStream.getChannel();
		FileChannel bakChannel = fileOutputStream.getChannel();
		//创建一个直接缓冲区
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1000);//测试结果为13毫秒，可以看出，在高访问量的情况下，对于io的性能的优化是很大的
		//创建一个堆内缓冲区
//		ByteBuffer byteBuffer = ByteBuffer.allocate(1000);//测试和结果为16毫秒
		int j=0;
		while(j<10000){
			while(true){
				byteBuffer.clear();
				int i = fileChannel.read(byteBuffer);
				byteBuffer.flip();
				if(i!=-1){
					bakChannel.write(byteBuffer);
				}else{
					break;
				}
			}
			j++;
		};
		
		fileInputStream.close();
		fileOutputStream.close();
		long end = System.currentTimeMillis();
		
		System.out.println("总共花费时间"+(end-start)+"毫秒");
	}

}
