package org.jis.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
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
	private File sourceDir;
	private File targetDir;

	// private LayoutGalerie copyDirTest;

	/**
	 * Test method for {@link org.jis.generator.LayoutGalerie#copyFile(File, File)}.
	 */
	@Test
	public final void testCopyFile() throws URISyntaxException {
		File file = new File("");
		// galerieUnderTest = new LayoutGalerie();
		if (file.exists()) {
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
			} catch (IOException | URISyntaxException e) {
				fail();
			}
		}

	}

	/**
	 * Test method for copy directories
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testCopyDir() throws IOException, FileNotFoundException {
		sourceDir = new File("");
		targetDir = new File("");
		if (sourceDir.exists() && targetDir.exists()) {
			galerieUnderTest.copyDir(sourceDir, targetDir);
			assertTrue(targetDir.exists());
		}
	}

	/**
	 * Test method for non-existing files
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testNonExistingFile() throws IOException, FileNotFoundException {
		File resourceFolder;
		try {
			resourceFolder = new File(this.getClass().getResource(File.separator).toURI());
			fromFile = new File(resourceFolder, "from");
			toFile = new File(resourceFolder, "to");
			if (fromFile.exists()) {
				assertEquals("There is no such file", fromFile, toFile);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test method for file existence check
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testExistenceCheck() throws FileNotFoundException, IOException {
		try {
			File resourceFolder;
			resourceFolder = new File(this.getClass().getResource(File.separator).toURI());
			fromFile = new File(resourceFolder, "from");
			toFile = new File(resourceFolder, "to");
			if (toFile.exists() && fromFile.exists()) {
				galerieUnderTest.copyDir(fromFile, toFile);
				assertTrue(toFile.exists());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for read access to source files
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testReadAccessToSourceFile() throws IOException, FileNotFoundException {
		String sour = "";
		URL resource = getClass().getResource(sour);
		if (resource != null) {
			File file = new File(resource.getFile());
			if (file.exists()) {
				assertTrue(file.setReadable(true));
			}

		}
	}

	/**
	 * Test method for write access to target files
	 * 
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	@Test
	public void testWriteAccessToTargetFile() throws URISyntaxException, IOException, FileNotFoundException {
		File resourceFolder;
		resourceFolder = new File(this.getClass().getResource(File.separator).toURI());
		fromFile = new File(resourceFolder, "from");
		toFile = new File(resourceFolder, "to");
		RandomAccessFile read = new RandomAccessFile(toFile, "rw"); 
		FileChannel cha = read.getChannel();
		byte[] array = new byte[10];
		new Random().nextBytes(array);
		String randomString = new String(array);
		FileLock block = null;
		try {
			block = read.getChannel().lock();
		} catch (IOException e) {
			read.close();
			cha.close();	
		}
		read.write(randomString.getBytes());
		block.release();

		read.close();
		cha.close();
		/*
		byte[] array = new byte[10];
		new Random().nextBytes(array);
		String randomString = new String(array);
		try {
			RandomAccessFile read = new RandomAccessFile(fromFile, "r"); 
			FileLock block = read.getChannel().lock();
			if (block != null) {
				block.release();
				read.close();
				toFile.delete();
				read.write(randomString.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
