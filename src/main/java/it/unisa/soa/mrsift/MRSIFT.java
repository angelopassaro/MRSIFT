package it.unisa.soa.mrsift;

import java.io.File;
import java.net.URL;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SIFT;
import org.opencv.features2d.Features2d;

/**
 *
 * @author didacus
 */
public class MRSIFT {
  
  private static final String OPENCV_LIB = "libopencv_java341.so";
  
  public static void load_library(){
    URL url = MRSIFT.class.getResource("/" + OPENCV_LIB);
    File opencv = new File(url.getFile());
    System.load(opencv.getAbsolutePath());
  }

  public static void main(String[] args) {
    load_library();
    String path = System.getProperty("user.home");
    Mat src = Imgcodecs.imread(path.concat("/albero.jpg"));
    Mat src_gray = new Mat();
    Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
    SIFT sift = SIFT.create();
    MatOfKeyPoint kp = new MatOfKeyPoint();
    sift.detect(src_gray, kp);
    Features2d.drawKeypoints(src, kp, src_gray);
    Imgcodecs.imwrite(path.concat("/result.jpg"), src_gray);
  }
  
}
