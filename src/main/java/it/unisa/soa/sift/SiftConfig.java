package it.unisa.soa.sift;

import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author didacus
 */
public interface SiftConfig {
  
  public static final int COLORED_IMAGE = Imgcodecs.IMREAD_GRAYSCALE;
  public static final float OPTIMAL_THRESHOLD = 0.75f;
  public static final Scalar RECT_SCALAR = new Scalar(0, 255, 0);
  
  
}
