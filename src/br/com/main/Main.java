package br.com.main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class Main {

	private static URLClassLoader classLoader;

	public static void main(String[] args) throws Exception {

		classLoader = new URLClassLoader(getUrls());

		String packageName = "Teste";

		compile(getFiles());

		try {
			Object teste = classLoader.loadClass(packageName).newInstance();
			System.out.println("Inciando a executar Classes Java...");

			teste.getClass().getClassLoader().loadClass(packageName).getMethod("setDescription", String.class)
					.invoke(teste, "ClassLoader e JavaCompiler exemplo.");

			teste.getClass().getClassLoader().loadClass(packageName).getMethod("setVersion", String.class).invoke(teste,
					"2.0.0");

			teste.getClass().getClassLoader().loadClass(packageName).getMethod("getInstance").invoke(teste);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			classLoader.close();
		}

	}

	public static URL[] getUrls() {
		URL[] urls = null;
		URL url = null;

		File dir = new File("binTeste");

		try {
			url = dir.toURI().toURL();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		urls = new URL[] { url };

		return urls;

	}

	public static File[] getFiles() {

		try {
			File[] files;
			File dir = new File("srcTeste");
			files = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".java");

				}
			});

			return files;

		} finally {
			
		}
	}

	public static void compile(File[] files) throws IOException {
		System.out.println("\nInciando compilador java...");

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		if (compiler == null) {
			System.err.println("\t> Precisa Utilizar a JDK para ter acesso a ferramenta ToolProvider.");
		}
		

		for (File file : files) {
			int result = compiler.run(null, null, null, file.getAbsolutePath());

			if (result == 0) {
				System.out.println("\t> "+ file.getName() + " compilado com sucesso");

			} else {
				System.out.println("\t> Error ao compilar arquivo: " + file.getName());
			}
		}

		File[] fileClasses;
		File dir = new File("srcTeste");
		fileClasses = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".class");

			}
		});

		for (File file : fileClasses) {

			Path targetPath = Paths.get("binTeste/");

			try {
				Files.move(file.toPath(), targetPath.resolve(file.toPath().getFileName()));
				System.out.println("\t> "+ file.getName() + " movido para a pasta de destino.");
				
			} catch (IOException e) {
				if (e instanceof FileAlreadyExistsException) {
					System.err.println("\t> "+ file.getName() + " já existe na pasta de destino.");
					Files.deleteIfExists(targetPath.resolve(file.toPath().getFileName()));
					Files.move(file.toPath(), targetPath.resolve(file.toPath().getFileName()));
					System.out.println("\t> "+ file.getName() + " foi sobrescrito na pasta de destino.");
				}
			}
			
			System.out.println("\n");
		}

	}

}
