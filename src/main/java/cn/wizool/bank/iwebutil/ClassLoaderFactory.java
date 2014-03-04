package cn.wizool.bank.iwebutil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassLoaderFactory {
	private List<String> classPaths = new ArrayList<String>();
	private List<String> jarPaths = new ArrayList<String>();
	private List<String> jarFiles = new ArrayList<String>();

	public List<String> getClassPaths() {
		return classPaths;
	}

	public void setClassPaths(List<String> classPaths) {
		this.classPaths = classPaths;
	}

	public void addClassPath(String classPath) {
		this.classPaths.add(classPath);
	}

	public List<String> getJarPaths() {
		return jarPaths;
	}

	public void setJarPaths(List<String> jarPaths) {
		this.jarPaths = jarPaths;
	}

	public void addJarPath(String jarPath) {
		this.jarPaths.add(jarPath);
	}

	public List<String> getJarFiles() {
		return jarFiles;
	}

	public void setJarFiles(List<String> jarFiles) {
		this.jarFiles = jarFiles;
	}

	public void addJarFile(String jarFile) {
		this.jarFiles.add(jarFile);
	}

	public ClassLoader create() throws MalformedURLException {
		List<URL> urls = new ArrayList<URL>();
		for (String classPath : classPaths) {
			urls.add(new File(classPath).toURI().toURL());
		}
		for (String jarFile : jarFiles) {
			urls.add(new File(jarFile).toURI().toURL());
		}
		for (String jarPath : jarPaths) {
			File[] paths = new File(jarPath).listFiles();
			for (File path : paths) {
				urls.add(path.toURI().toURL());
			}
		}
		URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]));
		return loader;
	}

	public static ClassLoader createServletContextClassLoader(String root) throws MalformedURLException {
		ClassLoaderFactory clf = new ClassLoaderFactory();
		clf.addClassPath(root+"/WEB-INF/classes");
		clf.addJarPath(root+"/WEB-INF/lib/");
		return clf.create();
	}
}
