package cn.wizool.bank.iwebutil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

public class FileToZip {

	public static void compressionFile(HttpServletResponse response,
			String srcPath, String fileName) {
		File srcFile = new File(srcPath);
		if (srcFile.exists()) {
			compressionFile(response, srcFile, fileName);
		}
	}

	public static void compressionFile(HttpServletResponse response,
			File srcFile, String fileName) {
		BufferedOutputStream buffStream;
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(fileName, "utf-8"));
			buffStream = new BufferedOutputStream(response.getOutputStream());
			setCommpressionFile(srcFile, buffStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void compressionFile(String srcPath, String targetPath) {
		File srcFile = new File(srcPath);
		File gzFile = new File(targetPath);
		compressionFile(srcFile, gzFile);
	}

	public static void compressionFile(File srcFile, File gzFile) {
		BufferedOutputStream buffStream;
		try {
			buffStream = new BufferedOutputStream(new FileOutputStream(gzFile));
			setCommpressionFile(srcFile, buffStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void setCommpressionFile(File srcFile,
			BufferedOutputStream buffStream) {
		ZipOutputStream zipOutStream = null;
		try {
			zipOutStream = new ZipOutputStream(buffStream);
			realCompressionFile(srcFile, zipOutStream, null);
			zipOutStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffStream != null) {
					buffStream.flush();
					buffStream.close();
				}
				if (zipOutStream != null)
					zipOutStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void realCompressionFile(File srcFile,
			ZipOutputStream zipOutStream, String dir) {
		try {
			if (srcFile.isDirectory()) {
				String base = "";
				if (dir != null)
					base = dir + srcFile.getName() + "/";
				else
					base = srcFile.getName() + "/";
				ZipEntry dirZipEntry = new ZipEntry(base);
				zipOutStream.putNextEntry(dirZipEntry);
				for (File f : srcFile.listFiles()) {
					realCompressionFile(f, zipOutStream, base);
				}
			} else
				compressionSingleFile(srcFile, zipOutStream, dir);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void compressionSingleFile(File srcFile,
			ZipOutputStream zipOutStream, String base) {
		BufferedInputStream buffInputStream = null;
		boolean flag = false;
		String zipEntryName;
		if (base != null) {
			zipEntryName = base + srcFile.getName();
		} else
			zipEntryName = srcFile.getName();
		ZipEntry zipEntry = new ZipEntry(zipEntryName);
		try {
			zipOutStream.putNextEntry(zipEntry);
			if (srcFile.isFile()) {
				buffInputStream = new BufferedInputStream(new FileInputStream(
						srcFile));
				byte[] ereryReadByte = new byte[1024 * 2];
				int length = 0;
				while ((length = buffInputStream.read(ereryReadByte)) != -1) {
					zipOutStream.write(ereryReadByte, 0, length);
				}
				zipOutStream.flush();
			}
			flag = true;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("压缩文件失败", e);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffInputStream != null)
					buffInputStream.close();
			} catch (IOException ex) {
				throw new RuntimeException("关闭流出现异常", ex);
			} finally {
				if (!flag) {
					srcFile.delete();
				}
			}
		}
	}

	/**
	 * 压缩文件夹下的文件 平级的 压缩完后并删除
	 */
	public static void compressionFileAndDelete(String from, String to) {
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(to);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			byte data[] = new byte[2048];
			File f = new File(from);
			File files[] = f.listFiles();


			for (int i = 0; i < files.length; i++) {
				if (to.indexOf(files[i].getName()) == -1
						&& files[i].getName().indexOf("desc.txt") == -1) {

					FileInputStream fi = new FileInputStream(files[i]);
					origin = new BufferedInputStream(fi, 2048);
					ZipEntry entry = new ZipEntry(files[i].getName());
					out.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, 2048)) != -1) {
						out.write(data, 0, count);
					}
					origin.close();
//					files[i].delete();
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// File f = new File("D:\\test");
		// File[] files = f.listFiles();
		// File endf = new File("D:\\test\\task.zip");
		// System.out.println(files.length);
		// for (int i = 0; i < files.length; i++) {
		// compressionFile(files[i], endf);
		// }
		
//		String name = String.format("%1$03d", 1);
//		System.out.println(name);
//		compressionFileAndDelete("D:\\test", "D:\\test\\task.zip");
	}
}
