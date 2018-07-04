
package it.unisa.soa.sift;

import java.util.List;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.Feature2D;

/**
 *
 * @author didacus
 * Main interface for sequential algorithm
 */
public interface SiftAlgorithms {
  
 
  MatOfKeyPoint extractKeypoints(Mat image, Feature2D extractor);
  MatOfKeyPoint extractDescriptors(Mat image, MatOfKeyPoint imageKeypoints, Feature2D extractor);
  MatOfDMatch calculateMatches(MatOfKeyPoint objectDescriptors, MatOfKeyPoint sceneDescriptors);
  Mat detectObject(Mat objectImage, Mat sceneImage, MatOfDMatch matches, MatOfKeyPoint objectKeyPoints, MatOfKeyPoint sceneKeyPoints);
}
