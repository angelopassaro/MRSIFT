package it.unisa.soa.mrsift;

import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import static org.junit.Assert.*;

/**
 *
 * @author didacus
 */
public class MrsiftTest {
  
  private final String testImagePath;

  public MrsiftTest() {
    super();
    this.testImagePath = new File(MrsiftTest.class.getResource("/testImage.jpg").getFile()).getAbsolutePath();
  }

  @Before
  public void setUp() {
    MRSIFT.load_library();
  }

  @Ignore
  public void testmatToByte() {
    Mat mat = Imgcodecs.imread(this.testImagePath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    assertNotNull(mat);
    byte[] result = SiftUtils.matToByte(mat);
    Mat mat2 = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1);
    mat2.put(0, 0, result);
    assertEquals("It should return same dump", mat.dump(), mat2.dump());
  }

  @Test
  public void testbyteToMat() {
    Mat expectedMat = Imgcodecs.imread(this.testImagePath, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    byte[] result = SiftUtils.matToByte(expectedMat);
    Mat actualMat = SiftUtils.byteToMat(result);
    assertEquals("It should return same dump", expectedMat.dump(), actualMat.dump());
  }
}
