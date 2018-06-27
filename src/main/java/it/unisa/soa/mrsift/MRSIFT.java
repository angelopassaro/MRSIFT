package it.unisa.soa.mrsift;

import java.io.File;
import java.net.URL;
import org.opencv.core.Mat;

import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author didacus
 */
public class MRSIFT {

  private static final String OPENCV_LIB = "libopencv_java341.so";

  public static void load_library() {
    URL url = MRSIFT.class.getResource("/" + OPENCV_LIB);
    File opencv = new File(url.getFile());
    System.load(opencv.getAbsolutePath());
  }

  public static void main(String[] args) {
    load_library();
    String firstImage = "/home/didacus/first.jpg";
    String secondImage = "/home/didacus/second.jpg";
    ConcreteSiftAlgorithm algorithm = new ConcreteSiftAlgorithm(firstImage, secondImage);
    Mat result = algorithm.findFeatures();
    Imgcodecs.imwrite("/home/didacus/result.jpg", result);
  }

}
