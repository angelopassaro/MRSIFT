package it.unisa.soa.mrsift;

import java.io.File;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


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
  public void setUp(){
    //MRSIFT.load_library();
  }


  @Ignore
  public void testmatToByte() {
    Mat testMat = Imgcodecs.imread(testImagePath, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    byte[] exb = SiftUtils.matToByte(testMat);
    Mat ac = new Mat(testMat.rows(), testMat.cols(), testMat.type());
    CvType.typeToString(testMat.type());
    ac.put(0, 0, exb);
    Assert.assertEquals("It should return same dump", testMat.dump(), ac.dump());
  }

  @Ignore
  public void testbyteToMat() {
     Mat testMat = Imgcodecs.imread(testImagePath, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
     byte[] exb = SiftUtils.matToByte(testMat);
     Mat ac = SiftUtils.byteToMat(exb, testMat.width(), testMat.height(), testMat.type());
     Assert.assertEquals("It should return same dump", testMat.dump(), ac.dump());
  }
  
  @Test
  public void testExtractFormat(){
    String filePath = "filepathverylong.ghd.png";
    Assert.assertEquals("It should return png", "png", SiftUtils.extractFormat(filePath));
    String notExtension = "sdf/hdss/dff";
    Assert.assertEquals("It should return jpg", "jpg", SiftUtils.extractFormat(notExtension));
  }
}
