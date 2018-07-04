package it.unisa.soa.mrsift;
import com.google.common.io.Files;
import it.unisa.soa.sift.SiftManager;
import it.unisa.soa.sift.SiftModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.opencv.xfeatures2d.SIFT;

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

  public static void main(String[] args) throws IOException {
    load_library();
  }
}
