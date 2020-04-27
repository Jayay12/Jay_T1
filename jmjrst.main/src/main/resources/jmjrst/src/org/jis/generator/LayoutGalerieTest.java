package org.jis.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import org.junit.Test;

import junit.framework.TestCase;

public class LayoutGalerieTest {
	
	private LayoutGalerie galerieUnderTest;
	
	private File fromFile;
	private File toFile;


	private LayoutGalerie copyDirTest; 
		
	/**
	 * Test method for {@link org.jis.generator.LayoutGalerie#copyFile(File, File)}.
	 */
	@Test
	public final void testCopyFile() throws URISyntaxException {
		
		galerieUnderTest = new LayoutGalerie();
		try {
			final File resourceFolder = new File(this.getClass().getResource(File.separator).toURI());
			fromFile = new File(resourceFolder, "from");
			toFile = new File(resourceFolder, "to");
			
			byte[] array = new byte[10];
			new Random().nextBytes(array);
			String randomString = new String(array);
		 			 
			fromFile.createNewFile();
			Path fromPath = FileSystems.getDefault().getPath(fromFile.getPath());
			Files.writeString(fromPath, randomString);
			 
			galerieUnderTest.copyFile(fromFile, toFile);
			
			assertTrue(toFile.exists());
			 
			Path toPath = FileSystems.getDefault().getPath(toFile.getPath());
			String contents = Files.readString(toPath);
			 		 
			assertEquals(randomString, contents);
		 }
		 catch (IOException | URISyntaxException e) {
			fail();
		 }
		
	}
	@Test
	public void testCopyDir() throws IOException, FileNotFoundException {
		copyDirTest = new LayoutGalerie();
		File sourceDir = new File("");
		File targetDir = new File("");
		this.copyDirTest = this.galerieUnderTest.copyDir(sourceDir, targetDir);
		assertTrue(targetDir.exists());
	}
	
	@Test
	public void testExistingFile() throws IOException, FileNotFoundException {
		String files = null;
		File file = new File(files);
		assertNull(file.exists());
	}
	
}