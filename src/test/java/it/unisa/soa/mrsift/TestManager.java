package it.unisa.soa.mrsift;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SIFT;

import java.io.File;
import org.opencv.features2d.DescriptorMatcher;

public class TestManager {

  private final String testImagePath;
  private final String scene;
  private SIFT sift;

  public TestManager() {
    super();
    this.testImagePath = new File(MrsiftTest.class.getResource("/object.png").getFile()).getAbsolutePath();
    this.scene = new File(MrsiftTest.class.getResource("/scene.jpg").getFile()).getAbsolutePath();
  }

  @Before
  public void setUp() {
    MRSIFT.load_library();
    this.sift = SIFT.create();
  }

  @Test
  public void testmatch() {
    Mat object = Imgcodecs.imread(testImagePath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    Mat scn = Imgcodecs.imread(scene, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    MatOfKeyPoint objectKeypoint = SiftManager.extractKeypoints(object, sift);
    MatOfKeyPoint sceneKeyPoint = SiftManager.extractKeypoints(scn, sift);
    MatOfKeyPoint objectDescriptors = SiftManager.extractDescriptors(object, objectKeypoint, sift);
    MatOfKeyPoint sceneDescriptor = SiftManager.extractDescriptors(scn, sceneKeyPoint, sift);
    MatOfDMatch matches = SiftManager.calculateMatches(objectDescriptors, sceneDescriptor, DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE));
    Mat homography = SiftManager.localizeObject(objectKeypoint.toList(), sceneKeyPoint.toList(), matches);
    if (SiftManager.checkHomography(homography)) {
      Mat out = SiftManager.drawImage(object, scn, objectKeypoint, sceneKeyPoint, matches, homography);
      Imgcodecs.imwrite(System.getProperty("user.home").concat("/out.jpg"), out);
    } else {
      System.out.println("Oggetto non trovato");
    }
  }

}
