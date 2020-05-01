package org.jis.generator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

public class SecondGeneratorTest {
	/**
	 * Input for test cases
	 */
	private Generator gen;
	private static final File TEST_DIRECTORY = new File("target/test");
	private static final String IMAGE_FILE = "/image.jpg";
	private Object imgName;

	/**
	 * making mock objects for test
	 */
	@Mock
	/**
	 * output for test case
	 */
	BufferedImage outputForRotateImage;
	/**
	 * input for test case
	 */
	BufferedImage testImg;
	int width;
	int height;
	File testDirectory;

	/**
	 * Make sure that the output directory exists and is empty.
	 **/
	@BeforeClass
	public static void setUpbeforeClass() throws Exception {
		if (TEST_DIRECTORY.exists()) {
			for (File f : TEST_DIRECTORY.listFiles()) {
				f.delete();
			}
		} else {
			TEST_DIRECTORY.mkdirs();
		}
	}

	@Before
	public void setUp() throws Exception {
		this.gen = new Generator(null, 0);
		this.testImg = null;
		this.outputForRotateImage = null;
		this.testDirectory = null;
		this.width = 0;
		this.height = 0;
		this.imgName = null;

		final URL imageRes = this.getClass().getResource(IMAGE_FILE);
		imgName = extractFile(new File(imageRes.getFile()));

		try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageRes.openStream())) {
			ImageReader imageReader = ImageIO.getImageReadersByFormatName("jpg").next();
			imageReader.setInput(imageInputStream, true);
			ImageReadParam parameters = imageReader.getDefaultReadParam();
			this.testImg = imageReader.read(0, parameters);
			this.height = this.testImg.getHeight();
			this.width = this.testImg.getWidth();
		} catch (IOException e) {
			fail(e.getMessage());
		}

	}

	private Object extractFile(File file) {
		String fileName = file.getName();
		if (fileName.indexOf(".") > 0) {
			return fileName.substring(0, fileName.lastIndexOf("."));
		} else {
			return fileName;
		}
	}

	public String getimgName() {
		return (String) imgName;
	}

	@Test
	public void testCreateZip() {
		// check if there is a target file for zip
		// making Mock objects
		File zipFileName = mock(File.class);
		if (zipFileName.exists()) {
			gen.createZip(zipFileName, new Vector<File>());
			assertTrue(zipFileName.exists());
			zipFileName.delete();
		}
	}

	@Test
	public void testGenerateText() throws IOException {
		File outputFile = new File(testDirectory + "/image.jpg");
		File inputFile = mock(File.class);
		if (inputFile.exists() && outputFile.exists()) {
			gen.generateText(inputFile, outputFile, width, height);
			assertTrue(outputFile.exists());
			outputFile.delete();
		}
	}

	@Test
	public void testRotateImage_Rotate360() {
		// through this test image become original position
		this.outputForRotateImage = this.gen.rotateImage(testImg, Generator.ROTATE_90);
		this.outputForRotateImage = this.gen.rotateImage(this.outputForRotateImage, Generator.ROTATE_270);

		assertEquals(this.testImg.getHeight(), this.outputForRotateImage.getHeight());
		assertEquals(this.testImg.getWidth(), this.outputForRotateImage.getWidth());

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				assertEquals(this.testImg.getRGB(j, i), this.outputForRotateImage.getRGB(j, i));
			}
		}
	}

	@Test
	public void testRotateImage_Rotate180() throws IllegalArgumentException {
		this.outputForRotateImage = this.gen.rotateImage(testImg, Generator.ROTATE_90);
		this.outputForRotateImage = this.gen.rotateImage(this.outputForRotateImage, Generator.ROTATE_90);

		assertEquals(this.testImg.getHeight(), this.outputForRotateImage.getHeight());
		assertEquals(this.testImg.getWidth(), this.outputForRotateImage.getWidth());

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				assertEquals(this.testImg.getRGB(j, i),
						this.outputForRotateImage.getRGB(this.width - 1 - j, this.height - 1 - i));
			}
		}
	}

	// generates IllegalArgumentException when image rotates wrong angle
	@Test(expected = IllegalArgumentException.class)
	public void rotateImage_RotateM150() {
		gen.rotateImage(testImg, Math.toRadians(150));
	}

	// generates IllegalArgumentException when there is no file
	@Test(expected = IllegalArgumentException.class)
	public void rotateImageWithNoImage() {
		gen.rotate(null);
	}

}
