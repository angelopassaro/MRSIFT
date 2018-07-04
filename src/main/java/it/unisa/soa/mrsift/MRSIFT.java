package it.unisa.soa.mrsift;
import java.io.File;
import java.net.URL;

/**
 *
 * @author didacus
 */
public class MRSIFT {

  private static final String OPENCV_LIB = "/libopencv_java341.so";

  public static void load_library() {
    URL url = MRSIFT.class.getResource(OPENCV_LIB);
    File opencv = new File(url.getFile());
    System.load(opencv.getAbsolutePath());
  }

  public static void main(String[] args) {
    load_library();
  }
}
