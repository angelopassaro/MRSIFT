
package it.unisa.soa.sift;

import java.util.List;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;

/**
 *
 * @author didacus
 * Main interface for sequential algorithm
 */
public interface SiftAlgorithms {
  
 
  void extractKeypoints(SiftModel model);
  void extractDescriptors(SiftModel model);
  MatOfDMatch calculateMatches(SiftModel objectModel, SiftModel sceneModel);
  Mat detectObject(SiftModel objectModel, SiftModel sceneModel);
}
