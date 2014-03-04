package cn.wizool.bank.iwebutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileCopy {
	/**
	 * 复制文件(以超快的速度复制文件)
	 * 
	 * @author:suhj 2006-8-31
	 * @param srcFile
	 *            源文件File
	 * @param destDir
	 *            目标目录File
	 * @param newFileName
	 *            新文件名
	 * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
	 */
	public static long copyFile2(File srcFile, File destDir, String newFileName) {
		long copySizes = 0;
		if (!srcFile.exists()) {
			//System.out.println("源文件不存在");
			copySizes = -1;
		} else if (!destDir.exists()) {
			//System.out.println("目标目录不存在");
			copySizes = -1;
		} else if (newFileName == null) {
			//System.out.println("文件名为null");
			copySizes = -1;
		} else {
			try {
				FileChannel fcin = new FileInputStream(srcFile).getChannel();
				FileChannel fcout = new FileOutputStream(new File(destDir,
						newFileName)).getChannel();
				ByteBuffer buff = ByteBuffer.allocate(1024);
				int b = 0, i = 0;
				// long t1 = System.currentTimeMillis();
				/*
				 * while(fcin.read(buff) != -1){ buff.flip(); fcout.write(buff);
				 * buff.clear(); i++; }
				 */
				long size = fcin.size();
				fcin.transferTo(0, fcin.size(), fcout);
				// fcout.transferFrom(fcin,0,fcin.size());
				// 一定要分清哪个文件有数据，那个文件没有数据，数据只能从有数据的流向
				// 没有数据的文件
				// long t2 = System.currentTimeMillis();
				fcin.close();
				fcout.close();
				copySizes = size;
				// long t = t2-t1;
				// System.out.println("复制了" + i + "个字节\n" + "时间" + t);
				// System.out.println("复制了" + size + "个字节\n" + "时间" + t);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return copySizes;
	}
}
