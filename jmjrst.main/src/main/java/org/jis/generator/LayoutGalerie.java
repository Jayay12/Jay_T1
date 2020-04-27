package org.jis.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LayoutGalerie {

	public void copyDirectories(File fromFile, File toFile) throws IOException, FileNotFoundException {
		File[] directories = fromFile.listFiles();
		toFile.mkdirs();
		for (File direc : directories) {
			if(direc.isDirectory()) {
				copyDirectories(direc, new File(toFile.getAbsolutePath() + System.getProperty(File.separator) + direc.getName()));
			} else {
				copyFile(direc, new File(toFile.getAbsolutePath() + System.getProperty(File.separator) + direc.getName()));
			}
		}
	}
	
	public void copyFile(File fromFile, File toFile) throws IOException, FileNotFoundException {
		File[] files = fromFile.listFiles();
		toFile.createNewFile();
		for (File file : files) {
			if(file.isFile()) {
				copyFile(file, new File(toFile.getAbsoluteFile() + System.getProperty(File.separator) + file.getName()));
			} else {
			}
			
		}
		
	}

}
