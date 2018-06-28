package it.unisa.soa.sift;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SIFT;


/**
 *
 * @author didacus
 */
public class SiftModel {
  
  private final SIFT sift;
  private final Mat objetImage, sceneImage;
  private final MatOfKeyPoint objectKeypoints, sceneKeyPoints;
  private final MatOfKeyPoint objectDescriptors, sceneDescriptors;
  private final MatOfDMatch matches;

  public SiftModel(String objectPath, String scenePath){
    this.objetImage = Imgcodecs.imread(objectPath, SiftConfig.COLORED_IMAGE);
    this.sceneImage = Imgcodecs.imread(scenePath, SiftConfig.COLORED_IMAGE);
    this.objectKeypoints = new MatOfKeyPoint();
    this.sceneKeyPoints = new MatOfKeyPoint();
    this.objectDescriptors = new MatOfKeyPoint();
    this.sceneDescriptors = new MatOfKeyPoint();
    this.matches = new MatOfDMatch();
    this.sift = SIFT.create();
  }

  public MatOfDMatch getMatches() {
    return matches;
  }

  public SIFT getSift() {
    return sift;
  }

  public Mat getObjetImage() {
    return objetImage;
  }

  public Mat getSceneImage() {
    return sceneImage;
  }

  public MatOfKeyPoint getObjectKeypoints() {
    return objectKeypoints;
  }

  public MatOfKeyPoint getSceneKeyPoints() {
    return sceneKeyPoints;
  }

  public MatOfKeyPoint getObjectDescriptors() {
    return objectDescriptors;
  }

  public MatOfKeyPoint getSceneDescriptors() {
    return sceneDescriptors;
  }
  
}
