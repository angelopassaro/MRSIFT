
package it.unisa.soa.sift;

import java.util.List;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;

/**
 *
 * @author didacus
 * Main interface for sequential algorithm
 */
public interface SiftAlgorithms {
  
  SiftModel findFeatures(String objectImagePath, String sceneImagePath);
  
  List<SiftModel> findFeatures(String objectImagePath, List<String> scenesImagePath);
  
  Mat detectObject(SiftModel model, List<KeyPoint> objectKeyPoints, List<KeyPoint> sceneKeyPoints);
 
  
}
