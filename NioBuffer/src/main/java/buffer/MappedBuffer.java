package buffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * MappedBuffer是一个内存映射的buffer,他可以将文件的全部或者部分映射到内存当中，
 * 允许应用程序可以直接通过操作内存来操作文件，而不需要通过跟磁盘进行io操作，这就提高了效率。
 * 它是通过文件的map方法获得的。
 * @author Administrator
 *
 */
public class MappedBuffer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			RandomAccessFile file = new RandomAccessFile("build.gradle.bak", "rw");
			FileChannel fileChannel = file.getChannel();
			//参数说明，第一个参数是映射的模式，第二个是从文件的什么地方开始映射，第三个是映射的范围是多大
			MappedByteBuffer mappedBuffer = fileChannel.map(MapMode.READ_WRITE, 4, 6);
			for(int i=0;i<mappedBuffer.capacity();i++){
				System.out.print((char)mappedBuffer.get());
			}
			mappedBuffer.put(3, (byte)'3');
			mappedBuffer.put(5, (byte)'3');
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
