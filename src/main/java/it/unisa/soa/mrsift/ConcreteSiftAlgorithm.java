package it.unisa.soa.mrsift;

import java.util.ArrayList;
import java.util.List;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;

/**
 *
 * @author didacus
 */
public class ConcreteSiftAlgorithm implements SiftAlgorithm {
  
  private final SiftObject sobject;

  public ConcreteSiftAlgorithm(String objectImagePath, String sceneImagePath) {
    this.sobject = new SiftObject(objectImagePath, sceneImagePath);
  }
  
  

  @Override
  public Mat findFeatures() {
    this.sobject.extractKeyPoints();
    this.sobject.extractFeatures();
    this.sobject.calculateMatches(DescriptorMatcher.FLANNBASED);
    Mat outputImage = new Mat(this.sobject.getSceneImage().rows(), this.sobject.getSceneImage().cols() * 2, this.sobject.getSceneImage().type());
    Features2d.drawMatches(this.sobject.getObjetImage(), this.sobject.getObjectKeypoints(), this.sobject.getSceneImage(), 
            this.sobject.getSceneKeyPoints(), this.sobject.getMatches(), outputImage);
    return outputImage;
  }

  @Override
  public List<Mat> findFeatures(String objectImagePath, List<String> scenesImagePath) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Mat findImage(List<KeyPoint> objectPoints, List<KeyPoint> scenePoints) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
