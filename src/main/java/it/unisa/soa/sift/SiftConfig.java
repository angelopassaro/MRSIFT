package it.unisa.soa.sift;

import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author didacus
 */
public interface SiftConfig {
  
  public static final int COLORED_IMAGE = Imgcodecs.IMREAD_COLOR;
  public static final float OPTIMAL_THRESHOLD = 0.7f;
  public static final int DESCRIPTOR_EXTRACTOR = DescriptorMatcher.FLANNBASED;
  public static final double RANSAC_THRESHOLD = 3.0;
  public static final Scalar RECT_SCALAR = new Scalar(0, 255, 0);
  public static final int N_FEATURE = 0;
  public static final int OCTAVE_LAYERS = 3;
  public static final double CONTRAST_THRESHOLD = 0.04;
  public static final double EDGE_THRESHOLD = 10;
  public static final double SIGMA = 1.6;
  
  
}
