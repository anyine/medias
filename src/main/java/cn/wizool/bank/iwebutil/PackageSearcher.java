package cn.wizool.bank.iwebutil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageSearcher {
	private PackageSearcherListener listener;
	private String packageName;
	private boolean recursive = true;
	private ClassLoader classLoader = Thread.currentThread()
			.getContextClassLoader();
	private String root;

	public PackageSearcherListener getListener() {
		return listener;
	}

	public void setListener(PackageSearcherListener listener) {
		this.listener = listener;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void search() {
		try {
			Enumeration<URL> dirs = this.getClassLoader().getResources(
					this.getPackageName());

			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				if (url.getProtocol().equals("jar")) {
					JarFile jar = ((JarURLConnection) url.openConnection())
							.getJarFile();
					this.root = jar.getName();
					Enumeration<JarEntry> entries = jar.entries();
					int count = count(this.getPackageName(), '/');
					while (entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						if (name.startsWith(this.getPackageName() + '/')) {
							if (this.isRecursive()
									|| count(name, '/') == count + 1) {
								this.listener.found(this.root,
										name.endsWith("/"),
										name.replaceAll("/$", ""));
							}
						}
					}
				} else if (url.getProtocol().equals("file")) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					File dir = new File(filePath);
					this.root = dir.getAbsolutePath().substring(
							0,
							dir.getAbsolutePath().length()
									- this.getPackageName().length());
					searchFile(dir);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int count(String string, char chr) {
		return string.replaceAll("[^" + chr + "]+", "").length();
	}

	private void searchFile(File dir) {
		File[] subs = dir.listFiles();
		for (File s : subs) {
			if (s.isDirectory() && this.isRecursive()) {
				searchFile(s);
			}
			this.listener.found(this.root, s.isDirectory(), s.getAbsolutePath()
					.replace(root, "").replace('\\', '/'));
		}
	}

	public static void main(String[] args) {
		PackageSearcher cs = new PackageSearcher();
		// cs.setClassLoader(Thread.currentThread().getContextClassLoader());
		// cs.setRecursive(true);
		cs.setPackageName("cn/wizool/eims/goods/servlet");
		cs.setListener(new PackageSearcherListener() {

			@Override
			public void found(String path, boolean isDir, String resource) {
				System.out.println(path + "\r\n" + isDir + "\r\n" + resource);
			}

		});
		cs.search();
	}
}
