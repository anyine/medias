package cn.wizool.bank.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
public class FileUtil {

	private static final int BUFFER_SIZE = 1024;

	public static void copy(InputStream is, File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStream os = new FileOutputStream(file);
			copy(is, os);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copy(String file, OutputStream os) {
		try {
			FileInputStream fis = new FileInputStream(file);
			copy(fis, os);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copy(String ifile, String ofile) {
		try {
			FileInputStream fis = new FileInputStream(ifile);
			FileOutputStream fos = new FileOutputStream(ofile);
			copy(fis, fos);
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void copy(InputStream is, OutputStream os) {
		try {
			byte bs[] = new byte[BUFFER_SIZE];
			int len;

			while ((len = is.read(bs, 0, BUFFER_SIZE)) > 0) {
				os.write(bs, 0, len);
			}
			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(String content, File file) {
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			pw.write(content);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String read(File file) {
		String content = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String line;
			while ((line = br.readLine()) != null) {
				content += line + "\r\n";
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static void unzip(String zipfile, String path) throws IOException {
		ZipFile zf = new ZipFile(zipfile);
		Enumeration<? extends ZipEntry> entries = zf.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String file = path + "/" + entry.getName();
			if (entry.getName().endsWith("/")) {
				new File(file).mkdirs();
			} else {
				InputStream is = zf.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(new File(file));
				byte bs[] = new byte[BUFFER_SIZE];
				int len;
				while ((len = is.read(bs, 0, BUFFER_SIZE)) > 0) {
					fos.write(bs, 0, len);
				}
				fos.close();
				is.close();
			}
		}
	}

	public static void deleteRecursive(File dirFile) {
		if (dirFile.isDirectory()) {
			File[] files = dirFile.listFiles();
			for (File file : files) {
				deleteRecursive(file);
			}
		}
		dirFile.delete();
	}

	public static byte[] md5(String file) throws IOException,
			NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance("MD5");
		InputStream is = new FileInputStream(file);
		byte buffer[] = new byte[1024];
		while (true) {
			int len = is.read(buffer);
			if (len > 0) {
				sha.update(buffer, 0, len);
			} else {
				break;
			}
		}
		is.close();
		return sha.digest();
	}
}
