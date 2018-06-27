
package it.unisa.soa.mrsift;

import java.util.List;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;

/**
 *
 * @author didacus
 * Main interface for sequential algorithm
 */
public interface SiftAlgorithm {
  
  Mat findFeatures();
  List<Mat> findFeatures(String objectImagePath, List<String> scenesImagePath);
  Mat findImage(List<KeyPoint> objectPoints, List<KeyPoint> scenePoints);
}
